package com.sherlock.shoppingassist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddProductActivity extends AppCompatActivity {
    TextView detailsTextView;
    TextView detailsTextView2;
    Button confirmButton;
    EditText quantityEditText;
    String productPrice ,productName ;
    ProgressBar progressBar;
    int n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        confirmButton = (Button) findViewById(R.id.confirmProductButton);
        quantityEditText = (EditText) findViewById(R.id.quantityEditText);
        detailsTextView = (TextView) findViewById(R.id.detailsTextView);
        detailsTextView2 = (TextView) findViewById(R.id.detailsTextView2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        confirmButton.setEnabled(false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String tempId = sharedPreferences.getString("tempProductId",null);
        detailsTextView.append("\nProduct code is : " + tempId);
        detailsTextView.append("\nSearching for product in database \n");

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
                int f = -1;
                for(int i=0;i<n;i++){
                    if(tempId.equals(dataSnapshot.child("productList").child(i+"").child("productId").getValue().toString())){
                        f = i;
                        break;
                    }

                }
                progressBar.setVisibility(View.INVISIBLE);
                confirmButton.setEnabled(true);

                if(f == -1){
                    detailsTextView2.setText("\nproduct is not in database");
                }
                else{
                    detailsTextView2.setText("\nProduct found.");
                    productName = dataSnapshot.child("productList").child(f+"").child("productName").getValue().toString();
                    productPrice = dataSnapshot.child("productList").child(f+"").child("productPrice").getValue().toString();
                    detailsTextView2.append("\n Product Name : " + productName);
                    detailsTextView2.append("\n Product Price : " +  productPrice);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productQuantity = quantityEditText.getText().toString();

                if(productQuantity.length() > 0) {
                    int quantity = Integer.parseInt(productQuantity);
                    if (quantity > 0 && quantity < 10000) {
                        CartData cartData = new CartData(tempId, productName, productPrice, productQuantity);
                        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        if (sharedPreferences1.getString("productsList", null) == null) {
                            ArrayList<CartData> arrayList = new ArrayList<CartData>();
                            arrayList.add(cartData);
                            Gson gson = new Gson();
                            String json = gson.toJson(arrayList);
                            editor.putString("productsList", json);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "FirstProduct added to cart", Toast.LENGTH_SHORT).show();

                        } else {
                            Gson gson = new Gson();
                            String json = sharedPreferences1.getString("productsList", null);
                            Type type = new TypeToken<ArrayList<CartData>>() {
                            }.getType();
                            ArrayList<CartData> arrayList = gson.fromJson(json, type);
                            arrayList.add(cartData);
                            String newjson = gson.toJson(arrayList);
                            editor.putString("productsList", newjson);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Snackbar.make(view, "Choose quantity >1(<10000) ", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else{
                    Snackbar.make(view,"Choose quantity",Snackbar.LENGTH_SHORT).show();
                }
            }
        });




    }
}
