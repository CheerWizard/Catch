package com.cws.kanvas

import com.cws.klog.KLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

expect class PlatformShaderLoader() {
    suspend fun load(name: String): String
}

object ShaderLoader {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun load(
        vararg names: String,
        onLoaded: (List<Pair<Int, String>>) -> Unit
    ) {
        scope.launch {
            val jobs = names.map { name ->
                async {
                    val shaderType = name.toShaderType()
                    if (shaderType == Kanvas.NULL) return@async shaderType to ""
                    shaderType to PlatformShaderLoader().load(name)
                }
            }
            val sources = jobs.awaitAll()
            RenderLoopJobs.push { onLoaded(sources) }
        }
    }

    private fun String.toShaderType(): Int {
        return when {
            contains(".vert") -> Kanvas.VERTEX_SHADER
            contains(".frag") -> Kanvas.FRAGMENT_SHADER
            else -> {
                KLog.warn("Unsupported shader type $this")
                Kanvas.NULL
            }
        }
    }

}