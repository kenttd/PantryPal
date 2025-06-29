package com.kenttravis.pantrypal.ui.recipes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import coil.load
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.R
import com.kenttravis.pantrypal.databinding.FragmentCategoriesBinding
import com.kenttravis.pantrypal.databinding.ItemCategoryBinding

// Data class for Category
data class Category(
    val id: Int,
    val name: String,
    val description: String = "",
    val imageUrl: String? = null,
    val imageRes: Int? = null
)

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    val vm by activityViewModels<RecipesViewModel>{ PantryPalViewModelFactory }
    private lateinit var categoriesAdapter: CategoriesAdapter
    private val originalCategoriesList = listOf(
        Category(1, "Loading...", "Quick and convenient meals"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        loadCategories()

        vm.data.observe(requireActivity()){data->
            categoriesAdapter.submitList(data.categories.map{Category(it.idCategory.toInt(),it.strCategory,it.strCategoryDescription,it.strCategoryThumb)})
        }
    }

    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesAdapter { selectedCategory ->
            val action = RecipesFragmentDirections.actionRecipesFragmentToFilterResultFragment(selectedCategory.name,"categories")
            findNavController().navigate(action)
        }

        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearch() {
        binding.searchCategoriesEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterCategories(s.toString().trim())
            }
        })
    }

    private fun loadCategories() {
        // Initially show all categories
        categoriesAdapter.submitList(originalCategoriesList)
    }

    private fun filterCategories(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalCategoriesList
        } else {
            originalCategoriesList.filter { category ->
                category.name.contains(query, ignoreCase = true) ||
                        category.description.contains(query, ignoreCase = true)
            }
        }

        categoriesAdapter.submitList(filteredList)

        // Show empty state if needed
        if (filteredList.isEmpty() && query.isNotEmpty()) {
            Toast.makeText(requireContext(), "No categories found for \"$query\"", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onCategorySelected(category: Category) {
        Toast.makeText(requireContext(), "Selected category: ${category.name}", Toast.LENGTH_SHORT).show()

        // Send result back to parent activity
        parentFragmentManager.setFragmentResult("category_selected", Bundle().apply {
            putInt("selected_category_id", category.id)
            putString("selected_category_name", category.name)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// DiffUtil for Categories
class CategoriesDiffUtil : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}

val categoriesDiffUtil = CategoriesDiffUtil()

// ListAdapter for Categories
class CategoriesAdapter(
    private val onCategoryClick: (Category) -> Unit
) : ListAdapter<Category, CategoriesAdapter.ViewHolder>(categoriesDiffUtil) {

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.binding.categoryNameTextView.text = category.name
        if(category.imageUrl!=null)holder.binding.categoryImageView.load(category.imageUrl)

        holder.binding.root.setOnClickListener {
            onCategoryClick(category)
        }
    }
}