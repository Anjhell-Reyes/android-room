package com.example.facturacion.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.example.facturacion.room.entities.Factura

@Dao
interface facturadao {

    @Insert
    suspend fun insert(factura: Factura) : Long

    @Update
    suspend fun update(factura: Factura)

    @Delete
    suspend fun delete(factura: Factura)

    @Query("SELECT * FROM factura")
    fun getAll():LiveData<List<Factura>>

    @Query("SELECT * FROM factura  WHERE idFactura = :id")
    fun getById(id:Long):LiveData<Factura>
}