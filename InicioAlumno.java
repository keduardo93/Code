package com.example.torre.circuloapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InicioAlumno extends AppCompatActivity {
    Button btnAgregar, btnEliminar,btnEnsayo, btnNotifica;
    Intent intent;
    String grupo= "";
    String moderador= "";
    String ctr="";
    String hora= "";
    String estado = "";
    String dias= "";
    ArrayList<String> datos;
    String genero= "";
    String saludo= "";
    DatosGrupo hilodatos;
    TextView tvMod, tvGen, tvDias, tvHora;

    String num = "";
    ObtenerWebServices hiloconexion;

    String IP = "http://servidorweb.esy.es"; //IP de mi URL
    //Rutas de los Web Services
    String VERIFICA_GRUPO = IP + "/existe_en_grupo.php";
    String DATOS_GRUPO = IP + "/getInfoGrupo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_alumno);
        tvMod = (TextView) findViewById(R.id.tvmoderador);
        tvGen = (TextView) findViewById(R.id.tvgenero);
        tvDias = (TextView) findViewById(R.id.tvdias);
        tvHora = (TextView) findViewById(R.id.tvhora);
        btnNotifica = (Button) findViewById(R.id.button);


        btnEnsayo = (Button) findViewById(R.id.btnEnsayo);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
       // btnEliminar = (Button) findViewById(R.id.btnEliminar);
        Bundle bundle = this.getIntent().getExtras();
        num = bundle.getString("Numero");

       verifica();

        btnEnsayo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RedactarEnsayo.class);
                startActivity(intent);
            }
        });
        btnNotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                System.out.println("hola" +  grupo);
                b.putString("idgrupo", grupo);
                Intent intent = new Intent(getApplicationContext(), Notificaciones.class);
                intent.putExtras(b);

                startActivity(intent);
            }
        });
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
            System.out.println("existe alumno en grupo?");
            System.out.println(s);



            try {
                JSONObject respuestaJSON = new JSONObject(s);
                String resultJSON = respuestaJSON.getString("estado");// estado es el nombre del campo en el JSON
                System.out.println(resultJSON);

               if(resultJSON.equals("2")){
                    estado = respuestaJSON.getString("mensaje");

                    System.out.println(estado);
                    if (estado.equals("No se obtuvo el registro")) {
                        Bundle b = new Bundle();
                        b.putString("Numero", num);
                        intent = new Intent(InicioAlumno.this, SeleccionarModerador.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        Toast.makeText(InicioAlumno.this, "Por favor elige un grupo", Toast.LENGTH_SHORT).show();
                        finish();

                    }}else  if (resultJSON.equals("1")) {      // hay alumnos a mostrar
                    String alumno = respuestaJSON.getString("alumno");
                    ctr = alumno.substring(12,20);
                    System.out.println(ctr + alumno);
                    grupo = ctr;
                    System.out.println(grupo);
                   muestraGrupo(grupo);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }}

            @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    public void verifica(){

        hiloconexion = new ObtenerWebServices();
        String cadena = VERIFICA_GRUPO + "?Numero_Control=" + num;
        hiloconexion.execute(cadena, "1");
    }
    public void muestraGrupo(String g){
        String grupob = g;
        hilodatos = new DatosGrupo();
        System.out.println(grupob);
        String cadena = DATOS_GRUPO + "?idgrupo="+grupob ;
        System.out.println(cadena);
        hilodatos.execute(cadena);


    }
    public class DatosGrupo extends AsyncTask<String,Void,String> {

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
            System.out.println("informacion de grupo");
            System.out.println(s);
            String devuelve= "";
            datos = new ArrayList<>();

            try {
                JSONObject respuestaJSON = new JSONObject(s);
                String resultJSON = respuestaJSON.getString("estado");// estado es el nombre del campo en el JSON
                if (resultJSON.equals("1")){
                    moderador = respuestaJSON.getJSONObject("info").getString("Moderador");
                    genero = respuestaJSON.getJSONObject("info").getString("Genero");
                    dias = respuestaJSON.getJSONObject("info").getString("Dias");
                    hora = respuestaJSON.getJSONObject("info").getString("Hora");
                }


            } catch (JSONException e1) {
                e1.printStackTrace();
            }
                System.out.println(" "+moderador+" "+genero+" "+dias+" "+hora);
                tvMod.setText(""+moderador);
                tvGen.setText(""+genero);
                tvDias.setText(""+dias);
                tvHora.setText(""+hora);
                Toast.makeText(InicioAlumno.this, "Bienvenido Alumno", Toast.LENGTH_SHORT).show();


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
}
