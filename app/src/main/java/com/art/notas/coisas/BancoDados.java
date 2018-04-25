package com.art.notas.coisas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.art.notas.R;

import java.util.ArrayList;

public class BancoDados {

public static ArrayList<Integer> ids;
public static ArrayList<String> notas;
public static RecyclerView.Adapter rAdapter;
public static RecyclerView.LayoutManager rLayoutManager;

    public static void recuperarNotas(SQLiteDatabase database, Context context, RecyclerView view){
        try{

            //Recuperar as tarefas
            Cursor cursor   =   database.rawQuery("SELECT * FROM notas ORDER BY id DESC", null);

            //recuperar os ids das colunas
            int indiceColunaId      =   cursor.getColumnIndex("id");
            int indiceColunaNota    =   cursor.getColumnIndex("nota");

            //Criar adaptador
            notas = new ArrayList<String>();
            ids = new ArrayList<Integer>();


            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            view.setHasFixedSize(true);

            // use a linear layout manager
            rLayoutManager = new LinearLayoutManager(context);
            view.setLayoutManager(rLayoutManager);

            // specify an adapter (see also next example)
            rAdapter = new RListAdapter(BancoDados.notas);
            view.setAdapter(rAdapter);


            //listar as tarefas
            cursor.moveToFirst();
            while ( cursor != null ){

                notas.add( cursor.getString( indiceColunaNota ) );
                ids.add( Integer.parseInt( cursor.getString( indiceColunaId ) ) );

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void rmNota(Integer id, SQLiteDatabase database, Context context, RecyclerView view){
        try {

            database.execSQL("DELETE FROM notas WHERE id=" + id);
            recuperarNotas(database, context, view);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void svNota(String texto, SQLiteDatabase database, Context context, RecyclerView view){
        // Solve de ' problem
        String textoR   =   texto.replace("'","''");
        try {
            database.execSQL("INSERT INTO notas (nota) VALUES('" + textoR + "') ");
            recuperarNotas(database, context, view);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void edNota(Integer id, String textoEd, SQLiteDatabase database, Context context, RecyclerView view){
        try {

            if (textoEd.equals("")) {
                Toast.makeText(context, R.string.nota_nula, Toast.LENGTH_SHORT).show();
            } else {
                database.execSQL( "UPDATE notas SET nota ='" + textoEd + "' WHERE id=" + id);
                recuperarNotas(database, context,view);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
