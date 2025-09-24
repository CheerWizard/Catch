package com.cws.kanvas.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController

class KanvasViewController(
    private val renderLoop: RenderLoop,
    private val uiContent: @Composable () -> Unit
): UIViewController() {

    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLoad() {
        super.viewDidLoad()

        val kanvasView = KanvasView(renderLoop)
        kanvasView.autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
        view.addSubview(kanvasView)

        val composeViewController = ComposeUIViewController(content = uiContent)
        addChildViewController(composeViewController)
        composeViewController.view.setFrame(view.bounds)
        composeViewController.view.autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
        view.addSubview(composeViewController.view)

        composeViewController.didMoveToParentViewController(this)
    }

}