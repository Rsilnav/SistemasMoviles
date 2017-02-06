package com.uva.asm2.cluster02.vista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uva.asm2.cluster02.R;
import com.uva.asm2.cluster02.modelo.Movimiento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    private final Context context;
    private final ArrayList<Movimiento> entradas;

    public ListViewAdapter(Context context, ArrayList<Movimiento> entradas) {
        this.context = context;
        this.entradas=entradas;

    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Movimiento getItem(int position) {
        return entradas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //Declaracion de variables
        TextView categoria;
        TextView txtPrecio;
        TextView txtDescripcion;
        TextView txtFecha;
        ImageView imgImg;
        //utilización del inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lista_row, parent, false);


        categoria = (TextView) itemView.findViewById(R.id.tipoGasto);
        txtPrecio = (TextView) itemView.findViewById(R.id.precio);
        txtDescripcion = (TextView) itemView.findViewById(R.id.descripcion);
        imgImg = (ImageView) itemView.findViewById(R.id.imageView);
        txtFecha = (TextView) itemView.findViewById(R.id.fecha);
        //Coger posición y ponersela a los textView e imagenView.
        categoria.setText(entradas.get(position).getCategoria().name());

        String msg;
        if(entradas.get(position).getTipo()== Movimiento.TIPO.GASTO){
            msg = context.getString(R.string.gasto)+entradas.get(position).getCantidad();
            txtPrecio.setText(msg);
        }else{
            msg = context.getString(R.string.ingreso)+entradas.get(position).getCantidad();
            txtPrecio.setText(msg);
        }
        Calendar dia = entradas.get(position).getFecha();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd");
        txtFecha.setText(formatter.format(dia.getTime()));

        //txtFecha.setText(dia.get(Calendar.DAY_OF_MONTH)+"/"+dia.get(Calendar.MONTH)+"/"+dia.get(Calendar.YEAR));
        txtDescripcion.setText(entradas.get(position).getDescripcion());
        imgImg.setImageResource(entradas.get(position).getPlaceholder());

        return itemView;
    }
}