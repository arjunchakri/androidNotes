package com.sherlock.shoppingassist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterRecyclerView adapterRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<CartData> arrayList = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartData>>(){}.getType();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPreferences.getString("productsList",null) == null){
            Toast.makeText(getApplicationContext(),"Cart Empty",Toast.LENGTH_SHORT).show();
        }
        else {
            String json = sharedPreferences.getString("productsList", null);
            arrayList = gson.fromJson(json, type);
        }
        adapterRecyclerView = new AdapterRecyclerView(this);
        adapterRecyclerView.setCartList(arrayList);
        recyclerView.setAdapter(adapterRecyclerView);
    }

}
