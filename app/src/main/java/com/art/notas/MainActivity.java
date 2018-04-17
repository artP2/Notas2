package com.art.notas;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends Activity {


    private Button      PAdd , PCancel , openConfig , imageAdd;
    private EditText    PText;
    private ListView    listNotas;
    private SQLiteDatabase          bancoDados;
    private ArrayAdapter<String>    arrayAdapter;
    private ArrayList<String>       notas;
    private ArrayList<Integer>      ids;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//**************************************************************************************************
        listNotas   =   findViewById(R.id.listViewNotes);
        openConfig  =   findViewById(R.id.buttonConfig);
        imageAdd    =   findViewById(R.id.buttonAdd);

//**************************************************************************************************
// Criar arquivo para guardar dados
        bancoDados  =   openOrCreateDatabase("appnotas",MODE_PRIVATE,null);
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS notas(id INTEGER PRIMARY KEY AUTOINCREMENT, nota VARCHAR ) ");

//**************************************************************************************************
// PopUp
        AlertDialog.Builder alertDialogBuider   =   new AlertDialog.Builder(MainActivity.this);
        alertDialogBuider.setView(R.layout.custom_popup);

        AlertDialog alertDialog =   alertDialogBuider.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();

                PAdd    =   alertDialog.findViewById(R.id.buttonPAdd);
                PCancel =   alertDialog.findViewById(R.id.buttonPCancel);
                PText   =   alertDialog.findViewById(R.id.editTextP);

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
                    }});
                PAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String PTextoD  =   PText.getText().toString();
                        if (PTextoD.equals("")){
                            Toast.makeText(MainActivity.this, R.string.nota_nula, Toast.LENGTH_SHORT).show();
                        }else{
                                svNota(PTextoD);
                                alertDialog.dismiss();
                    }}});

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

        listNotas.setLongClickable(true);
        listNotas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                rmNota(ids.get(position));
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
        recuperarNotas();

    }
//**************************************************************************************************
    //MUDANÇA DE ACTIVITY
    public void irParaConfig(){
        try {

            Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
            MainActivity.this.startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //METODOS
    private void recuperarNotas(){
            try{

                //Recuperar as tarefas
                Cursor cursor   =   bancoDados.rawQuery("SELECT * FROM notas ORDER BY id DESC", null);

                //recuperar os ids das colunas
                int indiceColunaId      =   cursor.getColumnIndex("id");
                int indiceColunaNota    =   cursor.getColumnIndex("nota");

                //Criar adaptador
                notas           =   new ArrayList<String>();
                ids             =   new ArrayList<Integer>();
                arrayAdapter    =   new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    notas){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View view       =   super.getView(position, convertView, parent);
                    TextView text   =   view.findViewById(android.R.id.text2);
                    text.setTextColor(Color.WHITE);
                    return view;

                }
            };
            listNotas.setAdapter( arrayAdapter );

            //listar as tarefas
            cursor.moveToFirst();
            while ( cursor != null ){

                Log.i("Resultado - ", "Id nota: " + cursor.getString( indiceColunaId ) + " nota: " + cursor.getString( indiceColunaNota ) );
                notas.add( cursor.getString( indiceColunaNota ) );
                ids.add( Integer.parseInt( cursor.getString( indiceColunaId ) ) );

                cursor.moveToNext();
            }

    }catch (Exception e){
            e.printStackTrace();
            }
    }
    private void rmNota(Integer id){
        try {

            bancoDados.execSQL("DELETE FROM notas WHERE id=" + id);
            recuperarNotas();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void svNota(String texto){
        // Solve de ' problem
        String textoR   =   texto.replace("'","''");
        try {
            bancoDados.execSQL("INSERT INTO notas (nota) VALUES('" + textoR + "') ");
            recuperarNotas();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //SÓ FALTA APLICAR
    private void edNota(Integer id, String textoEd){
        try {

            if (textoEd.equals("")) {
                Toast.makeText(MainActivity.this, R.string.nota_nula, Toast.LENGTH_SHORT).show();
            } else {
                bancoDados.execSQL( "UPDATE notas SET nota ='" + textoEd + "' WHERE id=" + id);
                recuperarNotas();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /***********************
     * METODOS DE TESTES   *
     **********************/
    private void mvNota(Integer position, Integer newPosition){

    }

    /***********************
     * METODOS DE TESTES   *
     **********************/
}
