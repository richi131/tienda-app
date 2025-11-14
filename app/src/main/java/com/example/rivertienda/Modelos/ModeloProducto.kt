package com.example.rivertienda.Modelos

class ModeloProducto {

    var id : String = ""
    var nombre : String = ""
    var descripcion : String = ""
    var precio : String = ""

    constructor()
    constructor(id: String, nombre: String, descripcion: String, precio: String) {
        this.id = id
        this.nombre = nombre
        this.descripcion = descripcion
        this.precio = precio
    }

}