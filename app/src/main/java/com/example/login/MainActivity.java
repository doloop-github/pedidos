package com.example.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity  {

    private EditText usuario_in;
    private EditText clave_in;
    private Button BtnLogin;
    private androidx.appcompat.widget.Toolbar tools;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario_in=findViewById(R.id.usuario);
        clave_in=findViewById(R.id.psw);
        BtnLogin=findViewById(R.id.btn_ingresar);

        //pongo la toolbar en la actividad
        tools=findViewById(R.id.enca);
        tools.setTitle("Login");
        setSupportActionBar(tools);

        leoDatosLogin();

        BtnLogin.setOnClickListener(login);


    }


    public void leoDatosLogin(){
        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);
        usuario_in.setText(datos.getString("USUARIO",null));
        clave_in.setText(datos.getString("PSW",null));
        ControlaResWSLogin();
    }

    public void guardoDatosLogin (String usu, String psw){
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor miEditor=datos.edit();
        miEditor.putString("USUARIO",usu);
        miEditor.putString("PSW",psw);
        miEditor.apply();
    }

    public void ControlaResWSLogin(){
        //hay que generar un hilo por el consumo de los ws lo requiere
        Thread tr=new Thread(){
            @Override
            public void run() {
                String str_url="http://doloop.dyndns.org/wsph/wsph.php?user="+usuario_in.getText().toString()+"&TCons=USU_S_001&psw="+clave_in.getText().toString();
                ConsumeWS ws=new ConsumeWS(str_url);
                final String resultado=ws.enviarDatosGET();
                //nos permite trabajar con la interfaz grafica desde el hilo
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //int r=obtDatosJSON(resultado);
                        try {
                            JSONArray json=new JSONArray(resultado);


                            //Toast.makeText(MainActivity.this, "llego "+resultado, Toast.LENGTH_SHORT).show();
                            if (json.length()>0){
                                JSONObject jsob=json.getJSONObject(0);   // passo a un objeto el resultado
                                if (!jsob.has("Error")){
                                    //Toast.makeText(MainActivity.this, getResources().getString(R.string.Login_ok), Toast.LENGTH_SHORT).show();
                                    //guardo los datos en un sharedpreference
                                    guardoDatosLogin(usuario_in.getText().toString(),clave_in.getText().toString());
                                    //voy a siguiente actividad
                                    Intent i=new Intent(getApplicationContext(),principal.class);
                                    //envio datos a la siguiente actividad
                                    i.putExtra("USUARIO",usuario_in.getText().toString());
                                    i.putExtra("PSW",clave_in.getText().toString());
                                    startActivity(i);

                                }
                                else{
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.Login_fail)+" ("+jsob.getString("Error")+")", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.Login_fail), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        };
        tr.start();   //lanzo el hilo

    }

    private View.OnClickListener login=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_ingresar:
                    ControlaResWSLogin();
                    break;

            }

        }
    };

}
