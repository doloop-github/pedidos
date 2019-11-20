package com.example.login;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdaptadorClientes extends BaseAdapter {

        protected Activity activity;
        protected List<Clientes> items;
        protected ArrayList<Clientes> arrayList;

        public AdaptadorClientes (Activity activity, List<Clientes> items){
            this.activity=activity;
            this.items=items;
            this.arrayList = new ArrayList<Clientes>();
            this.arrayList.addAll(items);
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;

            if (view == null) {
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.lista_clientes, null);
            }

            Clientes clte = items.get(i);

            TextView tv_rz = (TextView) v.findViewById(R.id.rz_clte);
            tv_rz.setText(clte.getRazonSocial());

            TextView tv_domicilio = (TextView) v.findViewById(R.id.domicilio_clte);
            tv_domicilio.setText(clte.getDomicilio());

            TextView tv_cp = (TextView) v.findViewById(R.id.cp_clte);
            tv_cp.setText(clte.getCp());

            TextView tv_localidad = (TextView) v.findViewById(R.id.localidad_clte);
            tv_localidad.setText(clte.getLocalidad());

            TextView tv_provincia = (TextView) v.findViewById(R.id.provincia_clte);
            tv_provincia.setText(clte.getProvincia());


            return v;
        }

        //filtro de busqueda de searchview
        public void filter(String charText){
            charText = charText.toLowerCase(Locale.getDefault());
            items.clear();
            if (charText.length()==0){
                items.addAll(arrayList);
            }
            else {
                for (Clientes model : arrayList){
                    if (model.getRazonSocial().toLowerCase(Locale.getDefault())
                            .contains(charText)){
                        items.add(model);
                    }
                }
            }
            notifyDataSetChanged();
        }


}
