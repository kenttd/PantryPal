package com.kenttravis.pantrypal.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.R
import com.kenttravis.pantrypal.databinding.FragmentHomeBinding
import com.kenttravis.pantrypal.databinding.ItemPantryBinding
import com.kenttravis.pantrypal.sources.local.AuthManager
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val vm by activityViewModels<PantryViewModel>{ PantryPalViewModelFactory }
    val vmScan by activityViewModels<ScanBarcodeViewModel>{ PantryPalViewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.getPantryData(AuthManager.getToken(requireContext())!!)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvPantry: RecyclerView = binding.rvPantry

        val pantryAdapter = PantryAdapter { item ->
            vmScan.setData(item)
            findNavController().navigate(R.id.action_global_productDetailFragment)
        }

        rvPantry.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        rvPantry.adapter = pantryAdapter

        // Setup swipe-to-delete
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = pantryAdapter.currentList[position]

                deleteItem(item, position, pantryAdapter)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvPantry)

        vm.data.observe(requireActivity()) { data ->
            pantryAdapter.submitList(data.data)
        }
    }

    private fun deleteItem(item: Map<String, Any>, position: Int, adapter: PantryAdapter) {
        val currentList = adapter.currentList.toMutableList()
        currentList.removeAt(position)
        adapter.submitList(currentList)

        var isUndone = false
        val snackbar = Snackbar.make(binding.root, "Item deleted", Snackbar.LENGTH_LONG)

        snackbar.setAction("UNDO") {
            isUndone = true
            val restoredList = adapter.currentList.toMutableList()
            restoredList.add(position, item)
            adapter.submitList(restoredList)
        }

        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (!isUndone) {
                    vm.deletePantryItem(AuthManager.getToken(requireContext())!!, item["id"].toString())
                }
            }
        })

        snackbar.show()
    }

}

class PantryDiffUtil : DiffUtil.ItemCallback<Map<String,Any>>() {
    override fun areItemsTheSame(oldItem: Map<String,Any>, newItem: Map<String,Any>): Boolean {
        return oldItem.get("id") == newItem.get("id")
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Map<String,Any>, newItem: Map<String,Any>): Boolean {
        return oldItem == newItem
    }
}

val pantryDiffUtil = PantryDiffUtil()

class PantryAdapter(
    private val onClick: (Map<String,Any>)->Unit
): ListAdapter<Map<String,Any>, PantryAdapter.ViewHolder>(
    pantryDiffUtil
) {
    class ViewHolder(val binding: ItemPantryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPantryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        val nestedData: Map<String, Any> = data.get("data") as Map<String, Any>
        Log.d("INFO", "allergens "+nestedData.get("allergens").toString())
        holder.binding.ivFoodImage.load(nestedData.get("image_url"))
        holder.binding.tvItemId.text = nestedData.get("product_name").toString()
        holder.binding.tvCreatedDate.text = formatTimestamp(data.get("created_at").toString())
        holder.binding.tvAllergenSample.text = nestedData.get("allergens").toString()

        holder.binding.root.setOnClickListener{
            onClick(data.get("data") as Map<String,Any>)
        }
    }
}

fun formatTimestamp(isoString: String): String {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = isoFormat.parse(isoString)

    val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return outputFormat.format(date!!)
}