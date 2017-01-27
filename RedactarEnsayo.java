package com.example.torre.circuloapp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RedactarEnsayo extends AppCompatActivity {

    EditText et1, et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redactar_ensayo);

        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText2);
    }

    public void grabar(View v) {
        String nomarchivo = et1.getText().toString();
        String contenido = et2.getText().toString();

        try {
            //Obtenemos el directorio raíz de la memoria y el mismo retorna un objeto de la clase File.
            File tarjeta = Environment.getExternalStorageDirectory();

            //Creamos un nuevo objeto de la clase File indicando el camino de la unidad y el nombre del archivo a crear.
            File file = new File(tarjeta.getAbsolutePath(), nomarchivo);

            //Grabamos el contenido el EditText y cerramos el archivo.
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            osw.write(contenido);
            osw.flush();
            osw.close();
            Toast.makeText(this, "Los datos fueron grabados correctamente", Toast.LENGTH_SHORT).show();
            et1.setText("");
            et2.setText("");
        }
        catch (IOException ioe) {
        }
    }

    public void recuperar(View v) {
        String nomarchivo = et1.getText().toString();
        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(), nomarchivo);

        try {
            FileInputStream fIn = new FileInputStream(file);
            InputStreamReader archivo = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            String todo = "";

            while (linea != null) {
                todo = todo + linea + "\n";
                linea = br.readLine();
            }
            br.close();
            archivo.close();
            et2.setText(todo);
        }
        catch (IOException e) {
        }
    }
}
