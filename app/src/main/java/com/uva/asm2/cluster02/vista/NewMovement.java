package com.uva.asm2.cluster02.vista;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uva.asm2.cluster02.R;
import com.uva.asm2.cluster02.modelo.Movimiento;
import com.uva.asm2.cluster02.modelo.MovimientoDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class NewMovement extends Fragment implements View.OnClickListener {
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    //private String mParam1;
    //private String mParam2;

    private DatePickerDialog picker;
    private EditText selector;

    private Button btn_add;
    private Button btn_clear;

    private TextView txt_titulo;
    private TextView txt_descripcion;
    private TextView txt_cantidad;

    private Spinner spinner_tipo;
    private Spinner spinner_categoria;


    private Toast toast;

    public NewMovement() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_movement, container, false);

        selector = (EditText) view.findViewById(R.id.selector);
        selector.setInputType(InputType.TYPE_NULL);
        selector.requestFocus();

        setDateTimeField();


        btn_add = (Button) view.findViewById(R.id.add_item_btn_add);
        btn_add.setOnClickListener(this);
        btn_clear = (Button) view.findViewById(R.id.add_item_btn_clear);
        btn_clear.setOnClickListener(this);

        txt_titulo = (TextView) view.findViewById(R.id.add_item_titulo);
        txt_descripcion = (TextView) view.findViewById(R.id.add_item_descripcion);
        txt_cantidad = (TextView) view.findViewById(R.id.add_item_cantidad);

        spinner_tipo = (Spinner) view.findViewById(R.id.spinner_tipo);
        spinner_categoria = (Spinner) view.findViewById(R.id.spinner_categoria);

        toast = null;

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == selector) {
            picker.show();
        }
        else if (v == btn_add) {

            if (toast != null)
                toast.cancel();

            if (txt_titulo.getText().toString().equals("")) {
                toast = Toast.makeText(getContext(),
                        R.string.fallo_titulo, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if (txt_cantidad.getText().toString().equals("")) {
                toast = Toast.makeText(getContext(),
                        R.string.fallo_cantidad, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            MovimientoDbHelper dbHelper = new MovimientoDbHelper(getContext());

            float cantidad = Float.parseFloat(txt_cantidad.getText().toString());
            String descripcion = txt_descripcion.getText().toString();
            String titulo = txt_titulo.getText().toString();

            Movimiento.TIPO tipo = Movimiento.TIPO.valueOf(spinner_tipo.getSelectedItem().toString());
            Movimiento.CATEGORIA categoria = Movimiento.CATEGORIA.valueOf(spinner_categoria.getSelectedItem().toString());

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            formatter.setLenient(false);
            Date date = null;
            try {
                date = formatter.parse(selector.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            GregorianCalendar calendar = new GregorianCalendar(Locale.US);
            calendar.setTime(date);

            Movimiento mov = new Movimiento(tipo, cantidad, descripcion, calendar, categoria, null, 0);
            dbHelper.saveMovimiento(mov);

            toast = Toast.makeText(getContext(),
                    R.string.movimiento_exito, Toast.LENGTH_SHORT);
            toast.show();
            clearFormulario();
        }
        else if (v == btn_clear) {
            clearFormulario();
        }

    }

    private void clearFormulario() {
        txt_titulo.setText("");
        txt_descripcion.setText("");
        txt_cantidad.setText("");
        setDateTimeField();
        txt_titulo.requestFocus();
    }

    private void setDateTimeField() {
        selector.setOnClickListener(this);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        final GregorianCalendar newCalendar = new GregorianCalendar(Locale.US);
        newCalendar.setTime(new Date());
        selector.setText(formatter.format(newCalendar.getTime()));

        picker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar newDate = new GregorianCalendar(Locale.US);
                newDate.set(year, monthOfYear, dayOfMonth);
                selector.setText(formatter.format(newDate.getTime()));
            }

        },newCalendar.get(GregorianCalendar.YEAR), newCalendar.get(GregorianCalendar.MONTH), newCalendar.get(GregorianCalendar.DAY_OF_MONTH));

    }

}
