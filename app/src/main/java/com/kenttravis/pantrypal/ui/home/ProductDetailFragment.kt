package com.kenttravis.pantrypal.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.databinding.FragmentProductDetailBinding
import com.kenttravis.pantrypal.sources.local.AuthManager

class ProductDetailFragment : Fragment() {
    lateinit var binding: FragmentProductDetailBinding
    val vm by activityViewModels<ScanBarcodeViewModel>{PantryPalViewModelFactory}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product_name: TextView = binding.tvProductName
        val barcode: TextView = binding.tvBarcode
        val grade: TextView = binding.tvGrade
        val allergens: TextView = binding.tvAllergens
        val image: ImageView = binding.ivProductImage
        val ingredientRv: RecyclerView = binding.rvIngredients
        val nutrimentsRv: RecyclerView = binding.rvNutriments
        val addBtn: FloatingActionButton = binding.fabAddToPantry

        val ingredientAdapter = IngredientAdapter()
        val nutrimentsAdapter = NutrimentsAdapter()
        ingredientRv.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        ingredientRv.adapter = ingredientAdapter
        nutrimentsRv.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        nutrimentsRv.adapter = nutrimentsAdapter

        vm.data.observe(requireActivity()){data->
            product_name.text = data.product_name
            barcode.text = data.barcode
            grade.text = if(data.grade==null)"No grade available" else "Grade: ${data.grade.toUpperCase()}"
            allergens.text = if(data.allergens==null)"No allergen data found" else data.allergens
            if(data.image_url!=null)image.load(data.image_url)
            ingredientAdapter.submitList(data.ingredients)
            nutrimentsAdapter.submitList(data.nutriments?.toList())
        }

        addBtn.setOnClickListener{
            vm.addToPantry(AuthManager.getToken(requireContext())!!){success: Boolean ->
                if(success){
                    Toast.makeText(requireContext(), "Added to pantry", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(), "Failed adding to pantry", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}