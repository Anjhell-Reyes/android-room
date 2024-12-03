package com.example.facturacion.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "factura")
data class Factura(
    @PrimaryKey(autoGenerate = true)
    val idFactura: Long=0,
    val producto: String,
    val cantidad: Int,
    val cedula: String,
    val precio: Double
)