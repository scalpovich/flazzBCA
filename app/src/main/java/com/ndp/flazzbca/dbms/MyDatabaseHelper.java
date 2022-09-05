package com.ndp.flazzbca.dbms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "FlazzBCA.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PARAMS = "params";
    private static final String TABLE_REGISTRY = "registry";
    private static final String TABLE_TRANSAKSI = "transaksi";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String transaksi = "CREATE TABLE "+TABLE_TRANSAKSI
                +" (id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "trace TEXT,"
                +"response TEXT, "
                +"status TEXT );";

        String param = "CREATE TABLE "+TABLE_PARAMS+
                " (id INTEGER PRIMARY KEY AUTOINCREMENT ," // 0
                +"mid TEXT, " // 1
                +"tid TEXT, " + // 2
                "ftp_port TEXT, " + // 3
                "ftp_host TEXT, "+ // 4
                "ftp_user TEXT, "+ // 5
                "ftp_pass TEXT, "+ // 6
                "merchant TEXT, "+ // 7
                "alamat TEXT," + // 8
                "sams TEXT," + // 9
                "path TEXT);"; // 10

        String registry = "CREATE TABLE " +TABLE_REGISTRY+
                "(id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "total_trx TEXT," +
                "total_amount TEXT," +
                "trace TEXT," +
                "batch TEXT, " +
                "max TEXT, " +
                "allready_save TEXT," +
                "data_trx TEXT);";

        db.execSQL(transaksi);
        db.execSQL(param);
        db.execSQL(registry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TRANSAKSI);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PARAMS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_REGISTRY);
        onCreate(db);
    }

    public int addTRX(String trace, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put("trace", trace);
        val.put("response", data);
        val.put("status", "0");

        long result = db.insert(TABLE_TRANSAKSI, null, val);
        if (result == -1){
            Toast.makeText(context, "Failed To Save Data", Toast.LENGTH_SHORT).show();
            return -1;
        }else{
            Log.e("addTRX: ", "Berhasil Menyimpan Data");
            return 0;
        }

    }

    public void addRegistry(String total_trx, String total_amount, String trace, String batch, String max, String save, String data_trx){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put("total_trx", total_trx);
        val.put("total_amount", total_amount);
        val.put("trace", trace);
        val.put("batch", batch);
        val.put("max", max);
        val.put("allready_save", save);
        val.put("data_trx", data_trx);

        long result = db.insert(TABLE_REGISTRY, null, val);
        if (result == -1){
            Toast.makeText(context, "Failed To Save Data", Toast.LENGTH_SHORT).show();
        }else{
            Log.e("addRegistry: ", "Berhasil Menyimpan Data");
        }
    }

    public void addParam(String mid, String tid, String ftp_port, String ftp_host,  String ftp_user,  String ftp_pass, String path, String merchant, String alamat, String sams){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put("sams", sams.trim());
        val.put("mid", mid);
        val.put("tid", tid);
        val.put("ftp_port", ftp_port);
        val.put("ftp_host", ftp_host);
        val.put("ftp_user", ftp_user);
        val.put("ftp_pass", ftp_pass);
        val.put("path", path);
        val.put("merchant", merchant);
        val.put("alamat", alamat);
        Log.e( "addParam: ", String.valueOf(val));

        long result = db.insert(TABLE_PARAMS, null, val);
        if (result == -1){
            Toast.makeText(context, "Failed To Save Data", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllTransaksi(){
        String query = "SELECT * FROM "+TABLE_TRANSAKSI+" ORDER BY trace DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllParam(){
        String query = "SELECT * FROM "+TABLE_PARAMS;
        SQLiteDatabase db = this.getReadableDatabase();
//
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllRegistry(){
        String query = "SELECT * FROM "+TABLE_REGISTRY+" LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor SearchData(String key){
        String query = "SELECT * FROM "+TABLE_TRANSAKSI+" WHERE "+
                "trace LIKE '"+key+"%'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateParams(String mid, String tid, String ftp_port, String ftp_host,  String ftp_user,  String ftp_pass, String path, String merchant, String alamat, String sams){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put("sams", sams.trim());
        val.put("mid", mid);
        val.put("tid", tid);
        val.put("ftp_port", ftp_port);
        val.put("ftp_host", ftp_host);
        val.put("ftp_user", ftp_user);
        val.put("ftp_pass", ftp_pass);
        val.put("path", path);
        val.put("merchant", merchant);
        val.put("alamat", alamat);

        long result = db.update(TABLE_PARAMS, val, "id=?", new String[]{"1"});
        if (result == -1){
            Toast.makeText(context, "Failed To Save Data", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateReg(String total_trx, String total_amount, String trace, String save, String data_trx){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put("total_trx", total_trx);
        val.put("total_amount", total_amount);
        val.put("trace", trace);
        val.put("allready_save", save);
        val.put("data_trx", data_trx);

        long result = db.update(TABLE_REGISTRY, val, "id=?", new String[]{"1"});
        if (result == -1){
            Toast.makeText(context, "Failed To Save Data", Toast.LENGTH_SHORT).show();
        }else{
            Log.e("addRegistry: ", "Berhasil Menyimpan Data");
        }
    }


    public void updateRegistry(String save){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put("allready_save", save);

        long result = db.update(TABLE_REGISTRY, val, "id=?", new String[]{"1"});
        if (result == -1){
            Toast.makeText(context, "Failed To Save Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateBatch(String batch, String trace){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put("batch", batch);
        val.put("trace", trace);
        val.put("total_amount", 0);
        val.put("total_trx", 0);

        long result = db.update(TABLE_REGISTRY, val, "id=?", new String[]{"1"});
        if (result == -1){
            Toast.makeText(context, "Failed To Save Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTrx(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+ TABLE_TRANSAKSI);
    }

    public void deletePrm(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+ TABLE_PARAMS);
    }
}
