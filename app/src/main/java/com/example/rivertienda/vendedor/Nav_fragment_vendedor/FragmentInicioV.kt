package com.example.rivertienda.vendedor.Nav_fragment_vendedor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rivertienda.R
import com.example.rivertienda.databinding.FragmentInicioVBinding
import com.example.rivertienda.vendedor.Bottom_Nav_fragment_vendedor.FragmentMisProductosV
import com.example.rivertienda.vendedor.Bottom_Nav_fragment_vendedor.FragmentOrdenesV
import com.example.rivertienda.vendedor.Productos.AgregarProductoActivity


class FragmentInicioV : Fragment() {

    private lateinit var binding :FragmentInicioVBinding
    private lateinit var Context: Context

    override fun onAttach(context: Context) {
        Context = context
        super.onAttach(context)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioVBinding.inflate(inflater,container,false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_mis_productos_v->{
                    replaceFragment(FragmentMisProductosV())

                }
                R.id.op_mis_ordenes_v->{
                    replaceFragment(FragmentOrdenesV())

                }
            }

            true

        }

        replaceFragment(FragmentMisProductosV())
        binding.bottomNavigation.selectedItemId = R.id.op_mis_productos_v

        binding.addfab.setOnClickListener {
            startActivity(Intent(Context, AgregarProductoActivity::class.java))

        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()

    }


}