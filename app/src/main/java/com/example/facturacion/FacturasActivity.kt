package com.example.facturacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facturacion.adapters.FacturaAdapter
import com.example.facturacion.room.db.FacturaDatabase
import com.example.facturacion.room.entities.Factura
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FacturasActivity : AppCompatActivity() {
    lateinit var base: FacturaDatabase
    lateinit var facturasArray: List<Factura>
    lateinit var rv_lista: RecyclerView
    lateinit var btn_back: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_facturas)
        base = FacturaDatabase.getdatabase(applicationContext)

        initUI()
        mostrar()

        btn_back.setOnClickListener(){
            irInicio(it)
        }

    }

    private fun irInicio(view: View?) {
            val intent = Intent(applicationContext, InicioActivity::class.java)
            startActivity(intent)
    }

    private fun initUI() {
        rv_lista = findViewById(R.id.rv_lista)
        btn_back = findViewById(R.id.btn_back_mostar)
    }

    fun mostrar(){
        GlobalScope.launch {
            val resultado = base.FacturasDao().getAll()
            runOnUiThread{
                resultado.observe(this@FacturasActivity)
                    {
                        facturasArray = arrayListOf()
                        facturasArray = it

                        val adaptador = FacturaAdapter(facturasArray)
                        rv_lista.layoutManager = LinearLayoutManager(applicationContext)
                        rv_lista.adapter = adaptador
                    }
            }
        }
    }

}