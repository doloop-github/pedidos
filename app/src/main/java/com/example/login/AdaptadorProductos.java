package com.example.login;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdaptadorProductos extends BaseAdapter {

    protected Activity activity;
    //protected ArrayList<Clie> items;
    protected List<productos> items;
    protected ArrayList<productos> arrayList;

//    public AdaptadorProductos (Activity activity, ArrayList<productos> items){
    public AdaptadorProductos (Activity activity, List<productos> items){
        this.activity=activity;
        this.items=items;
        this.arrayList = new ArrayList<productos>();
        this.arrayList.addAll(items);
    }

    /*
    public void addAll(ArrayList<productos> prod) {
        for (int i =0 ; i < prod.size(); i++) {
            items.add(prod.get(i));
        }
    }


     */
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
            v = inf.inflate(R.layout.lista_productos, null);
        }

        productos prod = items.get(i);

        TextView desc = (TextView) v.findViewById(R.id.descripcion_prod);
        desc.setText(prod.getDescripcion());

        TextView um = (TextView) v.findViewById(R.id.um_prod);
        um.setText(prod.getUm());


        TextView stock = (TextView) v.findViewById(R.id.stock_prod);


        if (prod.getStock()<=0){

            stock.setTextColor(activity.getResources().getColor(R.color.ColorRojo));
        }else{
            stock.setTextColor(activity.getResources().getColor(android.R.color.secondary_text_light));
        }

        stock.setText(String.format("%.2f",prod.getStock()));

        
        TextView precio = (TextView) v.findViewById(R.id.precio_prod);
        precio.setText(String.format("%.2f",prod.getPrecio()));


        return v;
    }

    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length()==0){
            items.addAll(arrayList);
        }
        else {
            for (productos model : arrayList){
                if (model.getDescripcion().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    items.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
