package com.github.jetbrains.rssreader.androidApp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import com.github.jetbrains.rssreader.androidApp.ui.fragment.BaseFragment
import com.github.jetbrains.rssreader.androidApp.ui.util.doOnApplyWindowInsets
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.ModoRender
import com.github.terrakok.modo.android.init
import com.github.terrakok.modo.back
import org.koin.android.ext.android.inject

class AppActivity : AppCompatActivity(R.layout.container) {
    private val modoRender by lazy { ModoRender(this, R.id.container) }
    private val modo: Modo by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        colorSystemBars()
        super.onCreate(savedInstanceState)
        modo.init(savedInstanceState, Screens.MainFeed())
        handleLeftAndRightInsets()
    }

    private fun colorSystemBars() {
        val color = Color.argb(200, 0, 0, 0)
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    private fun handleLeftAndRightInsets() {
        findViewById<View>(R.id.container).doOnApplyWindowInsets { view, insets, initialPadding ->
            view.setPadding(
                initialPadding.left + insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                initialPadding.top,
                initialPadding.right + insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                initialPadding.bottom
            )
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(
                    0,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    0,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                )
            ).build()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        modo.render = modoRender
    }

    override fun onPause() {
        modo.render = null
        super.onPause()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        (currentFragment as? BaseFragment)?.onBackPressed() ?: modo.back()
    }
}
