package com.example.torre.circuloapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Notificaciones extends AppCompatActivity {
    ArrayList<String> datos2;
    ListView listaProfesores;
    AdaptadorProfesores prof_adaptador;
    ObtenerWebServices hiloconexion;
    String num = "";
    String IP = "http://servidorweb.esy.es";
    String NOTIFICACIONES = IP + "/consultar_notificaciones.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        Bundle bundle = this.getIntent().getExtras();
        num = bundle.getString("idgrupo");
        notificaciones();
    }
    public void notificaciones()
    {
        hiloconexion = new ObtenerWebServices();
        hiloconexion.execute(NOTIFICACIONES);
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
                JSONArray jsonArray = respuestaJSON.getJSONArray("alumno");
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getString("idgrupo").equals(num)){
                        datos2.add(jsonArray.getJSONObject(i).getString("mensaje"));
                    }

                }

            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        //Añadiendo el nuevo adaptador
        listaProfesores = (ListView) findViewById(R.id.listaModeradores);
        prof_adaptador = new AdaptadorProfesores(getApplicationContext(), datos2);
        listaProfesores.setAdapter(prof_adaptador);

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
}
