package com.deventhirran.guitarapps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListSongFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_list_song, container, false)
        val recyclerView : RecyclerView = root.findViewById(R.id.songListRecyclerView)
        val type = arguments?.getString("level").toString()
        val listing = mutableListOf<Listing>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ListSongFragment.context)
                adapter = ListOption(listing)
            }
        }

        db.collection("tutorial").document(type).collection("list")
            .get()
            .addOnSuccessListener { results ->
                for (result in results) {
                    val item = result.id
                    listing.add(Listing(item, "level:$type"))
                }
                rv()
            }
        return root
    }

}