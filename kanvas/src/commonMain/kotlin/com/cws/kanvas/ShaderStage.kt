package com.cws.kanvas

class ShaderStage {

    private lateinit var handle: ShaderStageID

    fun init(type: Int) {
        handle = Kanvas.shaderStageInit(type)
    }

    fun release() {
        Kanvas.shaderStageRelease(handle)
    }

    fun compile(source: String): Boolean {
        return Kanvas.shaderStageCompile(handle, source)
    }

    fun attach(shader: ShaderID) {
        Kanvas.shaderStageAttach(shader, handle)
    }

    fun detach(shader: ShaderID) {
        Kanvas.shaderStageDetach(shader, handle)
    }

}