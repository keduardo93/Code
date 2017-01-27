package com.example.torre.circuloapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {
    Button  btnIngresar,  btnAdm;
    Button btnAgregar;
    EditText edtNombre, edtApellido, edtSemestre, edtNumeroC, edtNumeroConfirmacion, edtCarrera, edtCorreo, edtNip2, edtNipConfirmacion;
    EditText edtNumero, edtNip;
    TextView txt;
    String num = "";
    Bundle b = new Bundle();

    Intent intent;
    String usuario = "";

    ObtenerWebServices hiloconexion;

    String IP = "http://servidorweb.esy.es"; //IP de mi URL
    //Rutas de los Web Services
    String GET_USUARIO = IP + "/obtener_usuario.php";
     String GET_MOD = IP + "/obtener_moderador.php";
    String POST_USER = IP + "/insertarUsuario.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt = (TextView) findViewById(R.id.txt);
        txt.setText("Circulo APP ofrece una alternativa eficiente y agradable de inscripción y gestión de los" +
                " grupos de Círculo de Lectura " + "\n" + " • Permite que el alumno se inscriba a algunos de" +
                " los grupos disponibles en los distintos horarios de Círculo de Lectura."+ "\n" +
                " • Ofrece una vista entendible para el alumno y moderador, evitando confusiones al" +
                " momento de requerir algún servicio o consulta."+ "\n" +
                " • Gestiona el grupo inscrito, mostrando al moderador la información de los alumnos, " +
                "así como el registro de las tareas revisadas."+ "\n" +
                " •	Permite al alumno subir un ensayo a la plataforma, " +
                "con el fin de ser revisado por el moderador."+ "\n" +
                " Entre otros...");
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtNumeroC = (EditText) findViewById(R.id.edtNumero);
        edtNumeroConfirmacion = (EditText) findViewById(R.id.edtNumeroConfirmacion);
        edtCarrera = (EditText) findViewById(R.id.edtCarrera);
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        edtNip2 = (EditText) findViewById(R.id.edtNip);
        edtNipConfirmacion = (EditText) findViewById(R.id.edtNipConfirmacion);
        edtSemestre = (EditText) findViewById(R.id.edtSemestre);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        edtNumero = (EditText) findViewById(R.id.edtNumeroI);
        edtNip = (EditText) findViewById(R.id.edtNipI);
        btnAdm = (Button) findViewById(R.id.buttonadm);
       // txt = (TextView) findViewById(R.id.textView);


        Resources res = getResources();

        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("Mi tab 1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_menu_add));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("Mi tab 2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_menu_set_as));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("Mi tab 3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_menu_info_details));
        tabs.addTab(spec);

        tabs.setCurrentTab(1);

    }
    public void onClick(View v){





        switch (v.getId()){

            case (R.id.btnIngresar):
                String ad = "12345";
                if (edtNumero.getText().toString().isEmpty()){
                    edtNumero.setError("Ingrese Numero");
                } else if (edtNip.getText().toString().isEmpty()){
                    edtNip.setError("Ingrese Nip");
                }
                else if (edtNumero.getText().toString().equals(ad) && edtNip.getText().toString().equals(ad) ){
                    intent = new Intent(MainActivity.this, InicioAdministrador.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Iniciando ADMINISTRADOR", Toast.LENGTH_SHORT).show();
                } else{

                    hiloconexion = new ObtenerWebServices();
                    String cadenallamada = GET_USUARIO + "?NIP=" + edtNip.getText().toString();
                    hiloconexion.execute(cadenallamada, "1", edtNumero.getText().toString());
                }
                break;

            case (R.id.buttonadm):
                hiloconexion = new ObtenerWebServices();
                String cadenallamada2 = GET_MOD + "?NIP=" + edtNip.getText().toString();
                hiloconexion.execute(cadenallamada2, "2", edtNumero.getText().toString());
                break;

            case R.id.btnAgregar:
                if (edtNombre.getText().toString().isEmpty()){
                    edtNombre.setError("Ingrese Nombre");
                } else if (edtApellido.getText().toString().isEmpty()){
                    edtApellido.setError("Ingrese Apellido");
                } else if (edtNumeroC.getText().toString().isEmpty()){
                    edtNumeroC.setError("Ingrese numero de Control");
                } else if (edtNumeroConfirmacion.getText().toString().isEmpty()){
                    edtNumeroConfirmacion.setError("Ingrese Numero de Control");
                    num = edtNumeroC.getText().toString();
                } else if (edtCarrera.getText().toString().isEmpty()){
                    edtCarrera.setError("Ingrese Carrera");
                } else if (edtCorreo.getText().toString().isEmpty()) {
                    edtCorreo.setError("Ingrese Correo");
                }   else if (edtSemestre.getText().toString().isEmpty()){
                    edtSemestre.setError("Ingrese Correo");
                } else if (edtNip2.getText().toString().isEmpty()){
                    edtNip2.setError("Ingrese NIP");
                } else if (edtNipConfirmacion.getText().toString().isEmpty()){
                    edtNipConfirmacion.setError("Ingrese NIP");
                }

                if(edtNumeroC.getText().toString().equals(edtNumeroConfirmacion.getText().toString())) {
                    if(edtNip2.getText().toString().equals(edtNipConfirmacion.getText().toString())) {
                        hiloconexion = new ObtenerWebServices();
                        hiloconexion.execute(POST_USER, "3", edtNumeroC.getText().toString(),
                                edtNip2.getText().toString(),
                                edtNombre.getText().toString(),
                                edtCarrera.getText().toString(),
                                edtSemestre.getText().toString(),
                                edtCorreo.getText().toString(),
                                edtApellido.getText().toString());   // Parámetros que recibe doInBackground

                    } else{
                        Toast.makeText(getApplicationContext(), "Los NIP no coinciden", Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Los Números de Control no coinciden", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    public class ObtenerWebServices extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            String numero = params[2];
            URL url = null; // URL de donde queremos obtener información
            String devuelve = "";
            String resultado = "";

            if( params[1] == "1"){  //INICIO USUARIO
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");

                    //Toast.makeText(MainActivity.this, "Iniciando Conexion", Toast.LENGTH_SHORT).show();

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {


                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON
                        //  JSONArray alumnosJSON = respuestaJSON.getJSONArray("N_CONTROL");

                        if (resultJSON == "1") {      // hay un alumno que mostrar
                            devuelve = respuestaJSON.getJSONObject("N_CONTROL").getString("N_CONTROL");
                            resultado = numero +" " + devuelve + " " + "1";
                            // respuestaJSON.getJSONObject("alumno").getString("idalumno")
                        } else if (resultJSON == "2") {
                            resultado = "Error";
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return resultado;
            }
            if( params[1] == "2"){  //INICIO MODERADOR
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {


                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON == "1") {      // hay un alumno que mostrar
                            devuelve = devuelve + respuestaJSON.getJSONObject("N_CONTROL").getString("N_CONTROL");
                            resultado = numero +" " + devuelve + " " + "2";

                        } else if (resultJSON == "2") {
                            resultado = "Error";
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return resultado;
            } if(params[1] == "3"){
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
                    jsonParam.put("NIP", params[3]);
                    jsonParam.put("Nombre", params[4]);
                    jsonParam.put("Carrera", params[5]);
                    jsonParam.put("Semestre", params[6]);
                    jsonParam.put("Correo", params[7]);
                    jsonParam.put("Telefono", params[8]);
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
            return null;

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
            usuario = s;
            if(usuario.equals("Error en la conexion") ) {
                Toast.makeText(getApplicationContext(), "Errores en la información", Toast.LENGTH_LONG).show();
            }
            else if(usuario.equals("Información Agregada Correctamente") ){
                Bundle b = new Bundle();
                b.putString("Numero", num);
                intent = new Intent(getApplicationContext(), InicioAlumno.class);
                intent.putExtras(b);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "BIENVENIDO AL CIRCULO DE LECTURA", Toast.LENGTH_LONG).show();

            } else{


                // 11071434 11071434 2
                //01234567890123456789
                String nip = usuario.substring(4,8);
                String numeroc = usuario.substring(0,8);
                String respuesta = usuario.substring(9,17);
                String tipo = usuario.substring(18,19);

                //   txt.setText(numeroc + " " + respuesta + " " + tipo);
                Bundle b = new Bundle();

                b.putString("Numero", numeroc);
                b.putString("Nip", nip);

                if(tipo.equals("1")){
                    if(numeroc.equals(respuesta) ){
                        intent = new Intent(getApplicationContext(), InicioAlumno.class);
                        intent.putExtras(b);
                        startActivity(intent);


                    } else{
                        Toast.makeText(MainActivity.this, "No son iguales", Toast.LENGTH_SHORT).show();
                    }
                } else if(tipo.equals("2")){
                    if(numeroc.equals(respuesta) ){
                        intent = new Intent(getApplicationContext(), InicioModerador.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Iniciando Moderador", Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(MainActivity.this, "No son iguales", Toast.LENGTH_SHORT).show();
                    }
                }

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