package com.github.jetbrains.rssreader.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.github.jetbrains.rssreader.RssReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = RssReader().getFeed("https://blog.jetbrains.com/kotlin/feed/").toString()
    }
}
