package com.uva.asm2.cluster02.vista;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uva.asm2.cluster02.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button btn_export;
        Button btn_import;
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        btn_export = (Button) view.findViewById(R.id.exportar);
        btn_export.setOnClickListener(this);

        btn_import = (Button) view.findViewById(R.id.importar);
        btn_import.setOnClickListener(this);
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
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onSettingsFragmentInteraction(view);
        }
    }

    public interface OnFragmentInteractionListener {
        void onSettingsFragmentInteraction(View view);
    }
}
