package com.cws.kmemory

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeAliasSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.ksp.toTypeName

class NativeProcessor(
    private val codeGen: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    data class Field(
        val name: String,
        val offset: String,
        val typeName: TypeName,
        val type: String,
        val generatedTypeName: TypeName,
        val generatedType: String,
        val nested: Boolean
    )

    private val primitiveTypes = arrayOf(
        "Int", "Double", "Float", "Long", "Boolean", "Byte", "Short"
    )

    private val commonTypes = arrayOf(
        "Vec2", "Vec3", "Vec4", "Mat2", "Mat3", "Mat4"
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(NativeData::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
        symbols.forEach {
            generate(it)
        }
        return emptyList()
    }

    private fun generate(declaration: KSClassDeclaration) {
        val pkg = declaration.packageName.asString()
        val name = declaration.simpleName.asString()
        val generatedDataName = "${name}Data"
        val generatedArrayName = "${name}Array"

        // annotation metadata
        val annotation = declaration.annotations
            .find { it.shortName.asString() == "NativeData" } ?: return

        val autoCreate = annotation.arguments
            .find { it.name?.getShortName() == "autoCreate" }
            ?.value as? Boolean ?: false

        val gpuAlignment = annotation.arguments
            .find { it.name?.getShortName() == "gpuAlignment" }
            ?.value as? Boolean ?: false

        val fields = mutableListOf<Field>()
        var offset = "index + 0"
        declaration.getAllProperties().forEach { prop ->
            var typeName = prop.type.resolve().toTypeName()
            var type = typeName.toString().split(".").last()
            val nested = type !in primitiveTypes && type !in commonTypes

            if (gpuAlignment) {
                when (type) {
                    "Vec3" -> {
                        type = "Vec4"
                        typeName as ClassName
                        typeName = ClassName(typeName.packageName, "Vec4")
                    }
                    "Mat3" -> {
                        type = "Mat4"
                        typeName as ClassName
                        typeName = ClassName(typeName.packageName, "Mat4")
                    }
                }
            }

            val generatedTypeName = if (nested && typeName is ClassName) {
                ClassName(
                    typeName.packageName,
                    typeName.simpleName + "Data"
                )
            } else {
                typeName
            }

            val generatedType = if (nested) "${type}Data" else type

            fields += Field(
                name = prop.simpleName.asString(),
                offset = offset,
                generatedType = generatedType,
                generatedTypeName = generatedTypeName,
                type = type,
                typeName = typeName,
                nested = nested
            )

            offset += " + $generatedType.SIZE_BYTES"
        }

        val fileSpec = FileSpec.builder(pkg, generatedDataName)

        val size = fields.joinToString(" + ") {
            if (it.type == "Boolean") {
                if (gpuAlignment) {
                    "Int.SIZE_BYTES"
                } else {
                    "Byte.SIZE_BYTES"
                }
            } else {
                "${it.generatedType}.SIZE_BYTES"
            }
        }

        val primaryConstructor = if (autoCreate) {
            FunSpec
                .constructorBuilder()
                .addParameter(
                    ParameterSpec.builder("index", INT)
                        .defaultValue("NativeHeap.allocate(SIZE_BYTES)")
                        .build()
                )
                .build()
        } else {
            FunSpec
                .constructorBuilder()
                .addParameter("index", INT)
                .build()
        }

        val dataClass = TypeSpec.classBuilder(generatedDataName)
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
                            .returns(ClassName(pkg, generatedDataName))
                            .addStatement("return %T(NativeHeap.allocate(SIZE_BYTES))", ClassName(pkg, generatedDataName))
                            .build()
                    )
                    .build()
            )
            .addFunction(
                FunSpec.builder("free")
                    .addStatement("NativeHeap.free(index, SIZE_BYTES)")
                    .build()
            )

        // properties
        fields.forEach { field ->
            var getter = "get${field.generatedType}"
            var setter = "set${field.generatedType}"
            var getterCast = ""
            val fieldName = field.name.firstToUppercase()

            if (gpuAlignment && field.type == "Boolean") {
                getter = "getInt"
                setter = "setInt"
                getterCast = " == 1"
            }

            if (field.generatedType.isNotEmpty()) {
                if (field.nested || field.generatedType in commonTypes) {
                    dataClass.addFunction(
                        FunSpec.builder("get$fieldName")
                            .addStatement("return ${field.generatedType}(${field.offset})")
                            .returns(field.generatedTypeName)
                            .build()
                    )
                    dataClass.addFunction(
                        FunSpec.builder("set$fieldName")
                            .addParameter("value", field.generatedTypeName)
                            .addStatement("NativeHeap.copy(value.index, ${field.offset}, ${field.generatedType}.SIZE_BYTES)")
                            .build()
                    )
                } else {
                    dataClass.addFunction(
                        FunSpec.builder("get$fieldName")
                            .addStatement("return NativeHeap.$getter(${field.offset})$getterCast")
                            .returns(field.generatedTypeName)
                            .build()
                    )
                    dataClass.addFunction(
                        FunSpec.builder("set$fieldName")
                            .addParameter("value", field.generatedTypeName)
                            .addStatement("NativeHeap.$setter(${field.offset}, value)")
                            .build()
                    )
                }
            }
        }

        val blockParam = ParameterSpec.builder(
            "block",
            LambdaTypeName.get(
                parameters = listOf(
                    ParameterSpec
                        .builder(
                            "value",
                            ClassName(pkg, generatedDataName)
                        ).build()
                ),
                returnType = TypeVariableName("T")
            )
        ).build()

        val useFun = FunSpec.builder("use")
            .receiver(ClassName(pkg, generatedDataName))
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

        val toTypeParams = fields.joinToString(", \n") { field ->
            if (field.nested) {
                "${field.name} = ${field.name}.to${field.type}()"
            } else {
                "${field.name} = ${field.name}"
            }
        }

        val toTypeFun = FunSpec.builder("to$name")
            .receiver(ClassName(pkg, generatedDataName))
            .returns(ClassName(pkg, name))
            .addCode("return $name(\n $toTypeParams \n)")
            .build()

        val toGenTypeParams = fields.joinToString("\n") { field ->
            if (field.nested) {
                "${field.name} = this@to$generatedDataName.${field.name}.to${field.generatedType}()"
            } else {
                "${field.name} = this@to$generatedDataName.${field.name}"
            }
        }

        val toGenTypeFun = FunSpec.builder("to$generatedDataName")
            .receiver(ClassName(pkg, name))
            .returns(ClassName(pkg, generatedDataName))
            .addCode("return $generatedDataName.create().apply {\n $toGenTypeParams \n}")
            .build()

        val typealiasNativeArray = TypeAliasSpec.builder(
            generatedArrayName,
            ClassName(pkg, "NativeArray")
                .parameterizedBy(ClassName(pkg, generatedDataName))
        ).build()

        val createNativeArray = FunSpec.builder(generatedArrayName)
            .addParameter("size", INT)
            .returns(ClassName(pkg, generatedArrayName))
            .addCode("return NativeArray(\n " +
                    "size = size,\n" +
                    "elementSizeBytes = ${generatedDataName}.SIZE_BYTES,\n" +
                    "fromIndex = { $generatedDataName(it) },\n" +
                    "toIndex = { it.index }\n" +
                    " \n)")
            .build()

        val dataClassName = ClassName(pkg, generatedDataName)

        fileSpec.addImport("com.cws.kmemory", "NativeHeap")
        fileSpec.addImport("com.cws.kmemory", "NativeArray")
        fileSpec.addType(dataClass.build())
        fileSpec.addProperties(fields.map { field ->
            val type = if (field.nested) field.generatedTypeName else field.typeName
            PropertySpec.builder(field.name, type)
                .receiver(dataClassName)
                .mutable(true)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return get${field.name.firstToUppercase()}()")
                        .build()
                )
                .setter(
                    FunSpec.setterBuilder()
                        .addParameter("value", type)
                        .addStatement("set${field.name.firstToUppercase()}(value)")
                        .build()
                )
                .build()
        })
        fileSpec.addFunction(useFun)
        fileSpec.addFunction(toTypeFun)
        fileSpec.addFunction(toGenTypeFun)
        fileSpec.addTypeAlias(typealiasNativeArray)
        fileSpec.addFunction(createNativeArray)

        val dep = declaration.containingFile?.let {
            Dependencies(false, it)
        } ?: Dependencies(false)

        codeGen.createNewFile(dep, pkg, generatedDataName)
            .bufferedWriter()
            .use {
                fileSpec.build().writeTo(it)
            }
    }

    private fun String.firstToUppercase() = replace(this[0], this[0].uppercaseChar())

}