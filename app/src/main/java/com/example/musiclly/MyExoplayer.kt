package com.example.musiclly

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musiclly.model.SongModel
import com.google.firebase.firestore.FirebaseFirestore


//ye ek single tun class hai or ye banai kuki hame ek gane ke bad dusra play karna hai dono sath meplay nhihonge

object MyExoplayer {
    //r1 exoplayer ko declaye kiya
    private var exoPlayer: ExoPlayer? = null
    private var currentSong: SongModel? = null

    fun getCurrentSong(): SongModel? {
        return currentSong
    }
    //c1 Yeh function currentSong ko return karta hai. Iska use karke hum current song ki details le sakte hain.

    fun getInstance(): ExoPlayer? {
        return exoPlayer
    }
    //c2 Yeh function exoPlayer ka instance return karta hai. Agar exoPlayer ko kahin aur access karna hai toh is function ka use kar sakte hain.

    fun startPlaying(context: Context, song: SongModel) {
        if (exoPlayer == null)

        //r2 yaha exp ko ini kiya
            exoPlayer = ExoPlayer.Builder(context).build()

        //c3Yahaan check kiya gaya hai ki agar exoPlayer null hai toh usko initialize karo using ExoPlayer.Builder(context).build()

        if (currentSong != song) {
            //Its a new song so start playing
            currentSong = song

            //c4 Is section mein check kiya gaya hai ki kya currentSong aur song same hain ya nahi. Agar different hain toh currentSong ko new song se update karo aur ExoPlayer mein media item set karke play karwao.

            currentSong?.url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()

            }
        }


    }

    //cont
    //most recent view dekhne ke liye
    //user kisi song ko play karega to firebase me count var banega or usme data add hote jayega
    fun updateCount() {
        currentSong?.id.let { id ->
            FirebaseFirestore.getInstance().collection("songs")

                .document(id!!)
                .get().addOnSuccessListener {
                    var latestCount = it.getLong("count")
                    if (latestCount == null) {
                        latestCount = 1L
                    } else {
                        latestCount = latestCount + 1
                    }
                    FirebaseFirestore.getInstance().collection("songs")
                        .document(id)
                        .update(mapOf("count" to latestCount))
                }
        }
    }
}