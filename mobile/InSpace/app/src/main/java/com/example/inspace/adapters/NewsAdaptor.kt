package com.example.inspace.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inspace.News
import com.example.inspace.R
import com.squareup.picasso.Picasso


class NewsAdaptor(private val newsList: MutableList<News>) :
    RecyclerView.Adapter<NewsAdaptor.NewsViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class NewsViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.rv_title)
        val image: ImageView = itemView.findViewById(R.id.rv_image)
        val data: TextView = itemView.findViewById(R.id.rv_data)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv_news,
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val curNews = newsList[position]
        holder.title.text = curNews.title
        holder.data.text = curNews.data
        holder.image.setBackgroundResource(0)
        if(curNews.img != "")
        Picasso.get().load(curNews.img).resize(140, 110).into(holder.image)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun updateData(newData: ArrayList<News>) {
        newsList.clear()
        newsList.addAll(newData)

        notifyDataSetChanged()
    }

    fun clear(){
        newsList.clear()
    }

    fun addData(news: News) {
        newsList.add(news)
       // notifyItemInserted(newsList.size - 1)
        notifyDataSetChanged()
    }

    fun deleteItem(index: Int){
        newsList.removeAt(index)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }
}