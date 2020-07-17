package com.art.notas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.art.notas.coisas.Preferences
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : Activity(), AdapterView.OnItemSelectedListener {

    var itemSelected:Any? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.artTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val buttonG: Button = findViewById(R.id.buttonSource)
        val buttonR: Button = findViewById(R.id.buttonRequest)
        val imageViewR: ImageView = findViewById(R.id.imageReturn)

        val editTextR = findViewById<EditText>(R.id.editTextRed)
        val editTextG = findViewById<EditText>(R.id.editTextGreen)
        val editTextB = findViewById<EditText>(R.id.editTextBlue)
        val btSaveColor = findViewById<Button>(R.id.buttonSaveColor)


        //Spinner things
        val spinner = findViewById<Spinner>(R.id.spinnerColors)
        //Spinner adapter
        ArrayAdapter.createFromResource(this,R.array.keys, android.R.layout.simple_spinner_item)
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter }
        //Select this override of the function
        spinner.onItemSelectedListener = this

        //Get and save color values
        btSaveColor.setOnClickListener { v ->
            Preferences(this).addPref(itemSelected.toString()+"_R", editTextR.text.toString().toInt())
            Preferences(this).addPref(itemSelected.toString()+"_G", editTextG.text.toString().toInt())
            Preferences(this).addPref(itemSelected.toString()+"_B", editTextB.text.toString().toInt())
            Preferences(applicationContext).addPref("THEME_CHANGED", 1)
        }

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

        imageViewR.setOnClickListener { v ->
            this@ConfigActivity.finish()
        }
    }

    //Spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //what to do when a item is selected
        itemSelected = parent!!.getItemAtPosition(position) //Selected item
        val r = Preferences(applicationContext).getPreference(itemSelected.toString()+"_R", 0)
        val g = Preferences(applicationContext).getPreference(itemSelected.toString()+"_G", 0)
        val b = Preferences(applicationContext).getPreference(itemSelected.toString()+"_B", 0)
        if (r is Int && g is Int && b is Int){
            editTextRed.setText(r.toString())
            editTextGreen.setText(g.toString())
            editTextBlue.setText(b.toString())
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}
