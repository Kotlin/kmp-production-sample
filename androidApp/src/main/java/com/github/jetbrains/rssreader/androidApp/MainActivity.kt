package com.github.jetbrains.rssreader.androidApp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.jetbrains.rssreader.RssReader
import com.github.jetbrains.rssreader.create
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        GlobalScope.launch(Dispatchers.Main) {
            tv.text = RssReader.create(BuildConfig.DEBUG)
                .getFeed("https://blog.jetbrains.com/kotlin/feed/").toString()
        }
    }
}
