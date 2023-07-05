package com.example.oogway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmailET : EditText
    private lateinit var loginPasswordET : EditText
    private lateinit var loginBtn : Button
    private lateinit var loginForgotPasswordBtn : Button
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        listeners()
    }

    private fun listeners() {
        loginBtn.setOnClickListener {
            val email = loginEmailET.text.toString()
            val password = loginPasswordET.text.toString()

            if ( email.isEmpty() || password.isEmpty() ){
                Toast.makeText(this, "Values are empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if ( it.isSuccessful ) {
                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


        loginForgotPasswordBtn.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }
    }

    private fun init() {
        loginEmailET = findViewById(R.id.loginEmailET)
        loginPasswordET = findViewById(R.id.loginPasswordET)
        loginBtn = findViewById(R.id.loginBtn)
        loginForgotPasswordBtn = findViewById(R.id.loginForgotPasswordBtn)
    }
}