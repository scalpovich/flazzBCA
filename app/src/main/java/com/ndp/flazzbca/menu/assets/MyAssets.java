package com.ndp.flazzbca.menu.assets;

import android.util.Log;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyAssets {
    public String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getInstance(localeID);
        return formatRupiah.format(number);
    }

    public String changeFormat(String amount){
        return amount.replace(".","");
    }

    public String formatDate(String date) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yy | HH:mm");
        Date dates;
        try {
            dates = originalFormat.parse(date);
            String dt = targetFormat.format(dates);
            return  dt;
        } catch (ParseException ex) {
            return null;
        }
    }
}
