package com.cws.kanvas.shader

import com.cws.kanvas.core.Kanvas
import com.cws.kanvas.core.RenderLoopJobs
import com.cws.kanvas.loaders.ShaderLoader
import com.cws.printer.Printer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ShaderManager : KoinComponent {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val shaderLoader: ShaderLoader by inject()

    fun load(
        vararg names: String,
        onLoaded: (List<Pair<Int, String>>) -> Unit
    ) {
        scope.launch {
            val jobs = names.map { name ->
                async {
                    val shaderType = name.toShaderType()
                    if (shaderType == Kanvas.NULL) return@async shaderType to ""
                    shaderType to shaderLoader.load(name)
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
                Printer.w("Unsupported shader type $this")
                Kanvas.NULL
            }
        }
    }

}