package com.example.rivertienda.cliente

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.rivertienda.R
import com.example.rivertienda.SeleccionarTipoActivity
import com.example.rivertienda.cliente.Nav_Fragments_cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.example.rivertienda.cliente.Nav_Fragments_cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaCilente
import com.example.rivertienda.cliente.Nav_Fragments_cliente.FragmentInicio_Cliente
import com.example.rivertienda.cliente.Nav_Fragments_cliente.FragmentMiPerfilC

import com.example.rivertienda.databinding.ActivityMainClienteBinding

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityCliente : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainClienteBinding
    private var firebaseAuth : FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()


       binding.navigationView.setNavigationItemSelectedListener(this)

       val toggle = ActionBarDrawerToggle(
           this,
           binding.drawerLayout,
           toolbar,
           R.string.open_drawer,
           R.string.close_drawer
       )

        binding.drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        
        replaceFragment(FragmentInicio_Cliente())


    }
    private fun comprobarSesion(){
        /*sesion no registradro*/
        if(firebaseAuth!!.currentUser==null){
            startActivity(Intent(this@MainActivityCliente, SeleccionarTipoActivity::class.java))
            finishAffinity()

        }else{
            Toast.makeText(applicationContext, " Sesion iniciada ", Toast.LENGTH_SHORT).show()
        }

    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(this@MainActivityCliente, SeleccionarTipoActivity::class.java))
        finishAffinity()
        Toast.makeText(applicationContext, " Sesion cerrada ", Toast.LENGTH_SHORT).show()

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment,fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.op_inicio_c -> {
                replaceFragment(FragmentInicio_Cliente())
            }
            R.id.op_mi_perfil_c->{
                replaceFragment(FragmentMiPerfilC())

            }
            R.id.op_cerrar_c->{
                cerrarSesion()
            }
            R.id.op_tienda_c->{
                replaceFragment(FragmentTiendaCilente())

            }
            R.id.op_mis_ordenes_c->{
                replaceFragment(FragmentMisOrdenesC())
            }



        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}



