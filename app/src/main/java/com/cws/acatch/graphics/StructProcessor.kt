//package com.cws.acatch.graphics
//
//import com.google.devtools.ksp.processing.*
//import com.google.devtools.ksp.symbol.*
//import java.util.Locale
//
//class StructProcessor(
//    private val codeGenerator: CodeGenerator,
//    private val logger: KSPLogger
//) : SymbolProcessor {
//
//    override fun process(resolver: Resolver): List<KSAnnotated> {
//        val symbols = resolver.getSymbolsWithAnnotation("com.cws.acatch.graphics.Struct")
//            .filterIsInstance<KSClassDeclaration>()
//
//        for (cls in symbols) {
//            generateStruct(cls)
//        }
//
//        return emptyList()
//    }
//
//    private fun generateStruct(cls: KSClassDeclaration) {
//        val packageName = cls.packageName.asString()
//        val className = cls.simpleName.asString()
//        val generatedName = "${className}_Generated"
//
//        val properties = cls.getAllProperties().toList()
//        var offset = 0
//
//        val builder = StringBuilder()
//        builder.appendLine("package $packageName")
//        builder.appendLine("import java.nio.ByteBuffer")
//        builder.appendLine("import java.nio.ByteOrder")
//        builder.appendLine("class $generatedName : Struct(${getTotalSize(properties)}) {")
//
//        properties.forEach { property ->
//            val type = property.type.resolve().declaration.simpleName.asString()
//            val name = property.simpleName.asString()
//            val alignedOffset = alignOffset(offset, type)
//
//            builder.appendLine("    var $name: $type")
//            builder.appendLine("        get() = get${type.capitalize(Locale.ROOT)}($alignedOffset)")
//            builder.appendLine("        set(value) = set${type.capitalize(Locale.ROOT)}($alignedOffset, value)")
//
//            offset = alignedOffset + sizeof(type)
//        }
//
//        builder.appendLine("}")
//        codeGenerator.createNewFile(
//            Dependencies(false, cls.containingFile!!),
//            packageName,
//            generatedName
//        ).bufferedWriter().use { it.write(builder.toString()) }
//    }
//
//    private fun sizeof(type: String): Int = when (type) {
//        "Float", "Int", "UInt", "Boolean" -> 4
//        "Offset" -> 8
//        "Color" -> 16
//        else -> 0
//    }
//
//    private fun alignOffset(offset: Int, type: String): Int {
//        val align = sizeof(type)
//        return ((offset + align - 1) / align) * align
//    }
//
//    private fun getTotalSize(properties: List<KSPropertyDeclaration>): Int {
//        var size = 0
//        properties.forEach { property ->
//            val type = property.type.resolve().declaration.simpleName.asString()
//            size = alignOffset(size, type) + sizeof(type)
//        }
//        return size
//    }
//
//}