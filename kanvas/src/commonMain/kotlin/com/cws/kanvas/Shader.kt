package com.cws.kanvas

class Shader(
    private val stages: MutableList<ShaderStage> = mutableListOf(),
    private val loader: ShaderLoader
) {

    private lateinit var handle: ShaderID

    suspend fun init(
        paths: List<String>
    ) {
        val sources = paths.map { path ->
            val shaderType = path.toShaderType()
            if (shaderType == Kanvas.NULL) return@map shaderType to ""
            shaderType to loader.load(path)
        }
        init(sources)
    }

    private fun String.toShaderType(): Int {
        return when {
            contains("_vert") -> Kanvas.VERTEX_SHADER
            contains("_frag") -> Kanvas.FRAGMENT_SHADER
            else -> {
                println("Unsupported shader type $this")
                Kanvas.NULL
            }
        }
    }

    fun init(sources: List<Pair<Int, String>>) {
        handle = Kanvas.shaderInit()

        sources.forEachIndexed { i, source ->
            val stage = ShaderStage()
            stage.init(source.first)
            if (!stage.compile(source.second)) return@forEachIndexed
            stages.add(i, stage)
        }

        stages.forEach {
            it.attach(handle)
        }

        Kanvas.shaderLink(handle)

        stages.forEach {
            it.detach(handle)
            it.release()
        }
    }

    fun release() {
        Kanvas.shaderRelease(handle)
        stages.forEach {
            it.release()
        }
    }

    fun run() {
        Kanvas.shaderUse(handle)
    }

}