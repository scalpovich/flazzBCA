package com.ndp.flazzbca;

import static android.service.controls.actions.ControlAction.RESPONSE_OK;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ndp.flazzbca.Helper.BcaDocument;
import com.ndp.flazzbca.Helper.SessionManager;
import com.ndp.flazzbca.Paxstore.DemoConstants;
import com.ndp.flazzbca.Paxstore.DownloadManager;
import com.ndp.flazzbca.databinding.ActivityMainBinding;
import com.ndp.flazzbca.dbms.MyDatabaseHelper;
import com.ndp.flazzbca.library.FlazzLib;
import com.ndp.flazzbca.menu.Amount;
import com.ndp.flazzbca.menu.PiccDetect;
import com.ndp.flazzbca.menu.Settelment;
import com.ndp.flazzbca.menu.assets.MyAssets;
import com.ndp.flazzbca.util.Convert;
import com.pax.dal.IDAL;
import com.pax.dal.IIcc;
import com.pax.dal.exceptions.IccDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;

//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Hex;

import java.io.File;
import static com.ndp.flazzbca.Helper.Constant.XML_PATH;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private ImageButton sale, saldo,settlement;
    private TextView txtMerchant, txtAddress, txtTrace, txtAmount, txtTgl, txtSeeAll;
    private int config = 0;
    FlazzLib lib;
    private IIcc icc;
    private IDAL dal;
    MyDatabaseHelper db;
    Bundle savedInstance;
    Cursor params;
    SessionManager sessionManager;
    private boolean doubleBackToExitPressedOnce = false;
    String[] bulk       = new String[19];
    String[] lengRes    = {"16","6","14","10","10","1","8","8","16","16","8","6","4","12","8","16","2","4","20"};
    MyAssets assets;
    LinearLayout layLast, NoTrans;

    //GET PARAMETER
    private MsgReceiver msgReceiver;
    DownloadManager downloadManager;
    Boolean update;
    //END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance = savedInstanceState;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        assets = new MyAssets();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getSupportActionBar().hide();

        db = new MyDatabaseHelper(MainActivity.this);
        lib = new FlazzLib();

        //receiver to get UI update.
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DemoConstants.UPDATE_VIEW_ACTION);
        registerReceiver(msgReceiver, intentFilter);

        //method
        InitView();
        checkIntent();
        //downloadParamXml();
    }

    @Override
    protected void onStart() {
        super.onStart();
        lastTrans();

    }

    void InitView(){
        sale            = findViewById(R.id.btnSale);
        saldo           = findViewById(R.id.btnSaldo);
        settlement      = findViewById(R.id.btnSettlement);
        txtMerchant     = findViewById(R.id.txtMerchant);
        txtAddress      = findViewById(R.id.txtAddress);
        txtTrace        = findViewById(R.id.txtRefM);
        txtAmount       = findViewById(R.id.txt_amountM);
        txtTgl          = findViewById(R.id.txtTglM);
        txtSeeAll       = findViewById(R.id.seeAll);
        layLast         = findViewById(R.id.LayLast);
        NoTrans         = findViewById(R.id.layNoTrans);

        sessionManager  = new SessionManager(this);
        params          = db.readAllParam();

        sale.setOnClickListener(this);
        saldo.setOnClickListener(this);
        settlement.setOnClickListener(this);
        txtSeeAll.setOnClickListener(this);
        params.moveToFirst();

        if (params.getCount() > 0){
            if (params.getString(7) != null){
                txtMerchant.setText(params.getString(7));
                txtAddress.setText(params.getString(8));
            }
        }
    }

    void checkIntent(){
        if(savedInstance == null){
            Bundle in = getIntent().getExtras();
            if(in != null) {
                String app = in.getString("App");
                String error = in.getString("error");
                String menu = in.getString("menu");
                if (error == null){
                    if(menu != null){
                        Intent i = new Intent(MainActivity.this, PiccDetect.class);
                        i.putExtra("menu", "sale");
                        i.putExtra("Amount", in.getString("amount"));
                        i.putExtra("Third Party", "UI Payment");
                        startActivity(i);
                        finish();
                    }else if(in.getString("broadcast") != null){
                        finish();
                    }
                }else{
                    if(in.getString("error").equals("true")){
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                        sweetAlertDialog.setTitleText("Transaksi Tidak Berhasil!")
                                .setCustomImage(R.drawable.ic_close_alert)
                                .hideConfirmButton()
                                .show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                sweetAlertDialog.dismiss();
                            }
                        }, 3000);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSale:
                if (cekParams() == 0) {
                    setConfig();
                    if (initSAM() == 0) {
                        Cursor cr = db.readAllTransaksi();
                        if (cr.getCount() <= 999) {
                            Intent in = new Intent(this, Amount.class);
                            startActivity(in);
                        } else {
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                            sweetAlertDialog.setTitleText("Transaksi Sudah Melebihi Batas Maksimum!")
                                    .setCustomImage(R.drawable.ic_close_alert)
                                    .hideConfirmButton()
                                    .show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    sweetAlertDialog.dismiss();
                                }
                            }, 3000);
                        }
                    }
                }
                break;

            case R.id.btnSaldo:
                Intent i = new Intent(this, PiccDetect.class);
                i.putExtra("menu", "saldo");
                startActivity(i);
                break;

            case R.id.btnSettlement:
                Intent intent = new Intent(this, Settelment.class);
                startActivity(intent);
                break;
            case R.id.seeAll:
                Intent x = new Intent(this, Settelment.class);
                startActivity(x);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    int initSAM(){
        Cursor cursor       = db.readAllParam();
        cursor.moveToFirst();
        byte[] slot     = Convert.getInstance().strToBcd(cursor.getString(9), Convert.EPaddingPosition.PADDING_RIGHT);
        try {
            dal         = NeptuneLiteUser.getInstance().getDal(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte slotSAMS   = (byte) slot[0];
        icc             = dal.getIcc();
        try {
            byte[] resp = icc.init(slotSAMS);
            int sc      = lib.init(cursor.getString(9));
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
        return 0;
    }

    void setConfig(){
        if(config == 0){
            Cursor cursor = db.readAllParam();
            cursor.moveToFirst();
            String strConfig = cursor.getString(2)+","+cursor.getString(1)+",00001";
            Log.e("DEBUG'", "setConfig: "+strConfig );
//            String strConfig = sessionManager.getMid()+","+sessionManager.getTid()+",00001";
            String strResponse = lib.BCASetConfig(strConfig);
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

    void getConfig(){
        if(config == 1){
            String strResp = lib.BCAGetConfig();
            Toast.makeText(getApplicationContext(), strResp, Toast.LENGTH_SHORT).show();
        }
    }

    void lastTrans(){
        Cursor tranx = db.readAllTransaksi();
        if (tranx.getCount() > 0){
            NoTrans.setVisibility(View.INVISIBLE);
            layLast.setVisibility(View.VISIBLE);
            tranx.moveToFirst();
            cetakTrx(tranx.getString(2));
            String ref = String.format("%06d", Integer.parseInt(tranx.getString(1)));
            txtTrace.setText(ref);
            txtTgl.setText( assets.formatDate(bulk[2]));
            txtAmount.setText("Rp."+ assets.formatRupiah(Double.parseDouble(bulk[4])));
        }else{
            NoTrans.setVisibility(View.VISIBLE);
            layLast.setVisibility(View.INVISIBLE);
        }
    }

    void cetakTrx(String data){
        String datax = data.substring(4);
        int x = 19;
        for (int i = 0; i<x; i++){
            bulk[i] = datax.substring(0, Integer.parseInt(lengRes[i]));
            datax = datax.substring(Integer.parseInt(lengRes[i]));
        }
    }

    int cekParams(){
        if(Objects.equals(sessionManager.getTid(), "") || Objects.equals(sessionManager.getTid(), "null") || sessionManager.getTid()==null){
            new AlertDialog.Builder(this)
                    .setTitle("Parameter")
                    .setMessage("Push Parameter terlebih dahulu!")
                    .setNegativeButton("Ok",null)
                    .show();
            return -1;
        }
        return 0;
    }

    //*** GET PARAMETER FROM PAX ***//
    @Override
    protected void onResume() {
        super.onResume();
        downloadParamXml();
        Runnable runnable = new CountDownRunner();
        Thread clockThread = new Thread(runnable);
        clockThread.start();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }

    void downloadParamXml(){
        downloadManager = DownloadManager.getInstance();
        downloadManager.setFilePath(getFilesDir() + "/Download/");
        downloadManager.addDocument(new BcaDocument(XML_PATH, MainActivity.this));
    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //update main page UI for push status
            int resultCode = intent.getIntExtra(DemoConstants.DOWNLOAD_RESULT_CODE, 0);
            switch (resultCode){
                case DemoConstants.DOWNLOAD_STATUS_SUCCESS:
                    Toast.makeText(MainActivity.this,DemoConstants.DOWNLOAD_SUCCESS,Toast.LENGTH_SHORT).show();
                    break;
                case DemoConstants.DOWNLOAD_STATUS_START:
                    Toast.makeText(MainActivity.this,DemoConstants.DOWNLOAD_START,Toast.LENGTH_SHORT).show();
                    break;
                case DemoConstants.DOWNLOAD_STATUS_FAILED:
                    Toast.makeText(MainActivity.this,DemoConstants.DOWNLOAD_FAILED,Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    File file = new File(downloadManager.getFilePath()+XML_PATH);
                    if(file.exists()) {
                        downloadManager.updateData(MainActivity.this);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    //note: Note Use//
//    public void formatDate(String date) {
//        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//        Date dates;
//        try {
//            dates = originalFormat.parse(date);
//            String dt = targetFormat.format(dates);
//            Log.e( "formatDate: ", String.valueOf(dt));
//        } catch (ParseException ex) {
//        }
//    }
}