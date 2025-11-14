package com.example.rivertienda.vendedor.Bottom_Nav_fragment_vendedor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rivertienda.Adaptadores.adaptadorProducto
import com.example.rivertienda.Modelos.ModeloProducto
import com.example.rivertienda.databinding.FragmentMisProductosVBinding
import com.google.firebase.database.*

class FragmentMisProductosV : Fragment() {

    private lateinit var binding: FragmentMisProductosVBinding
    private lateinit var mContext: Context
    private lateinit var productoArrayList: ArrayList<ModeloProducto>
    private lateinit var adaptadorProducto: adaptadorProducto

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMisProductosVBinding.inflate(
            LayoutInflater.from(mContext),
            container,
            false
        )
        return binding.root
    }
    val ref = FirebaseDatabase.getInstance().getReference("productos")
    val idProducto = ref.push().key ?: ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productoArrayList = ArrayList()
        listarProductos()
    }

    private fun listarProductos() {

        val ref = FirebaseDatabase.getInstance().getReference("productos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productoArrayList.clear()

                for (ds in snapshot.children) {
                    val modeloProducto = ds.getValue(ModeloProducto::class.java)
                    if (modeloProducto != null) {
                        productoArrayList.add(modeloProducto)
                    }
                }

                adaptadorProducto = adaptadorProducto(mContext, productoArrayList)
                binding.productosRv.adapter = adaptadorProducto
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}

