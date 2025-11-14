package com.example.rivertienda.vendedor

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

import com.example.rivertienda.databinding.ActivityMainVendedorBinding
import com.example.rivertienda.vendedor.Bottom_Nav_fragment_vendedor.FragmentCategoriasV
import com.example.rivertienda.vendedor.Bottom_Nav_fragment_vendedor.FragmentMisProductosV
import com.example.rivertienda.vendedor.Bottom_Nav_fragment_vendedor.FragmentOrdenesV
import com.example.rivertienda.vendedor.Nav_fragment_vendedor.FragmentInicioV
import com.example.rivertienda.vendedor.Nav_fragment_vendedor.FragmentReseniaV
import com.example.rivertienda.vendedor.Nav_fragment_vendedor.FragmentTiendaV
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityVendedor : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainVendedorBinding
    private var firebaseAuth : FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainVendedorBinding.inflate(layoutInflater)

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

        replaceFragment(FragmentInicioV())
        binding.navigationView.setCheckedItem(R.id.op_inicio_v)

    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, SeleccionarTipoActivity::class.java))
        finish()
        Toast.makeText(applicationContext, " Sesion cerrada ", Toast.LENGTH_SHORT).show()

    }

    private fun comprobarSesion() {
        /*sesion no registradro*/
        if(firebaseAuth!!.currentUser==null){
            startActivity(Intent(applicationContext, SeleccionarTipoActivity::class.java))

        }else{
            Toast.makeText(applicationContext, " Sesion iniciada ", Toast.LENGTH_SHORT).show()
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.op_inicio_v -> {
                replaceFragment(FragmentInicioV())


            }
            R.id.op_mi_tienda_v ->  {
                replaceFragment(FragmentTiendaV())

            }
            R.id.op_categorias_v -> {
                replaceFragment(FragmentCategoriasV())

            }
            R.id.op_resenia_v->{
                replaceFragment(FragmentReseniaV())
            }
            R.id.op_cerrar_sesion_v->{
                cerrarSesion()
                Toast.makeText(applicationContext, " Sesion cerrada ", Toast.LENGTH_SHORT).show()

            }
            R.id.op_mis_productos_v->{
                replaceFragment(FragmentMisProductosV())

            }
            R.id.op_mis_ordenes_v->{
                replaceFragment(FragmentOrdenesV())

            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true


    }


}