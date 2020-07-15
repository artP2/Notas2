package com.art.notas.coisas

import android.content.Context

class Preferences(context: Context) {
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
}