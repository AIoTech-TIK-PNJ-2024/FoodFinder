package com.aiotechpnj.foodfinder.Items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.aiotechpnj.foodfinder.databinding.FragmentItemsBinding
import com.aiotechpnj.foodfinder.utils.ARG_POSITION
import com.aiotechpnj.foodfinder.utils.JsonLoader

class ItemsFragment : Fragment() {

    private var _binding: FragmentItemsBinding? = null
    private val binding get() = _binding!!
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvItems.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = ItemListAdapter()

            arguments?.let {
                position = it.getInt(ARG_POSITION, 0)
            }

            val json = JsonLoader().loadJSONFromAsset(requireActivity(), "nutrition.json")
            val data = JsonLoader().parseJSONFromAsset(json ?: "")

            when (position) {
                1 -> {
                    val lowCal = data.filter { it.calories!! in 0.0.. 245.0}
                    adapter.submitList(lowCal)
                    rvItems.adapter = adapter
                }
                else -> {
                    val highCal = data.filter { it.calories!! >= 246.0}
                    adapter.submitList(highCal)
                    rvItems.adapter = adapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}