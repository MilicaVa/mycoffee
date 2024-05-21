package com.example.mycoffee

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment

class VideoFragment: Fragment() {
    private lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        videoView = view.findViewById(R.id.videoView)
        // Postavite svoj video URI ovde
        val videoPath = "android.resource://" + activity?.packageName + "/" + R.raw.video
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        videoView.setOnCompletionListener {
            videoView.start()
        }

        // Postavljanje onTouchListenera
        videoView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Korisnik je pritisnuo na VideoView, pauzirajte video
                    videoView.pause()
                }
                MotionEvent.ACTION_UP -> {
                    // Korisnik je otpustio VideoView, nastavite reprodukciju
                    videoView.start()
                }
            }
            true // Return true to indicate the event was handled
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        videoView.start()
    }

    override fun onPause() {
        super.onPause()
        videoView.pause() // Pauzira video kada korisnik napusti fragment
    }

    override fun onResume() {
        super.onResume()
        videoView.start() // Nastavlja reprodukciju kada se fragment vrati u fokus
    }
}