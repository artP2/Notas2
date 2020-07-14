package com.art.notas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class ConfigActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val buttonG: Button = findViewById(R.id.buttonSource)
        val buttonR: Button = findViewById(R.id.buttonRequest)
        var btTheme: Button = findViewById(R.id.buttonTheme)
        val imageViewR: ImageView = findViewById(R.id.imageReturn)
        var imgTTheme: ImageView = findViewById(R.id.imageThemeTrasition)

        buttonG.setOnClickListener { v ->
            val uri = Uri.parse("https://github.com/artpin107/Notas2")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        buttonR.setOnClickListener { v ->
            val uri = Uri.parse("https://t.me/artP2")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        imageViewR.setOnClickListener { v -> this@ConfigActivity.finish() }


    }

}
