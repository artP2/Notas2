package com.art.notas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.art.notas.coisas.BancoDados;

import java.util.ArrayList;

public class MainActivity extends Activity {


    private Button PAdd, PCancel;
    private ImageView imageAdd, openConfig, imageFind;
    private EditText PText;
    private ListView listNotas;
    private SQLiteDatabase bancoDados;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> notas;
    private ArrayList<Integer> ids;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
//**************************************************************************************************
        listNotas = findViewById(R.id.listViewNotes);
        openConfig = findViewById(R.id.imageViewConfig);
        imageAdd = findViewById(R.id.imageViewAdd);

        imageFind = findViewById(R.id.imageViewFind);
//**************************************************************************************************
// Criar arquivo para guardar dados
        bancoDados = openOrCreateDatabase("appnotas", MODE_PRIVATE, null);
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS notas(id INTEGER PRIMARY KEY AUTOINCREMENT, nota VARCHAR ) ");

//**************************************************************************************************
// PopUp
        AlertDialog.Builder alertDialogBuider = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuider.setView(R.layout.custom_popup);

        AlertDialog alertDialog = alertDialogBuider.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();

                PAdd = alertDialog.findViewById(R.id.buttonPAdd);
                PCancel = alertDialog.findViewById(R.id.buttonPCancel);
                PText = alertDialog.findViewById(R.id.editTextP);

                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        PText.setText("");
                    }
                });
                PCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                PAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String PTextoD = PText.getText().toString();
                        if (PTextoD.equals("")) {
                            Toast.makeText(context, R.string.nota_nula, Toast.LENGTH_SHORT).show();
                        } else {
                            BancoDados.svNota(PTextoD, bancoDados, context, listNotas);
                            alertDialog.dismiss();
                        }
                    }
                });

            }
        });

//**************************************************************************************************
// Eventos de onClick
        openConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaConfig();
            }
        });
        imageFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, R.string.nothing, Toast.LENGTH_SHORT).show();
            }
        });

        listNotas.setLongClickable(true);
        listNotas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Integer posicaoID =  BancoDados.ids.get(position);
                BancoDados.rmNota(posicaoID , bancoDados , context , listNotas);
                return true;
            }
        });

/***************************************************************************************************
 *                                          TESTES                                                 *
 **************************************************************************************************/


/***************************************************************************************************
 *                                          TESTES                                                 *
 **************************************************************************************************/

//**************************************************************************************************
//LISTAR
        BancoDados.recuperarNotas(bancoDados, context, listNotas);

    }

    //**************************************************************************************************
    //MUDANÇA DE ACTIVITY
    public void irParaConfig() {
        try {

            Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
            MainActivity.this.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}