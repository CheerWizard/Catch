package com.cws.fmm.ksp

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
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

class FastProcessor(
    environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private val generator: CodeGenerator = environment.codeGenerator
    private val logger: KSPLogger = environment.logger

    data class Class(
        val declaration: KSClassDeclaration,
        val gpuAlignment: Boolean
    )

    data class Field(
        val name: String,
        val offset: String,
        val typeName: TypeName,
        val type: String,
        val generatedTypeName: TypeName,
        val generatedType: String,
        val nested: Boolean,
        val defaultValue: String,
    )

    private val primitiveTypes = arrayOf(
        "Int", "Double", "Float", "Long", "Boolean", "Byte", "Short"
    )

    private val filterMethods = arrayOf(
        "equals", "toString", "copy", "hashCode"
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("Scanning for @FastObject...")

        val classes = resolver
            .getSymbolsWithAnnotation("com.cws.fmm.FastObject")
            .filterIsInstance<KSClassDeclaration>()
            .filter { declaration ->
                declaration.annotations
                    .find { it.shortName.asString() == "FastObject" } != null
            }
            .map { declaration ->
                val annotation = declaration.annotations
                    .find { it.shortName.asString() == "FastObject" }!!

                val gpuAlignment = annotation.arguments
                    .find { it.name?.getShortName() == "gpuAlignment" }
                    ?.value as? Boolean ?: false

                Class(
                    declaration = declaration,
                    gpuAlignment = gpuAlignment
                )
            }

        classes.forEach { clazz ->
            logger.warn("Generate: $clazz")
            generate(clazz)
        }

        return emptyList()
    }

    private fun generate(clazz: Class) {
        val declaration = clazz.declaration
        val gpuAlignment = clazz.gpuAlignment
        val pkg = declaration.packageName.asString()
        val generatedName = declaration.simpleName.asString().removePrefix("_")
        val generatedClassName = ClassName(pkg, generatedName)
        val memoryHandleType = ClassName("com.cws.fmm", "MemoryHandle")

        val fields = mutableListOf<Field>()
        var offset = "handle"

        declaration.getAllProperties()
            .asIterable()
            .forEachIndexed { i, prop ->
                var typeName = prop.type.resolve().toTypeName()
                var type = typeName.toString().split(".").last()
                val nested = type !in primitiveTypes

                type = if (nested) type.removePrefix("_") else type

                if (gpuAlignment) {
                    when (type) {
                        "Boolean" -> {
                            type = "Int"
                            typeName as ClassName
                            typeName = ClassName(typeName.packageName, "Int")
                        }
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

                val defaultValue = when (type) {
                    "Boolean" -> "false"
                    "Byte", "Short", "Int" -> "0"
                    "Long" -> "0L"
                    "Float" -> "0f"
                    "Double" -> "0.0"
                    else -> "$type()"
                }

                fields += Field(
                    name = prop.simpleName.asString(),
                    offset = offset,
                    generatedType = type,
                    generatedTypeName = typeName,
                    type = type,
                    typeName = typeName,
                    nested = nested,
                    defaultValue = defaultValue
                )

                val typeSize = if (type == "Boolean") "Byte.SIZE_BYTES" else "$type.SIZE_BYTES"

                offset += " + $typeSize"
            }

        val fileSpec = FileSpec.builder(pkg, generatedName)

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

        val constructorBuilder = FunSpec.constructorBuilder()

        constructorBuilder.addParameters(fields.map { field ->
            ParameterSpec
                .builder(field.name, field.generatedTypeName)
                .defaultValue(field.defaultValue)
                .build()
        })

        constructorBuilder.addParameter(
            ParameterSpec
                .builder("handle", memoryHandleType)
                .defaultValue("create().handle")
                .build()
        )

        constructorBuilder.callThisConstructor("handle")

        fields.forEach { field ->
            constructorBuilder.addStatement("this.${field.name} = ${field.name}")
        }

        val constructor = constructorBuilder.build()

        val generatedClass = TypeSpec.classBuilder(generatedName)
            .addModifiers(KModifier.VALUE)
            .addAnnotation(JvmInline::class)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("handle", memoryHandleType)
                    .build()
            )
            .addFunction(constructor)
            .addProperty(
                PropertySpec.builder("handle", memoryHandleType)
                    .initializer("handle")
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
                            .returns(generatedClassName)
                            .addStatement("return %T(HeapMemory.allocate(SIZE_BYTES))", generatedClassName)
                            .build()
                    ).apply {
                        val companions = declaration.declarations
                            .filterIsInstance<KSClassDeclaration>()
                            .filter { it.isCompanionObject }
                            .asIterable()

                        companions.forEach { companion ->
                            addProperties(
                                companion
                                    .getDeclaredProperties()
                                    .asIterable()
                                    .map { it.toPropertySpec() }
                            )
                            // TODO: function are always with empty body,
                            //  because KotlinPoet can only see declarations, not definitions
//                            addFunctions(
//                                companion
//                                    .getDeclaredFunctions()
//                                    .asIterable()
//                                    .map { it.toFunSpec() }
//                                    .filterFunctions()
//                            )
                        }
                    }
                    .build()
            )
            .addFunction(
                FunSpec.builder("free")
                    .addStatement("HeapMemory.free(handle, SIZE_BYTES)")
                    .addStatement("return %T(NULL)", generatedClassName)
                    .returns(generatedClassName)
                    .build()
            )

        // TODO: function are always with empty body,
        //  because KotlinPoet can only see declarations, not definitions

//            .addFunctions(
//                declaration
//                    .getAllFunctions()
//                    .asIterable()
//                    .map { it.toFunSpec() }
//                    .filterFunctions()
//            )

        // properties
        fields.forEach { field ->
            val name = field.name
            val type = field.generatedTypeName as ClassName

            val property = PropertySpec.builder(name, type)
                .mutable(true)

            if (field.generatedType.isNotEmpty()) {
                if (field.nested) {
                    property.setter(
                        FunSpec.builder("set()")
                            .addParameter("value", field.generatedTypeName)
                            .addStatement("handle.checkNotNull()")
                            .addStatement("HeapMemory.copy(value.handle, ${field.offset}, ${field.generatedType}.SIZE_BYTES)")
                            .build()
                    )
                    property.getter(
                        FunSpec.builder("get()")
                            .addStatement("handle.checkNotNull()")
                            .addStatement("return ${field.generatedType}(${field.offset})")
                            .build()
                    )
                } else {
                    property.setter(
                        FunSpec.builder("set()")
                            .addParameter("value", field.generatedTypeName)
                            .addStatement("handle.checkNotNull()")
                            .addStatement("HeapMemory.set${field.generatedType}(${field.offset}, value)")
                            .build()
                    )
                    property.getter(
                        FunSpec.builder("get()")
                            .addStatement("handle.checkNotNull()")
                            .addStatement("return HeapMemory.get${field.generatedType}(${field.offset})")
                            .build()
                    )
                }
            }

            generatedClass.addProperty(property.build())
        }

        val blockParam = ParameterSpec.builder(
            "block",
            LambdaTypeName.get(
                parameters = listOf(
                    ParameterSpec
                        .builder(
                            "value",
                            generatedClassName
                        ).build()
                ),
                returnType = TypeVariableName("T")
            )
        ).build()

        val useFun = FunSpec.builder("use")
            .receiver(generatedClassName)
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

        val stackFunBuilder = FunSpec.builder(generatedName)
            .receiver(ClassName(pkg, "Stacfmm"))
            .returns(generatedClassName)

        fields.forEach { field ->
            stackFunBuilder.addParameter(
                ParameterSpec
                    .builder(field.name, field.generatedTypeName)
                    .defaultValue(field.defaultValue)
                    .build()
            )
        }

        var constructorParams = fields.joinToString(",") { it.name }
        if (constructorParams.isNotEmpty()) {
            constructorParams += ","
        }

        stackFunBuilder.addCode(
            """
            return $generatedName($constructorParams push($generatedName.SIZE_BYTES))
            """.trimIndent()
        )

        val stackFun = stackFunBuilder.build()

        val copyParams = fields
            .map { "\n${it.name} = ${it.name}," }
            .joinToString("")

        val copyFun = FunSpec.builder("copy")
            .returns(generatedClassName)
            .addStatement("return $generatedName($copyParams)")
            .build()

        generatedClass.addFunction(copyFun)

        fileSpec.addImport("com.cws.fmm", "NULL")
        fileSpec.addImport("com.cws.fmm", "checkNotNull")
        fileSpec.addImport("com.cws.fmm", "HeapMemory")
        fileSpec.addImport("com.cws.fmm", "Stacfmm")
        fileSpec.addType(generatedClass.build())
        fileSpec.addFunction(stackFun)
        fileSpec.addFunction(useFun)

        val dep = declaration.containingFile?.let {
            Dependencies(false, it)
        } ?: Dependencies(false)

        generator.createNewFile(dep, pkg, "$generatedName.ksp")
            .bufferedWriter()
            .use {
                fileSpec.build().writeTo(it)
            }

        // generate only once for updates!
//        generatePrimitiveCollections()

        generateObjectCollections(pkg, generatedName)
    }

    private fun generatePrimitiveCollections() {
        primitiveTypes.forEach { type ->
            generateFastList("com.cws.fmm.collections", type)
            generateFastSet("com.cws.fmm.collections", type)
        }
        primitiveTypes.forEach { key ->
            primitiveTypes.forEach { value ->
                generateFastMap("com.cws.fmm.collections", key, value)
            }
        }
    }

    private fun generateObjectCollections(pkg: String, name: String) {
        generateFastList(pkg, name)
        generateFastSet(pkg, name)
        primitiveTypes.forEach { key ->
            generateFastMap(pkg, key, name)
        }
    }

    private fun Iterable<FunSpec>.filterFunctions(): Iterable<FunSpec> {
        return filter {
            !it.isConstructor &&
            !it.isAccessor &&
            !it.name.contains("component") &&
            it.name !in filterMethods
        }
    }

    private fun readTemplate(name: String): String {
        val file = "templates/$name.txt"
        logger.warn("readTemplate: $file")
        return FastProcessor::class.java.classLoader
            .getResourceAsStream(file)
            ?.bufferedReader()
            ?.readText()
            ?: error("File not found $file")
    }

    private val generatedFiles = mutableSetOf<String>()

    private fun generateFile(pkg: String, name: String, code: String) {
        logger.warn("generateFile: $pkg.$name")
        generator
            .createNewFile(Dependencies.ALL_FILES, pkg, name)
            .bufferedWriter()
            .use {
                it.write(code)
                generatedFiles.add(name)
            }
    }

    fun generateFastList(pkg: String, name: String) {
        if (generatedFiles.contains("${name}List")) return

        var code = readTemplate("FastList")
            .replace("#pkg", pkg)
            .replace("#T", name)

        code = if (name == "Boolean") {
            code.replace("#S", "Byte")
        } else {
            code.replace("#S", name)
        }

        code = if (name in primitiveTypes) {
            code
                .replace("#add", "add$name(value)")
                .replace("#set", "set$name(handle, value)")
                .replace("#get", "get$name(handle)")
        } else {
            code
                .replace("#add", "addFastObject(value.handle)")
                .replace("#set", "setFastObject(handle, value.handle)")
                .replace("#get", "$name(handle = handle)")
        }

        generateFile(pkg, "${name}List", code)
    }

    fun generateFastSet(pkg: String, name: String) {
        if (generatedFiles.contains("${name}Set")) return

        val code = readTemplate("FastSet")
            .replace("#pkg", pkg)
            .replace("#T", name)

        generateFile(pkg, "${name}Set", code)
    }

    fun generateFastMap(pkg: String, key: String, value: String) {
        val name = "$key$value"
        if (generatedFiles.contains("${name}Map")) return

        val code = readTemplate("FastMap")
            .replace("#pkg", pkg)
            .replace("#T", name)
            .replace("#K", key)
            .replace("#V", value)

        generateFile(pkg, "${name}Map", code)
    }

}