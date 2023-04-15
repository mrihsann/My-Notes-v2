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

class SignInViewModel(application: Application) :BaseViewModel(application) {
    fun signIn(emailInput:TextInputLayout,passInput:TextInputLayout,email:String,password:String,context: Context,view:View){
        launch {
            emailInput.error = null
            passInput.error = null
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(context, R.string.welcome_my_notes, Toast.LENGTH_LONG).show()
                val action= LoginFragmentDirections.actionLoginFragmentToListFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener {
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val result = task.result?.signInMethods?.size ?: 0
                            if (result > 0) {
                                //kullanıcı var
                                passInput.error = R.string.wrong_pass.toString()
                            } else {
                                // Kullanıcı yok.
                                emailInput.error = R.string.user_not_registered.toString()
                            }
                        } else {
                            // Sorgu başarısız oldu.
                            Toast.makeText(context, R.string.error_login, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }
}