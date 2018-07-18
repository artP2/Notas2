package com.art.notas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

//It's mine!
import com.art.notas.coisas.RListAdapter;
import static com.art.notas.coisas.BancoDados.*;


public class MainActivity extends Activity {


    private ImageView imageAdd, openConfig;

    private SQLiteDatabase bancoDados;
    private RecyclerView listNotas;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = MainActivity.this;
//**************************************************************************************************
        listNotas = findViewById(R.id.viewNotas);
        openConfig = findViewById(R.id.imageViewConfig);
        imageAdd = findViewById(R.id.imageViewAdd);

//**************************************************************************************************
// Criar arquivo para guardar dados
        bancoDados = openOrCreateDatabase("appnotas", MODE_PRIVATE, null);
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS notas(id INTEGER PRIMARY KEY AUTOINCREMENT, nota VARCHAR ) ");

//**************************************************************************************************
// PopUp

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gNotes(bancoDados, context, listNotas, 6, null, null);

            }
        });

//**************************************************************************************************
// Eventos de onClick
                    //Abrir configurações
        openConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaConfig();
            }
        });
                    //Click na lista
        listNotas.addOnItemTouchListener(new RListAdapter.RecyclerViewTouchListener(context, listNotas, new RListAdapter.RecyclerViewClickListener() {
                    //Click
            @Override
            public void onClick(View view, int position) {
            }
                    //Click longo
            @Override
            public void onLongClick(View view, int position) {
                    //Abrir menu
                PopupMenu popupMenu = new PopupMenu(context , view );
                popupMenu.getMenuInflater().inflate(R.menu.list_menu, popupMenu.getMenu());
                    //Click menu

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Indentificar opção escolhida e faze-la
                        int modo = 0;
                        Integer id = ids.get(position);
                        String nota = notas.get(position);

                        switch (item.getItemId()){
                            case R.id.itemRm:
                                modo = 2;
                                break;
                            case R.id.itemEd:
                                modo = 6;
                                break;
                            case R.id.itemMv:
                                modo = 4;
                                break;
                            case R.id.itemC:
                                modo = 5;
                                break;
                        }
                        gNotes(bancoDados,context,listNotas,modo,id,nota);

                        return MainActivity.super.onContextItemSelected(item);
                    }
                });
                popupMenu.show();


            }
        }));


//**************************************************************************************************
//LISTAR
        aNotes(bancoDados, context, listNotas);

    }

    //**************************************************************************************************
    //Ir para as configurações
    public void irParaConfig() {
        try {

            Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
            MainActivity.this.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}