package com.example.torre.circuloapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegistroModerador extends Activity {

    EditText edtNombre, edtIngenieria, edtCorreo, edtNumero, edtSemestre, edtTelefono;
    TextView txtNombre, txtCarrera, txtCorreo, txtNumero, txtSemestre, txtTelefono;
    Button btnAgregar;
    ArrayList<String> datos2;
    TextView txt;
    ObtenerWebService hiloconexion;
    InfoModerador hiloinfo;
    String IP = "http://servidorweb.esy.es"; //IP de mi URL
    String INSERT_I_MODERADOR = IP + "/insertar_info_moderador.php";
    String GETIMODERADOR = IP + "/consultar_imoderador.php";
    String grupo = "";
    String tab = "";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_registro_moderador);

        Bundle bundle = this.getIntent().getExtras();
        grupo = bundle.getString("idgrupo");

        System.out.println(grupo);



        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtCarrera = (TextView) findViewById(R.id.txtCarrera);
        txtCorreo = (TextView) findViewById(R.id.txtCorreo);
        txtNumero= (TextView) findViewById(R.id.txtNumero);
        txtSemestre = (TextView) findViewById(R.id.txtSemestre);
        txtTelefono = (TextView) findViewById(R.id.txtTelefono);

        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtIngenieria = (EditText) findViewById(R.id.edtCarrera);
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        edtSemestre = (EditText) findViewById(R.id.edtSemestre);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
       // txt = (TextView) findViewById(R.id.textView2);


        Resources res = getResources();

        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("tab1");
        spec.setContent(R.id.tabDatos);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_menu_info_details));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("tab2");
        spec.setContent(R.id.tabConfig);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_menu_edit));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "tab1":
                        System.out.println("tab1");
                        getinfo();
                        break;

                }
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btnAgregar):
                if (edtNombre.getText().toString().isEmpty()) {
                    edtNombre.setError("Ingrese Nombre");
                } else if (edtNumero.getText().toString().isEmpty()) {
                    edtNumero.setError("Ingrese N° Control");
                } else if (edtIngenieria.getText().toString().isEmpty()) {
                    edtIngenieria.setError("Ingrese Carrera");
                } else if (edtSemestre.getText().toString().isEmpty()) {
                    edtSemestre.setError("Ingrese Semestre");
                } else if (edtCorreo.getText().toString().isEmpty()) {
                    edtCorreo.setError("Ingrese Correo");
                } else if (edtTelefono.getText().toString().isEmpty()) {
                    edtTelefono.setError("Ingrese Telefono");
                } else {
                    hiloconexion = new ObtenerWebService();
                    hiloconexion.execute(INSERT_I_MODERADOR, "1", edtNumero.getText().toString(), edtNombre.getText().toString(),
                            edtIngenieria.getText().toString(), edtSemestre.getText().toString(), edtCorreo.getText().toString(),
                            edtTelefono.getText().toString());   // Parámetros que recibe doInBackground
                }


                break;
        }
    }

    public class ObtenerWebService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            String devuelve = "hola";
            URL url = null; // URL de donde queremos obtener información
if(params[1] == "1"){
            try {
                HttpURLConnection urlConn;

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
                jsonParam.put("Nombre", params[3]);
                jsonParam.put("Carrera", params[4]);
                jsonParam.put("Semestre", params[5]);
                jsonParam.put("Correo", params[6]);
                jsonParam.put("Telefono", params[7]);
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
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        result.append(line);
                        //response+=line;
                    }

                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                    if (resultJSON == "1") {      // hay un alumno que mostrar
                        devuelve += "Información Agregada Correctamente";

                    } else if (resultJSON == "2") {
                        devuelve += "Error en la conexión";
                    }

                } else{
                    devuelve += " error";
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
        return "No entró a conexión";
    }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
           // super.onPostExecute(s);
         txt.setText(s);
            Toast.makeText(getApplicationContext()," = " + s, Toast.LENGTH_LONG).show();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    public class InfoModerador extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {



            String Direccion = params [0];

            try {
                OkHttpClient cliente = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Direccion)
                        .build();


                Response response = cliente.newCall(request).execute();

                String respuesta = response.body().string();
                return respuesta;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            datos2 = new ArrayList<>();


            try {
                JSONObject respuestaJSON = new JSONObject(s);
                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON=="1") {
                    JSONArray jsonArray = respuestaJSON.getJSONArray("alumno");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i).getString("idgrupo").equals(grupo)){
                            datos2.add(jsonArray.getJSONObject(i).getString("Numero_Control") + " " +
                                    jsonArray.getJSONObject(i).getString("Nombre") + " " +
                                    jsonArray.getJSONObject(i).getString("Carrera") + " " +
                                    jsonArray.getJSONObject(i).getString("Semestre") + " " +
                                    jsonArray.getJSONObject(i).getString("Correo")+ " " +
                                    jsonArray.getJSONObject(i).getString("Telefono"));
                        }
                                           }
                    txtNumero.setText(datos2.get(0));
                    txtNombre.setText(datos2.get(1));
                    txtCarrera.setText(datos2.get(2));
                    txtSemestre.setText(datos2.get(3));
                    txtCorreo.setText(datos2.get(4));
                    txtTelefono.setText(datos2.get(5));

                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            for(int i = 0; i < datos2.size(); i ++){
                System.out.println(datos2.get(i));


            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    public void getinfo()
    {
        System.out.println("get info moderador");
        hiloinfo = new InfoModerador();
        hiloinfo.execute(GETIMODERADOR);
    }
}


