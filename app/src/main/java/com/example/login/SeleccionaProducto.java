package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SeleccionaProducto extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private String usuario_in;
    private String clave_in;
    private androidx.appcompat.widget.Toolbar tools;
    private ListView ListaProductos;
    private SearchView busqueda;
    private productos prod;
    private AdaptadorProductos adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecciona_producto);

        //recupero datos de usuario y psw de la actividad principal
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        usuario_in=datos.getString("USUARIO",null);
        clave_in=datos.getString("PSW",null);

        //pongo la toolbar en la actividad
        tools=findViewById(R.id.enca);
        tools.setTitle("Pedidos");
        tools.setSubtitle("Altas -> Seleccione un producto");
        setSupportActionBar(tools);

        ListaProductos=findViewById(R.id.ListaProductos);
        busqueda=findViewById(R.id.busca_productos);
        ConsultaWSProd();


    }

    public void ConsultaWSProd(){
        //hay que generar un hilo por el consumo de los ws lo requiere
        Thread tr=new Thread(){
            @Override
            public void run() {
                String str_url="http://doloop.dyndns.org/wsph/wsph.php?user="+usuario_in+"&TCons=PROD_S_001&psw="+clave_in;
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
                                    CargaLista(json);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), jsob.getString("Error")+")", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Error en consulta de clientes", Toast.LENGTH_SHORT).show();
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

    private void CargaLista(JSONArray json){
        // initialize list
        final ArrayList<productos> Array_prod=new ArrayList<productos>();

        for(int i=0;i<json.length();i++) {
            // Get current json object
            JSONObject line = null;
            try {
                line = json.getJSONObject(i);

                prod=new productos(line.getInt("id_prod"),line.getString("descripcion_prod"),line.getString("descripcion_um"),line.getDouble("Stock_prod"),line.getDouble("PrecioVenta_prod"),0,0);
                Array_prod.add(prod);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter=new AdaptadorProductos(this,Array_prod);
        ListaProductos.setAdapter(adapter);
        ListaProductos.setTextFilterEnabled(true);

        busqueda.setIconifiedByDefault(false);
        busqueda.setOnQueryTextListener(this);
        busqueda.setSubmitButtonEnabled(true);
        busqueda.setQueryHint("Buscar...");

        ListaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(SeleccionaProducto.this, "llego producto: "+Array_prod.get(i).getPrecio(), Toast.LENGTH_LONG).show();
                // VOY A SIGUIENTE ACTIVIDAD
                Intent intension=new Intent(getApplicationContext(),CantidadProductos.class);
                intension.putExtra("DESC_PROD",Array_prod.get(i).getDescripcion());
                intension.putExtra("PRECIO_PROD",Array_prod.get(i).getPrecio());
                intension.putExtra("STOCK_PROD",Array_prod.get(i).getStock());
                intension.putExtra("ID_PROD",Array_prod.get(i).getId());
                intension.putExtra("UM_PROD",Array_prod.get(i).getUm());
                startActivity(intension);

            }
        });


    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)){
            adapter.filter("");
            ListaProductos.clearTextFilter();
        }
        else {
            adapter.filter(s);
        }
        return true;
    }

}
