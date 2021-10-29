package com.example.inspace

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.inspace.databinding.ActivityItemBinding
import com.squareup.picasso.Picasso

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding

    private var db: DataBaseManager =  DataBaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val title = bundle!!.getString("title")
        val img = bundle.getString("img")
        val description = bundle.getString("description")
        val data = bundle.getString("data")
        //  val id = bundle.getString("id")

        binding.itemDescription.text = description
        binding.itemTitle.text = title
        Picasso.get().load(img).into(binding.itemImage)
        binding.floatingButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#fefefe"));

        binding.floatingButton.setOnClickListener {
            db.insertData(News(title!!,description!!,img!!,data!!))
            binding.floatingButton.rippleColor = Color.parseColor("#5970e5");
            binding.floatingButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d71f2f"));
        }
    }
}