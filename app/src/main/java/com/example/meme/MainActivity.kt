package com.example.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    var currentImageUrl :String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadMeme()
    }
    //https://newsapi.org/v2/top-headlines?country=us&apiKey=b2e31c37853b4ddb854bdc62754ea2dc

    private fun LoadMeme(){
        val textView = findViewById<TextView>(R.id.text)
        progressBar.visibility=View.VISIBLE
 

        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url,null,
               Response.Listener { response ->
                    currentImageUrl=response.getString("url")

                    Glide.with(this).load(currentImageUrl).listener(object :RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility=View.GONE

                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility=View.GONE
                            return false
                        }
                    }).into(memeImageView)
                },
                 { })
        MySingleton.MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    fun NextMeme(view: View) {
        LoadMeme()
    }
    fun ShareMeme(view: View){
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout the meme that I got from the reddit \n$currentImageUrl")
        val chooser=Intent.createChooser(intent,"Share this meme using ...")
        startActivity(chooser)
    }
}