package com.ihsanarslan.mynotes.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.viewmodel.SignInViewModel

class signinFragment : Fragment() {
    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        view.findViewById<Button>(R.id.button2).setOnClickListener {
            signin(view)
        }
        val email= view.findViewById<EditText>(R.id.mailsignin)
        val password=view.findViewById<EditText>(R.id.pass1)

        deleteSpace(email)
        deleteSpace(password)
    }

    fun signin(view:View){
        val emailInput= view.findViewById<TextInputLayout>(R.id.mailLayoutsignin)
        val passInput= view.findViewById<TextInputLayout>(R.id.pass1Layoutt)
        val email= view.findViewById<EditText>(R.id.mailsignin).text.toString()
        val password=view.findViewById<EditText>(R.id.pass1).text.toString()

        if (email.isEmpty()){
            emailInput.error = requireContext().getString(R.string.empty_mail)
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailInput.error = requireContext().getString(R.string.error_mail)
        }
        else if (password.isEmpty()){
            emailInput.error = null
            passInput.error = requireContext().getString(R.string.empty_pass)
        }
        else if (password.length<6){
            emailInput.error = null
            passInput.error = requireContext().getString(R.string.short_pass)
        }
         else {
            viewModel.signIn(emailInput,passInput,email,password,requireContext(),view)
        }
    }
    fun deleteSpace(editText: EditText){
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                if (input.contains(" ")) {
                    val filtered = input.replace(" ", "")
                    editText.setText(filtered)
                    editText.setSelection(filtered.length)
                }
            }
        })
    }

}