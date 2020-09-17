package com.github.jetbrains.rssreader.androidApp

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jetbrains.rssreader.androidApp.databinding.ContainerBinding
import com.github.jetbrains.rssreader.androidApp.ui.base.BaseFragment
import com.github.jetbrains.rssreader.androidApp.ui.feedlist.FeedListFragment
import com.github.jetbrains.rssreader.androidApp.ui.mainfeed.MainFeedFragment
import com.github.jetbrains.rssreader.androidApp.ui.util.doOnApplyWindowInsets
import kotlin.math.roundToInt

class AppActivity : AppCompatActivity(R.layout.container) {
    private val vb by viewBinding(ContainerBinding::bind, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        applyAppTheme()
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.fragments.isEmpty()) {
            showFragment(MainFeedFragment())
        }

        handleLeftAndRightInsets()
    }

    private fun applyAppTheme() {
        setTheme(R.style.Theme_MyApp)
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                adjustAlpha(context.getColorFromAttr(R.attr.colorSurface), .7f).let { statusColor ->
                    statusBarColor = statusColor
                    navigationBarColor = statusColor
                }
                if (!context.isNightMode()) {
                    decorView.systemUiVisibility = decorView.systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
            } else {
                adjustAlpha(
                    context.getColorFromAttr(R.attr.colorOnSurface),
                    .7f
                ).let { statusColor ->
                    statusBarColor = statusColor
                    navigationBarColor = statusColor
                }
            }
        }
    }

    private fun handleLeftAndRightInsets() {
        vb.container.doOnApplyWindowInsets { view, insets, initialPadding ->
            view.updatePadding(
                left = initialPadding.left + insets.systemWindowInsetLeft,
                right = initialPadding.right + insets.systemWindowInsetRight
            )
            WindowInsetsCompat.Builder(insets).setSystemWindowInsets(
                Insets.of(
                    0,
                    insets.systemWindowInsetTop,
                    0,
                    insets.systemWindowInsetBottom
                )
            ).build()
        }
    }

    fun showFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment is FeedListFragment) {
            showFragment(MainFeedFragment())
        } else {
            super.onBackPressed()
        }
    }

    @ColorInt
    private fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    @ColorInt
    private fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    private fun Context.isNightMode(): Boolean =
        resources.configuration.uiMode
            .and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

}
