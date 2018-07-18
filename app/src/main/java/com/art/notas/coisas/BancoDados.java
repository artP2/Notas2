package com.art.notas.coisas;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.art.notas.R;

import java.util.ArrayList;

public class BancoDados {

    public static ArrayList<Integer> ids;
    public static ArrayList<String> notas;
    public static RecyclerView.Adapter rAdapter;
    public static RecyclerView.LayoutManager rLayoutManager;


    public static void aNotes(SQLiteDatabase database, Context context, RecyclerView view){

        try{
            //Recuperar as tarefas
            Cursor cursor   =   database.rawQuery("SELECT * FROM notas ORDER BY id DESC", null);

            //recuperar os ids das colunas
            int indiceColunaId      =   cursor.getColumnIndex("id");
            int indiceColunaNota    =   cursor.getColumnIndex("nota");

            //Criar adaptador
            notas = new ArrayList<String>();
            ids = new ArrayList<Integer>();

            view.setHasFixedSize(true);
            rLayoutManager = new LinearLayoutManager(context);
            view.setLayoutManager(rLayoutManager);
            rAdapter = new RListAdapter(notas);
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

    public static void gNotes(SQLiteDatabase database, Context context, RecyclerView view, int modoSvRmEdMvCpPu , Integer id, String textoN){

        switch (modoSvRmEdMvCpPu){
            case 2:
                //Remover
                database.execSQL("DELETE FROM notas WHERE id=" + id);
                //Atualizar
                aNotes(database,context,view);
                break;

            case 4:
                //Mover para o topo
                database.execSQL("INSERT INTO notas (nota) VALUES('" + textoN + "') ");
                database.execSQL("DELETE FROM notas WHERE id=" + id);
                //Atualizar
                aNotes(database,context,view);
                break;

            case 5:
                //Copiar
                ClipboardManager clipboard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("texto",textoN);
                clipboard.setPrimaryClip(clipData);
                break;

            case 6:
                //AlertDialog
                AlertDialog.Builder alertDialogBuider = new AlertDialog.Builder(context);
                alertDialogBuider.setView(R.layout.custom_popup);

                AlertDialog alertDialog = alertDialogBuider.create();
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                //Mostrar o AlertDialog
                alertDialog.show();
                //Setar os botoes
                Button PPositive = alertDialog.findViewById(R.id.buttonPAdd);
                Button PCancel = alertDialog.findViewById(R.id.buttonPCancel);
                EditText PText = alertDialog.findViewById(R.id.editTextP);

                if (textoN != null) {
                    //Setar texto que vai ser editado
                    PText.setText(textoN);
                }
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //Limpar o texto ao fechar
                        PText.setText("");
                    }
                });
                PCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                PPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String PTextoD = PText.getText().toString();
                        String textoR = PTextoD.replace("'", "''");

                        if (PTextoD.equals("")) {
                            Toast.makeText(context, R.string.nota_nula, Toast.LENGTH_SHORT).show();
                        } else {

                            if (id != null){
                                //Editar
                                database.execSQL( "UPDATE notas SET nota ='" + textoR + "' WHERE id=" + id);

                            }else {
                                //Salvar
                                database.execSQL("INSERT INTO notas (nota) VALUES('" + textoR + "') ");

                            }
                            //Atualizar
                            aNotes(database,context,view);
                            alertDialog.dismiss();
                        }
                    }
                });
                break;

            default:
                break;
        }



    }

}
