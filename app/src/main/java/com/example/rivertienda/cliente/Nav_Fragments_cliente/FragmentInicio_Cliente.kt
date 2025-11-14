package com.example.rivertienda.cliente.Nav_Fragments_cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rivertienda.R
import com.example.rivertienda.cliente.Nav_Fragments_cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.example.rivertienda.cliente.Nav_Fragments_cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaCilente
import com.example.rivertienda.databinding.FragmentInicioClienteBinding


class FragmentInicio_Cliente : Fragment() {

    private lateinit var binding: FragmentInicioClienteBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioClienteBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_tienda_c -> {
                    replaceFragment(FragmentTiendaCilente())
                }
                R.id.op_mis_ordenes_c->{
                    replaceFragment(FragmentMisOrdenesC())

                }

            }
            true

        }

        replaceFragment(FragmentTiendaCilente())
        binding.bottomNavigation.selectedItemId = R.id.op_tienda_c

        return binding.root
    }
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment,fragment)
            .commit()
    }


}