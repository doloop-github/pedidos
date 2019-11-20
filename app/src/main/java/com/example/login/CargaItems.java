package com.example.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CargaItems extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar tools;
    private String usuario_in;
    private String clave_in;
    private String RazonSocial_in;
    private int id_clte;
    private ListView ListaItems;
    private TextView txt_razsoc;
    private FloatingActionButton fab;
    private boolean click=false;
    private productos prod;
    private ListView ListaProductos;
    private AdaptadorProductos adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_items);

        //recupero datos de usuario y psw de la actividad principal
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        usuario_in = datos.getString("USUARIO", null);
        clave_in = datos.getString("PSW", null);
        RazonSocial_in = datos.getString("CLTE_RAZSOC", null);
        id_clte = datos.getInt("CLTE_ID", 0);

        ListaProductos=findViewById(R.id.ListaItemsPedido);

        //boton flotante
        fab= findViewById(R.id.fab);


        fab.setScaleX(0);
        fab.setScaleY(0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.fast_out_slow_in);

            fab.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(interpolador)
                    .setDuration(600)
                    .setStartDelay(1000)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                llamaActividadSelectProd();

            }
        });


        //cargo textview de razon social
        txt_razsoc=findViewById(R.id.rz_clte);
        txt_razsoc.setText(" Cliente: "+RazonSocial_in);



        //array de lista de items de ListView
        ListaItems=findViewById(R.id.ListaItemsPedido);
        final ArrayList<String> ItemsPed=new ArrayList<String>();

        //pongo la toolbar en la actividad
        tools = findViewById(R.id.enca);
        tools.setTitle("Pedidos");
        tools.setSubtitle("Carga de Items");
        setSupportActionBar(tools);
        //agrega el boton de regresar en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //carga lista de productos seleccionados
        CargaListaItems();

    }

    public void llamaActividadSelectProd(){
        Intent i =new Intent(this,SeleccionaProducto.class);
        startActivity(i);

    }

    private void CargaListaItems(){
        //recupero producto seleccionado
        Bundle datosRecibidos=getIntent().getExtras();
        prod=null;

        if (datosRecibidos!=null){

            prod=(productos) datosRecibidos.getSerializable("PROD_SELEC");
            //Toast.makeText(this, "llego " + prod.getDescripcion(), Toast.LENGTH_LONG).show();
            final ArrayList<productos> Array_prod=new ArrayList<productos>();
            Array_prod.add(prod);

            adapter=new AdaptadorProductos(this,Array_prod);
            ListaProductos.setAdapter(adapter);


        }



    }


}
