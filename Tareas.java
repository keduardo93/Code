package com.example.torre.circuloapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Tareas extends AppCompatActivity {

    // IP de mi Url
    String IP = "http://servidorweb.esy.es";
    // Rutas de los Web Services
    String INSERT = IP + "/revisar_tareas.php";
    //  ObtenerWebService hiloconexion;
    ObtenerWebService hiloconexion;
    String tarea1 = "", tarea2= "", tarea3= "", tarea4= "", tarea5= "";
    CheckBox cbxTarea1, cbxTarea2, cbxTarea3, cbxTarea4, cbxTarea5;
    Button btnRegistrar;
    EditText edtNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        cbxTarea1 = (CheckBox) findViewById(R.id.cbxTarea1);
        cbxTarea2 = (CheckBox) findViewById(R.id.cbxTarea2);
        cbxTarea3 = (CheckBox) findViewById(R.id.cbxTarea3);
        cbxTarea4 = (CheckBox) findViewById(R.id.cbxTarea4);
        cbxTarea5 = (CheckBox) findViewById(R.id.cbxTarea5);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        cbxTarea1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                if (isChecked) {
                    tarea1 = "Entregado";
                } else {
                    tarea1 = "No entregado";
                }
            }
        });
        cbxTarea2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                if (isChecked) {
                    tarea2 = "Entregado";
                } else {
                    tarea2 = "No entregado";
                }
            }
        });
        cbxTarea3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                if (isChecked) {
                    tarea3 = "Entregado";
                } else {
                    tarea3 = "No entregado";
                }
            }
        });
        cbxTarea4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                if (isChecked) {
                    tarea4 = "Entregado";
                } else {
                    tarea4 = "No entregado";
                }
            }
        });
        cbxTarea5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                if (isChecked) {
                    tarea5 = "Entregado";
                } else {
                    tarea5 = "No entregado";
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiloconexion = new ObtenerWebService();
                hiloconexion.execute(INSERT, "1", edtNumero.getText().toString(), tarea1, tarea2, tarea3, tarea4, tarea5);
                Toast.makeText(Tareas.this, "Ahi vamo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ObtenerWebService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve = "";
            //Definir el parametro a utilizar para insertar el moderador
            if (params[1] == "1") {    // insert

                try {
                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(cadena);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();
                    //Creo el Objeto JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("NoControl", params[2]);
                    jsonParam.put("Tarea1", params[3]);
                    jsonParam.put("Tarea2", params[4]);
                    jsonParam.put("Tarea3", params[5]);
                    jsonParam.put("Tarea4", params[6]);
                    jsonParam.put("Tarea5", params[7]);
                    // Envio los parámetros post.
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    int respuesta = urlConn.getResponseCode();


                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            result.append(line);
                            //response+=line;
                        }
                        System.out.println(result);
                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON == "1") {      // hay un alumno que mostrar
                            devuelve = "Evaluación insertada correctamente";

                        } else if (resultJSON == "2") {
                            devuelve = "La evaluación no pudo insertarse";
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;


            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            // resultado.setText(s);
            //super.onPostExecute(s);

            if (s.equals("Evaluación insertada correctamente")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), InicioModerador.class);
                startActivity(intent);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }
    }
}
