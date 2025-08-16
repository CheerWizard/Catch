package com.cws.nativeksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.sequences.forEach

class HeapDataProcessor(
    private val codeGen: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    data class FieldInfo(
        val name: String,
        val type: String,
        val offset: String,
        val typeName: TypeName,
        val nested: Boolean
    )

    private val primitiveTypes = arrayOf(
        "Int", "Double", "Float", "Long", "Boolean", "Byte", "Short",
        "Color", "Vec2", "Vec3", "Vec4"
    )

    private val commonTypes = arrayOf(
        "Mat2", "Mat3", "Mat4"
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(HeapData::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
        symbols.forEach {
            generate(it)
        }
        return emptyList()
    }

    private fun generate(declaration: KSClassDeclaration) {
        val pkg = declaration.packageName.asString()
        val name = declaration.simpleName.asString()
        val dataName = "${name}Data"
        val arrayName = "${name}Array"

        // annotation metadata
        val annotation = declaration.annotations
            .first { it.shortName.asString() == "HeapData" }

        val autoCreate = annotation.arguments
            .firstOrNull { it.name?.asString() == "autoCreate" }
            ?.value as? Boolean ?: false

        val fields = mutableListOf<FieldInfo>()
        var offset = "index + 0"
        declaration.getAllProperties().forEach { prop ->
            val typeName = prop.type.resolve().toTypeName()
            var type = typeName.toString().split(".").last()
            val nested = type !in primitiveTypes && type !in commonTypes

            val generatedTypeName = if (nested && typeName is ClassName) {
                ClassName(
                    typeName.packageName,
                    typeName.simpleName + "Data"
                )
            } else {
                typeName
            }

            type = if (nested) "${type}Data" else type

            fields += FieldInfo(
                name = prop.simpleName.asString(),
                type = type,
                offset = offset,
                typeName = generatedTypeName,
                nested = nested
            )

            offset += " + $type.SIZE_BYTES"
        }

        val fileSpec = FileSpec.builder(pkg, dataName)

        val memoryHeapClazz = ClassName("com.cws.nativeksp", "MemoryHeap")

        val size = fields.joinToString(" + ") {
            if (it.type == "Boolean") {
                "Byte.SIZE_BYTES"
            } else {
                "${it.type}.SIZE_BYTES"
            }
        }

        // ==== Generate XxxData ====

        val primaryConstructor = if (autoCreate) {
            FunSpec
                .constructorBuilder()
                .addParameter(
                    ParameterSpec.builder("index", INT)
                        .defaultValue("MemoryHeap.allocate(SIZE_BYTES)")
                        .build()
                )
                .build()
        } else {
            FunSpec
                .constructorBuilder()
                .addParameter("index", INT)
                .build()
        }

        val dataClass = TypeSpec.classBuilder(dataName)
            .addModifiers(KModifier.VALUE)
            .addAnnotation(JvmInline::class)
            .primaryConstructor(primaryConstructor)
            .addProperty(
                PropertySpec.builder("index", INT)
                    .initializer("index")
                    .build()
            )
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder("SIZE_BYTES", INT)
                            .addModifiers(KModifier.CONST)
                            .initializer(size)
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("create")
                            .returns(ClassName(pkg, dataName))
                            .addStatement("return %T(%T.allocate(SIZE_BYTES))", ClassName(pkg, dataName), memoryHeapClazz)
                            .build()
                    )
                    .build()
            )
            .addFunction(
                FunSpec.builder("free")
                    .addStatement("%T.free(index, SIZE_BYTES)", memoryHeapClazz)
                    .build()
            )

        // properties
        fields.forEach { field ->
            val getter = "get${field.type}"
            val setter = "set${field.type}"
            val propBuilder = PropertySpec.builder(field.name, field.typeName).mutable(true)

            if (field.type.isNotEmpty()) {
                if (field.nested || field.type in commonTypes) {
                    propBuilder
                        .getter(FunSpec.getterBuilder()
                            .addStatement("return ${field.type}(${field.offset})")
                            .build()
                        )
                    propBuilder
                        .setter(FunSpec.setterBuilder()
                            .addParameter("value", field.typeName)
                            .addStatement("%T.copy(value.index, ${field.offset}, ${field.type}.SIZE_BYTES)", memoryHeapClazz)
                            .build())
                } else {
                    propBuilder
                        .getter(FunSpec.getterBuilder()
                            .addStatement("return %T.$getter(${field.offset})", memoryHeapClazz)
                            .build()
                        )
                    propBuilder
                        .setter(FunSpec.setterBuilder()
                            .addParameter("value", field.typeName)
                            .addStatement("%T.$setter(${field.offset}, value)", memoryHeapClazz)
                            .build())
                }
            }

            dataClass.addProperty(propBuilder.build())
        }

        val blockParam = ParameterSpec.builder(
            "block",
            LambdaTypeName.get(
                parameters = listOf(
                    ParameterSpec
                        .builder(
                            "value",
                            ClassName(pkg, dataName)
                        ).build()
                ),
                returnType = TypeVariableName("T")
            )
        ).build()

        val useFun = FunSpec.builder("use")
            .receiver(ClassName(pkg, dataName))
            .addTypeVariable(TypeVariableName("T"))
            .addParameter(blockParam)
            .returns(TypeVariableName("T"))
            .addModifiers(KModifier.INLINE)
            .addCode(
                """
        try {
            return block(this)
        } finally {
            free()
        }
        """.trimIndent()
            )
            .build()

        fileSpec.addType(dataClass.build())
        fileSpec.addFunction(useFun)

        val dep = declaration.containingFile?.let {
            Dependencies(false, it)
        } ?: Dependencies(false)

        codeGen.createNewFile(dep, pkg, dataName)
            .bufferedWriter()
            .use {
                fileSpec.build().writeTo(it)
            }
    }

}