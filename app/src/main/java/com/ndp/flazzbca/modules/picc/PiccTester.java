package com.ndp.flazzbca.modules.picc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.ndp.flazzbca.MainActivity;
import com.ndp.flazzbca.library.FlazzLib;
import com.ndp.flazzbca.menu.PiccDetect;
import com.ndp.flazzbca.menu.Saldo;
import com.pax.dal.IPicc;
import com.pax.dal.entity.ApduSendInfo;
import com.pax.dal.entity.EBeepMode;
import com.pax.dal.entity.EDetectMode;
import com.pax.dal.entity.EM1KeyType;
import com.pax.dal.entity.EPiccRemoveMode;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.entity.PiccCardInfo;
import com.pax.dal.entity.PiccPara;
import com.pax.dal.exceptions.EPiccDevException;
import com.pax.dal.exceptions.PiccDevException;
import com.ndp.flazzbca.base.DemoApp;
import com.ndp.flazzbca.modules.system.SysTester;
import com.ndp.flazzbca.util.BaseTester;
import com.ndp.flazzbca.util.Convert;
import com.ndp.flazzbca.util.IApdu;
import com.ndp.flazzbca.util.IApdu.IApduReq;
import com.ndp.flazzbca.util.Packer;

public class PiccTester extends BaseTester {
    private static PiccTester piccTester;
    private IPicc picc;
    private static EPiccType piccType;

    FlazzLib lib = new FlazzLib();
    private PiccTester(EPiccType type) {
        piccType = type;
        picc = DemoApp.getDal().getPicc(piccType);
    }

    public static PiccTester getInstance(EPiccType type) {
        if (piccTester == null || type != piccType) {
            piccTester = new PiccTester(type);
        }
        return piccTester;
    }

    // 读取当前参数设置
    public PiccPara setUp() {
        try {
            PiccPara readParam = picc.readParam();
            logTrue("readParam");
            return readParam;
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("readParam", e.toString());
            return null;
        }
    }

    public void open() {
        try {
            picc.open();
            logTrue("open");
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("open", e.toString());
        }
    }

    public PiccCardInfo detect(EDetectMode mode) {
        try {
            PiccCardInfo cardInfo = picc.detect(mode);
            logTrue("detect");
            return cardInfo;
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("detect", e.toString());
            return null;
        }
    }

    public void setLed(byte led) {
        try {
            picc.setLed(led);
            logTrue("setLed");
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("setLed", e.getMessage());
        }
    }

    public void close() {
        try {
            picc.close();
            logTrue("close");
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("close", e.toString());
        }
    }

    public int detectAorBandCommand(Handler handler, String tag) {
        PiccCardInfo cardInfo;
        if (null != (cardInfo = detect(EDetectMode.ISO14443_AB))) {
            String cardString = "";
            if(tag.equals("saldo")){
                String res = lib.BCAIsMyCard();
                if (!res.equals("0000")){
                    cardString = "error";
                }else{
                    String x = lib.BCACheckBalance();
                    cardString += x;
                    Log.e( "Balance: ", String.valueOf(x));
                }
            }

            Message message = Message.obtain();
            message.what = 0;
            message.obj = cardString;
            handler.sendMessage(message);
            Log.e( "detectAorBandCommand: ", "sdasd");


            while (true) {
                break;
            }
            SysTester.getInstance().beep(EBeepMode.FREQUENCE_LEVEL_1, 500);
            SystemClock.sleep(1000);
            // setLed((byte) 0x00);
            return 1;
        }
        return 0;
    }

}
