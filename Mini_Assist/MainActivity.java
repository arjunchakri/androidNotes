package com.sherlock.shoppingassist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button scanButton,cartButton,memoButton,budgetButton;
    TextView commentsTextView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressSpent);
        scanButton = (Button) findViewById(R.id.scanButton);
        cartButton = (Button) findViewById(R.id.cartButton);
        memoButton = (Button) findViewById(R.id.memoButton);
        budgetButton = (Button) findViewById(R.id.budgetButton);
        commentsTextView = (TextView) findViewById(R.id.textViewComments);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                if( sharedPreferences.getString("productsList", null) != null) {
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(intent);
                }
                else{
                    Snackbar.make(v, "Cart Empty",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        budgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , BudgetActivity.class);
                startActivity(intent);
            }
        });
        memoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , MemoActivity.class);
                startActivity(intent);
            }
        });

        final Activity activity = this ;


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Point at the barcode.");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        commentsTextView.setText("\n Your Shopping Status: \n");
        if(sharedPreferences.getString("budget",null) == null){
            scanButton.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            commentsTextView.append("\n Please choose your budget value.");

        }
        else{
            String bud = sharedPreferences.getString("budget" , null);
            commentsTextView.append("\n Your Budget is " + bud + "\n");
            if(sharedPreferences.getString("productsList",null) == null){
                progressBar.setVisibility(View.INVISIBLE);

                commentsTextView.setTextColor(Color.parseColor("#1B5E20"));
                commentsTextView.append("\n Add Products to your cart");
            }
            else {
                String json = sharedPreferences.getString("productsList",null);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<CartData>>(){}.getType();
                ArrayList<CartData> arrayList = gson.fromJson(json,type);
                int spent = 0;
                for(int i=0;i<arrayList.size();i++){
                    spent = spent + Integer.parseInt(arrayList.get(i).productPrice)
                            * Integer.parseInt(arrayList.get(i).productQuantity) ;
                }
                int budget = Integer.parseInt(bud);
                int remaining = budget - spent;
                //Toast.makeText(getApplicationContext(),"HAHA " + remaining, Toast.LENGTH_SHORT).show();
                if(remaining > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress((100*spent)/budget);

                    commentsTextView.setTextColor(Color.parseColor("#2E7D32"));
                    commentsTextView.append("\n You still have " + remaining + " to Spend.");
                }
                else if(remaining == 0){
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress((100*spent)/budget);

                    commentsTextView.setTextColor(Color.parseColor("#00796B"));
                    commentsTextView.append("\n You have spent the exact money as your budget");
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);

                    commentsTextView.setTextColor(Color.parseColor("#EF6C00"));
                    commentsTextView.append("\n Oops...You have spent " + -1*remaining + " extra than your estimated budget.");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(intentResult.getContents() == null){
                Toast.makeText(this,"Scan cancelled by user", Toast.LENGTH_LONG).show();
            }
            else{
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String productId = intentResult.getContents();
                int present = 0;
                if(sharedPreferences.getString("productsList",null) != null){
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("productsList",null);
                    Type type = new TypeToken<ArrayList<CartData>>(){}.getType();
                    ArrayList<CartData> arrayList = gson.fromJson(json, type);
                    for(int i=0;i<arrayList.size();i++) {
                        if (arrayList.get(i).productId.equals(productId)){
                            Toast.makeText(getApplicationContext(), "Product already in Cart.",Toast.LENGTH_SHORT).show();
                            present = 1;
                            break;
                        }
                    }
                }
                if(present == 0) {
                    Toast.makeText(this,"Scan Completed : ", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tempProductId", intentResult.getContents());
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                    startActivity(intent);
                }
            }
    }
}
