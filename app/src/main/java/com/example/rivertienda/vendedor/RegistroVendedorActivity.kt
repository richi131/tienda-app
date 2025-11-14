package com.example.rivertienda.vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.rivertienda.Constantes

import com.example.rivertienda.R
import com.example.rivertienda.databinding.ActivityRegistroVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroVendedorActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegistroVendedorBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrarV.setOnClickListener {
            validarInformacion()
        }
    }
    private var nombres = ""
    private var email = ""
    private var password = ""
    private var cpassword = ""
    private fun validarInformacion() {
        nombres = binding.etNombresV.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        cpassword = binding.etCPassword.text.toString().trim()
        if (nombres.isEmpty()){
            binding.etNombresV.error = "Ingrese sus nombres"
            binding.etNombresV.requestFocus()
        } else if (email.isEmpty()){
            binding.etEmail.error = "Ingrese su email"
            binding.etEmail.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error = "email no valido"
            binding.etEmail.requestFocus()
        } else if (password.isEmpty()){
            binding.etPassword.error = "Ingrese su contraseña"
            binding.etPassword.requestFocus()
        } else if (password.length < 6){
            binding.etPassword.error = "necesita 6 o mas caracteres"
            binding.etPassword.requestFocus()
        }else if (cpassword.isEmpty()){
            binding.etCPassword.error = "confirme  contraseña"
            binding.etCPassword.requestFocus()
        } else if (password!=cpassword){
            binding.etCPassword.error = "no coincide"
            binding.etCPassword.requestFocus()
        }else{
            registrarVendedor()
        }
    }

    private fun registrarVendedor() {
        progressDialog.setMessage("creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password)
        .addOnSuccessListener {
            insertarInfoBD()

        }

            .addOnFailureListener { e->
                Toast.makeText(this,"fallo el registro a ${e.message}",Toast.LENGTH_SHORT).show()
            }

    }

    private fun insertarInfoBD() {
        progressDialog.setMessage("guardando informacion")

        val uidBD = firebaseAuth.uid
        val nombresBD = nombres
        val emailBD = email
        val tipoUsurio ="vendedor"
        val tiempoBD = Constantes().obtenerTiempoD()

        val datosVendedor = HashMap<String, Any>()

        datosVendedor["uid"] = "$uidBD"
        datosVendedor["nombres"] = "$nombresBD"
        datosVendedor["email"] = "$emailBD"
        datosVendedor["tipoUsuario"] = "Vendedor"
        datosVendedor["tiempo"] = tiempoBD

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uidBD!!)
            .setValue(datosVendedor)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this,MainActivityVendedor::class.java))
                finish()

            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this, "fallo el registro debido a ${e.message}",Toast.LENGTH_SHORT).show()

            }




    }
}