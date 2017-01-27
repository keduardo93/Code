package com.example.torre.circuloapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class CrearGrupo extends AppCompatActivity {

    // IP de mi Url
    String IP = "http://servidorweb.esy.es";
    // Rutas de los Web Services

    String INSERT = IP + "/insertar_grupo.php";
    ObtenerWebService hiloconexion;
    String idgrupo, nip;

Intent intent;
    EditText edtDia, edtHora, edtGenero, edtMensaje, edtNombre, edtNumero;
    Button btnAgregar;
    TextView txtv;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);

        txtv = (TextView) findViewById(R.id.textView2);
        edtDia = (EditText) findViewById(R.id.edtDía);
        edtHora = (EditText) findViewById(R.id.edtHora);
        edtGenero = (EditText) findViewById(R.id.edtGeneroLibro);
        edtMensaje = (EditText) findViewById(R.id.edtMensaje);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
      //  edtNumero = (EditText) findViewById(R.id.edtN_Control);

        Bundle bundle = this.getIntent().getExtras();
        idgrupo = bundle.getString("Numero");
        nip = bundle.getString("Nip");

       txtv.setText(idgrupo);

    }

    public void onClick(View v){
        switch (v.getId()){
            case (R.id.btnAgregar):
                if (edtDia.getText().toString().isEmpty()){
                    edtDia.setError("Ingrese Dia");
                } else if(edtHora.getText().toString().isEmpty()){
                    edtHora.setError("Ingrese Hora");
                } else if(edtGenero.getText().toString().isEmpty()){
                    edtGenero.setError("Ingrese Genero");
                }
                else if(edtNombre.getText().toString().isEmpty()){
                    edtNombre.setError("Ingrese Hora");
                }
                else if(edtNumero.getText().toString().isEmpty()){
                    edtNumero.setError("Ingrese Hora");
                } else{
                  //  Toast.makeText(CrearGrupo.this, "Agregado Correctamente", Toast.LENGTH_SHORT).show();
                    hiloconexion = new ObtenerWebService();
                    hiloconexion.execute(INSERT, "1", edtNumero.getText().toString(),
                            edtNombre.getText().toString(),
                            edtHora.getText().toString(),
                            edtDia.getText().toString(),
                            edtGenero.getText().toString(),
                            edtMensaje.getText().toString());// Parámetros que recibe doInBackground

                }
                            break;
        }
    }


    public class ObtenerWebService extends AsyncTask<String,Void,String> {

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
                    jsonParam.put("idgrupo", params[2]);
                    jsonParam.put("Moderador", params[3]);
                    jsonParam.put("Hora", params[4]);
                    jsonParam.put("Dias", params[5]);
                    jsonParam.put("Genero", params[6]);
                    jsonParam.put("Saludo", params[7]);
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
                            devuelve = "Grupo insertado correctamente";

                        } else if (resultJSON == "2") {
                            devuelve = "El grupo no pudo insertarse";
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

            if (s.equals("Grupo insertado correctamente")){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                intent = new Intent(CrearGrupo.this, InicioModerador.class);
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