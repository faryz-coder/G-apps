package com.deventhirran.guitarapps

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.lang.Exception

class ListOption(private val listing: MutableList<Listing>) :

    RecyclerView.Adapter<ListOption.ViewHolder>() {
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item : TextView = itemView.findViewById(R.id.li_item)
        val db = Firebase.firestore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loc = listing[position]
        val itm = loc.item
        holder.item.text = loc.item

        holder.item.setOnClickListener { v ->
            d("bomoh", "item clicked ${loc.type}")


            if (loc.type == "tutorial") {
                val bundle = bundleOf("level" to itm)
                v.findNavController().navigate(R.id.action_ListFragment_to_ListSongFragment, bundle)
            }
            if (loc.type.contains("level")) {
                val sp = loc.type.split(":")
                val bundle = bundleOf("level" to sp[1], "song" to itm)
                v.findNavController().navigate(R.id.action_ListSongFragment_to_LessonFragment, bundle)
            }

            else {
                holder.db.collection(loc.type).document(loc.item)
                    .get()
                    .addOnSuccessListener {
                        val link = it.getField<String>("pdf").toString()
                        val bundle = bundleOf("link" to link)
                        try {
                            v.findNavController().navigate(R.id.action_ListFragment_to_SecondFragment, bundle)
                        } catch (e: Exception) {}

                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return listing.size
    }

}
