package com.art.notas.coisas

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class Preferences(context: Context) {
    val context = context
    val sharedPref = context.getSharedPreferences("NOTAS2_PREFERENCES", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()


    ///Add value to preference
    fun <G> addPref(key:String, value:G?){
        when(value){
            is String -> editor.putString(key,value)
            is Int -> editor.putInt(key,value)
        }
        editor.apply()
    }

    //Get preference
    fun <G> getPreference(key: String, defValue:G):Any?{
        var retorno:Any? = null
        when(defValue){
            is String -> retorno = sharedPref.getString(key, defValue)
            is Int -> retorno = sharedPref.getInt(key, defValue)
        }
        return retorno
    }

    fun drawableWithColor(@DrawableRes drawableRes: Int, @ColorInt color: Int): Drawable? {
        val pic = ContextCompat.getDrawable(context, drawableRes)
        pic?.setTint(color)
        return pic
    }

    fun getRGB(key:String):Triple<Int, Int, Int> {
        val r = getPreference(key+"_R", -1)
        val g = getPreference(key+"_G", -1)
        val b = getPreference(key+"_B", -1)
        if (r is Int && g is Int && b is Int){
            return Triple(r, g, b)
        }else return Triple(0,0,0)
    }
}