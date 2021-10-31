package com.example.inspace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.inspace.adapters.ViewPagerAdapter
import com.example.inspace.databinding.ActivityMainBinding
import com.example.inspace.fragments.FavoriteFragment
import com.example.inspace.fragments.NewsListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTabs()
    }
    private fun setUpTabs(){
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(NewsListFragment(), "Home")
        adapter.addFragment(FavoriteFragment(), "Favorite")
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
        binding.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_favorite_24)
    }
}