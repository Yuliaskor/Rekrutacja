package com.example.inspace.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inspace.ApiInterface
import com.example.inspace.ItemActivity
import com.example.inspace.MyDataItem
import com.example.inspace.News
import com.example.inspace.adapters.NewsAdaptor
import com.example.inspace.databinding.FragmentNewslistBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val BASE_URL = "https://api.spaceflightnewsapi.net/v3/"
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
        getMyDate()
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

    private fun getMyDate(){
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.getData()

        retrofitData!!.enqueue(object : Callback<List<MyDataItem>?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<MyDataItem>?>,
                response: Response<List<MyDataItem>?>
            ) {
                val responseBody = response.body()!!
                val newsArray = ArrayList<News>()
                    for (myData in responseBody) {
                        var date = myData.publishedAt
                        date = date.replace("Z", "")
                        val localDateTime = LocalDateTime.parse(date)
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        val publishedAt = formatter.format(localDateTime)
                        val news = News(
                            myData.title,
                           myData.summary,
                            myData.imageUrl,
                            publishedAt
                        )
                        newsArray.add(news)
                    }
                newsAdapter.updateData(newsArray)
            }

            override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                Log.d(TAG, "onFailure:" + t.message )
            }
        })
    }

}