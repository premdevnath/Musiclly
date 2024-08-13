package com.example.musiclly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musiclly.databinding.ActivityMainBinding
import com.example.musiclly.model.CategoryModel
import com.example.musiclly.model.SongModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects


//clode me app banaka fayda ki hame app me chnge ke liye app ko update nhi karan hai hum kevalfirebas ko update karet hai to vo chnges app mebho hojange

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var categoryAdapter: adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("hi0", "getCategories: ")
        getCategories()

        //yaha se section de sakte hai trending,hindi,english
        //yaha hum is fun me jis section ko show karana hai uske perameter pass karenge
        setupSection("section_1",binding.section1Main,binding.section1Tilte,binding.section1RecyclerView)

        setupSection("section_2",binding.section2Main,binding.section2Tilte,binding.section2RecyclerView)
        setupSection("section_3",binding.section3Main,binding.section3Tilte,binding.section3RecyclerView)
        setupSection("mostly_played",binding.mostpaly,binding.sectionMTilte,binding.sectionMRecyclerView)

        Log.d("last", "getCategories: ${binding.section2Main}")
        Log.d("last", "getCategories: ${binding.section3Main}")

    }

    //abhi kosa music paly ho rah ahai ye showkarne ke liye

    override fun onResume() {
        super.onResume()
        showPlayer()
    }
fun showPlayer()
{
    MyExoplayer.getCurrentSong()?.let {
        binding.playerView.visibility=View.VISIBLE
        binding.songTitleTextView.text="playing:"+it.title
        Glide.with(binding.songCoverImageView).load(it.coverUrl)
            .apply (
                RequestOptions().transform(RoundedCorners(32))
                ).into(binding.songCoverImageView)
            }?:run {
                binding.playerView.visibility=View.GONE
            }
    }


    //category
    fun getCategories() {

        Log.d("hi1", "getCategories: ")
        FirebaseFirestore.getInstance().collection("category")

            .get().addOnSuccessListener {
                Log.d("hi1", "getCategories: ")
                val categoryList = it.toObjects(CategoryModel::class.java)
                setupCategoryRecyclerView(categoryList)
                Log.d("hi4", "getCategories:${categoryList} ")
            }
    }

    fun setupCategoryRecyclerView(categoryList: List<CategoryModel>) {
        categoryAdapter = adapter(categoryList)
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

    //section

    //r1 isme hum kisi section trending ke ya english ppe clik karenge to song list acvtivty me jaynge

    fun setupSection(id:String,mainlayout:RelativeLayout,title:TextView,recylerview:RecyclerView) {
        Log.d("hic", "getCategories: ${title} ")
        Log.d("hic", "getCategories: ${mainlayout} ")
        Log.d("hic", "getCategories: ${id} ")
        FirebaseFirestore.getInstance().collection("sections")

            .document(id)
            .get().addOnSuccessListener {
                Log.d("hi1", "getCategories: ")
                val section = it.toObject(CategoryModel::class.java)
                section?.apply {
                    //hamne section ki visibility ko gone kiya hum jab inme
                    //or yaha set kiya ki isme data hoga to hi ye visible honge
                   mainlayout.visibility=View.VISIBLE
                    title.text=name
                    Log.d("hic", "getCategories: ${name} ")
                    recylerview.layoutManager=LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    recylerview.adapter=sladapter(songs)
                    Log.d("hic", "getCategories: ${songs} ")
                    //smlayout
                   mainlayout.setOnClickListener(){
                        Songlist.category =section

                        startActivity(Intent(this@MainActivity,Songlist::class.java))
                    }
                }

            }
    }
    fun mostplaySection(id:String,mainlayout:RelativeLayout,title:TextView,recylerview:RecyclerView) {

        FirebaseFirestore.getInstance().collection("sections")

            .document(id)
            .get().addOnSuccessListener {
                //get most played songs
                FirebaseFirestore.getInstance().collection("songs")
                    .orderBy("count",Query.Direction.DESCENDING)
                    .limit(5)
                    .get().addOnSuccessListener { snapshot->
                        val songModellist=snapshot.toObjects<SongModel>()
                        val songlist=songModellist.map {
                            it.id
                        }.toList()
                        val section = it.toObject(CategoryModel::class.java)
                        section?.apply {
                            //hamne section ki visibility ko gone kiya hum jab inme
                            //or yaha set kiya ki isme data hoga to hi ye visible honge

                            section.songs=songlist
                            mainlayout.visibility=View.VISIBLE
                            title.text=name
                            Log.d("hic", "getCategories: ${name} ")
                            recylerview.layoutManager=LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                            recylerview.adapter=sladapter(songs)
                            Log.d("hic", "getCategories: ${songs} ")
                            //smlayout
                            mainlayout.setOnClickListener(){
                                Songlist.category =section

                                startActivity(Intent(this@MainActivity,Songlist::class.java))
                            }
                        }
                    }


            }
    }

}