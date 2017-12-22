package com.sherlock.shoppingassist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BudgetActivity extends AppCompatActivity {
    Button confirmBudgetButton;
    Button resetBudgetButton;
    Button resetMemoButton;
    Button resetCartButton;
    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        confirmBudgetButton = (Button)  findViewById(R.id.confirmBudgetButton);
        resetBudgetButton = (Button)  findViewById(R.id.resetBudgetButton);
        resetMemoButton = (Button)  findViewById(R.id.resetMemoButton);
        resetCartButton = (Button)  findViewById(R.id.resetCartButton);

        textView = (TextView)  findViewById(R.id.budgetTextView);
        editText = (EditText) findViewById(R.id.budgetEditText);

        confirmBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budget = editText.getText().toString();
                if(budget.length() > 0) {
                    if (Integer.parseInt(budget) > 1 && Integer.parseInt(budget) < 1000000) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("budget", budget);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Budget-Set Successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Snackbar.make(v, "budget between 1 & 1000000", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar.make(v, "Enter budget ", Snackbar.LENGTH_SHORT).show();
                }

        }
        });

        resetBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("budget",null);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Reset Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        resetMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("memoList",null);
                editor.commit();
                Toast.makeText(getApplicationContext(),"MemoReset Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        resetCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("productsList",null);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Reset Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

    }
}
