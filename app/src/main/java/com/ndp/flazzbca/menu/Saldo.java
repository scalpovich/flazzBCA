package com.ndp.flazzbca.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ndp.flazzbca.R;

import java.text.NumberFormat;
import java.util.Locale;

public class Saldo extends AppCompatActivity {
    TextView pan, saldo;
    Intent dt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);
        view();

        dt = getIntent();
        pan.setText(addSpace(dt.getStringExtra("card")));
        saldo.setText(formatRupiah(Double.parseDouble(dt.getStringExtra("balance"))));

    }

    void view(){
        pan = findViewById(R.id.label_PAN);
        saldo = findViewById(R.id.label_saldo);
    }

    public String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    public String addSpace(String data){
        StringBuilder sb = new StringBuilder();
        int x = 1;
        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            sb.append(ch);
                int z = (x * 4)-1;
                if(i == z){
                    x = x+1;
                    sb.append(' ');
                }
        }
       return sb.toString();
    }
}