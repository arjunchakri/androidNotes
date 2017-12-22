package com.sherlock.shoppingassist;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity {
    AutoCompleteTextView notesTextView;
    Button saveButton;
    RecyclerView recyclerView;
    AdapterRecyclerViewMemo adapterRecyclerViewMemo;
    ProgressBar progressBar;
    int n;
    ArrayList<MemoData> arrayList = new ArrayList<>();
    ArrayList<CartData> arrayListCart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        progressBar = (ProgressBar) findViewById(R.id.progressBar22);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMemo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        saveButton = (Button) findViewById(R.id.saveButton);
        notesTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        saveButton.setEnabled(false);

        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        if(sharedPreferences1.getString("productsList", null) != null) {
            String json2 = sharedPreferences1.getString("productsList", null);
            Type type2 = new TypeToken<ArrayList<CartData>>(){}.getType();
            arrayListCart = gson.fromJson(json2, type2);
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("allProducts");
        DatabaseReference databaseReferenceForNumber = firebaseDatabase.getReference("allProducts");
        databaseReferenceForNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String integerN = dataSnapshot.child("numberOfProducts").getValue().toString();
                n = Integer.parseInt(integerN);
                //detailsTextView.append("\n"+n);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //detailsTextView.append("\n"+dataSnapshot.child("productList").getValue());
                String[] productNames = new String[n];
                for(int i=0;i<n;i++){
                    productNames[i] = dataSnapshot.child("productList").child(i+"").child("productName").getValue().toString();
                }
                final ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.select_dialog_item, productNames);
                notesTextView.setAdapter(arrayAdapter);
                notesTextView.setThreshold(1);
                progressBar.setVisibility(View.INVISIBLE);
                saveButton.setEnabled(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}});



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<MemoData>>() {
                }.getType();
                String json = sharedPreferences.getString("memoList",null);
                int found = 0;
                int itemAlreadyPresent = 0;
                if(sharedPreferences.getString("memoList",null) != null) {
                    ArrayList<MemoData> arrayList = gson.fromJson(json, type);
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).productName.equals(notesTextView.getText().toString())) {
                            Toast.makeText(view.getContext(), "Item already in memo", Toast.LENGTH_SHORT).show();
                            found = 1;
                            break;
                        }
                    }
                }
                if(sharedPreferences.getString("productsList", null) != null) {
                    for(int i=0;i<arrayListCart.size();i++){
                            if(arrayListCart.get(i).productName.equals(notesTextView.getText().toString())){
                                itemAlreadyPresent = 1;//product  added
                                Toast.makeText(view.getContext(), "Item already in Cart", Toast.LENGTH_SHORT).show();
                            }
                    }
                }
                if(found == 0) {
                    arrayList.add(new MemoData(notesTextView.getText().toString(), itemAlreadyPresent));

                    String json2 = gson.toJson(arrayList);

                    editor.putString("memoList", json2);
                    editor.commit();
                    adapterRecyclerViewMemo.notifyIt(arrayList);

                    notesTextView.dismissDropDown();
                    notesTextView.setText("");
                    //Toast.makeText(view.getContext(), json2, Toast.LENGTH_SHORT).show();
                }
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String json = sharedPreferences.getString("memoList",null);
        Type type = new TypeToken<ArrayList<MemoData>>(){}.getType();
        if(sharedPreferences.getString("memoList",null) != null) {
            arrayList = gson.fromJson(json, type);
        }
        else {
            //Toast.makeText(getApplicationContext(),"Empty memo",Toast.LENGTH_SHORT).show();
        }

        if(sharedPreferences1.getString("productsList", null) != null) {
            for(int i=0;i<arrayList.size();i++){
                for(int j=0; j<arrayListCart.size();j++){
                    if(arrayListCart.get(j).productName.equals(arrayList.get(i).productName)){
                        arrayList.get(i).added = 1; //product  added
                        Toast.makeText(getApplicationContext(), "Assigned to 1", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
        adapterRecyclerViewMemo = new AdapterRecyclerViewMemo(this);
        adapterRecyclerViewMemo.setList(arrayList);
        recyclerView.setAdapter(adapterRecyclerViewMemo);
    }
}
