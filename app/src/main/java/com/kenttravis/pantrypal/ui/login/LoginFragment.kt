package com.kenttravis.pantrypal.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.kenttravis.pantrypal.ui.home.MainActivity
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.databinding.FragmentLoginBinding
import com.kenttravis.pantrypal.sources.remote.AuthenticateRequest
import kotlin.getValue

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    val vm by viewModels<LoginViewModel>{ PantryPalViewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginBtn: Button = binding.loginButton
        val email: EditText = binding.emailEditText
        val password: EditText = binding.passwordEditText
        vm.data.observe(requireActivity()){data->
            if(data!=null){
                if(!data.error){
                    vm.saveToken(data.token!!,requireContext())
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(requireContext(),data.message,Toast.LENGTH_SHORT).show()
                }
                loginBtn.isEnabled = true
                loginBtn.text = "Sign In"
            }
        }
        loginBtn.setOnClickListener {
            vm.authenticate(AuthenticateRequest(email.text.toString(),password.text.toString()))
            loginBtn.isEnabled = false
            loginBtn.text = "Logging in..."
        }
    }
}