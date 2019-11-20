package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MuestraClientes extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar tools;
    private String usuario_in;
    private String clave_in;
    private SearchView busqueda;
    private ListView ListaClientes;
    private Clientes clientes;
    private AdaptadorClientes adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra_clientes);

        //recupero datos de usuario y psw de la actividad principal
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        usuario_in=datos.getString("USUARIO",null);
        clave_in=datos.getString("PSW",null);

        busqueda=(SearchView)findViewById(R.id.search_view);
        ListaClientes=findViewById(R.id.ListaClientes);


        //pongo la toolbar en la actividad
        tools=findViewById(R.id.enca);
        tools.setTitle("Pedidos");
        tools.setSubtitle("Altas -> Seleccione un cliente");
        setSupportActionBar(tools);
        //agrega el boton de regresar en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        ConsultaWSCltes();


    }


    public void ConsultaWSCltes(){
        //hay que generar un hilo por el consumo de los ws lo requiere
        Thread tr=new Thread(){
            @Override
            public void run() {
                String str_url="http://doloop.dyndns.org/wsph/wsph.php?user="+usuario_in+"&TCons=CLTE_S_001&psw="+clave_in;
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
        final ArrayList<Clientes> Array_cltes = new ArrayList<Clientes>();
        final ArrayList<Integer> id_cltes = new ArrayList<Integer>();

        for(int i=0;i<json.length();i++) {
            // Get current json object
            JSONObject line = null;
            try {
                line = json.getJSONObject(i);
                clientes=new Clientes(line.getString("razonsocial_cli"),line.getInt("id_cli"),line.getString("domicilio"),line.getString("DomCP_cli"),line.getString("nombre_loc"),line.getString("nombre_provincia"));
                Array_cltes.add(clientes);

                id_cltes.add(line.getInt("id_cli"));   //guardo el id del cliente
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter=new AdaptadorClientes(this,Array_cltes);
        ListaClientes.setAdapter(adapter);

        ListaClientes.setTextFilterEnabled(true);

        busqueda.setIconifiedByDefault(false);
        busqueda.setOnQueryTextListener(this);
        busqueda.setSubmitButtonEnabled(true);
        busqueda.setQueryHint("Buscar...");

        ListaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MuestraClientes.this, "clic en "+i+" "+Array_cltes.get(i).getRazonSocial()+" "+id_cltes.get(i), Toast.LENGTH_SHORT).show();


                //GUARDO DATOS DEL CLIENTE
                guardoDatosClte(id_cltes.get(i),Array_cltes.get(i).getRazonSocial());

                // VOY A SIGUIENTE ACTIVIDAD
                Intent intension=new Intent(getApplicationContext(),CargaItems.class);
                startActivity(intension);

            }
        });
    }

    public void guardoDatosClte (int id_cli, String RazonSocial){
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor miEditor=datos.edit();
        miEditor.putInt("CLTE_ID",id_cli);
        miEditor.putString("CLTE_RAZSOC",RazonSocial);
        miEditor.apply();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)){
            adapter.filter("");
            ListaClientes.clearTextFilter();
        }
        else {
            adapter.filter(s);
        }
        return true;
    }
}
