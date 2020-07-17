package com.art.notas.coisas

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

import com.art.notas.R

import java.util.ArrayList

object BancoDados {

    lateinit var ids: ArrayList<Int>
    lateinit var notas: ArrayList<String>
    //var rAdapter: RecyclerView.Adapter<*> = null
    //var rLayoutManager: RecyclerView.LayoutManager = null


    fun aNotes(database: SQLiteDatabase, context: Context, view: RecyclerView) {

        try {
            //Recuperar as tarefas
            val cursor = database.rawQuery("SELECT * FROM notas ORDER BY id DESC", null)

            //recuperar os ids das colunas
            val indiceColunaId = cursor!!.getColumnIndex("id")
            val indiceColunaNota = cursor.getColumnIndex("nota")

            //Criar adaptador
            notas= ArrayList()
            ids= ArrayList()

            view.setHasFixedSize(true)
            val rLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
            view.layoutManager = rLayoutManager
            val rAdapter: RecyclerView.Adapter<*> = RListAdapter(notas)
            view.adapter = rAdapter


            //listar as tarefas
            cursor.moveToFirst()
            while (cursor != null) {

                notas.add(cursor.getString(indiceColunaNota))
                ids.add(Integer.parseInt(cursor.getString(indiceColunaId)))

                cursor.moveToNext()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun gNotes(database: SQLiteDatabase, context: Context, view: RecyclerView, modoSvRmEdMvCpPu: Int, id: Int?, textoN: String?) {

        when (modoSvRmEdMvCpPu) {
            2 -> {
                //Remover
                database.execSQL("DELETE FROM notas WHERE id=" + id!!)
                //Atualizar
                aNotes(database, context, view)
            }

            4 -> {
                //Mover para o topo
                database.execSQL("INSERT INTO notas (nota) VALUES('$textoN') ")
                database.execSQL("DELETE FROM notas WHERE id=" + id!!)
                //Atualizar
                aNotes(database, context, view)
            }

            5 -> {
                //Copiar
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("texto", textoN)
                clipboard.setPrimaryClip(clipData)
            }

            6 -> {
                //AlertDialog
                val alertDialogBuider = AlertDialog.Builder(context)
                alertDialogBuider.setView(R.layout.custom_popup)

                val alertDialog = alertDialogBuider.create()
                alertDialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                //Mostrar o AlertDialog
                alertDialog.show()
                //Setar os botoes
                val pPositive = alertDialog.findViewById<Button>(R.id.buttonPAdd)
                val pCancel = alertDialog.findViewById<Button>(R.id.buttonPCancel)
                val pText = alertDialog.findViewById<EditText>(R.id.editTextP)
                val pLayout = alertDialog.findViewById<ConstraintLayout>(R.id.popupLayout)

                fun String.getRGB() = Preferences(context).getRGB(this)
                //EditText background color
                if ("POPUP_EDITTEXT_BACKGROUND".getRGB().first != -1) {
                    val drawable = Preferences(context).drawableWithColor(R.drawable.arredondado, Color.rgb(
                            "POPUP_EDITTEXT_BACKGROUND".getRGB().first,
                            "POPUP_EDITTEXT_BACKGROUND".getRGB().second,
                            "POPUP_EDITTEXT_BACKGROUND".getRGB().third ))
                    pText!!.setBackgroundDrawable(drawable)
                }
                //EditText text color
                if ("POPUP_EDITTEXT_TEXT_COLOR".getRGB().first != -1) {
                    pText!!.setTextColor(Color.rgb(
                            "POPUP_EDITTEXT_TEXT_COLOR".getRGB().first,
                            "POPUP_EDITTEXT_TEXT_COLOR".getRGB().second,
                            "POPUP_EDITTEXT_TEXT_COLOR".getRGB().third))
                    pText.setHintTextColor(Color.rgb(
                            "POPUP_EDITTEXT_TEXT_COLOR".getRGB().first,
                            "POPUP_EDITTEXT_TEXT_COLOR".getRGB().second,
                            "POPUP_EDITTEXT_TEXT_COLOR".getRGB().third))
                }
                //Popup button okay color
                if ("POPUP_BUTTON_OKAY".getRGB().first != -1) {
                    pPositive!!.setBackgroundColor(Color.rgb(
                            "POPUP_BUTTON_OKAY".getRGB().first,
                            "POPUP_BUTTON_OKAY".getRGB().second,
                            "POPUP_BUTTON_OKAY".getRGB().third ))
                }
                //text
                if ("POPUP_BUTTON_OKAY_TEXT".getRGB().first != -1) {
                    pPositive!!.setTextColor(Color.rgb(
                            "POPUP_BUTTON_OKAY_TEXT".getRGB().first,
                            "POPUP_BUTTON_OKAY_TEXT".getRGB().second,
                            "POPUP_BUTTON_OKAY_TEXT".getRGB().third ))
                }
                //Popup button cancel color
                if ("POPUP_BUTTON_CANCEL".getRGB().first != -1) {
                    pCancel!!.setBackgroundColor(Color.rgb(
                            "POPUP_BUTTON_CANCEL".getRGB().first,
                            "POPUP_BUTTON_CANCEL".getRGB().second,
                            "POPUP_BUTTON_CANCEL".getRGB().third ))
                }
                //text
                if ("POPUP_BUTTON_CANCEL_TEXT".getRGB().first != -1) {
                    pCancel!!.setTextColor(Color.rgb(
                            "POPUP_BUTTON_CANCEL_TEXT".getRGB().first,
                            "POPUP_BUTTON_CANCEL_TEXT".getRGB().second,
                            "POPUP_BUTTON_CANCEL_TEXT".getRGB().third ))
                }
                //Popup background color
                if ("POPUP_BACKGROUND".getRGB().first != -1) {
                    pLayout!!.setBackgroundColor(Color.rgb(
                            "POPUP_BACKGROUND".getRGB().first,
                            "POPUP_BACKGROUND".getRGB().second,
                            "POPUP_BACKGROUND".getRGB().third ))
                }

                if (textoN != null) {
                    //Setar texto que vai ser editado
                    pText!!.setText(textoN)
                }
                alertDialog.setOnDismissListener {
                    //Limpar o texto ao fechar
                    pText!!.setText("")
                }
                pCancel!!.setOnClickListener { alertDialog.dismiss() }
                pPositive!!.setOnClickListener {
                    val pTextoD = pText!!.text.toString()
                    val textoR = pTextoD.replace("'", "''")

                    if (pTextoD == "") {
                        Toast.makeText(context, R.string.nota_nula, Toast.LENGTH_SHORT).show()
                    } else {

                        if (id != null) {
                            //Editar
                            database.execSQL("UPDATE notas SET nota ='$textoR' WHERE id=$id")

                        } else {
                            //Salvar
                            database.execSQL("INSERT INTO notas (nota) VALUES('$textoR') ")

                        }
                        //Atualizar
                        aNotes(database, context, view)
                        alertDialog.dismiss()
                    }
                }
            }

            else -> {
            }
        }


    }

}
