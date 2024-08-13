package com.example.musiclly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.example.musiclly.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayerBinding
    lateinit var exoPlayer: ExoPlayer
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

     */

    //gif 3 or yaha se us gif ko controll kiya ki gif kab show hoga or kab nhi
    //c1Yeh playerListener object Player.Listener ka anonymous implementation hai. onIsPlayingChanged method ko override karke yeh check karta hai ki ExoPlayer play kar raha hai ya nahi, aur accordingly showGif method ko call karta hai.
    var playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            showGif(isPlaying)
        }
    }

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //yaha song jese koi user clik karega to vo is activity me aayega or yaha song par media palyer lagega
        MyExoplayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text = title
            binding.songSubtitleTextView.text = subtitle
            Glide.with(binding.songCoverImageView).load(coverUrl)
                .circleCrop()
                .into(binding.songCoverImageView)

            //gif 1
            // yaha cover image par animation lagaya
            Glide.with(binding.songGifImageView).load(R.drawable.media_playing)
                .circleCrop()
                .into(binding.songGifImageView)
            exoPlayer = MyExoplayer.getInstance()!!
            binding.playerView.player = exoPlayer
            //ye show methdo ko call karne par controller hamnesa show hoga
            binding.playerView.showController()

            //
            exoPlayer.addListener(playerListener)


        }
        //c2
        //  MyExoplayer.getCurrentSong()?.apply { ... }: Agar koi current song hai toh uske details ko UI elements mein set karta hai.
        //binding.songTitleTextView.text = title: Song ka title set karta hai.
        //binding.songSubtitleTextView.text = subtitle: Song ka subtitle set karta hai.
        //Glide.with(binding.songCoverImageView)...: Glide library ka use karke song cover image ko load karta hai.
        //Glide.with(binding.songGifImageView)...: Glide library ka use karke GIF image ko load karta hai.
        //exoPlayer = MyExoplayer.getInstance()!!: ExoPlayer ka instance set karta hai.
        //binding.playerView.player = exoPlayer: PlayerView mein ExoPlayer set karta hai.
        //binding.playerView.showController(): Player controller ko show karta hai.
        //exoPlayer.addListener(playerListener): Player listener ko add karta hai.

    }

    //
    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.removeListener(playerListener)
    }


    //gif2 yaha bataya ki gif kab show honga or kab nhi
    //r4Yeh method onDestroy lifecycle method ko override karta hai aur ExoPlayer listener ko remove karta hai jab activity destroy hoti hai.
    fun showGif(show: Boolean) {
        if (show)
            binding.songGifImageView.visibility = View.VISIBLE
        else
            binding.songGifImageView.visibility = View.INVISIBLE
    }
}