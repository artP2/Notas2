package com.art.notas

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi

class ConfigActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        val themePreferenceString = getString(R.string.preference_theme)
        val sharedPref = applicationContext.getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE)
        val themePreference = sharedPref.getString(themePreferenceString,"artTheme")
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
            val editor = sharedPref.edit()
            var themeReverse: String
            if (themePreference == "artTheme"){
                themeReverse = "reverseArtTheme"
            }else{
                themeReverse = "artTheme"
            }
            editor.putString(themePreferenceString, themeReverse)
            editor.putInt(getString(R.string.preference_theme_changed), 1)
            editor.apply()
        }

        imageViewR.setOnClickListener { v ->
            this@ConfigActivity.finish()
        }


    }
    /*//Theme manager
    private fun setTheme(){
        var imgTTheme: ImageView = findViewById(R.id.imageThemeTrasition)
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
        anim.start()
        }*/
}
