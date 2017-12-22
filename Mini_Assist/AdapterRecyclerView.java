package com.sherlock.shoppingassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter < AdapterRecyclerView.ViewHolder > {
    ArrayList <CartData> arrayList = new ArrayList<CartData>();
    Context context;
    static int number;
    LayoutInflater layoutInflater;

    public AdapterRecyclerView(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notifyIt(ArrayList<CartData> newArrayList,int position){
        arrayList = newArrayList;
        //Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
        notifyItemRangeChanged(0,arrayList.size());
        notifyItemChanged(4);
    }


    public void setCartList(ArrayList < CartData > arrayList) {
        this.arrayList = arrayList;
        notifyItemRangeChanged(0, arrayList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_view, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartData cartData = arrayList.get(position);
        holder.setValues(cartData);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;
        public TextView textPrice;
        public FloatingActionButton delete;
        public RelativeLayout relativeLayout;
        public Button plusButton,minusButton;
        public void setValues(CartData cartData) {
            textName.setText("Product Name : " + cartData.productName + " ( " + cartData.productId + " ) ");
            textPrice.setText("Price : " + cartData.productPrice + " ( X " + cartData.productQuantity + " ) ");
            number++;

        }

        public ViewHolder(final View itemView, final Context context) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textViewName);
            textPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            delete = (FloatingActionButton) itemView.findViewById(R.id.floatingActionButton);
            plusButton = (Button) itemView.findViewById(R.id.plusButton);
            minusButton = (Button) itemView.findViewById(R.id.minusButton);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = getAdapterPosition();
                    //Toast.makeText(context, n + "", Toast.LENGTH_LONG).show();
                    arrayList.remove(n);
                    notifyItemRemoved(n);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(arrayList);
                    if(arrayList.size() != 0)
                        editor.putString("productsList", json);
                    else{
                        editor.putString("productsList", null);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                    editor.commit();
                }
            });
            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = getAdapterPosition();
                    Toast.makeText(context, n + "Plus", Toast.LENGTH_LONG).show();
                    int productQuantity = 1 + Integer.parseInt(arrayList.get(n).productQuantity);
                    arrayList.get(n).productQuantity = productQuantity + "";
                    notifyItemChanged(n);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(arrayList);
                    editor.putString("productsList", json);
                    editor.commit();
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = getAdapterPosition();
                    //Toast.makeText(context, n + "", Toast.LENGTH_LONG).show();
                    int productQuantity = Integer.parseInt(arrayList.get(n).productQuantity) - 1;
                    if(productQuantity != 0) {
                        arrayList.get(n).productQuantity = productQuantity + "";
                        notifyItemChanged(n);

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(arrayList);
                        editor.putString("productsList", json);
                        editor.commit();
                    }
                    else{
                        delete.performClick();
                    }
                }
            });
        }
    }
}