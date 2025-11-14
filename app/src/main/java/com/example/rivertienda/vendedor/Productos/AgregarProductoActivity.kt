package com.example.rivertienda.vendedor.Productos

import android.R
import android.R.string
import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rivertienda.Adaptadores.AdaptadorImagenSeleccionada
import com.example.rivertienda.Constantes
import com.example.rivertienda.Modelos.ModeloCategoria
import com.example.rivertienda.Modelos.ModeloImagenSelecionada
import com.github.dhaval2404.imagepicker.ImagePicker
import com.example.rivertienda.databinding.ActivityAgregarProductoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imagenUri : Uri?=null
    private lateinit var imagenSelecArrayList: ArrayList<ModeloImagenSelecionada>
    private lateinit var adaptadorImagenSeleccionada: AdaptadorImagenSeleccionada
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var progressDialog: ProgressDialog


    private lateinit var  categoriaArrayList: ArrayList<ModeloCategoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarCategoria()

        imagenSelecArrayList = ArrayList()
        adaptadorImagenSeleccionada = AdaptadorImagenSeleccionada(this,imagenSelecArrayList)
        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImagenes()
        }

        binding.categoria.setOnClickListener {
            seleccionarImagenes()
        }

        binding.btnAgregarProducto.setOnClickListener {
            validarDatos()

        }







        cargarImagenes()

    }
    private var nombreP = ""
    private var descripcionP = ""
    private var categoria = ""
    private var precioP = ""

    private fun AgregarProductoActivity.validarDatos() {
        nombreP = binding.etNombreP.text.toString().trim()
        descripcionP = binding.etDescripcionP.text.toString().trim()
        categoria= binding.categoria.text.toString().trim()
        precioP = binding.etPrecio.text.toString().trim()

        if (nombreP.isEmpty()){
            binding.etNombreP.error = "ingrese nombre"
            binding.etNombreP.requestFocus()

        }
        else if (descripcionP.isEmpty()){
            binding.etDescripcionP.error = "ingrese descripcion"
            binding.etDescripcionP.requestFocus()


        }
        else if (precioP.isEmpty()){
            binding.etPrecio.error = "ingrese precio"
            binding.etPrecio.requestFocus()


        }
        else if (imagenUri == null){
            Toast.makeText(this,"Seleccione al menos una imagen",Toast.LENGTH_SHORT).show()

        }

        else{
            agregarProducto()
        }

    }
    private fun AgregarProductoActivity.agregarProducto() {
        progressDialog.setMessage("Espere por favor")
        progressDialog.show()

        var ref = FirebaseDatabase.getInstance().getReference("productos")
        val keyId = ref.push().key

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$keyId"
        hashMap["nombre"] = "$nombreP"
        hashMap["descripcion"] = "$descripcionP"
        hashMap["precio"] = "$precioP"


        ref.child(keyId!!)
            .setValue(hashMap)
            .addOnSuccessListener {

                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()

            }
        limpiarCampo()

    }

    private fun AgregarProductoActivity.limpiarCampo() {


        binding.etNombreP.text.clear()
        binding.etDescripcionP.text.clear()
        binding.etPrecio.text.clear()


    }

    private fun cargarCategoria() {
        categoriaArrayList = ArrayList()
        val ref= FirebaseDatabase.getInstance().getReference("categorias").orderByChild("categorias")
        ref.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    categoriaArrayList.clear()
                    for (ds in snapshot.children){
                        val modelo = ds.getValue(ModeloCategoria::class.java)
                        categoriaArrayList.add(modelo!!)

            }

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private var idcet =""
    private var titulocat =""

    private fun seleccionarCategorias(){

        val categoriaArray = arrayOfNulls<String>(categoriaArrayList.size)
        for (i in categoriaArray.indices){
            categoriaArray[i] = categoriaArrayList[i].categoria
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione una categoria")
            .setItems(categoriaArray) { dialog, which ->
                idcet = categoriaArrayList[which].id
                titulocat = categoriaArrayList[which].categoria
                binding.categoria.text = titulocat
            }
            .show()

    }

    private fun cargarImagenes() {
         adaptadorImagenSeleccionada = AdaptadorImagenSeleccionada(this,imagenSelecArrayList)
        binding.RVImagenesProducto.adapter = adaptadorImagenSeleccionada
    }

    private fun seleccionarImagenes(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent->
                resultadoImg.launch(intent)
            }
    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK){
                val data = resultado.data
                imagenUri = data!!.data
                val tiempo = "${Constantes().obtenerTiempoD()}"

                val modeloImagenSelecionada = ModeloImagenSelecionada(tiempo, imagenUri, null, false)
                imagenSelecArrayList.add(modeloImagenSelecionada)
                cargarImagenes()
            }else{
                Toast.makeText(this,"Imagen no seleccionada",Toast.LENGTH_SHORT).show()
            }
        }


}





