package com.example.facturacion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.facturacion.room.db.FacturaDatabase
import com.example.facturacion.room.entities.Factura
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var base: FacturaDatabase
    lateinit var txtProducto: EditText
    lateinit var txtCantidad: EditText
    lateinit var txtCedula: EditText
    lateinit var txtPrecio: EditText
    lateinit var txtTotal: TextView
    lateinit var btn_cancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        base = FacturaDatabase.getdatabase(applicationContext)
        initUi()

        btn_cancelar.setOnClickListener {
            backInicio(it)
        }

        txtCantidad.addTextChangedListener(createTextWatcher(txtCantidad, txtPrecio, txtTotal))
        txtPrecio.addTextChangedListener(createTextWatcher(txtCantidad, txtPrecio, txtTotal))
    }

    private fun createTextWatcher(
        cantidadEditText: EditText,
        precioEditText: EditText,
        totalTextView: TextView
    ): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calcularTotal(cantidadEditText, precioEditText, totalTextView)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun calcularTotal(
        cantidadEditText: EditText,
        precioEditText: EditText,
        totalTextView: TextView
    ) {
        val cantidadText = cantidadEditText.text.toString()
        val precioText = precioEditText.text.toString()

        // Manejo de entradas vacías
        val cantidad = if (cantidadText.isNotEmpty()) cantidadText.toIntOrNull() ?: 0 else 0
        val precio = if (precioText.isNotEmpty()) precioText.toDoubleOrNull() ?: 0.0 else 0.0

        val total = cantidad * precio
        totalTextView.text = total.toString()
    }


    private fun initUi() {
        txtProducto = findViewById(R.id.etProducto)
        txtCantidad = findViewById(R.id.etCantidad)
        txtCedula = findViewById(R.id.etCedula)
        txtPrecio = findViewById(R.id.etPrecio)
        txtTotal = findViewById(R.id.tvTotal)
        btn_cancelar = findViewById(R.id.btn_cancelar)

        if (intent.extras?.isEmpty == false) {
            txtProducto.setText(intent.extras?.getString("producto", ""))
            txtCantidad.setText(intent.extras?.getInt("cantidad", 0).toString())
            txtPrecio.setText(intent.extras?.getDouble("precio", 0.0).toString())
            txtCedula.setText(intent.extras?.getString("cedula", ""))  // Cambiado de "detalle" a "cedula"
        }
    }

    @SuppressLint("ResourceType")
    fun guardar(v: View) {
        if (txtProducto.text.toString().length < 2) {
            txtProducto.error = "Ingrese un producto"
        } else if (txtCantidad.text.toString().isEmpty()) {
            txtCantidad.error = "Ingrese una cantidad válida"
        } else if (txtCedula.text.toString().isEmpty()) {  // Cambiado de txtDetalle a txtCedula
            txtCedula.error = "Ingrese una cédula válida"
        } else if (txtPrecio.text.toString().isEmpty()) {
            txtPrecio.error = "Ingrese un precio válido"
        } else {
            GlobalScope.launch {
                val cantidad = txtCantidad.text.toString().toInt()
                val precio = txtPrecio.text.toString().toDouble()
                val idFactura = base.FacturasDao().insert(
                    Factura(
                        producto = txtProducto.text.toString(),
                        cantidad = cantidad,
                        precio = precio,
                        cedula = txtCedula.text.toString()
                    )
                )
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Factura generada con éxito", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    irFactura(idFactura)
                }
            }
        }
    }

    private fun limpiarCampos() {
        txtProducto.text?.clear()
        txtCantidad.text?.clear()
        txtPrecio.text?.clear()
        txtCedula.text?.clear()
        txtTotal.text = "0"
    }

    private fun backInicio(view: View?) {
        val intent = Intent(applicationContext, InicioActivity::class.java)
        startActivity(intent)
    }

    private fun irFactura(idFactura: Long) {
        val intent = Intent(applicationContext, Factura_Activity::class.java)
        intent.putExtra("idFactura", idFactura)
        startActivity(intent)
    }
}



