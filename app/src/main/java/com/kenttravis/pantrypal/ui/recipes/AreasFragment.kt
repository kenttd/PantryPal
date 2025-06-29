package com.kenttravis.pantrypal.ui.recipes


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.databinding.FragmentAreasBinding
import com.kenttravis.pantrypal.databinding.ItemAreaBinding
import com.kenttravis.pantrypal.sources.local.AuthManager

class AreasFragment : Fragment() {

    private var _binding: FragmentAreasBinding? = null
    private val binding get() = _binding!!
    val vm by activityViewModels<RecipesViewModel>{ PantryPalViewModelFactory }

    private lateinit var areasAdapter: AreasAdapter
    private val originalAreasList = listOf(
        "Loading..."
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAreasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        loadAreas()
        vm.data.observe(requireActivity()){data->
            areasAdapter.submitList(data.areas.map{it.strArea})
        }
    }

    private fun setupRecyclerView() {
        areasAdapter = AreasAdapter { selectedArea ->
            val action = RecipesFragmentDirections.actionRecipesFragmentToFilterResultFragment(selectedArea,"areas")
            findNavController().navigate(action)
        }

        binding.areasRecyclerView.apply {
            adapter = areasAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearch() {
        binding.searchAreasEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterAreas(s.toString().trim())
            }
        })
    }

    private fun loadAreas() {
        // Initially show all areas
        areasAdapter.submitList(originalAreasList)
    }

    private fun filterAreas(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalAreasList
        } else {
            originalAreasList.filter { area ->
                area.contains(query, ignoreCase = true)
            }
        }

        areasAdapter.submitList(filteredList)

        // Show empty state if needed
        if (filteredList.isEmpty() && query.isNotEmpty()) {
            Toast.makeText(requireContext(), "No areas found for \"$query\"", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onAreaSelected(area: String) {
        Toast.makeText(requireContext(), "Selected area: $area", Toast.LENGTH_SHORT).show()

        // Send result back to parent activity
        parentFragmentManager.setFragmentResult("area_selected", Bundle().apply {
            putString("selected_area", area)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// DiffUtil for Areas
class AreasDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

val areasDiffUtil = AreasDiffUtil()

// ListAdapter for Areas
class AreasAdapter(
    private val onAreaClick: (String) -> Unit
) : ListAdapter<String, AreasAdapter.ViewHolder>(areasDiffUtil) {

    class ViewHolder(val binding: ItemAreaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAreaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val area = getItem(position)
        holder.binding.areaNameTextView.text = area

        holder.binding.root.setOnClickListener {
            onAreaClick(area)
        }
    }
}