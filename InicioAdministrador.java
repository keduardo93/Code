package com.example.torre.circuloapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InicioAdministrador extends AppCompatActivity {

    Button btnAgregar, btnEliminar;
    Intent intent;
    String numeroc = "";
    ArrayList<String> datos2;
    ListView listaProfesores;
    AdaptadorProfesores prof_adaptador;
    ObtenerWebServices hiloconexion;
    ServiceEliminar hiloeliminar;
    String IP = "http://servidorweb.esy.es";
    String GETIMODERADOR = IP + "/consultar_imoderador.php";
    String DELETEMODERADOR = IP + "/eliminar_moderador.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_administrador);

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
       // btnEliminar = (Button) findViewById(R.id.btnEliminar);
reinicia();


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btnAgregar):
                intent = new Intent(InicioAdministrador.this, AltaModerador.class);
                startActivity(intent);
                break;

           // case (R.id.btnEliminar):
             //   Toast.makeText(InicioAdministrador.this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
               // break;
        }
    }
    public void reinicia()
    {
        hiloconexion = new ObtenerWebServices();
        hiloconexion.execute(GETIMODERADOR);
    }    public class ObtenerWebServices extends AsyncTask<String,Void,String> {

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
                        datos2.add(jsonArray.getJSONObject(i).getString("N_CONTROL") + " " +
                                jsonArray.getJSONObject(i).getString("Nombre") + " " +
                                jsonArray.getJSONObject(i).getString("Carrera") + " " +
                                jsonArray.getJSONObject(i).getString("Semestre") + " " +
                                jsonArray.getJSONObject(i).getString("Correo") + " " +
                                jsonArray.getJSONObject(i).getString("Telefono"));
                    }

                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            for(int i = 0; i < datos2.size(); i ++){
                System.out.println(datos2.get(i));
            }
            //Añadiendo el nuevo adaptador
            listaProfesores = (ListView) findViewById(R.id.listaModeradores);
            prof_adaptador = new AdaptadorProfesores(getApplicationContext(), datos2);
            listaProfesores.setAdapter(prof_adaptador);

            listaProfesores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {
                    String seleccion = datos2.get(index);
                    numeroc = seleccion.substring(0, 8);
                    //Esto va dentro del onClick del ListView papi
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(InicioAdministrador.this);
                    dialogo1.setTitle("¡Ten Cuidado!");
                    dialogo1.setMessage("¿Desea eliminar este moderador?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            aceptar(numeroc);
                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            cancelar();
                        }
                    });
                    dialogo1.show();





                    return true;
                }
            });
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
    public class AdaptadorProfesores extends ArrayAdapter<String> {
        ArrayList<String> datos;

        public AdaptadorProfesores(Context context, ArrayList<String> datos) {
            super(context, R.layout.vista_profesor, datos);
            this.datos = datos; }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.vista_profesor, null);

            TextView tvNombreProfesor = (TextView) item.findViewById(R.id.tvNombreProfesor);
            tvNombreProfesor.setText(datos.get(position));

            return (item);
        }
    }
    public class ServiceEliminar extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve ="";

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
                jsonParam.put("N_CONTROL", params[1]);
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
                        devuelve = "Alumno borrado correctamente";

                    } else if (resultJSON == "2") {
                        devuelve = "No hay alumnos";
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


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            if (s.equals("Alumno borrado correctamente")){
                reinicia();
            } else{
                Toast.makeText(getApplicationContext(),"Algo ocurrio mal", Toast.LENGTH_SHORT).show();
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
    public void aceptar(String s) {
        String control = s;

        //Aqui el codigo para eliminarlo a la verga
        hiloeliminar = new ServiceEliminar();
        hiloeliminar.execute(DELETEMODERADOR, control);
        Toast t = Toast.makeText(this,"Moderador Eliminado", Toast.LENGTH_SHORT);
        t.show();
    }

    public void cancelar() {
        //Pero no quiero
        Toast t = Toast.makeText(this,"Buena decisión", Toast.LENGTH_SHORT);
        t.show();

    }
}