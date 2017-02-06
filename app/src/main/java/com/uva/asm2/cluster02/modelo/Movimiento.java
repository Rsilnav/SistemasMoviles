package com.uva.asm2.cluster02.modelo;

import android.content.ContentValues;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.UUID;

public class Movimiento implements Serializable {

    public enum TIPO {
        INGRESO,
        GASTO
    }

    public enum CATEGORIA {
        OCIO,
        REGALO,
        ALIMENTACION,
        TRABAJO,
        VENTA,
        PROPINA,
        ROPA
    }

    private final String id;
    private final float cantidad;
    private final String descripcion;
    private final GregorianCalendar fecha;
    private final CATEGORIA categoria;
    private final TIPO tipo;
    private String foto;
    private final int placeholder;

    public Movimiento(TIPO tipo, float cantidad, String descripcion, GregorianCalendar fecha, CATEGORIA categoria, String foto, int placeholder) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.categoria = categoria;
        this.foto = foto;
        this.placeholder = placeholder;
    }

    public Movimiento(String id, TIPO tipo, float cantidad, String descripcion, GregorianCalendar fecha, CATEGORIA categoria, String foto, int placeholder) {
        this.id = id;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.categoria = categoria;
        this.foto = foto;
        this.placeholder = placeholder;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public float getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public GregorianCalendar getFecha() {
        return fecha;
    }

    public CATEGORIA getCategoria() {
        return categoria;
    }

    public String getFoto() {
        return foto;
    }

    public Integer getPlaceholder() {
        return placeholder;
    }

    public ContentValues toContentValues() {

        ContentValues values = new ContentValues();
        values.put(MovimientoContract.MovimientoEntry.ID, id);
        values.put(MovimientoContract.MovimientoEntry.TIPO, tipo.toString());
        values.put(MovimientoContract.MovimientoEntry.CANTIDAD, cantidad);
        values.put(MovimientoContract.MovimientoEntry.DESCRIPCION, descripcion);
        values.put(MovimientoContract.MovimientoEntry.FECHA, String.valueOf(fecha.getTimeInMillis()));
        values.put(MovimientoContract.MovimientoEntry.CATEGORIA, categoria.ordinal());
        values.put(MovimientoContract.MovimientoEntry.FOTO, foto);
        values.put(MovimientoContract.MovimientoEntry.PLACEHOLDER, placeholder);
        return values;
    }

    public void setFoto(String parsedUri) {
        this.foto = parsedUri;
    }
}