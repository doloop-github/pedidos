package com.example.login;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConsumeWS {
    private String URLConec;

    public ConsumeWS (String URLConeccion){
        URLConec=URLConeccion;
    }

    public String enviarDatosGET ()  {
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;

        try {
            url=new URL(URLConec);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();  //codigo de respuesta
            resul=new StringBuilder();

            if (respuesta== HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                //leemos el string resultante
                while ((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //devulvo un string
        return resul.toString();
    }

}
