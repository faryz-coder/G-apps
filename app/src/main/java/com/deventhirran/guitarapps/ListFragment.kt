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

class ListFragment : Fragment() {
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView : RecyclerView = root.findViewById(R.id.listRecyclerView)
        val type = arguments?.getString("type").toString()
        val listing = mutableListOf<Listing>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ListFragment.context)
                adapter = ListOption(listing)
            }
        }

        db.collection(type)
            .get()
            .addOnSuccessListener { results ->
                for (result in results) {
                    val item = result.id
                    listing.add(Listing(item, type))
                }
                rv()
            }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

data class Listing (
    val item: String,
    val type: String
)
