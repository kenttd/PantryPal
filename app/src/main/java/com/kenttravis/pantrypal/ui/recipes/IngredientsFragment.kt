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
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.R
import com.kenttravis.pantrypal.databinding.FragmentIngredientsBinding
import com.kenttravis.pantrypal.databinding.ItemIngredientBinding

// Data class for Ingredient
data class Ingredient(
    val id: Int,
    val name: String
)

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    val vm by activityViewModels<RecipesViewModel>{ PantryPalViewModelFactory }
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private val selectedIngredients = mutableSetOf<Int>()


    private val originalIngredientsList = listOf(
        Ingredient(0, "Loading...")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        loadIngredients()

        vm.data.observe(requireActivity()){data->
            ingredientsAdapter.submitList(data.ingredients.map{Ingredient(it.idIngredient.toInt(),it.strIngredient)})
        }
    }

    private fun setupRecyclerView() {
        ingredientsAdapter = IngredientsAdapter(
            selectedIngredients = selectedIngredients,
            onIngredientClick = { ingredient ->
                val action = RecipesFragmentDirections.actionRecipesFragmentToFilterResultFragment(ingredient.name,"ingredients")
                findNavController().navigate(action)
            }
        )

        binding.ingredientsRecyclerView.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearch() {
        binding.searchIngredientsEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterIngredients(s.toString().trim())
            }
        })
    }

    private fun loadIngredients() {
        // Initially show all ingredients
        ingredientsAdapter.submitList(originalIngredientsList)
    }

    private fun filterIngredients(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalIngredientsList
        } else {
            originalIngredientsList.filter { ingredient ->
                ingredient.name.contains(query, ignoreCase = true)
            }
        }

        ingredientsAdapter.submitList(filteredList)

        // Show empty state if needed
        if (filteredList.isEmpty() && query.isNotEmpty()) {
            Toast.makeText(requireContext(), "No ingredients found for \"$query\"", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// DiffUtil for Ingredients
class IngredientsDiffUtil : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}

val ingredientsDiffUtil = IngredientsDiffUtil()

// ListAdapter for Ingredients
class IngredientsAdapter(
    private val selectedIngredients: MutableSet<Int>,
    private val onIngredientClick: (Ingredient) -> Unit
) : ListAdapter<Ingredient, IngredientsAdapter.ViewHolder>(ingredientsDiffUtil) {

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = getItem(position)
        holder.binding.ingredientNameTextView.text = ingredient.name

        // Show selection state
        val isSelected = selectedIngredients.contains(ingredient.id)
        holder.binding.root.isSelected = isSelected
        holder.binding.root.alpha = if (isSelected) 0.7f else 1.0f

        // Handle click
        holder.binding.root.setOnClickListener {
            onIngredientClick(ingredient)
        }
    }
}