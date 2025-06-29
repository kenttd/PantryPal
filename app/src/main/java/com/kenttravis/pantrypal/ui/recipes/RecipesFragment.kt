package com.kenttravis.pantrypal.ui.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.R
import com.kenttravis.pantrypal.databinding.FragmentLoginBinding
import com.kenttravis.pantrypal.databinding.FragmentRecipesBinding
import com.kenttravis.pantrypal.sources.local.AuthManager

class RecipesFragment : Fragment() {
    lateinit var binding: FragmentRecipesBinding
    val vm by activityViewModels<RecipesViewModel>{ PantryPalViewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.getData(AuthManager.getToken(requireContext())!!)
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        val adapter = FilterPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        // Optional: Disable user swiping if you want tab-only navigation
        // binding.viewPager.isUserInputEnabled = false
    }

    private fun setupTabLayout() {
        // Connect TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Areas"
                    tab.setIcon(R.drawable.location_on_24px)
                }
                1 -> {
                    tab.text = "Categories"
                    tab.setIcon(R.drawable.category_24px)
                }
                2 -> {
                    tab.text = "Ingredients"
                    tab.setIcon(R.drawable.grocery_24px)
                }
            }
        }.attach()
    }
}

class FilterPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AreasFragment()
            1 -> CategoriesFragment()
            2 -> IngredientsFragment()
            else -> AreasFragment()
        }
    }
}