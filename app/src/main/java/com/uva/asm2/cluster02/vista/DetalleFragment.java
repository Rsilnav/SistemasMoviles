package com.uva.asm2.cluster02.vista;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uva.asm2.cluster02.R;
import com.uva.asm2.cluster02.modelo.Movimiento;

import java.text.SimpleDateFormat;

public class DetalleFragment extends Fragment implements View.OnClickListener {
    public static final String MOVIMIENTO = "movimiento";

    private Movimiento mov;

    private OnFragmentInteractionListener mListener;

    private TextView precio;
    private TextView fecha;
    private TextView tipo;
    private TextView categoria;
    private TextView descripcion;
    private TextView titulo;
    private ImageView imagen;

    private Button borrar;
    private Button share;
    private ImageView image;


    public DetalleFragment() { }

    public static DetalleFragment newInstance(Movimiento mov) {
        DetalleFragment fragment = new DetalleFragment();
        Bundle args = new Bundle();
        args.putSerializable(MOVIMIENTO, mov);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mov = (Movimiento) getArguments().getSerializable(MOVIMIENTO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle, container, false);

        descripcion = (TextView) view.findViewById(R.id.descripcion_ampliada);
        fecha = (TextView) view.findViewById(R.id.fecha_ampliada);
        imagen = (ImageView) view.findViewById(R.id.imagen_ampliada);
        precio = (TextView) view.findViewById(R.id.precio_ampliado);
        tipo = (TextView) view.findViewById(R.id.tipo_ampliado);
        //categoria = (TextView) view.findViewById(R.id.tipo_ampliado);

        borrar = (Button) view.findViewById(R.id.delete);
        borrar.setOnClickListener(this);

        share = (Button) view.findViewById(R.id.share);
        share.setOnClickListener(this);

        image = (ImageView) view.findViewById(R.id.imagen_ampliada);
        image.setOnClickListener(this);


        if (mov != null) {
            descripcion.setText(mov.getDescripcion());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd");
            fecha.setText(formatter.format(mov.getFecha().getTime()));

            if (!("null".equals(mov.getFoto())) && mov.getFoto() != null) {
                image.setImageURI(null);
                image.setImageURI(Uri.parse(mov.getFoto()));
            }


            precio.setText(String.valueOf(mov.getCantidad()));
            tipo.setText(mov.getTipo().toString());
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateWithInfo(Movimiento mov) {
        this.mov = mov;

        //categoria = (TextView) view.findViewById(R.id.tipo_ampliado);

        descripcion.setText(mov.getDescripcion());

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy MM dd");
        fecha.setText(formatter.format(mov.getFecha().getTime()));

        if (mov.getFoto() != null) {
            imagen.setImageURI(null);
            imagen.setImageURI(Uri.parse(mov.getFoto()));
        }

        precio.setText(String.valueOf(mov.getCantidad()));
        tipo.setText(mov.getTipo().toString());
    }

    @Override
    public void onClick(View view) {
        if (view == borrar) {
            if (mListener != null) {
                mListener.onDetalleFragmentInteraction(mov);
            }
        }
        else if (view == share){
            //shareWithoutImage();
            share();
        }
        else if (view == image) {
            if (mListener != null) {
                mListener.onDetalleFragmentInteractionOpenCamera();
            }
        }
    }

    private void shareWithoutImage() {
        String msg = crearMsg();
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(intent, getString(R.string.enviar_a)));
    }

    private void share() {
        String msg = crearMsg();
        Uri imageUri = null;
        if (mov.getFoto() != null)
            imageUri = Uri.parse(mov.getFoto());


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);

        if (imageUri != null)
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.compartir_con)));
    }

    public interface OnFragmentInteractionListener {
        void onDetalleFragmentInteraction(Movimiento mov);

        void onDetalleFragmentInteractionOpenCamera();
    }

    private String crearMsg(){
        String msg;
        if(mov.getTipo() == Movimiento.TIPO.INGRESO){
            msg = getString(R.string.he_ingresado);
        }else{
            msg = getString(R.string.He_hecho_un_gasto);
        }
        msg += (mov.getCantidad()+" €");
        if(mov.getTipo() == Movimiento.TIPO.GASTO){
            msg +=(getString(R.string.en) + mov.getCategoria().toString());
        }

        return msg;
    }

}
