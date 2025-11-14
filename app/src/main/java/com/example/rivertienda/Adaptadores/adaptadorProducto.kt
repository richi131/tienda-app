package com.example.rivertienda.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.rivertienda.Modelos.ModeloProducto
import com.example.rivertienda.R
import com.example.rivertienda.databinding.ItemProductoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class adaptadorProducto : Adapter<adaptadorProducto.HolderProducto> {

    private lateinit var binding: ItemProductoBinding

    private var mContex: Context
    private var productoArrayList: ArrayList<ModeloProducto>

    constructor(mContex: Context, productoArrayList: ArrayList<ModeloProducto>) {
        this.mContex = mContex
        this.productoArrayList = productoArrayList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderProducto {
        binding = ItemProductoBinding.inflate(LayoutInflater.from(mContex), parent, false)
        return HolderProducto(binding.root)
    }



    override fun onBindViewHolder(
        holder: HolderProducto,
        position: Int
    ) {
        val modeloProducto= productoArrayList[position]

        val nombre = modeloProducto.nombre
        val precio = modeloProducto.precio

        cargarPrimeraImg(modeloProducto,holder)

        holder.item_nombre_p.text ="{$nombre}"
        holder.item_presio_p.text ="{$precio}"


    }

    private fun cargarPrimeraImg(modeloproducto: ModeloProducto, producto: HolderProducto){
        val idproducto = modeloproducto
        val ref = FirebaseDatabase.getInstance().getReference("productos")
        ref.child(idproducto).child("imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        for (ds in snapshot.children){
                            val imagenUrl = "${ds.child("imagenUrl").value}"

                            try{
                                Glide.with(mContex)
                                    .load(imagenUrl)
                                    .placeholder(R.drawable.agregarproducto)
                                    .into(producto.imagenp)
                            }catch (e: Exception){

                            }

                        }

                    }

                }
            })




    }

    override fun getItemCount(): Int {
        return productoArrayList.size

    }


    inner class HolderProducto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagenp = binding.imagenp
        var item_nombre_p = binding.itemNombreP
        var item_presio_p = binding.itemPresioP

    }






}