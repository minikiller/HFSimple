package com.test.hfsimple;

import android.app.Activity;
import android.app.Application;


import com.pda.hf.HFReader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * use to global value
 * Created by admin on 2017/1/16.
 */
public class MyApplication extends Application {


    private HFReader hfReader = null ;


    private  int port = 14 ;
    private int power = HFReader.POWER_PSAM ;
    private ParaSave para ;

    public HFReader getHfReader() {
//        hfReader = new HFReader(13, 115200 , HFReader.POWER_RFID) ;
        if (hfReader == null) {
            hfReader = new HFReader(port, 115200, power);
        }
        return hfReader;
    }

    public void setHfReader(HFReader hfReader) {
        this.hfReader = hfReader;
    }

    private List<Activity> listAc  = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        para = new ParaSave(this) ;
        String serialStr = para.getSerial() ;
        String powerStr = para.getPower() ;
        if(serialStr.equals("com0")){
            port = 0 ;
        }else if(serialStr.equals("com2")){
            port = 2 ;
        }else if(serialStr.equals("com3")){
            port = 3 ;
        }else if(serialStr.equals("com11")){
            port = 11 ;
        }else if(serialStr.equals("com12")){
            port = 12 ;
        }else if(serialStr.equals("com13")){
            port = 13 ;
        }else if(serialStr.equals("com14")){
            port = 14 ;
        }


        if(powerStr.equals("3.3v")){
            power = HFReader.POWER_3_3V ;
        }else if(powerStr.equals("5v")){
            power = HFReader.POWER_5V ;
        }else if(powerStr.equals("scan power")){
            power = HFReader.POWER_SCAN ;
        }else if(powerStr.equals("rfid power")){
            power = HFReader.POWER_RFID ;
        }else if(powerStr.equals("psam power")){
            power = HFReader.POWER_PSAM ;
        }
        hfReader = new HFReader(port, 115200, power) ;
    }

    /**
     * add activity
     * @param activity
     */
    public void addActivity(Activity activity){
        if(!listAc.contains(activity)){
            listAc.add(activity) ;
        }

    }

    /**
     * kill all activity
     */
    public void exit(){
        if (hfReader != null) {
            hfReader.close(port);
            hfReader = null ;
        }
        try {
            for(Activity ac : listAc){
                if (ac != null)
                    ac.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }

    }
}
