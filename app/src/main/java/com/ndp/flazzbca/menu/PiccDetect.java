package com.ndp.flazzbca.menu;

import static android.service.controls.actions.ControlAction.RESPONSE_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
//import android.support.annotation.WorkerThread;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ndp.flazzbca.Helper.Beep;
import com.ndp.flazzbca.Helper.SessionManager;
import com.ndp.flazzbca.MainActivity;
import com.ndp.flazzbca.R;
import com.ndp.flazzbca.dbms.MyDatabaseHelper;
import com.ndp.flazzbca.library.FlazzLib;
import com.ndp.flazzbca.menu.assets.MyAssets;
import com.ndp.flazzbca.modules.picc.PiccTester;
import com.ndp.flazzbca.util.Convert;
import com.pax.dal.IDAL;
import com.pax.dal.IIcc;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.exceptions.IccDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PiccDetect extends AppCompatActivity {

    public static DetectPicc detectABThread;
    private EPiccType piccType;
    private PiccTester tester;
    FlazzLib lib;
    String[] bulk = new String[19];
    String[] lengRes = {"16","6","14","10","10","1","8","8","16","16","8","6","4","12","8","16","2","4","20"};
    private ImageView nfc;
    public SharedPreferences session;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Intent in;
    private IIcc icc;
    private IDAL dal;
    private MyDatabaseHelper db;
    private int trace;
    private TextView label_amount;
    Bundle savedInstance;
    MyAssets assets;
    private View line;
    int Time = 0;
    String broadcast;
    byte[] hmacSha256;
    String signature;
    String dt1;
    SessionManager sessionManager;
    Beep beep;
    private int config = 0;

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (in.getStringExtra("menu").equals("saldo")) {
                        Boolean res = DetectCard(msg.obj.toString());
                        if(res == true){
                            Intent i = new Intent(PiccDetect.this, Saldo.class);
                            String[] separated = msg.obj.toString().split(",");
                            separated[0] = separated[0].trim();
                            separated[1] = separated[1].trim();
                            Log.e( "balance: ", separated[0]);
                            i.putExtra("balance", separated[0]);
                            i.putExtra("card", separated[1]);
                            startActivity(i);
                            finish();
                        }

                    }
                    else{
                        String res = lib.BCAIsMyCard();
                        Cursor prm = db.readAllParam();
                        prm.moveToFirst();
                        if (res.equals("0000")){
                            //old
                            //String data = lib.BCADebitBalance(getString(R.string.sams), date, in.getStringExtra("amount"));
                            //new
                            String data = lib.BCADebitBalance(prm.getString(9), date, in.getStringExtra("amount"));
//                            lib.init();
                            Log.e("saldo", data );

                            if(data.equals("Saldo Kurang")){
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PiccDetect.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                sweetAlertDialog.setTitleText("Saldo Tidak Cukup")
                                        .setCustomImage(R.drawable.ic_close_alert)
                                        .hideConfirmButton()
                                        .show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        sweetAlertDialog.dismiss();
                                        String stl = in.getStringExtra("Stand Alone");
                                        if(stl == null){
                                            broadcastFail("FAIL");
                                            finish();
                                        }else{
                                            Intent in = new Intent(PiccDetect.this, MainActivity.class);
                                            startActivity(in);
                                            finish();
                                        }

                                    }
                                }, 3000);
                            }
                            else if(data.equals("Saldo Nol")){
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PiccDetect.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                sweetAlertDialog.setTitleText("Saldo Tidak Cukup")
                                        .setCustomImage(R.drawable.ic_close_alert)
                                        .hideConfirmButton()
                                        .show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        sweetAlertDialog.dismiss();
                                        String stl = in.getStringExtra("Stand Alone");
                                        if(stl == null){
                                            broadcastFail("FAIL");
                                            finish();
                                        }else{
                                            Intent in = new Intent(PiccDetect.this, MainActivity.class);
                                            startActivity(in);
                                            finish();
                                        }

                                    }
                                }, 3000);
                            }
                            else if(data.equals("Loss Contact")){
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PiccDetect.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                sweetAlertDialog.setTitleText("Transaksi Belum Selesai, Silahkan Letakan Kartu Kembali")
                                        .setCustomImage(R.drawable.ic_close_alert)
                                        .hideConfirmButton()
                                        .show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        sweetAlertDialog.dismiss();
                                        detectABThread = null;
//                                        Intent x = new Intent(PiccDetect.this, PiccDetect.class);
//                                        x.putExtra("Detect", "sale");
//                                        x.putExtra("Amount",in.getStringExtra("Amount"));
                                        view();
//                                        initSAM();
//                                        startActivity(x);
//                                        finish();
                                    }
                                }, 3000);
                            }
                            else if(data.equals("Abnormal")){
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PiccDetect.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                sweetAlertDialog.setTitleText("Transaksi Gagal / Tansaksi Tidak Berhasil")
                                        .setCustomImage(R.drawable.ic_close_alert)
                                        .hideConfirmButton()
                                        .show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        sweetAlertDialog.dismiss();
                                        String stl = in.getStringExtra("Stand Alone");
                                        if(stl == null){
                                            broadcastFail("FAIL");
                                            finish();
                                        }else{
                                            Intent in = new Intent(PiccDetect.this, MainActivity.class);
                                            startActivity(in);
                                            finish();
                                        }
                                    }
                                }, 3000);
                            }
                            else {
                                cetakTrx(data);
                                Cursor dbTrans = db.readAllTransaksi();
                                Cursor cursor = db.readAllRegistry();
                                cursor.moveToFirst();
                                if (cursor.getCount() == 0){
                                    String tot_amount = in.getStringExtra("amount");
                                    db.addRegistry(String.valueOf(dbTrans.getCount() + 1), tot_amount, String.valueOf(dbTrans.getCount()+1), "1", "999", "no", data);
                                }else{
                                    long tot_amount = Long.parseLong(cursor.getString(2)) + Long.parseLong(in.getStringExtra("amount"));
                                    long dtTrace = Long.parseLong(cursor.getString(3)) + 1;
                                    db.updateReg(String.valueOf(dbTrans.getCount() + 1), String.valueOf(tot_amount), String.valueOf(dtTrace), "no", data);
                                }
                                beep.beepSuccess();
                                Intent i = new Intent(PiccDetect.this, PrintTransaksi.class);
                                i.putExtra("data", data);
                                String stl = in.getStringExtra("Stand Alone");
                                if(stl == null){
                                    broadcast("OK");
                                    i.putExtra("Third Party", "Yes");
                                }
                                startActivity(i);
                                finish();
                            }

                        }else{
                            String stl = in.getStringExtra("Stand Alone");
                            if(stl == null){
                                broadcast("FAIL");
                                Intent in = new Intent(PiccDetect.this, MainActivity.class);
                                startActivity(in);
                                finish();
                            }else{
                                Intent in = new Intent(PiccDetect.this, MainActivity.class);
                                startActivity(in);
                                finish();
                            }
                            Boolean x = DetectCard("error");
                        }
                    }
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piccdetect);

        assets = new MyAssets();
        savedInstance = savedInstanceState;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        date = dateFormat.format(calendar.getTime());
        in = getIntent();
        sessionManager = new SessionManager(this);
        beep = new Beep(this);


        //Function
        db = new MyDatabaseHelper(PiccDetect.this);
        lib = new FlazzLib();
        view();

        SimpleDateFormat Ftime = new SimpleDateFormat("yyyyMMddHHmmss");
        dt1 = Ftime.format(new Date());
        String serialNumber = Build.SERIAL;
//        String serialNumber = dal.getSys().getTermInfo().get(ETermInfoKey.SN);

        String msg = dt1+serialNumber+"FLAZZ"+"sale";
        try {
            hmacSha256 = calcHmacSha256("0000000000000000".getBytes("UTF-8"), msg.getBytes("UTF-8"));
            signature =  String.format("Hex: %064x", new BigInteger(1, hmacSha256));
            Log.e("onCreate: ",signature );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(initSAM() == 1){
            broadcastFail("FAIL");
        }
    }


    void view(){
        piccType = EPiccType.INTERNAL;
        tester = PiccTester.getInstance(piccType);
        nfc = findViewById(R.id.nfc);
        Glide.with(this).load(R.drawable.contactless).into(nfc);

        if (detectABThread == null) {
            detectABThread = new DetectPicc();
            Log.e( "view: ","tester" );
            tester.open();
            detectABThread.start();

        }

        label_amount = findViewById(R.id.lbl_Amount);
        line         = findViewById(R.id.line);

        if (in != null) {
            if (in.getStringExtra("menu").equals("sale")){
                label_amount.setText("Rp. "+assets.formatRupiah(Double.parseDouble(in.getStringExtra("amount"))));
            }else{
                line.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (in != null) {
            if (in.getStringExtra("menu").equals("sale")){
                broadcastFail("CANCELED");
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tester.setLed((byte) 0x00);// close all lights.
        tester.close();
        if (detectABThread != null) {
            detectABThread.interrupt();
            detectABThread = null;
        }
    }

    public class DetectPicc extends Thread{

        @Override
        public void run() {
            super.run();
            while (!Thread.interrupted()) {
                int i = tester.detectAorBandCommand(handler, in.getStringExtra("menu"));
                if (i == 1) {
                    break;
                }
                if(Time == 10){
                    alertCard();
                }
                ++Time;
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    @WorkerThread
    void alertCard(){
        runOnUiThread(new Runnable() {
            public void run() {
                Time = 0;
                Toast.makeText(getApplicationContext(), "Silahkan Tempelkan Kartu", Toast.LENGTH_LONG).show();
            }
        });
    }

    public Boolean DetectCard(String alert){
        if(alert.equals("error")){
            Intent i = new Intent(PiccDetect.this, MainActivity.class);
            i.putExtra("error", "true");
            startActivity(i);
            finish();
            return false;
        }
        return true;
    }

    static public byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return hmacSha256;
    }

    void cetakTrx(String data){
        String datax = data.substring(4);
        int x = 19;
        for (int i = 0; i<x; i++){
            bulk[i] = datax.substring(0, Integer.parseInt(lengRes[i]));
            datax = datax.substring(Integer.parseInt(lengRes[i]));
        }
    }

    void broadcast(String resp){
        String msg = dt1+Build.SERIAL+"FLAZZ"+"sale";
        try {
            hmacSha256 = calcHmacSha256("0000000000000000".getBytes("UTF-8"), msg.getBytes("UTF-8"));
            signature =  String.format("Hex: %064x", new BigInteger(1, hmacSha256));
            Log.e("onCreate: ",signature );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Cursor params = db.readAllParam();

        params.moveToFirst();
        Cursor reg = db.readAllRegistry();
        reg.moveToFirst();
        broadcast = params.getString(2)+"|"
                +params.getString(1)+"|"
                +"FLAZZ"+"|"
                +bulk[0]+"|"
                +"EXPDATE:"+bulk[1].substring(2,4)+"/"+bulk[1].substring(4,6)+"|"
                +"TAP|"
                +"SALE|"
                +String.format("%06d", Integer.parseInt(reg.getString(4)))+"|"
                +String.format("%06d", Integer.parseInt(reg.getString(3)))+"|"
                +formatDate(bulk[2])+"|"
                +formatHour(bulk[2])+"|"
                +"123456789012|"
                +bulk[11]+"|"
                +in.getStringExtra("amount")+"|"
                +"00|FLZ"
        ;

        Intent intent = new Intent();
        intent.putExtra("App", "com.ndp.flazzbca");
        intent.putExtra("Respon", resp);
        intent.putExtra("Data", broadcast);
        intent.putExtra("Signature",signature);
        setResult(RESPONSE_OK,intent );
//        finish();
    }

    void broadcastFail(String resp){
        String msg = dt1+Build.SERIAL+"FLAZZ"+"sale";
        try {
            hmacSha256 = calcHmacSha256("0000000000000000".getBytes("UTF-8"), msg.getBytes("UTF-8"));
            signature =  String.format("Hex: %064x", new BigInteger(1, hmacSha256));
            Log.e("onCreate: ",signature );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        Intent intent = new Intent();
        intent.putExtra("App", "com.ndp.flazzbca");
        intent.putExtra("Respon", resp);
        intent.putExtra("Data", "CANCELED");
        intent.putExtra("Signature",signature);
        setResult(RESPONSE_OK,intent );
//        finish();
    }

    public String formatDate(String date) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat targetFormat = new SimpleDateFormat("ddMMMyyyy");
        Date dates;
        String dt = null;
        try {
            dates = originalFormat.parse(date);
            dt = targetFormat.format(dates);
            return dt;
        } catch (ParseException ex) {
        }
        return date;
    }

    public String formatHour(String date) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm:ss");
        Date dates;
        String dt = null;
        try {
            dates = originalFormat.parse(date);
            dt = targetFormat.format(dates);
            return dt;
        } catch (ParseException ex) {
        }
        return date;
    }

    //init sam
    int initSAM(){
        if(!in.getStringExtra("menu").equals("saldo")){
            setConfig();
            Cursor cursor = db.readAllParam();
            cursor.moveToFirst();
            //old
//        byte[] slot = Convert.getInstance().strToBcd(getString(R.string.sams), Convert.EPaddingPosition.PADDING_RIGHT);
            //new
            Log.e( "sams: ", cursor.getString(9));

            byte[] slot = Convert.getInstance().strToBcd(cursor.getString(9), Convert.EPaddingPosition.PADDING_RIGHT);
//        byte[] slot = Convert.getInstance().strToBcd(sessionManager.getSams(), Convert.EPaddingPosition.PADDING_RIGHT);

            try {
                dal = NeptuneLiteUser.getInstance().getDal(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte slotSAMS = (byte) slot[0];
            icc = dal.getIcc();
            try {
                byte[] resp = icc.init(slotSAMS);
                int sc = lib.init(cursor.getString(9));
//            int sc = lib.init(sessionManager.getSams());
                if(sc == 1){
                    new AlertDialog.Builder(this)
                            .setTitle("Init Sams False")
                            .setMessage("Sams Tidak Terbaca!")
                            .setNegativeButton("Ok",null)
                            .show();
                    return -1;
                }
            } catch (IccDevException e) {
                e.printStackTrace();
                new AlertDialog.Builder(this)
                        .setTitle("Init Sams False")
                        .setMessage("Sams Tidak Terbaca!")
                        .setNegativeButton("Ok",null)
                        .show();
                return -1;
            }
        }
        return 0;
    }

    void setConfig(){
        if(config == 0){
            Cursor cursor = db.readAllParam();
            cursor.moveToFirst();
            String strConfig = cursor.getString(2)+","+cursor.getString(1)+",00001";
//            String strConfig = sessionManager.getMid()+","+sessionManager.getTid()+",00001";
            String strResponse = lib.BCASetConfig(strConfig);
//            int strResponse = lib.BCASetConfig(strConfig);
//            if(strResponse==0)
//                Toast.makeText(getApplicationContext(), "Error Access Library", Toast.LENGTH_SHORT).show();
//            else
//                config = 1;
            if(strResponse.equals(0))
                Toast.makeText(getApplicationContext(), "Error Access Library", Toast.LENGTH_SHORT).show();
            else if(strResponse.equals("Terminal ID or Merchant ID is NOT Alpha Numeric format"))
                Toast.makeText(getApplicationContext(), "Terminal ID atau Merchant ID bukan Alpha Numeric format", Toast.LENGTH_SHORT).show();
            else if(strResponse.equals("Terminal ID Invalid"))
                Toast.makeText(getApplicationContext(), "Terminal ID Invalid", Toast.LENGTH_SHORT).show();
            else if(strResponse.equals("Merchant ID Invalid"))
                Toast.makeText(getApplicationContext(), "Merchant ID Invalid", Toast.LENGTH_SHORT).show();
            else
                config = 1;
        }
    }
}