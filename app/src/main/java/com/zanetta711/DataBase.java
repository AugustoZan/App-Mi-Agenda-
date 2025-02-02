package com.zanetta711;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contactos.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CONTACTOS = "contactos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_APELLIDO = "apellido";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_DOMICILIO = "domicilio";
    private static final String COLUMN_GENERO = "genero";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//Se crea una tabla la cual tendrá los datos de los contactos, es decir, sus atributos.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTOS_TABLE = "CREATE TABLE " + TABLE_CONTACTOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_APELLIDO + " TEXT,"
                + COLUMN_TELEFONO + " TEXT,"
                + COLUMN_DOMICILIO + " TEXT,"
                + COLUMN_GENERO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTOS_TABLE);
    }
//Para cuando se actualiza la base de datos.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTOS);
        onCreate(db);
    }

    public void agregarContacto(Contacto contacto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, contacto.getNombre());
        values.put(COLUMN_APELLIDO, contacto.getApellido());
        values.put(COLUMN_TELEFONO, contacto.getTelefono());
        values.put(COLUMN_DOMICILIO, contacto.getDomicilio());
        values.put(COLUMN_GENERO, contacto.getGenero());

        try {
            db.insert(TABLE_CONTACTOS, null, values);
        } catch (Exception e) {
            Log.e("Database", "Error al agregar contacto: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public List<Contacto> obtenerTodosLosContactos() {
        List<Contacto> contactos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Contacto contacto = new Contacto(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_APELLIDO)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TELEFONO)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DOMICILIO)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_GENERO))
                );
                contactos.add(contacto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactos;
    }
//Falta implementar eliminación y modificación de contactos
    public void eliminarContacto(String telefono) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTOS, COLUMN_TELEFONO + "=?", new String[]{telefono});
        db.close();
    }

    public void actualizarContacto(Contacto contacto, String telefonoAntiguo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, contacto.getNombre());
        values.put(COLUMN_APELLIDO, contacto.getApellido());
        values.put(COLUMN_TELEFONO, contacto.getTelefono());
        values.put(COLUMN_DOMICILIO, contacto.getDomicilio());
        values.put(COLUMN_GENERO, contacto.getGenero());
        db.update(TABLE_CONTACTOS, values, COLUMN_TELEFONO + "=?", new String[]{telefonoAntiguo});
        db.close();
    }
}