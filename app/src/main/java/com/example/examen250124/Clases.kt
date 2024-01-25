package com.example.examen250124

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Clases {
}

/***********************************************************************************************************************
 *  CLASE: Mochila
 *  CONSTRUCTOR:
 *      pesoMochila      - > Peso máximo que puede soportar la mochila (Int)
 *
 *  METODOS
 *      getPesoMochila()        - > Devuelve el peso máximo como Int
 *      addArticulo()           - > Añade un artículo (clase Articulo) a la mochila, comprobando que el peso del
 *                                  artículo sumado al peso total del resto de artículos de la Mochila no supere el
 *                                  límite (pesoMochila)
 *      getContenido()          - > Devuelve el ArrayList de artículos que contiene la mochila
 *      findObjeto(nombre)      - > Devuelve la posición del artículo que cuyo nombre (Articulo.Nombre) pasamos como
 *                                  entrada o -1 si no lo encuentra
 *
 **********************************************************************************************************************/
class Mochila(var context: Context){

    fun getPesoMochila():Int{
        val db = DatabaseHelper(context)
        val contenido = db.getArticulo()
        var pesoMochila = 0
        for(e in contenido){
            pesoMochila++
        }
        return pesoMochila
    }

    fun addArticulo(articulo: Articulo) {
        val db = DatabaseHelper(context)
        db.insertarArticulo(articulo)
    }
    fun getContenido(): ArrayList<Articulo> {
        val db = DatabaseHelper(context)
        val contenido = db.getArticulo()
        return contenido
    }
    fun findObjeto(nombre: String): Boolean {
        val contenido = getContenido()
        var encontrado = false
        for (e in contenido){
            if(nombre==e.getNombre().toString()){
                encontrado= true
            }
        }
        return encontrado
    }

}
/***********************************************************************************************************************
 *  CLASE: Articulo
 *  CONSTRUCTOR:
 *      tipoArticulo  - > Enumeración con valores ARMA, OBJETO, PROTECCION
 *      nombre        - > Enumeración con valores BASTON, ESPADA, DAGA, MARTILLO, GARRAS, POCION, IRA, ESCUDO, ARMADURA
 *      peso          - > Peso del artículo
 *
 *  METODOS
 *      getPeso()           - > Devuelve el peso como Int
 *      getNombre()         - > Devuelve el nombre del artículo
 *      getTipoArticulo()   - > Devuelve el tipo del artículo
 *      toString()          - > Sobreescribimos el método toString para darle formato a la visualización de los
 *                              artículos
 *      getAumentoAtaque()  - > Devuelve el aumento de ataque según el nombre del arma o si el objeto es IRA
 *      getAumentoDefensa() - > Devuelve el aumento de defensa según el nombre de la proteccion
 *      getAumentoVida()    - > Devuelve el aumento de vida si el objeto es POCION
 *
 *
 **********************************************************************************************************************/

class Articulo(private var tipoArticulo: TipoArticulo, private var nombre: Nombre, private var peso: Int, private var precio: Int) {

    enum class TipoArticulo { ARMA, OBJETO, PROTECCION }
    enum class Nombre { BASTON, ESPADA, DAGA, MARTILLO, GARRAS, POCION, IRA, ESCUDO, ARMADURA }

    fun getPeso(): Int {
        return peso
    }
    fun getNombre(): Nombre {
        return nombre
    }
    fun getPrecio(): Int {
        return precio
    }
    fun getTipoArticulo(): TipoArticulo {
        return tipoArticulo
    }
    fun getAumentoAtaque(): Int {
        return when (nombre) {
            Nombre.BASTON -> 10
            Nombre.ESPADA -> 20
            Nombre.DAGA -> 15
            Nombre.MARTILLO -> 25
            Nombre.GARRAS -> 30
            Nombre.IRA -> 80
            else -> 0 // Para otros tipos de armas no especificados
        }
    }
    fun getAumentoDefensa(): Int {
        return when (nombre) {
            Nombre.ESCUDO -> 10
            Nombre.ARMADURA -> 20
            else -> 0 // Para otros tipos de protecciones no especificados
        }
    }
    fun getAumentoVida(): Int {
        return when (nombre) {
            Nombre.POCION -> 100
            else -> 0 // Para otros tipos de objetos no especificados
        }
    }
    override fun toString(): String {
        return "[Tipo Artículo:$tipoArticulo, Nombre:$nombre, Peso:$peso]"
    }
}


//BASE DE DATOS

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE = "BasedeDatos.db"
        private const val TABLA_ARTICULOS = "articulos"
        private const val KEY_ID = "_id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_PESO = "peso"
        private const val COLUMN_TIPOARTICULO = "tipoArticulo"
        private const val COLUMN_PRECIO = "precio"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLA_ARTICULOS" +
                "($KEY_ID INTEGER PRIMARY KEY, $COLUMN_NOMBRE TEXT," +
                "$COLUMN_PESO INTEGER, $COLUMN_TIPOARTICULO TEXT, $COLUMN_PRECIO INTEGER)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_ARTICULOS")
        onCreate(db)
    }

    fun insertarArticulo(articulo: Articulo){
        val db = this.writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_NOMBRE, articulo.getNombre().toString())
            put(COLUMN_TIPOARTICULO, articulo.getTipoArticulo().toString())
            put(COLUMN_PESO, articulo.getPeso())
            put(COLUMN_PRECIO, articulo.getPrecio())
        }
        db.insert(TABLA_ARTICULOS, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getArticulo(): ArrayList<Articulo> {
        val articulos = ArrayList<Articulo>()
        val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
                val tipoArticulo = cursor.getString(cursor.getColumnIndex(COLUMN_TIPOARTICULO))
                val peso = cursor.getInt(cursor.getColumnIndex(COLUMN_PESO))
                val precio = cursor.getInt(cursor.getColumnIndex(COLUMN_PRECIO))
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return articulos
    }

}