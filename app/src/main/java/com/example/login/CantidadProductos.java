package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CantidadProductos extends AppCompatActivity {

    private Toolbar tools;
    private String desc_prod;
    private int id_prod;
    private double stock_prod;
    private double precio_prod;
    private String um_prod;

    private TextView descrpod;
    private TextView precioprod;
    private TextView stockprod;
    private TextView umprod;

    private Button suma_c;
    private Button resta_c;
    private Button confirma_c;
    private EditText cantidad_c;

    private Double contador=0.0;
    private Double subtotal=0.0;

    private productos prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantidad_productos);

        //pongo la toolbar en la actividad
        tools=findViewById(R.id.enca);
        tools.setTitle("Pedidos");
        tools.setSubtitle("Altas -> Cantidad del producto");
        setSupportActionBar(tools);
        //agrega el boton de regresar en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recupero datos de usuario y psw de la actividad principal
        Bundle datos = getIntent().getExtras();
        desc_prod=datos.getString("DESC_PROD");
        id_prod=datos.getInt("ID_PROD");
        stock_prod=datos.getDouble("STOCK_PROD");
        precio_prod=datos.getDouble("PRECIO_PROD");
        um_prod=datos.getString("UM_PROD");

        //Toast.makeText(this, "predio: "+precio_prod, Toast.LENGTH_LONG).show();

        //Cargo textView de datos del producto
        descrpod=findViewById(R.id.descripcion_prod);
        precioprod=findViewById(R.id.precio_prod);
        stockprod=findViewById(R.id.stock_prod);
        umprod=findViewById(R.id.um_prod);

        descrpod.setText(desc_prod);
        precioprod.setText(String.format("%.2f",precio_prod));
        stockprod.setText(String.format("%.2f",stock_prod));
        umprod.setText(um_prod);
        ///////////////////////////////////////////////

        suma_c=findViewById(R.id.mas);
        resta_c=findViewById(R.id.menos);
        confirma_c=findViewById(R.id.Confirma_cantidad);
        cantidad_c=findViewById(R.id.cantidad);

        cantidad_c.setText(String.format("%.2f",contador));

        //pongo a la escucha los botones
        suma_c.setOnClickListener(ClicBotones);
        resta_c.setOnClickListener(ClicBotones);
        confirma_c.setOnClickListener(ClicBotones);


    }

    private View.OnClickListener ClicBotones=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mas:
                    suma_contador();
                    break;
                case R.id.menos:
                    resta_contador();
                    break;
                case R.id.Confirma_cantidad:
                    confirma();
                    break;

            }
        }
    };


    private void suma_contador(){
        if (contador<stock_prod) {
            contador++;
            cantidad_c.setText(String.format("%.2f", contador));
        }
    }

    private void resta_contador(){
        if (contador>0) {
            contador--;
            cantidad_c.setText(String.format("%.2f", contador));
        }
    }

    private void confirma(){

        Intent i=new Intent(this, CargaItems.class);
        subtotal=precio_prod*contador;
        prod=new productos(id_prod,desc_prod,um_prod,stock_prod,precio_prod,contador,subtotal);

        Bundle datos=new Bundle();
        datos.putSerializable("PROD_SELEC",prod);
        i.putExtras(datos);
        startActivity(i);

    }
}
