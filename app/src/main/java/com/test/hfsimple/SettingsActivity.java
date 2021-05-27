package com.test.hfsimple;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pda.hf.HFReader;

/**
 * Created by admin on 2017/4/7.
 */
public class SettingsActivity extends MyBaseActivity {

    private Spinner spinnerSerial ;
    private Spinner spinnerPower ;
    private Button btnSet ;
    private TextView tvTips ;
    private EditText viewSymbol ;

    private TextView tvAndroid ;
    private EditText editRegister ;


    private MyApplication mapp ;
    private HFReader hfReader ;

    private int powerInt ;
    private int serialInt ;
    String power ;
    String symbol;

    String serial;

    private String[] powers = new String[]{"3.3v", "5v", "scan power", "rfid power", "psam power"} ;
    private String[] serials = new String[]{"com0", "com2", "com3", "com11", "com12", "com13", "com14"} ;
    private ParaSave para ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getResources().getString(R.string.settings));
        setContext(this);
        mapp = (MyApplication) getApplication() ;
        hfReader = mapp.getHfReader() ;
        para = new ParaSave(this);
        initView();
    }

    private void initView(){
        spinnerSerial = (Spinner) findViewById(R.id.spinnerSerialport) ;
        spinnerPower = (Spinner) findViewById(R.id.spinnerPower) ;
        btnSet = (Button) findViewById(R.id.buttonSettings) ;
        tvTips = (TextView) findViewById(R.id.textSettingTips) ;
        viewSymbol= (EditText) findViewById(R.id.textViewSymbol) ;
        tvAndroid = (TextView) findViewById(R.id.textViewAndroid) ;
        editRegister= (EditText) findViewById(R.id.editTextRegister) ;
        spinnerPower.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, powers));
        spinnerSerial.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, serials));
        serial = para.getSerial() ;
        power = para.getPower() ;
        symbol=para.getSymbol();

        tvAndroid.setText(para.getAndroidId());
        editRegister.setText(para.getRegister());

        if(serial.equals("com0")){
            spinnerSerial.setSelection(0);
            serialInt = 0 ;
        }else if(serial.equals("com2")){
            spinnerSerial.setSelection(1);
            serialInt = 2 ;
        }else if(serial.equals("com3")){
            spinnerSerial.setSelection(2);
            serialInt = 3 ;
        }else if(serial.equals("com11")){
            spinnerSerial.setSelection(3);
            serialInt = 11 ;
        }else if(serial.equals("com12")){
            spinnerSerial.setSelection(4);
            serialInt = 12 ;
        }else if(serial.equals("com13")){
            spinnerSerial.setSelection(5);
            serialInt = 13 ;
        }else if(serial.equals("com14")){
            spinnerSerial.setSelection(6);
            serialInt = 14 ;
        }


        if(power.equals("3.3v")){
            spinnerPower.setSelection(0);
            powerInt = HFReader.POWER_3_3V ;
        }else if(power.equals("5v")){
            spinnerPower.setSelection(1);
            powerInt = HFReader.POWER_5V ;
        }else if(power.equals("scan power")){
            spinnerPower.setSelection(2);
            powerInt = HFReader.POWER_SCAN ;
        }else if(power.equals("rfid power")){
            spinnerPower.setSelection(3);
            powerInt = HFReader.POWER_RFID ;
        }else if(power.equals("psam power")){
            spinnerPower.setSelection(4);
            powerInt = HFReader.POWER_PSAM ;
        }
        viewSymbol.setText(symbol);

        spinnerSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                serial = serials[i] ;
                switch (i) {
                    case 0://com0
                        serialInt = 0 ;

                        break ;
                    case 1://com2
                        serialInt = 2 ;
                        break ;
                    case 2://com3
                        serialInt = 3 ;
                        break ;
                    case 3://com11
                        serialInt = 11 ;
                        break ;
                    case 4://com12
                        serialInt = 12 ;
                        break ;
                    case 5://com13
                        serialInt = 13 ;
                        break ;
                    case 6://com14
                        serialInt = 14 ;
                        break ;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                power = powers[i] ;
                switch (i){
                    case 0://3.3V
                        powerInt = HFReader.POWER_3_3V ;
                        break ;
                    case 1://5V
                        powerInt = HFReader.POWER_5V ;
                        break ;
                    case 2://SCAN POWER
                        powerInt = HFReader.POWER_SCAN ;
                        break ;
                    case 3://RFID POWER
                        powerInt = HFReader.POWER_RFID;
                        break ;
                    case 4://PSAM POWER
                        powerInt = HFReader.POWER_PSAM ;
                        break ;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hfReader = new HFReader(serialInt, 115200, powerInt);
                mapp.setHfReader(hfReader);
                para.savePower(power);
                para.saveSerial(serial);
                para.saveSymbol(viewSymbol.getText().toString());
                para.saveRegister(editRegister.getText().toString());
                toastMsg(getResources().getString(R.string.set_success));
            }
        });
    }
}
