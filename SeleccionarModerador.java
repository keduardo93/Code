package com.example.torre.circuloapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

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

public class SeleccionarModerador extends AppCompatActivity {
    String IP = "http://servidorweb.esy.es";
    String GETGRUPOS = IP + "/getGrupos.php";
    String INSERT_A_GRUPO = IP + "/insert_alumno_a_grupo.php";
    String REGISTRO = IP + "/insert_key.php";
    String MISDATOS = IP + "/getInfoAlumno.php";
    ListView listaGrupos;
    AdaptadorProfesores prof_adaptador;
    AltaDispositivo alta_disp;
    String PROJECT_NUMBER = "132779880855";
    GoogleCloudMessaging gcm;
    String regid;
    String KeyRegistro = "";
    String usuario = "";
    Intent intent;
    String nombre="";
    String carrera="";
    String semestre="";
    String correo="";

    AltaAlumnoAGrupo alta;
    ArrayList<String> datos2;

    ObtenerWebServices hiloconexion;
    ObtenerInfoAlumno hiloinfo;
    String numeroc = "";
    String num = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_moderador);
        Bundle bundle = this.getIntent().getExtras();
        num = bundle.getString("Numero");
        infoAlumno();

        cargaList();


    }

    public void cargaList(){
        hiloconexion = new ObtenerWebServices();
        hiloconexion.execute(GETGRUPOS);
    }
    public void infoAlumno(){

        hiloinfo = new ObtenerInfoAlumno();
        String cadena = MISDATOS +"?N_CONTROL=" + num;
        hiloinfo.execute(cadena);
    }
    public void altaDispositivo(String k){
        String key = k;
        alta_disp = new AltaDispositivo();
        alta_disp.execute(REGISTRO,numeroc, num, key);

    }
    public class ObtenerWebServices extends AsyncTask<String,Void,String> {

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
                    JSONArray jsonArray = respuestaJSON.getJSONArray("grupos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        datos2.add(jsonArray.getJSONObject(i).getString("idgrupo") + " " +
                                jsonArray.getJSONObject(i).getString("Moderador") + " " +
                                jsonArray.getJSONObject(i).getString("Hora") + " " +
                                jsonArray.getJSONObject(i).getString("Dias") + " " +
                                jsonArray.getJSONObject(i).getString("Genero") + " " +
                                jsonArray.getJSONObject(i).getString("Saludo"));
                    }

                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            for(int i = 0; i < datos2.size(); i ++){
                System.out.println(datos2.get(i));
            }
            //Añadiendo el nuevo adaptador
            listaGrupos = (ListView) findViewById(R.id.listView);
            prof_adaptador = new AdaptadorProfesores(getApplicationContext(), datos2);
            listaGrupos.setAdapter(prof_adaptador);

            listaGrupos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {
                    String seleccion = datos2.get(index);
                    numeroc = seleccion.substring(0, 8);
                    //Esto va dentro del onClick del ListView papi
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(SeleccionarModerador.this);
                    dialogo1.setTitle("ALERTA");
                    dialogo1.setMessage("¿Deseas inscribirte a este grupo?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            System.out.println("boton aceptar 2+");
                            aceptar();
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
            super(context, R.layout.vista_moderadores, datos);
            this.datos = datos; }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.vista_moderadores, null);

            TextView tvNombreProfesor = (TextView) item.findViewById(R.id.tvModeradores);
            tvNombreProfesor.setText(datos.get(position));

            return (item);
        }
    }
    public void aceptar() {
        System.out.println("boton aceptar");
        Toast t = Toast.makeText(this,"info Alumno" +numeroc+num+nombre+carrera+semestre+correo, Toast.LENGTH_SHORT);
        t.show();

        alta = new AltaAlumnoAGrupo();
        alta.execute(INSERT_A_GRUPO, numeroc, num, nombre, carrera, semestre, correo);

    }

    public void cancelar() {
        //Pero no quiero
        Toast t = Toast.makeText(this,"Escoge uno", Toast.LENGTH_SHORT);
        t.show();

    }
    public class AltaAlumnoAGrupo extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // URL de donde queremos obtener información
            String devuelve = "";


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
                    jsonParam.put("idgrupo", params[1]);
                    jsonParam.put("Numero_Control", params[2]);
                    jsonParam.put("Nombre", params[3]);
                    jsonParam.put("Carrera", params[4]);
                    jsonParam.put("Semestre", params[5]);
                    jsonParam.put("Correo", params[6]);

                    // Envio los parámetros post.
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    int respuesta = urlConn.getResponseCode();


                    StringBuilder resultado = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            resultado.append(line);
                            //response+=line;
                        }
String result = resultado.toString();
                       // devuelve = resultado.toString();
                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON == "1") {      // hay un alumno que mostrar
                            devuelve += "Información Agregada Correctamente";

                        } else if (resultJSON == "2") {
                            devuelve += "Error en la conexión";
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
            //super.onPostExecute(s);
            //Toast.makeText(getApplicationContext(), "Sale esto = " + s , Toast.LENGTH_LONG).show();

            if(s.equals("Error en la conexion") ) {
                Toast.makeText(getApplicationContext(), "Errores en la información", Toast.LENGTH_LONG).show();
            }
            else if(s.equals("Información Agregada Correctamente") ){
                registraDispositivo();
                Bundle b = new Bundle();
                b.putString("Numero", num);
                intent = new Intent(getApplicationContext(), InicioAlumno.class);
                intent.putExtras(b);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "TE HAS DADO DE ALTA EN EL GRUPO", Toast.LENGTH_LONG).show();
                finish();
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
    public class ObtenerInfoAlumno extends AsyncTask<String,Void,String> {

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



            try {
                JSONObject respuestaJSON = new JSONObject(s);
                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON=="1") {
                    nombre = respuestaJSON.getJSONObject("alumno").getString("NOMBRE");
                   carrera = respuestaJSON.getJSONObject("alumno").getString("CARRERA");
                    semestre = respuestaJSON.getJSONObject("alumno").getString("SEMESTRE");
                    correo = respuestaJSON.getJSONObject("alumno").getString("CORREO");


                }
                System.out.println(nombre + carrera+ semestre+ correo);

            } catch (JSONException e1) {
                e1.printStackTrace();
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
    public class AltaDispositivo extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];

            URL url = null; // URL de donde queremos obtener información
            String devuelve = "";


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
                jsonParam.put("idgrupo", params[1]);
                jsonParam.put("N_Control", params[2]);
                jsonParam.put("Clave", params[3]);


                // Envio los parámetros post.
                OutputStream os = urlConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int respuesta = urlConn.getResponseCode();


                StringBuilder resultado = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK) {

                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        resultado.append(line);
                        //response+=line;
                    }
                    String result = resultado.toString();
                    // devuelve = resultado.toString();
                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                    if (resultJSON == "1") {      // hay un alumno que mostrar
                        devuelve += "Información Agregada Correctamente";

                    } else if (resultJSON == "2") {
                        devuelve += "Error en la conexión";
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
            //super.onPostExecute(s);
            //Toast.makeText(getApplicationContext(), "Sale esto = " + s , Toast.LENGTH_LONG).show();

            if(s.equals("Error en la conexion") ) {
                Toast.makeText(getApplicationContext(), "Errores al guardar KEY de Dispositivo", Toast.LENGTH_LONG).show();
            }
            else if(s.equals("Información Agregada Correctamente") ){


                Toast.makeText(getApplicationContext(), "Registro en GCM Exitoso", Toast.LENGTH_LONG).show();

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

    public void registraDispositivo(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
               KeyRegistro = msg;
                System.out.println(KeyRegistro);

                altaDispositivo(KeyRegistro);
            }
        }.execute(null, null, null);
    }
}
