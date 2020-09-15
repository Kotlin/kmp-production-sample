package com.github.jetbrains.rssreader.androidApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.jetbrains.rssreader.androidApp.ui.mainfeed.MainFeedFragment

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFeedFragment())
                .commit()
        }
    }
}
