package com.kenttravis.pantrypal.ui.recipes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.R
import com.kenttravis.pantrypal.databinding.FragmentIngredientsBinding
import com.kenttravis.pantrypal.databinding.FragmentRecipeDetailBinding
import com.kenttravis.pantrypal.databinding.ItemIngredientBinding
import com.kenttravis.pantrypal.sources.local.AuthManager
import com.kenttravis.pantrypal.sources.remote.RecipeResponse
import kotlin.reflect.full.memberProperties

class RecipeDetailFragment : Fragment() {
    val args: RecipeDetailFragmentArgs by navArgs()
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    val vm by activityViewModels<RecipesViewModel>{ PantryPalViewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.getMeal(AuthManager.getToken(requireActivity())!!,args.id)
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.recipe.observe(requireActivity()){recipe->
            binding.ivRecipeImage.load(recipe.strImageSource)
            binding.tvRecipeTitle.setText(recipe.strMeal)
            if(recipe.strMealAlternate!=null)binding.tvRecipeAlternate.setText(recipe.strMealAlternate)
            binding.tvRecipeCategory.setText(recipe.strCategory)
            binding.tvRecipeArea.setText(recipe.strArea)
            binding.tvRecipeTags.setText("Tags: "+recipe.strTags)
            binding.tvRecipeIngredients.setText(getIngredients(recipe))
            binding.tvRecipeInstructions.setText(recipe.strInstructions.replace("\r\n", "\n")
                .replace(Regex("\n{2,}"), "\n\n")
                .trim())
            if(recipe.dateModified!=null)binding.tvDateModified.setText("Last updated "+recipe.dateModified)
            else binding.tvDateModified.setText("")

            binding.layoutYoutube.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.strYoutube))
                startActivity(intent)
            }

            binding.layoutSource.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.strSource))
                startActivity(intent)
            }
        }
    }

    private fun getIngredients(recipe: RecipeResponse): StringBuilder{
        val builder = StringBuilder()

        for (i in 1..20) {
            val ingredient = RecipeResponse::class.memberProperties
                .firstOrNull { it.name == "strIngredient$i" }
                ?.get(recipe)
                ?.toString()
                ?.takeIf { it.isNotBlank() }

            val measure = RecipeResponse::class.memberProperties
                .firstOrNull { it.name == "strMeasure$i" }
                ?.get(recipe)
                ?.toString()
                ?.takeIf { it.isNotBlank() }

            if (ingredient != null) {
                if (measure != null) {
                    builder.append("$ingredient - $measure\n")
                } else {
                    builder.append("$ingredient\n")
                }
            }
        }

        return builder
    }
}