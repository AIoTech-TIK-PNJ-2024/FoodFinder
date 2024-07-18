package com.aiotechpnj.foodfinder.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aiotechpnj.foodfinder.Items.SectionsPagerAdapter
import com.aiotechpnj.foodfinder.databinding.ActivityMainBinding
import com.aiotechpnj.foodfinder.recommendation.NutritionActivity
import com.aiotechpnj.foodfinder.search.SearchActivity
import com.aiotechpnj.foodfinder.utils.TAB_TITLES
import com.aiotechpnj.foodfinder.utils.search_data
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewPager.adapter = SectionsPagerAdapter(this@MainActivity)
            TabLayoutMediator(tabs, viewPager){ tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                val searchData = searchView.text.toString()
                if (searchBar.text == searchView.text) {
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    intent.putExtra(search_data, searchData)
                    searchBar.clearText()
                    startActivity(intent)
                }
                false
            }

            fab.setOnClickListener {
                startActivity(Intent(this@MainActivity, NutritionActivity::class.java))
            }
        }
    }
}