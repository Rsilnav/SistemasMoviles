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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

public class ImportActivity extends AppCompatActivity {
    private EditText nombre;
    private ListView lista;
    private TextView ruta;
    private String ubicaccion;
    private ArrayList<String> listaRutaArchivo;
    private ArrayAdapter<String> adaptador;
    private String directorioRaiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importador);
        Button botonImportarYEliminar;
        Button botonImportar;
        directorioRaiz = Environment.getExternalStorageDirectory().getPath();
        lista = (ListView)findViewById(R.id.lista_importador);
        botonImportar = (Button)findViewById(R.id.aceptar_Archivo);
        botonImportarYEliminar = (Button)findViewById(R.id.boton_eliminar_importar) ;
        nombre = (EditText)findViewById(R.id.editTextNobre);
        ruta= (TextView)findViewById(R.id.rutaText);
        verDirectorios(directorioRaiz);
        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre.setText("");
            }
        });
        botonImportar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FileReader file;

                BufferedReader bf;
                System.out.println("Si que llego aqui");
                try
                {
                    file = new FileReader(ubicaccion);
                    bf = new BufferedReader(file);
                    String[] arrayColores;
                    String[] dia;
                    MovimientoDbHelper bd= new MovimientoDbHelper(v.getContext());
                    Movimiento movimiento;
                    GregorianCalendar diaMovimiento;
                    String linea;
                    while((linea= bf.readLine())!=null) {
                        arrayColores = linea.split(",");
                        dia = arrayColores[3].split("/");
                        diaMovimiento = new GregorianCalendar(Integer.parseInt(dia[2]), Integer.parseInt(dia[1]), Integer.parseInt(dia[0]));
                        movimiento = new Movimiento(Movimiento.TIPO.valueOf(arrayColores[0]),
                                Float.parseFloat(arrayColores[1]), arrayColores[2], diaMovimiento,
                                Movimiento.CATEGORIA.valueOf(arrayColores[4]), arrayColores[5], Integer.parseInt(arrayColores[6]));
                        bd.saveMovimiento(movimiento);
                    }
                    bf.close();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Toast toast = Toast.makeText(getBaseContext(),
                            R.string.importado, Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }


            }

        });
        botonImportarYEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FileReader file;

                BufferedReader bf;
                try
                {
                    file = new FileReader(ubicaccion);
                    bf = new BufferedReader(file);
                    String[] arrayColores;
                    String[] dia;
                    MovimientoDbHelper bd= new MovimientoDbHelper(v.getContext());
                    bd.deleteAll();
                    Movimiento movimiento;
                    GregorianCalendar diaMovimiento;
                    String linea;
                    while((linea= bf.readLine())!=null) {
                        arrayColores = linea.split(",");
                        dia = arrayColores[3].split("/");
                        diaMovimiento = new GregorianCalendar(Integer.parseInt(dia[2]), Integer.parseInt(dia[1]), Integer.parseInt(dia[0]));
                        movimiento = new Movimiento(Movimiento.TIPO.valueOf(arrayColores[0]),
                                Float.parseFloat(arrayColores[1]), arrayColores[2], diaMovimiento,
                                Movimiento.CATEGORIA.valueOf(arrayColores[4]), arrayColores[5], Integer.parseInt(arrayColores[6]));
                        bd.saveMovimiento(movimiento);
                    }
                    bf.close();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Toast toast = Toast.makeText(getBaseContext(),
                            R.string.importado, Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
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
        ubicaccion =rutaDirectorio;
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
        ArrayList<String> archivosRuta = new ArrayList<>();
        ArrayList<String> carpetasRuta = new ArrayList<>();
        for (File archivo:listaArchivos){
            if (archivo.isFile()){
                archivosRuta.add(archivo.getPath());
            }else{
                carpetasRuta.add(archivo.getPath());
            }
        }
        Collections.sort(archivosRuta, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(carpetasRuta, String.CASE_INSENSITIVE_ORDER);
        listaRutaArchivo= apilarArray(listaRutaArchivo,carpetasRuta);
        listaRutaArchivo=apilarArray(listaRutaArchivo,archivosRuta);
        //Ordenamos la lista alfabeticamente
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
                    ubicaccion = archivo.getAbsolutePath();
                    nombre.setText(archivo.getName());

                }else{
                    verDirectorios(listaRutaArchivo.get(position));
                }


            }
        });

    }
}