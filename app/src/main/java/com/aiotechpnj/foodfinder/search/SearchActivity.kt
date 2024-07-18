package com.aiotechpnj.foodfinder.search

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aiotechpnj.foodfinder.databinding.ActivitySearchBinding
import com.aiotechpnj.foodfinder.utils.JsonLoader
import com.aiotechpnj.foodfinder.utils.search_data
import com.aiotechpnj.foodfinder.utils.show

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchData = intent.getStringExtra(search_data)
        binding.apply {
            rvSearchResult.layoutManager = LinearLayoutManager(this@SearchActivity)
            val adapter = SearchAdapter()
            val json = JsonLoader().loadJSONFromAsset(this@SearchActivity, "nutrition.json")
            val data = JsonLoader().parseJSONFromAsset(json ?: "")

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            searchBar.setText(searchData)
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                if(searchBar.text.isNotEmpty()){
                    val result = data.filter { it.name.lowercase().contains(searchBar.text.toString().lowercase()) }
                    if (result.isNotEmpty()){
                        errorMessage.show(false)
                        rvSearchResult.show(true)
                        adapter.submitList(result)
                        rvSearchResult.adapter = adapter
                    } else {
                        errorMessage.show(true)
                        rvSearchResult.show(false)
                    }
                }
                false
            }

            val result = data.filter { it.name.lowercase().contains(searchData?.lowercase() ?: "") }
            if (result.isNotEmpty()){
                errorMessage.show(false)
                rvSearchResult.show(true)
                adapter.submitList(result)
                rvSearchResult.adapter = adapter
            } else {
                errorMessage.show(true)
                rvSearchResult.show(false)
            }
        }
    }
}