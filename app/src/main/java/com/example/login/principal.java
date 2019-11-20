package com.example.login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class principal extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar tools;
    private Button btn_nuevo;
    private Button btn_consultar;
    private String usuario_in;
    private String clave_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        //pongo la toolbar en la actividad
        tools=findViewById(R.id.enca);
        tools.setTitle("Pedidos");
        tools.setSubtitle("Menu Principal");
        setSupportActionBar(tools);
        //agrega el boton de regresar en la toolbar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        usuario_in=datos.getString("USUARIO",null);
        clave_in=datos.getString("PSW",null);


        //recupero datos de usuario y psw de la actividad principal
        /*
        Bundle datos=getIntent().getExtras();
        //if (datos!=null){
            usuario_in=datos.getString("USUARIO");
            clave_in=datos.getString("PSW");
        //}

         */


        btn_nuevo=findViewById(R.id.btn_add);
        btn_consultar=findViewById(R.id.btn_con);

        btn_nuevo.setOnClickListener(ClicBotones);


    }

    private View.OnClickListener ClicBotones=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_add:
                    Intent i = new Intent(getApplicationContext(), MuestraClientes.class);
                    i.putExtra("USUARIO",usuario_in);
                    i.putExtra("PSW",clave_in);

                    startActivity(i);

                    break;

            }
        }
    };
}
