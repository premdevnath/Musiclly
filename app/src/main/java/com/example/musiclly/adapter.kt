package com.example.musiclly

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musiclly.databinding.CitemBinding
import com.example.musiclly.model.CategoryModel

class adapter  (private val categoryList : List<CategoryModel>) :
    RecyclerView.Adapter<adapter.MyViewHolder>() {

    class MyViewHolder(private val binding :  CitemBinding) :
        RecyclerView.ViewHolder(binding.root){
        //bind the data with views
        fun bindData(category : CategoryModel){
            binding.nameTextView.text = category.name
            Glide.with(binding.coverImageView).load(category.coverurl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                )
                .into(binding.coverImageView)
            Log.d("hiima", "getCategories: ${category.coverurl} ")
            Log.d("hiima", "getCategories: ${binding.coverImageView} ")
            //Start SongsList Activity
            val context = binding.root.context
            binding.root.setOnClickListener {
                Songlist.category = category
                context.startActivity(Intent(context,Songlist::class.java))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(categoryList[position])
    }

}