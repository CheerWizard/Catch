package com.cws.kanvas.pipeline

import com.cws.kanvas.core.Kanvas
import com.cws.kanvas.core.ShaderID
import com.cws.kanvas.core.ShaderStageID

class ShaderStage {

    private var id: ShaderStageID? = null

    fun init(type: Int) {
        id = Kanvas.shaderStageInit(type)
    }

    fun release() {
        Kanvas.shaderStageRelease(id ?: return)
    }

    fun compile(source: String): Boolean {
        return Kanvas.shaderStageCompile(id ?: return false, source)
    }

    fun attach(shader: ShaderID) {
        Kanvas.shaderStageAttach(shader, id ?: return)
    }

    fun detach(shader: ShaderID) {
        Kanvas.shaderStageDetach(shader, id ?: return)
    }

}