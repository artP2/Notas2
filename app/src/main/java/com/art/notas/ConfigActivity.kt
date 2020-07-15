package com.art.notas

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.art.notas.coisas.Preferences
import kotlin.math.hypot

class ConfigActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        val themePreference = Preferences(applicationContext).getPreference("THEME_PREFERENCE","artTheme")
        if (themePreference == "reverseArtTheme"){
            setTheme(R.style.reverseArtTheme)
        }else{
            setTheme(R.style.artTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val buttonG: Button = findViewById(R.id.buttonSource)
        val buttonR: Button = findViewById(R.id.buttonRequest)
        val btTheme: Button = findViewById(R.id.buttonTheme)
        val imageViewR: ImageView = findViewById(R.id.imageReturn)

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
        btTheme.setOnClickListener { v ->
            val themeReverse: String
            if (themePreference == "artTheme"){
                themeReverse = "reverseArtTheme"
            }else{
                themeReverse = "artTheme"
            }
            Preferences(applicationContext).addPref("THEME_PREFERENCE", themeReverse)
            Preferences(applicationContext).addPref("THEME_CHANGED", 1)
            animTheme()
        }

        imageViewR.setOnClickListener { v ->
            this@ConfigActivity.finish()
        }


    }
    //Theme manager
    private fun animTheme(){
        val imgTTheme: ImageView = findViewById(R.id.imageThemeTrasition)
        val container: ConstraintLayout = findViewById(R.id.tContainer)
        if (imgTTheme.visibility == View.VISIBLE){
            return
        }

        val w = container.measuredWidth
        val h = container.measuredHeight

        val bitmap = Bitmap.createBitmap(w, h , Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        container.draw(canvas)

        imgTTheme.setImageBitmap(bitmap)
        imgTTheme.visibility = View.VISIBLE

        val finalRadius = hypot(w.toFloat(), h.toFloat())

        val anim = ViewAnimationUtils.createCircularReveal(container, w/2, h/2, 0f, finalRadius)
        anim.duration = 400L
        anim.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                imgTTheme.visibility = View.GONE
            }
        })
        this.recreate()
        anim.start()
        }
}
