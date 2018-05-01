package com.art.notas.coisas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.art.notas.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class BancoDados {

public static ArrayList<Integer> ids;
public static ArrayList<String> notas;
public static RecyclerView.Adapter rAdapter;
public static RecyclerView.LayoutManager rLayoutManager;

    public static void aNotes(SQLiteDatabase database, Context context, RecyclerView view, int modoSvRmEd , Integer id, String texto){
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

            switch (modoSvRmEd){
                case 1:
                    //Sv
                    // Solve de ' problem
                    String textoR   =   texto.replace("'","''");
                    database.execSQL("INSERT INTO notas (nota) VALUES('" + textoR + "') ");
                    break;

                case 2:
                    //Rm
                    database.execSQL("DELETE FROM notas WHERE id=" + id);
                    break;

                case 3:
                    //Ed
                    database.execSQL( "UPDATE notas SET nota ='" + texto + "' WHERE id=" + id);

                    break;

                default:
                    break;
            }

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
}
