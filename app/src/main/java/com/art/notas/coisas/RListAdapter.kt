package com.art.notas.coisas

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.art.notas.R

import java.util.ArrayList

class RListAdapter(
        /** PROVE UM CONSTRUTOR ADEQUADO - DEPENDE DO TIPO DE DATASET  */
        private val rDataset: ArrayList<String>) : RecyclerView.Adapter<RListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RListAdapter.ViewHolder {
        /** CRIA AS VIEWS
         * CHAMADO PELO LAYOUT MANAGER  */
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.notas_view, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /** REPASSA O CONTEUDO DO DATASET PARA A VIEW  */
        val textView = holder.rView.findViewById<TextView>(R.id.textViewNotas)
        textView.text = rDataset[position]


    }

    override fun getItemCount(): Int {
        /** RETORNA O TAMANHO DO DATASET  */
        return rDataset.size
    }

    inner class ViewHolder(
            /** PROVE MAIOR (NÃO ENTENDI BEM) REFERENCIA, ACESSO   */
            var rView: View) : RecyclerView.ViewHolder(rView)

    /** PARA PODER TER O EVENTO DE CLICK E LONG CLICK NO RECYCLERVIEW  */
    interface RecyclerViewClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    class RecyclerViewTouchListener(context: Context, recyclerView: RecyclerView, val clickListener: RecyclerViewClickListener) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector
        //private val clickListener: RListAdapter.RecyclerViewClickListener?
        //private val clickListener= clickListener

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }


}
