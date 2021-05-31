package com.deventhirran.guitarapps

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_second, container, false)
        val link = arguments?.getString("link").toString()
        val pdfView : PDFView = root.findViewById(R.id.pdfView)
        var pdfurl = ""

        fun show() {
            val connection = URL(pdfurl).openConnection() as HttpURLConnection
            val txt = root.findViewById<TextView>(R.id.textView)

            CoroutineScope(Dispatchers.IO).launch {
                val stream =BufferedInputStream(connection.inputStream)
                withContext(Dispatchers.Main) {
                    // then update the UI
                    pdfView.fromStream(stream)

                        .load();
                    txt.text = link
                }
            }
        }

        if (link != "intro") {
            pdfurl = link
            show()
        } else {
//            pdfurl = "https://firebasestorage.googleapis.com/v0/b/guitarapps-c52f7.appspot.com/o/WHT2109-01%20AGREEMENT%20SMART%20CHILD%20TRACKER%20USING%20IOT.pdf?alt=media&token=bb97ea97-5204-4351-90f3-8689d4532c93"
            db.collection("intro").document("view")
                .get()
                .addOnSuccessListener {
                    pdfurl = it.getField<String>("pdf").toString()
                    show()
                }

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_second).setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }
}