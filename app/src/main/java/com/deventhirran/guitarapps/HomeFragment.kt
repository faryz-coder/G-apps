package com.deventhirran.guitarapps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // INTRO
        view.findViewById<CardView>(R.id.card_intro).setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_SecondFragment)
        }
        //LESSON
        view.findViewById<CardView>(R.id.card_lesson).setOnClickListener {
            val bundle = bundleOf("type" to "lesson")
            it.findNavController().navigate(R.id.action_HomeFragment_to_ListFragment, bundle)
        }
        //GUITAR VIEW
        view.findViewById<CardView>(R.id.card_guitarView).setOnClickListener { v ->
            db.collection("guitar").document("view")
                .get()
                .addOnSuccessListener {
                    val link = it.getField<String>("pdf").toString()
                    val bundle = bundleOf("link" to link)
                    v.findNavController().navigate(R.id.action_HomeFragment_to_SecondFragment, bundle)
                }
        }
        // CHORD TRAINING
        view.findViewById<CardView>(R.id.card_chord).setOnClickListener {
            val bundle = bundleOf("type" to "chord")
            it.findNavController().navigate(R.id.action_HomeFragment_to_ListFragment, bundle)
        }
        // TUTORIAL
        view.findViewById<CardView>(R.id.card_tutorial).setOnClickListener {
//            it.findNavController().navigate(R.id.action_HomeFragment_to_LessonFragment)
            val bundle = bundleOf("type" to "tutorial")
            it.findNavController().navigate(R.id.action_HomeFragment_to_ListFragment, bundle)
        }


    }
}