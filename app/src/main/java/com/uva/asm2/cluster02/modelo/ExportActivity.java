package com.uva.asm2.cluster02.modelo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uva.asm2.cluster02.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

public class ExportActivity extends AppCompatActivity {
    private EditText nombre;
    private TextView ruta;
    private ListView lista;
    private String ubicación;
    private ArrayList<String> listaRutaArchivo;
    private ArrayAdapter<String> adaptador;
    private String directorioRaiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        Button boton;
        directorioRaiz = Environment.getExternalStorageDirectory().getPath();
        lista = (ListView)findViewById(R.id.lista_exportador);
        boton = (Button)findViewById(R.id.aceptar_Archivo);
        nombre = (EditText)findViewById(R.id.editTextNobre);
        ruta= (TextView)findViewById(R.id.rutaText);
        verDirectorios(directorioRaiz);

        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre.setText("");
            }
        });
        boton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!nombre.getText().equals("")){
                    File file = new File(ubicación, nombre.getText()+".txt");
                    BufferedWriter bf=null;
                    try
                    {
                        System.out.print("llego");
                        bf = new BufferedWriter(new FileWriter(file,false));

                        MovimientoDbHelper bd= new MovimientoDbHelper(v.getContext());
                        ArrayList<Movimiento> listaIngresos = bd.listaIngresos();
                        System.out.println(listaIngresos.size());
                        for (int i = 0; i < listaIngresos.size(); i++){
                            Movimiento mov = listaIngresos.get(i);
                            GregorianCalendar fecha= mov.getFecha();
                            String dia= fecha.get(Calendar.DAY_OF_MONTH)+"/"+fecha.get(Calendar.MONTH)+"/"+fecha.get(Calendar.YEAR);
                            bf.write(mov.getTipo()+","+mov.getCantidad()+","+mov.getDescripcion()+","+dia+","+mov.getCategoria()+","+mov.getFoto()+","+mov.getPlaceholder()+"\n");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            // Nuevamente aprovechamos el finally para
                            // asegurarnos que se cierra el fichero.
                            if (null != bf)
                                bf.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }finally {
                            Toast toast = Toast.makeText(getBaseContext(),
                                    R.string.exportado, Toast.LENGTH_SHORT);
                            toast.show();
                            finish();
                        }
                    }

                }
            }

        });
    }

    private ArrayList<String> apilarArray(ArrayList<String> archivosTotales, ArrayList<String> parciales){
        for(int i = 0; i<parciales.size();i++){
            archivosTotales.add(parciales.get(i));
        }
        return archivosTotales;
    }

    private void verDirectorios(String rutaDirectorio) {
        ArrayList<String> listaNombreArchivo;
        listaNombreArchivo = new ArrayList<>();
        listaRutaArchivo = new ArrayList<>();
        ubicación=rutaDirectorio;
        File directorioActual = new File(rutaDirectorio);
        File [] listaArchivos = directorioActual.listFiles();

        int x=0; //Variable de posición

        if(!rutaDirectorio.equals(directorioRaiz)){
            listaNombreArchivo.add("../");
            ruta.setText(directorioActual.getPath());
            listaRutaArchivo.add(directorioActual.getParent());
            x=1;
        }else{
            ruta.setText(directorioRaiz);
        }
        //Listamos los archivos y cogemos las ubicaciones
        //Necesitamos separarlos ya que los queremos en order alfabetico con prioridad para las carpetas.
        ArrayList<String> archivosRuta = new ArrayList<>();
        ArrayList<String> carpetasRuta = new ArrayList<>();
        for (File archivo:listaArchivos){
            if (archivo.isFile()){
                archivosRuta.add(archivo.getPath());
            }else{
                carpetasRuta.add(archivo.getPath());
            }
        }
        //Ordenamos las listas alfabeticamente
        Collections.sort(archivosRuta, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(carpetasRuta, String.CASE_INSENSITIVE_ORDER);
        listaRutaArchivo= apilarArray(listaRutaArchivo,carpetasRuta);
        listaRutaArchivo=apilarArray(listaRutaArchivo,archivosRuta);
        ArrayList<String> archivos = new ArrayList<>();
        ArrayList<String> carpetas = new ArrayList<>();
        for(int i=x; i<listaRutaArchivo.size();i++) {
            File archivo = new File(listaRutaArchivo.get(i));

            if (archivo.isFile()) {
                //Si es un archivo le cogemos el nombre
                archivos.add(archivo.getName());
            } else {
                carpetas.add(archivo.getName());
            }
        }
        listaNombreArchivo = apilarArray(listaNombreArchivo,carpetas);
        listaNombreArchivo = apilarArray(listaNombreArchivo,archivos);
        if(listaArchivos.length<1){
            listaNombreArchivo.add(getString(R.string.no_hay_archivos));
            listaRutaArchivo.add(rutaDirectorio);
        }
        ArrayAdapter adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNombreArchivo);

        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                File archivo = new File(listaRutaArchivo.get(position));

                if(archivo.isFile()){//Si es un archivo
                    String ubicacion = archivo.getAbsolutePath();
                    //De esta forma no cogemos el .txt .
                    String texto = archivo.getName().substring(0,archivo.getName().lastIndexOf('.'));
                    nombre.setText(texto);

                }else{
                    verDirectorios(listaRutaArchivo.get(position));
                }


            }
        });

    }
}