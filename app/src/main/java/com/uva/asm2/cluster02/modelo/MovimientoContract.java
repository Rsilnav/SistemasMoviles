package com.uva.asm2.cluster02.modelo;

import android.provider.BaseColumns;

class MovimientoContract {
        public static abstract class MovimientoEntry implements BaseColumns {
                public static final String TABLE_NAME = "movimiento";

                public static final String ID = "id";
                public static final String TIPO = "tipo";
                public static final String DESCRIPCION = "descripcion";
                public static final String FECHA = "fecha";
                public static final String CANTIDAD = "cantidad";
                public static final String CATEGORIA = "categoria";
                public static final String FOTO = "foto";
                public static final String PLACEHOLDER = "placeholder";
    }
}