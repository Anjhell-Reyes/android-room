package com.example.facturacion.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.facturacion.room.daos.facturadao
import com.example.facturacion.room.entities.Factura

@Database(entities = [Factura::class], version = 1)
abstract class FacturaDatabase:RoomDatabase() {
    abstract fun FacturasDao(): facturadao

    companion object{
        fun getdatabase(ctx: Context): FacturaDatabase {
            val db = Room.databaseBuilder(ctx, FacturaDatabase::class.java, "factura").build()
            return db
        }
    }
}