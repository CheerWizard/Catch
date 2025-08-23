package com.cws.kanvas

class TextureBuffer(
    private val type: Int
) {

    private lateinit var handle: TextureID

    fun init(texture: Texture) {
        Kanvas.run {
            handle = textureInit(type)
            textureBind(type, handle)
            textureParameter(type, TEXTURE_MIN_FILTER, texture.minFilter)
            textureParameter(type, TEXTURE_MAG_FILTER, texture.magFilter)
            textureParameter(type, TEXTURE_WRAP_S, texture.wrapS)
            textureParameter(type, TEXTURE_WRAP_T, texture.wrapT)
            textureParameter(type, TEXTURE_WRAP_R, texture.wrapR)
            textureImage2D(type, texture)
            if (texture.generateMipMaps) {
                textureGenerateMipmap(type)
            }
            textureUnbind(type)
        }
    }

    fun release() {
        Kanvas.textureRelease(handle)
    }

    fun bind() {
        Kanvas.textureBind(type, handle)
    }

    fun unbind() {
        Kanvas.textureUnbind(type)
    }

    fun activate(slot: Int) {
        Kanvas.textureActive(slot)
    }

}