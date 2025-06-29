package com.kenttravis.pantrypal.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kenttravis.pantrypal.ui.home.MainActivity
import com.kenttravis.pantrypal.R
import com.kenttravis.pantrypal.databinding.ActivityLoginBinding
import com.kenttravis.pantrypal.sources.local.AuthManager

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if(AuthManager.hasToken(this)){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNav: BottomNavigationView = binding.bottomNavigationView
        val container: FragmentContainerView = binding.fragmentContainerView

        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_login ->{
                    container.getFragment<Fragment>().findNavController().navigate(R.id.action_global_loginFragment)
                }
                R.id.menu_register ->{
                    container.getFragment<Fragment>().findNavController().navigate(R.id.action_global_registerFragment)
                }
            }
            true
        }
    }
}