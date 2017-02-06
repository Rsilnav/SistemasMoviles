package com.uva.asm2.cluster02.modelo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class MovimientoDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movimientos.db";

    public MovimientoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MovimientoContract.MovimientoEntry.TABLE_NAME + " ("
                + MovimientoContract.MovimientoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovimientoContract.MovimientoEntry.ID + " TEXT NOT NULL,"
                + MovimientoContract.MovimientoEntry.TIPO + " TEXT NOT NULL,"
                + MovimientoContract.MovimientoEntry.DESCRIPCION + " TEXT NOT NULL,"
                + MovimientoContract.MovimientoEntry.CANTIDAD + " TEXT NOT NULL,"
                + MovimientoContract.MovimientoEntry.FECHA + " INTEGER NOT NULL,"
                + MovimientoContract.MovimientoEntry.CATEGORIA + " TEXT NOT NULL,"
                + MovimientoContract.MovimientoEntry.FOTO + " TEXT,"
                + MovimientoContract.MovimientoEntry.PLACEHOLDER + " TEXT,"
                + "UNIQUE (" + MovimientoContract.MovimientoEntry.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void dropDatabase() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.rawQuery("DROP TABLE " + MovimientoContract.MovimientoEntry.TABLE_NAME, null);
    }

    public void saveMovimiento(Movimiento mov) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.insert(
                MovimientoContract.MovimientoEntry.TABLE_NAME,
                null,
                mov.toContentValues());

        sqLiteDatabase.close();
    }

    public void deleteMovimiento(Movimiento mov) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(MovimientoContract.MovimientoEntry.TABLE_NAME, "id = '" + mov.getId() + "'", null);
        sqLiteDatabase.close();
    }

    public void deleteAll(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(MovimientoContract.MovimientoEntry.TABLE_NAME, "", null);
        sqLiteDatabase.close();
    }

    /*
    Cursor c = db.query(
            LawyerEntry.TABLE_NAME,  // Nombre de la tabla
            null,  // Lista de Columnas a consultar
            null,  // Columnas para la cláusula WHERE
            null,  // Valores a comparar con las columnas del WHERE
            null,  // Agrupar con GROUP BY
            null,  // Condición HAVING para GROUP BY
            null  // Cláusula ORDER BY
    );
    */

    public ArrayList<Movimiento> getMovimientos() {
        Cursor c;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        c = sqLiteDatabase.rawQuery("SELECT * FROM " + MovimientoContract.MovimientoEntry.TABLE_NAME, null);
        ArrayList<Movimiento> lista = new ArrayList<>();
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.ID));
            String tipo = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.TIPO));
            Float cantidad = Float.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.CANTIDAD)));
            String desc = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.DESCRIPCION));
            Long fecha = Long.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.FECHA)));
            Integer cat = Integer.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.CATEGORIA)));
            String foto = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.FOTO));
            Integer placeholder = Integer.parseInt(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.PLACEHOLDER)));

            GregorianCalendar fechaMov = new GregorianCalendar();
            fechaMov.setTimeInMillis(fecha);

            Movimiento.TIPO tipoMov = Movimiento.TIPO.valueOf(tipo);
            Movimiento.CATEGORIA catMov = Movimiento.CATEGORIA.values()[cat];

            Movimiento m = new Movimiento(id, tipoMov, cantidad, desc, fechaMov, catMov, foto, placeholder);
            lista.add(m);
        }
        c.close();
        sqLiteDatabase.close();

        return lista;
    }

    public ArrayList<Movimiento> listaGastos() {
        Cursor c;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        c = sqLiteDatabase.rawQuery("SELECT * FROM " + MovimientoContract.MovimientoEntry.TABLE_NAME + " WHERE tipo='" + Movimiento.TIPO.GASTO + "'", null);
        ArrayList<Movimiento> lista = new ArrayList<>();
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.ID));
            String tipo = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.TIPO));
            Float cantidad = Float.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.CANTIDAD)));
            String desc = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.DESCRIPCION));
            Long fecha = Long.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.FECHA)));
            Integer cat = Integer.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.CATEGORIA)));
            String foto = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.FOTO));
            Integer placeholder = Integer.parseInt(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.PLACEHOLDER)));

            GregorianCalendar fechaMov = new GregorianCalendar();
            fechaMov.setTimeInMillis(fecha);

            Movimiento.TIPO tipoMov = Movimiento.TIPO.valueOf(tipo);
            Movimiento.CATEGORIA catMov = Movimiento.CATEGORIA.values()[cat];

            Movimiento m = new Movimiento(id, tipoMov, cantidad, desc, fechaMov, catMov, foto, placeholder);
            lista.add(m);
        }
        c.close();
        sqLiteDatabase.close();

        return lista;
    }

    public ArrayList<Movimiento> listaIngresos() {
        Cursor c;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        c = sqLiteDatabase.rawQuery("SELECT * FROM " + MovimientoContract.MovimientoEntry.TABLE_NAME + " WHERE tipo='" + Movimiento.TIPO.INGRESO + "'", null);
        ArrayList<Movimiento> lista = new ArrayList<>();
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.ID));
            String tipo = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.TIPO));
            Float cantidad = Float.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.CANTIDAD)));
            String desc = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.DESCRIPCION));
            Long fecha = Long.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.FECHA)));
            Integer cat = Integer.valueOf(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.CATEGORIA)));
            String foto = c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.FOTO));
            Integer placeholder = Integer.parseInt(c.getString(c.getColumnIndex(MovimientoContract.MovimientoEntry.PLACEHOLDER)));

            GregorianCalendar fechaMov = new GregorianCalendar();
            fechaMov.setTimeInMillis(fecha);

            Movimiento.TIPO tipoMov = Movimiento.TIPO.valueOf(tipo);
            Movimiento.CATEGORIA catMov = Movimiento.CATEGORIA.values()[cat];

            Movimiento m = new Movimiento(id, tipoMov, cantidad, desc, fechaMov, catMov, foto, placeholder);
            lista.add(m);
        }
        c.close();
        sqLiteDatabase.close();

        return lista;
    }

}