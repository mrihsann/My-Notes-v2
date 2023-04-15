package com.ihsanarslan.mynotes.viewmodel

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.util.auth
import com.ihsanarslan.mynotes.view.LoginFragmentDirections
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) :BaseViewModel(application) {

    fun creatUser(email: String,password:String,context: Context,view:View,emailLayout: TextInputLayout,passwordLayout:TextInputLayout,password2Layout:TextInputLayout){

        launch {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(context,R.string.welcome_my_notes, Toast.LENGTH_LONG).show()
                val action= LoginFragmentDirections.actionLoginFragmentToListFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener {
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val result = task.result?.signInMethods?.size ?: 0
                            if (result > 0) {
                                emailLayout.error = R.string.alredy_register_user.toString()
                            } else {
                                // Kullanıcı yok.
                            }
                        } else {
                            // Sorgu başarısız oldu.
                            Toast.makeText(context, R.string.unknown_error_register, Toast.LENGTH_LONG).show()
                        }
                    }
                passwordLayout.error = null
                password2Layout.error = null
            }
        }
    }
}