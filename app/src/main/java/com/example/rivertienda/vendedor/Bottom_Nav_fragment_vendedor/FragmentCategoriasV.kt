package com.example.rivertienda.vendedor.Bottom_Nav_fragment_vendedor

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rivertienda.databinding.FragmentCategoriasVBinding
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rivertienda.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class FragmentCategoriasV : Fragment() {

    internal lateinit var binding: FragmentCategoriasVBinding
    private lateinit var mcontext: Context
    internal lateinit var progressDialog: ProgressDialog
    internal var imageUri: Uri? = null

    override fun onAttach(context: android.content.Context) {
        mcontext = context
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriasVBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(mcontext)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.imgcategorias.setOnClickListener {
            seleccionarImg()
        }

        binding.btnAgregarcat.setOnClickListener {
            validarInfo()
        }




        return binding.root
    }

    private fun seleccionarImg() {
        ImagePicker.with(requireActivity())
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                resulatadoImg.launch(intent)

            }

    }

    private val resulatadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == android.app.Activity.RESULT_OK) {
                val data = resultado.data
                imageUri = data!!.data
                binding.imgcategorias.setImageURI(imageUri)

            } else {
                Toast.makeText(context, "Imagen no seleccionada", Toast.LENGTH_SHORT).show()
            }

        }


    private var categoria = ""
    private fun validarInfo() {
        categoria = binding.etCategoria.text.toString().trim()
        if (categoria.isEmpty()) {
            Toast.makeText(context, "Ingrese una categoria", Toast.LENGTH_SHORT).show()
        } else if (imageUri == null) {
            Toast.makeText(context, "Seleccione una imagen", Toast.LENGTH_SHORT).show()


        } else {
            agregarcatBD()

        }
    }

    private fun agregarcatBD() {

        progressDialog.setMessage("agregando categoria")
        progressDialog.show()

        val ref = FirebaseDatabase.getInstance().getReference("categorias")
        val keyId = ref.push().key

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$keyId"
        hashMap["categoria"] = "${categoria}"

        ref.child(keyId!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //progressDialog.dismiss()
                // Toast.makeText(context, "categoria agregada", Toast.LENGTH_SHORT).show()
                // binding.etCategoria.setText("")
                subirImgStorage(keyId)


            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun FragmentCategoriasV.subirImgStorage(keyId: String) {
        progressDialog.setMessage("subiendo imagen")
        progressDialog.show()


        val nombreImagen = keyId
        val nombreCarpeta = "Categorias/$nombreImagen"
        val storege = FirebaseStorage.getInstance().getReference(nombreCarpeta)
        storege.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                progressDialog.dismiss()
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val urlImgCargada = uriTask.result

                if (uriTask.isSuccessful) {
                    val hashMap = HashMap<String, Any>()
                    hashMap["imagenUrl"] = "$urlImgCargada"
                    val ref = FirebaseDatabase.getInstance().getReference("categorias")
                    ref.child(nombreImagen).updateChildren(hashMap)
                    Toast.makeText(context, "categoria agregada", Toast.LENGTH_SHORT).show()
                    binding.etCategoria.setText("")
                    imageUri = null

                    binding.imgcategorias.setImageURI(imageUri)
                    binding.imgcategorias.setImageResource(R.drawable.foto)
                }

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}

