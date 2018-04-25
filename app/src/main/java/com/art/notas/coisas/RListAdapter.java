package com.art.notas.coisas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.art.notas.R;

import java.util.ArrayList;

public class RListAdapter extends RecyclerView.Adapter<RListAdapter.ViewHolder>{
    @NonNull
    @Override
    public RListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
/** CRIA AS VIEWS
 * CHAMADO PELO LAYOUT MANAGER */
 View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notas_view, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
/** REPASSA O CONTEUDO DO DATASET PARA A VIEW */
        TextView textView = holder.rView.findViewById(R.id.textViewNotas);
        textView.setText(rDataset.get(position));


    }

    @Override
    public int getItemCount() {
/** RETORNA O TAMANHO DO DATASET */
        return rDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
/** PROVE MAIOR (NÃO ENTENDI BEM) REFERENCIA, ACESSO  */
        public View rView;
        public ViewHolder(View viewH){
            super(viewH);
            rView = viewH;
        }

    }

/** PROVE UM CONSTRUTOR ADEQUADO - DEPENDE DO TIPO DE DATASET */
    private ArrayList<String> rDataset;
    public RListAdapter(ArrayList<String> myDataset) {
        rDataset = myDataset;
    }

    /** PARA PODER TER O EVENTO DE CLICK E LONG CLICK NO RECYCLERVIEW */
    public interface RecyclerViewClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
    public static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private RListAdapter.RecyclerViewClickListener clickListener;

        public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView, final RecyclerViewClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
