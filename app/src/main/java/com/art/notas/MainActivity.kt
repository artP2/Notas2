package com.art.notas

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu

//It's mine!
import com.art.notas.coisas.RListAdapter
import com.art.notas.coisas.BancoDados.aNotes
import com.art.notas.coisas.BancoDados.gNotes
import com.art.notas.coisas.BancoDados.ids
import com.art.notas.coisas.BancoDados.notas
import com.art.notas.coisas.Preferences


class MainActivity : Activity() {
    private var imageAdd: ImageView? = null
    private var openConfig: ImageView? = null

    private lateinit var bancoDados: SQLiteDatabase
    private lateinit var listNotas: RecyclerView

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        val themePreference = Preferences(applicationContext).getPreference("THEME_PREFERENCE","artTheme")
        val changedInt = Preferences(applicationContext).getPreference("THEME_CHANGED", 0)
        if (themePreference == "reverseArtTheme") {
            setTheme(R.style.reverseArtTheme)
        } else {
            setTheme(R.style.artTheme)
        }
        if (changedInt == 1) {
            Preferences(applicationContext).addPref("THEME_CHANGED", 0)
            recreate()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = this@MainActivity
        //**************************************************************************************************
        listNotas = findViewById(R.id.viewNotas)
        openConfig = findViewById(R.id.imageViewConfig)
        imageAdd = findViewById(R.id.imageViewAdd)

        //**************************************************************************************************
        // Criar arquivo para guardar dados
        bancoDados = openOrCreateDatabase("appnotas", Context.MODE_PRIVATE, null)
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS notas(id INTEGER PRIMARY KEY AUTOINCREMENT, nota VARCHAR ) ")

        //**************************************************************************************************
        // PopUp

        imageAdd!!.setOnClickListener { gNotes(bancoDados, context, listNotas, 6, null, null) }

        //**************************************************************************************************
        // Eventos de onClick
        //Abrir configurações
        openConfig!!.setOnClickListener { irParaConfig() }
        //Click na lista
        listNotas.addOnItemTouchListener(RListAdapter.RecyclerViewTouchListener(context, listNotas, object : RListAdapter.RecyclerViewClickListener {
            //Click
            override fun onClick(view: View, position: Int) {
            }
            //Click longo
            override fun onLongClick(view: View?, position: Int) {
                //Abrir menu
                val popupMenu = PopupMenu(context, view)
                popupMenu.menuInflater.inflate(R.menu.list_menu, popupMenu.menu)
                //Click menu

                popupMenu.setOnMenuItemClickListener { item ->
                    //Indentificar opção escolhida e faze-la
                    var modo = 0
                    val id = ids[position]
                    val nota = notas[position]

                    when (item.itemId) {
                        R.id.itemRm -> modo = 2
                        R.id.itemEd -> modo = 6
                        R.id.itemMv -> modo = 4
                        R.id.itemC -> modo = 5
                    }
                    gNotes(bancoDados, context, listNotas, modo, id, nota)

                    super@MainActivity.onContextItemSelected(item)
                }
                popupMenu.show()


            }
        }))


        //**************************************************************************************************
        //LISTAR
        aNotes(bancoDados, context, listNotas)

    }

    override fun onResume() {
        val changedInt = Preferences(applicationContext).getPreference("THEME_CHANGED", 0)
        if (changedInt == 1) {
            Preferences(applicationContext).addPref("THEME_CHANGED", 0)
            recreate()
        }
        super.onResume()

    }

    //**************************************************************************************************
    //Ir para as configurações
    private fun irParaConfig() {
        try {

            val intent = Intent(this@MainActivity, ConfigActivity::class.java)
            this@MainActivity.startActivity(intent)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}