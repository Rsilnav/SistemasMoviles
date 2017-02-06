package com.uva.asm2.cluster02.vista;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.uva.asm2.cluster02.R;
import com.uva.asm2.cluster02.modelo.Movimiento;
import com.uva.asm2.cluster02.modelo.MovimientoDbHelper;

import java.util.ArrayList;

public class ListaFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;


    private Spinner opciones;
    private int posicion;

    private ListView list;

    int[] imagenes = {
            R.drawable.ic_favorite,
            R.drawable.ic_audiotrack,
            R.drawable.ic_favorite,
            R.drawable.ic_audiotrack,
            R.drawable.ic_favorite,
            R.drawable.ic_audiotrack,
            R.drawable.ic_favorite,
            R.drawable.ic_audiotrack
    };
    private ArrayList<Movimiento> entradas;

    public ListaFragment() { }

    public static ListaFragment newInstance() {
        return new ListaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listas, container, false);

        list =(ListView) view.findViewById(R.id.lista);
        list.setOnItemClickListener(this);
        listar();

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mListener != null) {
            Movimiento mov = entradas.get(i);
            mListener.onListFragmentInteraction(mov);
        }
    }

    public interface OnFragmentInteractionListener {
        void onListFragmentInteraction(Movimiento mov);
    }

    private void listar() {
        ListViewAdapter adapter;
        entradas = new ArrayList<>();
        MovimientoDbHelper mHelper = new MovimientoDbHelper(getContext());


        entradas = mHelper.getMovimientos();

        adapter = new ListViewAdapter(getContext(),entradas);
        list.setAdapter(adapter);
    }
}
