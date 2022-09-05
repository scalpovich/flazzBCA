package com.ndp.flazzbca.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.ndp.flazzbca.MainActivity;
import com.ndp.flazzbca.R;
import com.ndp.flazzbca.base.DemoApp;
import com.ndp.flazzbca.menu.PiccDetect;
import com.ndp.flazzbca.util.Convert;
import com.pax.dal.IIcc;
import com.pax.dal.IPicc;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.exceptions.IccDevException;
import com.pax.dal.exceptions.PiccDevException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FlazzLib {
    private IPicc picc;
    private IIcc icc;
    public int Balance;
    public String CardNo;
    byte[] resp;
    String[] SlotA    = {"80","82","83","84","85"};

    static {
        System.loadLibrary("flazzbca");
    }

    public native int init(String sams);
    public native String BCAVersionDll();
    public native String BCAGetConfig();
    public native String BCASetConfig(String strConfig);
    public native String BCAIsMyCard();
    public native String BCACheckBalance();
    public native int test(String strdata);

    public native String BCADebitBalance(String sams, String curentTime, String Amount);

    public  String CLessAPDU(String dataOut)  {
        picc = DemoApp.getDal().getPicc(EPiccType.INTERNAL);

        Log.e("CLessAPDU: ", dataOut);
        int len = dataOut.length();
        byte[] res = new byte[len];

        byte[] cmdEx = Convert.getInstance().strToBcd(dataOut, Convert.EPaddingPosition.PADDING_RIGHT );
        try{
            res = picc.cmdExchange(cmdEx, len);
        }catch(PiccDevException e){
            e.printStackTrace();
        }
        String strCmdEx = Convert.getInstance().bcdToStr(res);
        return strCmdEx;
    }

    public String CAPDU(String dataOut, String SAMSSlot)  {
        icc = DemoApp.getDal().getIcc();
        int len = dataOut.length();
        byte[] res = new byte[len];
        int x = 5;
        Log.e("DataOut", dataOut );

        for (int i = 0; i<x; i++){
            byte[] slot = Convert.getInstance().strToBcd(SlotA[i], Convert.EPaddingPosition.PADDING_RIGHT);
            Log.e( "CAPDU: ", String.valueOf(SlotA[i]));

            Log.e( "CAPDU: ", String.valueOf(slot[0]));

            byte[] cmdEx = Convert.getInstance().strToBcd(dataOut, Convert.EPaddingPosition.PADDING_RIGHT );
            try{
                res = icc.isoCommand( (byte) slot[0], cmdEx);
                break;
            }catch(IccDevException e){
                e.printStackTrace();
                continue;
            }
        }


        String strCmdEx = Convert.getInstance().bcdToStr(res);
        Log.e( "res: ", String.valueOf(strCmdEx));

        return strCmdEx;
    }

    public void setBalance (int balance){
        this.Balance = balance;
        Log.e( "setBalance: ", String.valueOf(Balance));
    }

    public void setCardNo (String cardNo){
        this.CardNo = cardNo;
    }

    public int getBalance(){
        return this.Balance;
    }

    public String getCardNo(){
        return this.CardNo;
    }
}


