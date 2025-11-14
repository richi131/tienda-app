package com.example.rivertienda.Adaptadores

import android.content.Context
import android.graphics.ColorSpace.Model
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.R
import com.example.rivertienda.Adaptadores.AdaptadorImagenSeleccionada
import com.example.rivertienda.Modelos.ModeloImagenSelecionada
import com.example.rivertienda.databinding.ItemImagenesSeleccionadasBinding

class AdaptadorImagenSeleccionada (
    private val context : Context,
    private val imagenesSelecArrayList : ArrayList<ModeloImagenSelecionada>

):  Adapter<AdaptadorImagenSeleccionada.HolderImagenSeleccionada>() {
    private lateinit var  binding : ItemImagenesSeleccionadasBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderImagenSeleccionada {

        binding = ItemImagenesSeleccionadasBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderImagenSeleccionada(binding.root)

    }

    override fun onBindViewHolder(holder: HolderImagenSeleccionada, position: Int) {
        val modelo = imagenesSelecArrayList[position]

        val imagenUri = modelo.imagenUri
        try{
            Glide.with (context)
                .load(imagenUri)
                .placeholder(com.example.rivertienda.R.drawable.foto)
                .into(holder.item_foto)


        }catch (e: Exception){

        }

        holder.item_borrar.setOnClickListener {
            imagenesSelecArrayList.remove(modelo)
            notifyDataSetChanged()
        }

    }


    override fun getItemCount(): Int {
        return imagenesSelecArrayList.size

    }


    inner class HolderImagenSeleccionada(itemView : View) :ViewHolder(itemView){

        var item_foto = binding.itemFoto
        var item_borrar =binding.borrarItem
    }


    }
