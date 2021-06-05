package com.deventhirran.guitarapps

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class LessonFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_lesson, container, false)
        val recyclerView : RecyclerView = root.findViewById(R.id.lessonRecyclerView)
        val youtubePlayerView = root.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        val level = arguments?.getString("level").toString()
        val song = arguments?.getString("song").toString()

        val chordList = mutableListOf<ChordList>()
        val rvAdapter = ChordAdapter(chordList)
        var backupList = mutableListOf<ChordList>()
        fun rv() {
            recyclerView.apply {
                layoutManager = GridLayoutManager(this@LessonFragment.context, 4)
                adapter = rvAdapter
            }
        }

        var cTime = -1
        CoroutineScope(Dispatchers.IO).launch {
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    super.onReady(youTubePlayer)
//                    val videoId = "aTvJWKu9bMs"
//                    youTubePlayer.cueVideo(videoId, 0f)
//                }

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    super.onCurrentSecond(youTubePlayer, second)
                    val p = second.toInt()
                    val s = chordList.size

                    if (p < s) {
                        if (p > cTime) {
                            val c = chordList[p].chord
                            if (p > 0) {
                                val c2 = chordList[p - 1].chord
                                chordList[p-1] = ChordList(c2, "0")

                                chordList[p] = ChordList(c, "1")
                                recyclerView.scrollToPosition(p)
                                rvAdapter.notifyItemRangeChanged(p-1, 2)

                            } else {
                                chordList[p] = ChordList(c, "1")
                                rvAdapter.notifyItemChanged(p)
                            }

                        }
                    }

                    cTime = p
                }

            })

            withContext(Dispatchers.Main) {

            }
        }
        /*
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                val videoId = "aTvJWKu9bMs"
                youTubePlayer.cueVideo(videoId, 0f)
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                val p = second.toInt()
                val s = chordList.size

                if (p < s) {
                    if (p > cTime) {
                        val c = chordList[p].chord
                        if (p > 0) {
                            val c2 = chordList[p - 1].chord
                            chordList[p-1] = ChordList(c2, "0")

                            chordList[p] = ChordList(c, "1")
//                            d("bomoh", "F")
                            recyclerView.scrollToPosition(p)
                            rvAdapter.notifyItemRangeChanged(p-1, 2)

//                            d("bomoh", "1: $chordList")
                        } else {
//                            d("bomoh", "S")
                            chordList[p] = ChordList(c, "1")
                            rvAdapter.notifyItemChanged(p)
                        }

                    }
                }

                cTime = p
            }

        }) */

        fun showVid(id: String) {
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    val videoId = id
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }

        d("bomoh", "level: $level, song: $song")

        db.collection("tutorial").document(level).collection("list").document(song)
            .get()
            .addOnSuccessListener {
                val chord : MutableList<String> = ArrayList()
                chord.addAll(it.get("chord") as Collection<String>)
                val vidId = it.getField<String>("video").toString()
                d("bomoh", "vidId: $vidId")
                showVid(vidId)

                for (i in chord) {
//                    d("bomoh", "s: $i")
                    val splitChord = i.split(",")

                    for (c in splitChord) {
//                        d("bomoh", "s: $c")
                        chordList.add(ChordList(c, "0"))
                    }
//                    d("bomoh", "s: $chordList")
                }
                backupList = chordList
                rv()
            }

        return root
    }
}

data class ChordList (
    val chord : String,
    val stat : String
)
