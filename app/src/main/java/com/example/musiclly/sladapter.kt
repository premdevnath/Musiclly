package com.example.musiclly

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musiclly.databinding.SonglistitemBinding
import com.example.musiclly.model.SongModel
import com.google.firebase.firestore.FirebaseFirestore

class sladapter  (private  val songIdList : List<String>): RecyclerView.Adapter<sladapter.MyViewHolder>() {

    class MyViewHolder(private val binding: SonglistitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //bind data with view
        fun bindData(songId: String) {

            //yaha fire base me jo songuplode kiye unhe recyler view me add kiya
            FirebaseFirestore.getInstance().collection("songs")
                .document(songId).get()
                .addOnSuccessListener {
                    val song = it.toObject(SongModel::class.java)
                    song?.apply {
                        binding.songTitleTextView.text = title
                        Log.d("hip", "getCategories:${title} ")
                        binding.songSubtitleTextView.text = subtitle
                        Glide.with(binding.songCoverImageView).load(coverUrl)
                            .apply(
                                RequestOptions().transform(RoundedCorners(32))
                            )
                            .into(binding.songCoverImageView)
                        Log.d("hi1", "getCategories:${coverUrl} ")
                    }
                }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            SonglistitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songIdList.size
    }


    override fun onBindViewHolder(holder: sladapter.MyViewHolder, position: Int) {
        holder.bindData(songIdList[position])
    }
}
