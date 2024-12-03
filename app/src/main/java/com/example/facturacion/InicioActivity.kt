package com.example.facturacion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InicioActivity : AppCompatActivity() {

    lateinit var btn_add : Button
    lateinit var btn_mostrar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        btn_add = findViewById(R.id.btn_add)
        btn_mostrar = findViewById(R.id.btn_show)

        btn_mostrar.setOnClickListener{
            mostrar()
        }
        btn_add.setOnClickListener{
            registroFactura(it)
        }
    }
    fun registroFactura(v: View){
        val  intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
    fun mostrar(){
        val  intent = Intent(applicationContext, FacturasActivity::class.java)
        startActivity(intent)
    }
}