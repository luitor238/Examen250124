package com.example.examen250124

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mochila = Mochila(this)

        val selector = findViewById<EditText>(R.id.selector).text.toString().toInt()

        val nombre = findViewById<TextView>(R.id.nombre)
        val tipoArticulo = findViewById<TextView>(R.id.tipoArticulo)
        val peso = findViewById<TextView>(R.id.peso)
        val precio = findViewById<TextView>(R.id.precio)

        val btnAnadir=findViewById<Button>(R.id.btnAnadir)
        val btnVer=findViewById<Button>(R.id.btnVer)

        val nombreBuscar = findViewById<EditText>(R.id.nombreBuscar).text.toString()
        val btnBuscar=findViewById<Button>(R.id.btnBuscar)
        val encontrado = findViewById<TextView>(R.id.encontrado)

        val pesoMochila = findViewById<TextView>(R.id.pesoMochila)


        btnAnadir.setOnClickListener {
            val articulo = Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.ESPADA,5,10)
            mochila.addArticulo(articulo)
            pesoMochila.text=mochila.getPesoMochila().toString()
        }

        btnVer.setOnClickListener {
            val contenido = mochila.getContenido()

            nombre.text = contenido[selector].getNombre().toString()
            tipoArticulo.text = contenido[selector].getTipoArticulo().toString()
            peso.text = contenido[selector].getPeso().toString()
            precio.text = contenido[selector].getPrecio().toString()

        }

        btnBuscar.setOnClickListener {
            var encontrad = ""
            if(mochila.findObjeto(nombreBuscar)){
                encontrad = "Encontrado"}
            else{
                encontrad = "No encontrado"
            }

            encontrado.text= encontrad
        }
    }
}





















