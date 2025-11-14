package com.example.rivertienda.cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.rivertienda.R
import com.example.rivertienda.databinding.ActivityLoginClienteBinding
import com.example.rivertienda.databinding.ActivityLoginVendedorBinding
import com.example.rivertienda.vendedor.MainActivityVendedor
import com.example.rivertienda.vendedor.RegistroVendedorActivity
import com.google.firebase.auth.FirebaseAuth

class LoginClienteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginClienteBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginC.setOnClickListener {
            validarInfo()



        }

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(this@LoginClienteActivity, RegistroClienteActivity::class.java))
        }


    }
    private var email = ""
    private var password = ""
    private fun validarInfo() {
        email=  binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (email.isEmpty()){
            binding.etEmail.error = "Ingrese su email"
            binding.etEmail.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error = "email no valido"
            binding.etEmail.requestFocus()
        }else if (password.isEmpty()){
            binding.etPassword.error = "Ingrese su contraseña"
            binding.etPassword.requestFocus()
        }else{
            loginCliente()

        }

    }

    private fun loginCliente() {
        progressDialog.setMessage("Iniciando sesion")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
                Toast.makeText(this,"Bienvenido!! ", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"fallo el registro a ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }


}


