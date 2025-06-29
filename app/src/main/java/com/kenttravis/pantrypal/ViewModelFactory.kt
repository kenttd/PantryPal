package com.kenttravis.pantrypal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.kenttravis.pantrypal.ui.account.ProfileViewModel
import com.kenttravis.pantrypal.ui.chatbot.ChatbotViewModel
import com.kenttravis.pantrypal.ui.home.PantryViewModel
import com.kenttravis.pantrypal.ui.home.ScanBarcodeViewModel
//import com.kenttravis.pantrypal.ui.home.ProductDetailViewModel
import com.kenttravis.pantrypal.ui.login.LoginViewModel
import com.kenttravis.pantrypal.ui.login.RegisterViewModel
import com.kenttravis.pantrypal.ui.recipes.RecipesViewModel

val PantryPalViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val application = checkNotNull(extras[APPLICATION_KEY]) as App
            val repo = application.repo
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repo)
                isAssignableFrom(RegisterViewModel::class.java) ->
                    RegisterViewModel(repo)
                isAssignableFrom(ScanBarcodeViewModel::class.java) ->
                    ScanBarcodeViewModel(repo)
                isAssignableFrom(RecipesViewModel::class.java) ->
                    RecipesViewModel(repo)
                isAssignableFrom(ChatbotViewModel::class.java) ->
                    ChatbotViewModel(repo)
                isAssignableFrom(PantryViewModel::class.java) ->
                    PantryViewModel(repo)
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repo)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}