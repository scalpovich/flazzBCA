package com.ndp.flazzbca.menu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ndp.flazzbca.MainActivity;
import com.ndp.flazzbca.R;
import com.ndp.flazzbca.menu.assets.MyAssets;

public class Amount extends AppCompatActivity implements View.OnClickListener{

    Context context;
    EditText inpAmount;
    LinearLayout keyboard;
    Button btn1, btn2, btn3, btn4,btn5,btn6,btn7,btn8,btn9, btn0, btn00, btn000;
    ImageButton btnDone, btnDelete;
    String amount;
    Intent i;
    MyAssets assets;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);
        context = Amount.this;
        assets = new MyAssets();
        view();

        i = getIntent();
        if (i != null){
            inpAmount.setText(i.getStringExtra("amount"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn1:
                setAmount("1");
                break;

            case R.id.btn2:
                setAmount("2");
                break;

            case R.id.btn3:
                setAmount("3");
                break;

            case R.id.btn4:
                setAmount("4");
                break;

            case R.id.btn5:
                setAmount("5");
                break;

            case R.id.btn6:
                setAmount("6");
                break;

            case R.id.btn7:
                setAmount("7");
                break;

            case R.id.btn8:
                setAmount("8");
                break;

            case R.id.btn9:
                setAmount("9");
                break;

            case R.id.btn0:
                setAmount("0");
                break;

            case R.id.btn00:
                setAmount("00");
                break;

            case R.id.btn000:
                setAmount("000");
                break;

            case R.id.hapus:
                amount = assets.changeFormat(inpAmount.getText().toString());
                if (!amount.equals("")){
                    inpAmount.setText(amount.substring(0, amount.length() - 1));
                }
                break;

            case R.id.done:
//                String amount = inpAmount.getText().toString().replace(".","");
                Intent i = new Intent(Amount.this, PiccDetect.class);
                i.putExtra("menu", "sale");
                i.putExtra("amount", assets.changeFormat(inpAmount.getText().toString()));
                i.putExtra("Stand Alone", "Yes");
                startActivity(i);
                finish();
                break;
        }
    }

    void view(){
        inpAmount = findViewById(R.id.inp_amount);
        keyboard  = findViewById(R.id.keyboard);
        btn1      = findViewById(R.id.btn1);
        btn2      = findViewById(R.id.btn2);
        btn3      = findViewById(R.id.btn3);
        btn4      = findViewById(R.id.btn4);
        btn5      = findViewById(R.id.btn5);
        btn6      = findViewById(R.id.btn6);
        btn7      = findViewById(R.id.btn7);
        btn8      = findViewById(R.id.btn8);
        btn9      = findViewById(R.id.btn9);
        btn0      = findViewById(R.id.btn0);
        btn00     = findViewById(R.id.btn00);
        btn000    = findViewById(R.id.btn000);
        btnDelete = findViewById(R.id.hapus);
        btnDone   = findViewById(R.id.done);

        inpAmount.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn00.setOnClickListener(this);
        btn000.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        keyboard.setVisibility(View.VISIBLE);
    }

    void setAmount(String data){
        amount = assets.changeFormat(inpAmount.getText().toString());
        if(amount.equals("")){
            amount = "0";
        }
        String FirstData =  amount.substring(0, 1);
        if (amount.length() <= 10){
            if (FirstData.equals("0")){
                inpAmount.setText(assets.formatRupiah(Double.valueOf(data)));
            }else{
                Log.e( "setAmount: ", "tes" );
                inpAmount.setText(assets.formatRupiah(Double.valueOf(amount+""+data)));
            }
        }

    }

    public void back(View view) {
        Intent intent = new Intent(Amount.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//    public void keyboard(){
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.keyboard);
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//    }


}