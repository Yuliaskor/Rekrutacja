package com.example.inspace.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.inspace.ItemActivity
import com.example.inspace.News
import com.example.inspace.adapters.NewsAdaptor
import com.example.inspace.databinding.FragmentNewslistBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NewsListFragment : Fragment() {

    private lateinit var binding: FragmentNewslistBinding
    private lateinit var newsAdapter: NewsAdaptor
    private var newsList: ArrayList<News> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewslistBinding.inflate(inflater, container, false)
        newsAdapter = NewsAdaptor(mutableListOf())
        newsAdapter = NewsAdaptor(newsList)
        fetchNews()
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
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchNews() {
        val queue = Volley.newRequestQueue(activity)
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
       // val url = "https://api.spaceflightnewsapi.net/v3/articles"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                println("before function body")
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    var date = newsJsonObject.getString("publishedAt")
                    date = date.replace("Z", "")
                    val localDateTime = LocalDateTime.parse(date)
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val publishedAt = formatter.format(localDateTime)
                    val news = News(
                        newsJsonObject.getString("title"),
                        // newsJsonObject.getString("summary"),
                        newsJsonObject.getString("description"),
                        //newsJsonObject.getString("imageUrl"),
                        newsJsonObject.getString("urlToImage"),
                        publishedAt
                    )

                    newsArray.add(news)
                }
                newsAdapter.updateData(newsArray)
            },
            {
                println("Error")
            }

        )
        queue.add(jsonObjectRequest)
    }

}