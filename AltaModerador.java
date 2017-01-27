package com.example.torre.circuloapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class AltaModerador extends AppCompatActivity {

    Button btnAgregar;
    EditText edtNumero, edtContraseña, edtConfirmacionContraseña;
    Intent intent;

    // IP de mi Url
    String IP = "http://servidorweb.esy.es";
    // Rutas de los Web Services

    String INSERT = IP + "/insertar_moderador.php";
    ObtenerWebService hiloconexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_moderador);

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtContraseña = (EditText) findViewById(R.id.edtContraseña);
        edtConfirmacionContraseña = (EditText) findViewById(R.id.edtContraseñaConfirmacion);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNumero.getText().toString().isEmpty()) {
                    edtNumero.setError("Ingrese Numero");
                } else if(edtContraseña.getText().toString().isEmpty()){
                    edtContraseña.setError("Ingrese NIP");
                } else if(edtConfirmacionContraseña.getText().toString().equals(edtContraseña.getText().toString())){
                   // Toast.makeText(AltaModerador.this, "Moderador  Agregado", Toast.LENGTH_SHORT).show();

                    hiloconexion = new ObtenerWebService();
                    hiloconexion.execute(INSERT,"1",edtNumero.getText().toString(),edtContraseña.getText().toString());// Parámetros que recibe doInBackground
                    Toast.makeText(AltaModerador.this, "Agregando...", Toast.LENGTH_SHORT).show();
                } else {
                    edtConfirmacionContraseña.setError("Confirmacion Invalida");


                }
                edtNumero.setText("");
                edtContraseña.setText("");
                edtConfirmacionContraseña.setText("");

                // intent = new Intent(AltaModerador.this, InicioAdministrador.class);
                //startActivity(intent);
            }


        });

    }


    public class ObtenerWebService extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve ="";
            //Definir el parametro a utilizar para insertar el moderador
            if(params[1]=="1"){    // insert

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
                    jsonParam.put("N_CONTROL", params[2]);
                    jsonParam.put("NIP", params[3]);
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

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON == "1") {      // hay un alumno que mostrar
                            devuelve = "Moderador insertado correctamente";

                        } else if (resultJSON == "2") {
                            devuelve = "El moderador no pudo insertarse";
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

            return "No conexión";
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            // resultado.setText(s);
            //super.onPostExecute(s);

            if (s.equals("Moderador insertado correctamente")){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                intent = new Intent(AltaModerador.this, InicioAdministrador.class);
                startActivity(intent);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}