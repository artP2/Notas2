package com.art.notas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//It's my!
import com.art.notas.coisas.BancoDados;
import com.art.notas.coisas.RListAdapter;


public class MainActivity extends Activity {


    private Button PAdd, PCancel;
    private ImageView imageAdd, openConfig, imageFind;
    private EditText PText;
    private SQLiteDatabase bancoDados;

    private RecyclerView listNotas;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
//**************************************************************************************************
        listNotas = findViewById(R.id.viewNotas);
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

        listNotas.addOnItemTouchListener(new RListAdapter.RecyclerViewTouchListener(context, listNotas, new RListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Integer posicaoID =  BancoDados.ids.get(position);
                BancoDados.rmNota(posicaoID, bancoDados, context, listNotas);
            }
        }));

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