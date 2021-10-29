package com.example.inspace.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inspace.DataBaseManager
import com.example.inspace.ItemActivity
import com.example.inspace.News
import com.example.inspace.adapters.NewsAdaptor
import com.example.inspace.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var newsAdapter: NewsAdaptor
    private var newsList: ArrayList<News> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val db = DataBaseManager(requireActivity())
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        newsAdapter = NewsAdaptor(mutableListOf())

       //db.deleteDate()
        var data = db.readData()

        for (i in 0 until data.size) {
            println(data[i])
           // newsAdapter.addData(data[i])
            newsList.add(data[i])
        }
        newsAdapter.notifyDataSetChanged()
        //newsAdapter = NewsAdaptor(newsList)
        binding.rvNews.layoutManager = LinearLayoutManager(activity)
        binding.rvNews.isClickable = true
        binding.rvNews.setHasFixedSize(true)
        binding.rvNews.adapter = newsAdapter

        newsAdapter.setOnItemClickListener(object : NewsAdaptor.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(activity, "You click on item $position", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(activity, ItemActivity::class.java)
                intent.putExtra("title", newsList[position].title)
                intent.putExtra("img", newsList[position].img)
                intent.putExtra("description", newsList[position].description)
                intent.putExtra("data", newsList[position].data)
                startActivity(intent)
            }

        })

        binding.swiper.setOnRefreshListener {
            newsAdapter.clear()
            newsList.clear()

            data = db.readData()

            for (i in 0 until data.size) {
                println(data[i])
                // newsAdapter.addData(data[i])
                newsList.add(data[i])
            }
            newsAdapter.updateData(newsList)

            binding.swiper.isRefreshing = false
        }

        return binding.root
    }

}