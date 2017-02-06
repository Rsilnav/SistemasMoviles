package com.uva.asm2.cluster02.vista;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uva.asm2.cluster02.R;
import com.uva.asm2.cluster02.modelo.Movimiento;

public class MiddleFragment extends Fragment {


    private Movimiento mov;

    public MiddleFragment() {}

    public static MiddleFragment newInstance() {
        return new MiddleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listadetalle, container, false);

        if (view.findViewById(R.id.fragment_container_ambos) != null) {

            if (savedInstanceState != null) {
                return view;
            }

            ListaFragment firstFragment = new ListaFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_ambos, firstFragment).commit();
        }
        else {
            ListaFragment firstFragment = new ListaFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_lista, firstFragment).commit();
        }

        return view;
    }

    public void setDetalle(Movimiento mov) {

        if (getView() != null && getView().findViewById(R.id.fragment_container_detalle) != null) {
            DetalleFragment detalleFragment = (DetalleFragment) getChildFragmentManager().findFragmentById(R.id.fragment_container_detalle);

            if (detalleFragment != null) {
                detalleFragment.updateWithInfo(mov);
            }
            else {
                detalleFragment = new DetalleFragment();
                Bundle args = new Bundle();
                args.putSerializable(DetalleFragment.MOVIMIENTO, mov);
                detalleFragment.setArguments(args);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_container_detalle, detalleFragment);

                transaction.commit();
            }
        }
        else {
            DetalleFragment newFragment = new DetalleFragment();
            Bundle args = new Bundle();
            args.putSerializable(DetalleFragment.MOVIMIENTO, mov);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_container_ambos, newFragment);

            transaction.commit();

        }
    }

    public void quitarDetalle() {
        if (getView() != null && getView().findViewById(R.id.fragment_container_detalle) != null) {
            DetalleFragment detalleFragment = (DetalleFragment) getChildFragmentManager().findFragmentById(R.id.fragment_container_detalle);
            if (detalleFragment != null) {
                getChildFragmentManager().beginTransaction().remove(detalleFragment).commit();
            }
        }
    }

}
