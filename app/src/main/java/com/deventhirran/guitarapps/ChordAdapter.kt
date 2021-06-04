package com.deventhirran.guitarapps

import android.graphics.Color
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.lang.Exception

class ChordAdapter(private val chordList: MutableList<ChordList>) :
    RecyclerView.Adapter<ChordAdapter.ViewHolder>() {
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chord : TextView = itemView.findViewById(R.id.ch_chord)
        val lay: ConstraintLayout = itemView.findViewById(R.id.ch_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chord_row, parent, false )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loc = chordList[position]
        holder.chord.text = loc.chord

        if (loc.stat != "0") {
            holder.lay.setBackgroundColor(Color.CYAN)
            d("bomoh", "position : $position")
            try {
                d("bomoh", "fk : ${chordList[position-1]}")
            } catch (e: Exception) {}

        } else {
            holder.lay.setBackgroundColor(Color.GRAY)
        }
    }

    override fun getItemCount(): Int {
        return chordList.size
    }

}
