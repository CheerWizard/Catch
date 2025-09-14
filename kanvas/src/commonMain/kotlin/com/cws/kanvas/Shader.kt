package com.cws.kanvas

class Shader(
    private val stages: MutableList<ShaderStage> = mutableListOf()
) {

    private var id: ShaderID? = null

    fun init(sources: List<Pair<Int, String>>) {
        id = Kanvas.shaderInit()

        sources.forEachIndexed { i, source ->
            val stage = ShaderStage()
            stage.init(source.first)
            if (!stage.compile(source.second)) return@forEachIndexed
            stages.add(i, stage)
        }

        id?.let { id ->
            stages.forEach {
                it.attach(id)
            }
            Kanvas.shaderLink(id)
            stages.forEach {
                it.detach(id)
                it.release()
            }
        }
    }

    fun release() {
        Kanvas.shaderRelease(id ?: return)
        stages.forEach {
            it.release()
        }
    }

    fun run() {
        Kanvas.shaderUse(id ?: return)
    }

    fun isReady() = id != null

}