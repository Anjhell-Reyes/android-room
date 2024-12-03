package com.example.facturacion

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.facturacion.room.db.FacturaDatabase
import com.example.facturacion.room.entities.Factura
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Factura_Activity : AppCompatActivity() {
    lateinit var base: FacturaDatabase
    lateinit var txtProducto: EditText
    lateinit var txtCantidad: EditText
    lateinit var txtPrecio: EditText
    lateinit var txtCedula: EditText
    lateinit var txtId: TextView
    lateinit var txtSeguro: TextView
    lateinit var txtSubtotal: TextView
    lateinit var txtImpuesto: TextView
    lateinit var txtTotal1: TextView
    lateinit var txtTotal2: TextView


    lateinit var btn_volver: Button
    lateinit var btn_aceptar: Button
    lateinit var btn_cancelar: Button
    lateinit var btn_editar: Button
    lateinit var btn_eliminar: Button


        var idFactura: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_factura)
        base = FacturaDatabase.getdatabase(applicationContext)

        initUI()

        mostrarPorId()

        disableEdit()

        btn_volver.setOnClickListener {
            backInicio(it)
        }

        btn_editar.setOnClickListener{
            enableEdit()
            btn_aceptar.setOnClickListener{
                editar(it)
            }
            btn_cancelar.setOnClickListener{
                disableEdit()
            }
        }

        btn_eliminar.setOnClickListener{
            txtSeguro.visibility = View.VISIBLE
            enableButton()

            btn_aceptar.setOnClickListener{
                eliminar(it)
            }
            btn_cancelar.setOnClickListener{
                disableButton()
                txtSeguro.visibility = View.GONE
            }
        }
        txtCantidad.addTextChangedListener(createTextWatcher(txtCantidad, txtPrecio, txtTotal1, txtTotal2, txtImpuesto, txtSubtotal))
        txtPrecio.addTextChangedListener(createTextWatcher(txtCantidad, txtPrecio, txtTotal1, txtTotal2, txtImpuesto, txtSubtotal))
    }

    private fun createTextWatcher(
        cantidadEditText: EditText,
        precioEditText: EditText,
        totalTextView1: TextView,
        totalTextView2: TextView,
        impuestoTextView: TextView,
        subtotalTextView: TextView
    ): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Llamar a la función para calcular el total, el impuesto y el subtotal
                calcularTotal(cantidadEditText, precioEditText, totalTextView1, totalTextView2, impuestoTextView, subtotalTextView)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun calcularTotal(
        cantidadEditText: EditText,
        precioEditText: EditText,
        totalTextView1: TextView,
        totalTextView2: TextView,
        impuestoTextView: TextView,
        subtotalTextView: TextView
    ) {
        val cantidadText = cantidadEditText.text.toString()
        val precioText = precioEditText.text.toString()

        // Manejo de entradas vacías
        val cantidad = if (cantidadText.isNotEmpty()) cantidadText.toIntOrNull() ?: 0 else 0
        val precio = if (precioText.isNotEmpty()) precioText.toDoubleOrNull() ?: 0.0 else 0.0

        val total = cantidad * precio

        val iva = precio * 0.19

        val subtotal = precio - iva


        totalTextView1.text = "$%.2f".format(total)
        totalTextView2.text = total.toString()
        subtotalTextView.text = "$%.2f".format(subtotal)
        impuestoTextView.text = "$%.2f".format(iva)
    }
    private fun initUI() {
        txtProducto = findViewById(R.id.txtProducto)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtCedula = findViewById(R.id.txtCedula)
        txtCantidad = findViewById(R.id.txtCantidad)
        txtId = findViewById(R.id.txtId)
        btn_volver = findViewById(R.id.btn_back_inicio)
        btn_aceptar = findViewById(R.id.btn_confirm)
        btn_cancelar = findViewById(R.id.btn_cancel)
        btn_editar = findViewById(R.id.btn_edit)
        btn_eliminar = findViewById(R.id.btn_delete)
        idFactura = intent.extras!!.getLong("idFactura")
        txtSeguro = findViewById(R.id.seguro)
        txtSubtotal = findViewById(R.id.txtSubtotal)
        txtImpuesto = findViewById(R.id.txtImpuesto)
        txtTotal1 = findViewById(R.id.total3)
        txtTotal2 = findViewById(R.id.total4)

        txtSeguro.visibility = View.GONE
    }

    private fun mostrarPorId() {
        GlobalScope.launch {
            // Obtén el LiveData de la factura por ID
            val resultado = base.FacturasDao().getById(idFactura)
            runOnUiThread {
                // Observa el LiveData en el hilo principal
                resultado.observe(this@Factura_Activity, Observer { factura ->
                    factura?.let {
                        txtId.setText(it.idFactura.toString())
                        txtProducto.setText(it.producto)
                        txtCantidad.setText(it.cantidad.toString())
                        txtCedula.setText(it.cedula)
                        txtPrecio.setText(it.precio.toString())
                    }
                })
            }
        }
    }

    private fun backInicio(view: View?) {
        val intent = Intent(applicationContext, FacturasActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("ResourceType")
    fun editar(v: View) {

            GlobalScope.launch {
                val cantidad = txtCantidad.text.toString().toInt()
                val precio = txtPrecio.text.toString().toDouble()
                base.FacturasDao()
                    .update(
                        Factura(
                            idFactura = txtId.text.toString().toLong(),
                            producto = txtProducto.text.toString(),
                            cantidad = cantidad, precio = precio,
                            cedula = txtCedula.text.toString()))
                runOnUiThread {
                    Toast.makeText(this@Factura_Activity, "Factura editada con éxito", Toast.LENGTH_SHORT).show()
                    disableEdit()
                }
            }
        }

    @SuppressLint("ResourceType")
    fun eliminar(v: View) {
            GlobalScope.launch {
                val cantidad = txtCantidad.text.toString().toInt()
                val precio = txtPrecio.text.toString().toDouble()

                base.FacturasDao()
                    .delete(
                        Factura(

                            idFactura = idFactura,
                            producto = txtProducto.text.toString(),
                            cantidad = cantidad, precio = precio,
                            cedula = txtCedula.text.toString()))
                runOnUiThread {
                    Toast.makeText(this@Factura_Activity, "Factura eliminada con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, InicioActivity::class.java)
                    startActivity(intent)
                }
            }
    }

    fun enableEdit(){
        txtProducto.isEnabled = true
        txtProducto.setTextColor(Color.GRAY)
        txtProducto.setTypeface(null, Typeface.BOLD)
        txtProducto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        txtPrecio.isEnabled = true
        txtPrecio.setTextColor(Color.GRAY)
        txtCedula.isEnabled = true
        txtCedula.setTextColor(Color.GRAY)
        txtCantidad.isEnabled = true
        txtCantidad.setTextColor(Color.GRAY)

        enableButton()
    }

    fun disableEdit(){
        txtProducto.isEnabled = false
        txtProducto.setTextColor(Color.WHITE)
        txtProducto.setTypeface(null, Typeface.BOLD)
        txtProducto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) // 24sp

        txtPrecio.isEnabled = false
        txtPrecio.setTextColor(Color.WHITE)
        txtCedula.isEnabled = false
        txtCedula.setTextColor(Color.WHITE)
        txtCantidad.isEnabled = false
        txtCantidad.setTextColor(Color.WHITE)

        disableButton()
    }

    fun enableButton(){
        btn_aceptar.visibility = View.VISIBLE
        btn_cancelar.visibility = View.VISIBLE


    }

    fun disableButton(){
        btn_aceptar.visibility = View.GONE
        btn_cancelar.visibility = View.GONE
    }

}

