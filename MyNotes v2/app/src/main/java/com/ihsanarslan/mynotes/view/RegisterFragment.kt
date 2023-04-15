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
import com.ihsanarslan.mynotes.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {
    private lateinit var viewModel: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        view.findViewById<Button>(R.id.button).setOnClickListener {
            signup(view)
        }
        val password=view.findViewById<EditText>(R.id.pass1Reg)
        val password2=view.findViewById<EditText>(R.id.pass2)
        val mail=view.findViewById<EditText>(R.id.mail)

        deleteSpace(password)
        deleteSpace(password2)
        deleteSpace(mail)

    }

    fun signup(view:View){
        val email= view.findViewById<EditText>(R.id.mail).text.toString()
        val emailLayout= view.findViewById<TextInputLayout>(R.id.mailLayout)
        val password=view.findViewById<EditText>(R.id.pass1Reg).text.toString()
        val passwordLayout=view.findViewById<TextInputLayout>(R.id.pass1RegLayout)
        val password2=view.findViewById<EditText>(R.id.pass2).text.toString()
        val password2Layout=view.findViewById<TextInputLayout>(R.id.pass2Layout)

        if (email.isEmpty()) {
            emailLayout.error = requireContext().getString(R.string.empty_mail)
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            passwordLayout.error = null
            password2Layout.error = null
            emailLayout.error = requireContext().getString(R.string.error_mail)
        }
        else if (password.isEmpty()) {
            passwordLayout.error = requireContext().getString(R.string.empty_pass)
            emailLayout.error =null
        }
        else if (password.length<6) {
            passwordLayout.error = requireContext().getString(R.string.short_pass)
            emailLayout.error =null
        }
        else if (password2.isEmpty()) {
            passwordLayout.error = null
            emailLayout.error =null
            password2Layout.error = requireContext().getString(R.string.empty_pass)
        }
        else if (password!=password2){
            emailLayout.error = null
            passwordLayout.error = requireContext().getString(R.string.different_pass)
            password2Layout.error = requireContext().getString(R.string.different_pass)

        }
        else{
            viewModel.creatUser(email,password,requireContext(),view,emailLayout,passwordLayout,password2Layout)
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