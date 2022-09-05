package com.ndp.flazzbca.Helper;

import android.content.Context;

import com.pax.dal.IDAL;
import com.pax.dal.ISys;
import com.pax.dal.entity.EBeepMode;
import com.pax.neptunelite.api.NeptuneLiteUser;

public class Beep {

    private IDAL dal;
    private ISys sys;

    public Beep(Context context) {
        try {
            this.dal = NeptuneLiteUser.getInstance().getDal(context);
            this.sys = dal.getSys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beepButton(){
        sys.beep(EBeepMode.FREQUENCE_LEVEL_6, 100);
    }

    public void beepButtonWarn(){
        sys.beep(EBeepMode.FREQUENCE_LEVEL_5, 300);
    }

    public void beepSuccess(){
        sys.beep(EBeepMode.FREQUENCE_LEVEL_3, 100);
        sys.beep(EBeepMode.FREQUENCE_LEVEL_4, 100);
        sys.beep(EBeepMode.FREQUENCE_LEVEL_5, 100);
    }
    public void beepError(){
        sys.beep(EBeepMode.FREQUENCE_LEVEL_5, 300);
    }
}
