package com.example.facturacion.adapters

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.facturacion.room.entities.Factura
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.facturacion.Factura_Activity
import com.example.facturacion.R

class FacturaAdapter(private val datos: List<Factura>)
    : RecyclerView.Adapter<FacturaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val producto: TextView
        val id: TextView
        val cedula: TextView
        val total: TextView

        val mycard: CardView

        init {
            producto = view.findViewById(R.id.txt_item_producto)
            mycard = view.findViewById(R.id.my_card)
            id = view.findViewById(R.id.txt_item_id)
            cedula = view.findViewById(R.id.txt_item_cedula)
            total = view.findViewById(R.id.totalMostrar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.producto.text = datos[position].producto
        holder.id.text = datos[position].idFactura.toString()
        holder.cedula.text = datos[position].cedula
        holder.total.text = (datos[position].precio * datos[position].cantidad).toString()

        holder.mycard.setOnClickListener {
            val intent = Intent(holder.mycard.context, Factura_Activity::class.java)
            intent.putExtra("idFactura", datos[position].idFactura)
            holder.mycard.context.startActivity(intent)
        }
    }
}
