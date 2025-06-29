package com.kenttravis.pantrypal.ui.home

import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.R
import com.kenttravis.pantrypal.databinding.ActivityMainBinding
import com.kenttravis.pantrypal.sources.local.AuthManager

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val vm by viewModels<ScanBarcodeViewModel>{ PantryPalViewModelFactory}
    lateinit var container: FragmentContainerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNav: BottomNavigationView = binding.bottomNavigation
        container = binding.fragmentContainerView

        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_scan ->{
                    scanBarcode()
                }
                R.id.nav_recipes->{
                    container.getFragment<Fragment>().findNavController().navigate(R.id.action_global_recipesFragment)
                }
                R.id.nav_chatbot->{
                    container.getFragment<Fragment>().findNavController().navigate(R.id.action_global_chatListFragment)
                }
                R.id.nav_pantry->{
                    container.getFragment<Fragment>().findNavController().navigate(R.id.action_global_homeFragment)
                }
                R.id.nav_account->{
                    container.getFragment<Fragment>().findNavController().navigate(R.id.action_global_userProfileFragment)
                }
            }
            true
        }

        bottomNav.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                bottomNav.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Find the camera menu item view and scale it
                val menuView = bottomNav.getChildAt(0) as BottomNavigationMenuView
                val cameraItem = menuView.getChildAt(2) // Camera is the 3rd item (index 2)

                // Scale the camera icon
                cameraItem.scaleX = 2f
                cameraItem.scaleY = 2f
                // Disable click effects and animations
//                cameraItem.isClickable = false
//                cameraItem.isFocusable = true
//                cameraItem.background = null // Remove ripple effect
//                cameraItem.foreground = null // Remove any foreground effects
            }
        })
    }

    private fun scanBarcode() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(this)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                val result = barcode.rawValue
//                Toast.makeText(this, result ?: "No data found", Toast.LENGTH_SHORT).show()
                // Handle the barcode result here
                handleBarcodeResult(result)
            }
            .addOnCanceledListener {
                // Task canceled
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                Toast.makeText(this, "Scan failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleBarcodeResult(barcodeData: String?) {
        // Handle the scanned barcode data here
        // For example, navigate to another fragment or perform some action
        barcodeData?.let {
            vm.getData(AuthManager.getToken(this)!!, barcodeData){ found, data->
                if(found){
                    Log.d("INFO","product ress: "+data?.product_name)
                    container.getFragment<Fragment>().findNavController().navigate(R.id.action_global_productDetailFragment)
                }else{
                    Toast.makeText(this, "Product not found", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}