package com.example.torre.circuloapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InicioModerador extends AppCompatActivity {
    Intent intent;
    ArrayList<String> datos2;
    ObtenerWebServices hiloconexion;
    Button btnInformacionPersonal, btnInformacionGrupo, btnTareas, btnEnsayos, btnCrearGrupo, btnEnviar, btnNota;
    EditText edtNota;
    String IP = "http://servidorweb.esy.es";
    String NOTAS =  IP + "/peticion_PUSH.php";
    String SUBENOTA = IP + "/insert_notificacion.php";
    String KEYS = IP + "/getKeys.php";
    NotificacionPush hilo;
    SubeNota subir;
    String se = "";
    String idgrupo;
    String nota="";
    String nip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_moderador);
        btnInformacionPersonal = (Button) findViewById(R.id.btnInformacionPersonal);
        btnInformacionGrupo = (Button) findViewById(R.id.btnInformacionGrupo);
        btnTareas = (Button) findViewById(R.id.btnTareas);
        btnEnsayos = (Button) findViewById(R.id.btnEnsayo);
        btnCrearGrupo = (Button) findViewById(R.id.btnCrearGrupo);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnNota = (Button) findViewById(R.id.btnNota);
        edtNota = (EditText) findViewById(R.id.edtNota);
        Bundle bundle = this.getIntent().getExtras();
        idgrupo = bundle.getString("Numero");
        nip = bundle.getString("Nip");

        keys();

    }
    public String getIdgrupo(){
        return idgrupo;
    }
    public void keys()
    {
        hiloconexion = new ObtenerWebServices();
        hiloconexion.execute(KEYS);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnInformacionPersonal:
                Bundle b3 = new Bundle();
                b3.putString("idgrupo", idgrupo);
                Intent intent = new Intent(getApplicationContext(), RegistroModerador.class);
                intent.putExtras(b3);
                startActivity(intent);
                break;

            case R.id.btnInformacionGrupo:
                Bundle b2 = new Bundle();
                b2.putString("idgrupo", idgrupo);
                Intent intent1 = new Intent(getApplicationContext(), InformacionGrupo.class);
                intent1.putExtras(b2);
                startActivity(intent1);
                break;

            case R.id.btnTareas:
                Intent intent2 = new Intent(getApplicationContext(), Tareas.class);
                startActivity(intent2);
                break;

            case R.id.btnEnsayo:
                Intent intent3 = new Intent(getApplicationContext(), Ensayos.class);
                startActivity(intent3);
                break;

            case R.id.btnCrearGrupo:
                Bundle b = new Bundle();
                b.putString("Numero", idgrupo);
                b.putString("Nip",nip);
                Intent intent5 = new Intent(getApplicationContext(), CrearGrupo.class);
                intent5.putExtras(b);
                startActivity(intent5);
                break;

            case R.id.btnNota:
                promptSpeechInput();
                break;
            case R.id.btnEnviar:
                nota = edtNota.getText().toString();
                subeNota(idgrupo, nota);

                for (int i = 0; i < datos2.size(); i ++){
                    String clave = datos2.get(i);
                    enviaNota(clave, nota);
                }

                edtNota.setText("");
                break;
        }
    }

    //metodo de la entrada de la voz muestra la pantalla donde se escuchara el sonido
    public void promptSpeechInput(){
        //creamos el intent para la acccion del reconocimiento de voz
        Intent i= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //pasamos datos a nuestra variable i con put extra
        //(EXTRA_LANGUAGE_MODEL)  Informa al reconocedor de cuál es el modelo del habla para preferir al realizar
        //(LANGUAJE_MODEL_FREE_FORM)  Utiliza un modelo de lenguaje basado en el reconocimiento de voz de forma libre
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //Escoge el lenguaje con el que vamos atrabajar
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //abre el la ventana para hablar
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"DIGA UN MENSAJE");
        try{
            //iniciamos la actividad enviando como parametro nuestro intent y un valor para que lo resiva nuestro switch
            startActivityForResult(i,100);
        }
        catch(ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),"Su dispositivo no soporta el speech !", Toast.LENGTH_LONG).show();
        }
    }

    //resultado de la actividad con parametros request_code, result_code, y un intent
    public void onActivityResult (int request_code,int result_code,Intent i){
        // sobreescribimos el metodo con el super mandando los mimos parametros
        super.onActivityResult(request_code,result_code,i);

        //creamos nuestro switch cuya variable sera request_code(codigo de solicitud)
        switch (request_code){
            //con lo que mandamos en nuestra starActivityForResult en este case podremos ver lo que hemos hablado
            case 100: if (result_code==RESULT_OK && i!=null){
                //en un arreglo guardamos nuestro resultado para poder mostrar con el extra_result
                ArrayList<String> result=i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                nota = result.get(0);

                edtNota.setText(result.get(0));
            }
                break;
        }
    }
    public void enviaNota(String clave,String note){
      //  String j = "APA91bFLloXjkvpES9L2PCoTT6Dih2ShHZCJzbZXJkCR5H7jgMuWX727-yCD-4snG_ev638dOSfcc2BbSsj76_edELXSfNdsb7qPsFiibdG0RmBricjsnXPl43SijtxzoTWnh1Y_1YKD";


        hilo = new NotificacionPush();
        hilo.execute(NOTAS, clave, note);
    }
    public void subeNota(String id, String msg){
        subir = new SubeNota();
        subir.execute(SUBENOTA,id, msg);
    }

    public class NotificacionPush extends AsyncTask<String,Void,String> {

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
                jsonParam.put("json", params[1]);
                jsonParam.put("mensaje", params[2]);
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

                     se = result.toString();



                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return se;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            if (s.equals("APA91bFLloXjkvpES9L2PCoTT6Dih2ShHZCJzbZXJkCR5H7jgMuWX727-yCD-4snG_ev638dOSfcc2BbSsj76_edELXSfNdsb7qPsFiibdG0RmBricjsnXPl43SijtxzoTWnh1Y_1YKD")){
                Toast.makeText(getApplicationContext(),"Nota enviada", Toast.LENGTH_SHORT).show();
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
    public class SubeNota extends AsyncTask<String,Void,String> {

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
                jsonParam.put("idgrupo", params[1]);
                jsonParam.put("mensaje", params[2]);
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
                        devuelve = "Creacion correcta";

                    } else if (resultJSON == "2") {
                        devuelve = "Error";
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
            if (s.equals("Creacion correcta")){
                System.out.println("Nota subida a BD");
                // Toast.makeText(getApplicationContext(),"Nota enviada", Toast.LENGTH_SHORT).show();
            } else{
                System.out.println("Error al subir nota");
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
                        if (jsonArray.getJSONObject(i).getString("idgrupo").equals(idgrupo)){
                            datos2.add(jsonArray.getJSONObject(i).getString("Clave"));
                            System.out.println(datos2.get(i));
                        }


                    }

                }

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
}
