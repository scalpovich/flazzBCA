package com.ndp.flazzbca.menu;

import static android.service.controls.actions.ControlAction.RESPONSE_OK;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ndp.flazzbca.Helper.SessionManager;
import com.ndp.flazzbca.MainActivity;
import com.ndp.flazzbca.R;
import com.ndp.flazzbca.base.DemoApp;
import com.ndp.flazzbca.dbms.MyDatabaseHelper;
import com.ndp.flazzbca.menu.assets.MyAssets;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PrintTransaksi extends AppCompatActivity implements View.OnClickListener {

    TextView lTerm, lEDC, lBatch, lHost, lPan, lAppr, lBalAfter, lAmount, lTrace, lTrn, lDate, MerchantName, Address;
    Button btnCetak;
    String[] bulk = new String[19];
    String[] lengRes = {"16","6","14","10","10","1","8","8","16","16","8","6","4","12","8","16","2","4","20"};
    DecimalFormat formatter;
    LinearLayout layout_view;
    IPrinter printer;
    private MyDatabaseHelper db;
    private String trace;
    private MyAssets assets;
    private Cursor reg;
    Intent i;
    String broadcast;
    Cursor params;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_transaksi);
        printer     = DemoApp.getDal().getPrinter();
        formatter   = new DecimalFormat("#,###,###");
        db          = new MyDatabaseHelper(PrintTransaksi.this);
        i           = getIntent();
        assets      = new MyAssets();
        reg         = db.readAllRegistry();
        reg.moveToFirst();


        Cursor cursor = db.readAllTransaksi();
        Cursor reg = db.readAllRegistry();
        params = db.readAllParam();

        params.moveToFirst();
        reg.moveToFirst();
        String dtl = null;
        dtl = i.getStringExtra("detail");
        if(dtl == null){
            trace = String.format("%06d", Integer.parseInt(reg.getString(3)));
            int save = db.addTRX(trace, i.getStringExtra("data"));
            if (save == 0){
                db.updateRegistry("yes");
            }
        }
        // trace ? akan di set ulang setelah settlement atau tidak ?

        if (i != null){
            cetakTrx(i.getStringExtra("data"));
        }
        view();

    }

    void view(){
        layout_view = findViewById(R.id.buktiTrans);
        lTerm       = findViewById(R.id.label_term);
        lEDC        = findViewById(R.id.label_edc);
        lBatch      = findViewById(R.id.label_batch);
        lPan        = findViewById(R.id.label_pan);
        lAmount     = findViewById(R.id.label_amount);
        lTrace      = findViewById(R.id.label_trace);
        lTrn        = findViewById(R.id.label_trn);
        lDate       = findViewById(R.id.label_date);
        btnCetak    = findViewById(R.id.btnCetak);
        lAppr       = findViewById(R.id.label_aprov);
        MerchantName= findViewById(R.id.txtMerchant);
        Address     = findViewById(R.id.txtAlamat);

        lTerm.setText("MERC#"+ params.getString(2));
        lEDC.setText("TERM#"+ params.getString(1));
        MerchantName.setText(params.getString(7));
        Address.setText(params.getString(8));
//        lEDC.setText("TERM#"+ params.getString(1));
        lBatch.setText("BATCH : "+String.format("%06d", Integer.parseInt(reg.getString(4))));
        lPan.setText(bulk[0]);
        String dt = String.valueOf(bulk[2]);
        lDate.setText("DATE/TIME "+ assets.formatDate(dt));
        lAmount.setText("Rp. "+assets.formatRupiah(Double.parseDouble(bulk[4])));
        lTrace.setText("TRACE NO : "+trace);
        lTrn.setText("TRN "+bulk[15].toUpperCase());
        lAppr.setText("APPR.CODE "+bulk[11].toUpperCase());

        btnCetak.setOnClickListener(this);

//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("Data", i.getStringExtra("data"));
//        resultIntent.putExtra("Signature", "??");
//        setResult(RESULT_OK, resultIntent);

    }

    void cetakTrx(String data){
        String datax = data.substring(4);
        int x = 19;
        for (int i = 0; i<x; i++){
            bulk[i] = datax.substring(0, Integer.parseInt(lengRes[i]));
            datax = datax.substring(Integer.parseInt(lengRes[i]));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnCetak:
                Bitmap image = getBitmapFromView(layout_view);
                new Thread(new Runnable() {
                    public void run() {

                        try {
                            printer.init();
                        } catch (PrinterDevException e) {
                            e.printStackTrace();
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inScaled = false;
                        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.check,options);

                        if (true) {
                            try {
                                printer.leftIndent(Short.parseShort("0")); // posisi kiri
                            } catch (PrinterDevException e) {
                                e.printStackTrace();
                            }
                            try {
                                printer.printBitmap(image);
                            } catch (PrinterDevException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            printer.spaceSet(Byte.parseByte("0"), // space word
                                    Byte.parseByte("0")); // line space
                        } catch (PrinterDevException e) {
                            e.printStackTrace();
                        }
                        try {
                            printer.leftIndent(Short.parseShort("0")); // posisi kiri
                        } catch (PrinterDevException e) {
                            e.printStackTrace();
                        }
                        try {
                            printer.setGray(Integer.parseInt("1")); // warna
                        } catch (PrinterDevException e) {
                            e.printStackTrace();
                        }
                        try {
                            printer.step(Integer.parseInt("150".trim()));
                        } catch (PrinterDevException e) {
                            e.printStackTrace();
                        }
                        try {
                            printer.start();
                        } catch (PrinterDevException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                ObjectAnimator animation = ObjectAnimator.ofFloat(layout_view, "translationY", -1400);
                animation.setDuration(2000);
                animation.start();


                Handler handler = new Handler();
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PrintTransaksi.this, SweetAlertDialog.SUCCESS_TYPE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sweetAlertDialog.setTitleText("Success")
                                .hideConfirmButton()
                                .show();
                        animation.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animation.removeListener(this);
                                animation.setDuration(0);
                                ((ValueAnimator) animation).reverse();
                            }
                        });
                    }
                },1500);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sweetAlertDialog.dismiss();
                        if (i.getStringExtra("Third Party") != null){
//                            Intent in = new Intent(PrintTransaksi.this, MainActivity.class);
//                            in.putExtra("Third Party", i.getStringExtra("Third Party"));
//                            in.putExtra("broadcast", "yes");
//                            startActivity(in);
                            finish();
                        }else{
                            finish();
                        }

                    }
                },4000);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Log.e( "getBitmapFromView: ", String.valueOf(view.getWidth()));
        Log.e( "getBitmapFromView: ", String.valueOf(view.getHeight()));

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


}