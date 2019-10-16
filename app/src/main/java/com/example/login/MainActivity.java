package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario_in=findViewById(R.id.usuario);
        clave_in=findViewById(R.id.psw);
        BtnLogin=findViewById(R.id.btn_ingresar);
        BtnLogin.setOnClickListener(login);


    }


    private View.OnClickListener login=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_ingresar:
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
                                                Toast.makeText(MainActivity.this, getResources().getString(R.string.Login_ok), Toast.LENGTH_SHORT).show();
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
                    break;

            }

        }
    };

}
