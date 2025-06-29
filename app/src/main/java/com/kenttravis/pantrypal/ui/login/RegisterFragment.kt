package com.kenttravis.pantrypal.ui.login

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.databinding.FragmentRegisterBinding
import com.kenttravis.pantrypal.sources.remote.RegisterRequest
import java.util.Calendar

class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    val vm by viewModels<RegisterViewModel>{ PantryPalViewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name: EditText = binding.nameEditText
        val email: EditText = binding.emailEditText
        val dob: EditText = binding.dobEditText
        val password: EditText = binding.passwordEditText
        val password_confirm: EditText = binding.confirmPasswordEditText

        binding.dobEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "${month + 1}/$dayOfMonth/$year"
                    binding.dobEditText.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.registerButton.setOnClickListener{
            vm.register(RegisterRequest(name.text.toString(),password.text.toString(), password_confirm.text.toString(),dob.text.toString(), email.text.toString()))
        }

        vm.data.observe(requireActivity()){data->
            Toast.makeText(requireContext(),data.message,Toast.LENGTH_LONG).show()
        }
    }
}