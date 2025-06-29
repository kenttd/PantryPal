package com.kenttravis.pantrypal.ui.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.R
import com.kenttravis.pantrypal.databinding.FragmentFilterResultBinding
import com.kenttravis.pantrypal.databinding.ItemMealBinding
import com.kenttravis.pantrypal.sources.local.AuthManager

// Data class for Meal
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)

class FilterResultFragment : Fragment() {

    val args: FilterResultFragmentArgs by navArgs()
    val vm by activityViewModels<RecipesViewModel>{ PantryPalViewModelFactory }
    lateinit var binding: FragmentFilterResultBinding
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.getFilteredData(AuthManager.getToken(requireContext())!!, args.type, args.id.toString())
        binding = FragmentFilterResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        setupToolbar()
    }

    private fun setupRecyclerView() {
        mealsAdapter = MealsAdapter(
            onMealClick = { meal ->
                // Navigate to meal detail or handle meal click
                 val action = FilterResultFragmentDirections.actionFilterResultFragmentToRecipeDetailFragment(meal.idMeal)
                 findNavController().navigate(action)
            }
        )

        binding.mealsRecyclerView.apply {
            adapter = mealsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeData() {
        vm.result.observe(viewLifecycleOwner) { meals ->
            if (meals != null) {
                val mealList = meals.map {
                    Meal(
                        idMeal = it.idMeal,
                        strMeal = it.strMeal,
                        strMealThumb = it.strMealThumb
                    )
                }
                mealsAdapter.submitList(mealList)

                // Show/hide empty state
                binding.emptyStateTextView.visibility = if (mealList.isEmpty()) View.VISIBLE else View.GONE
                binding.mealsRecyclerView.visibility = if (mealList.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    private fun setupToolbar() {
        // Set title based on filter type and value
        val title = when (args.type) {
            "ingredients" -> "Recipes with ${args.id}"
            "category" -> "Category: ${args.id}"
            "area" -> "Cuisine: ${args.id}"
            else -> "Filtered Results"
        }

        binding.toolbarTitle.text = title
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}

// DiffUtil for Meals
class MealsDiffUtil : DiffUtil.ItemCallback<Meal>() {
    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.idMeal == newItem.idMeal
    }

    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem == newItem
    }
}

val mealsDiffUtil = MealsDiffUtil()

// ListAdapter for Meals
class MealsAdapter(
    private val onMealClick: (Meal) -> Unit
) : ListAdapter<Meal, MealsAdapter.ViewHolder>(mealsDiffUtil) {

    class ViewHolder(val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMealBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = getItem(position)

        holder.binding.apply {
            mealNameTextView.text = meal.strMeal

            mealImageView.load(meal.strMealThumb)

            // Handle click
            root.setOnClickListener {
                onMealClick(meal)
            }

            // Add ripple effect for better UX
            root.isClickable = true
            root.isFocusable = true
        }
        holder.binding.root.setOnClickListener{
            onMealClick(meal)
        }
    }
}