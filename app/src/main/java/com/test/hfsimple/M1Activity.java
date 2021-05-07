package com.test.hfsimple;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pda.hf.HFReader;

/**
 * Created by admin on 2017/4/1.
 */
public class M1Activity extends MyBaseActivity implements View.OnClickListener{

    private EditText editUID ;
    private EditText editSector ;
    private EditText editBlock ;
    private EditText editPass ;
    private RadioButton rbKeyA ;
    private RadioButton rbKeyB ;
    private EditText editRead ;
    private EditText editWrite ;
    private Button btnSearch ;
    private Button btnAuth ;
    private Button btnRead ;
    private Button btnWrite ;

    private MyApplication mapp ;
    private HFReader hfReader ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m1);
        setTitle(getResources().getString(R.string.m1));
        setContext(this);
        mapp = (MyApplication) getApplication() ;
        hfReader = mapp.getHfReader() ;
        Util.initSoundPool(this);
        initView();
    }

    private void initView() {
        editUID = (EditText) findViewById(R.id.editTextUid);
        editSector = (EditText) findViewById(R.id.editTextSector);
        editBlock = (EditText) findViewById(R.id.editTextBlock);
        editPass = (EditText) findViewById(R.id.editTextPassword);
        rbKeyA = (RadioButton) findViewById(R.id.radioButtonpassword_a);
        rbKeyB = (RadioButton) findViewById(R.id.radioButtonpassword_b);
        editRead = (EditText) findViewById(R.id.editTextReadData);
        editWrite = (EditText) findViewById(R.id.editTextWriteData);
        btnSearch = (Button) findViewById(R.id.buttonFind14443A);
        btnAuth = (Button) findViewById(R.id.buttonAuth);
        btnRead = (Button) findViewById(R.id.buttonRead14443);
        btnWrite = (Button) findViewById(R.id.buttonWrite14443);

        btnSearch.setOnClickListener(this);
        btnAuth.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnWrite.setOnClickListener(this);

        rbKeyA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                keyType = HFReader.AUTH_A ;
            }
        });

        rbKeyB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                keyType = HFReader.AUTH_B ;
            }
        });

    }


    byte[] uid = null ;
    int sector = 0 ;
    int block = 0 ;
    byte[] keys = null ;
    int keyType = HFReader.AUTH_A ;
    boolean authFlag = false ;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonFind14443A:
                search() ;
                break ;
            case R.id.buttonAuth:
                auth() ;
                break ;
            case R.id.buttonRead14443:
                read();
                break ;
            case R.id.buttonWrite14443:
                write() ;
                break ;
        }
    }

    //search card
    private void search(){
        if (hfReader != null) {
            uid = hfReader.search14443Acard() ;
            if (uid != null) {
                editUID.setText(Tools.Bytes2HexString(uid, uid.length));
                Util.play(1, 0);
            }else{
                toastMsg(getResources().getString(R.string.no_card_search));
            }
        }else{
            toastMsg(getResources().getString(R.string.device_exception));
        }
    }

    //auth
    private void auth(){
        String sectorStr = editSector.getText().toString() ;
        sector = Integer.valueOf(sectorStr) ;
        String keysStr = editPass.getText().toString() ;
        //keys length 6 bytes
        if(keysStr == null || keysStr.length() != 12){
            toastMsg(getResources().getString(R.string.please_input_right_key));
            return ;
        }
        keys = Tools.HexString2Bytes(keysStr);
        if (hfReader != null ) {
            if(uid != null){
                authFlag =  hfReader.authM1(keyType, sector * 4, keys, uid);
                if (authFlag) {
                    toastMsg(getResources().getString(R.string.auth_success));
                    Util.play(1, 0);
                }else{
                    toastMsg(getResources().getString(R.string.auth_fail));
                }
            }else{
                toastMsg(getResources().getString(R.string.please_search_card));
            }

        }else{
            toastMsg(getResources().getString(R.string.device_exception));
        }
    }

    //read block data
    private void read() {
        byte[] readData = null ;
        String sectorStr = editSector.getText().toString() ;
        sector = Integer.valueOf(sectorStr) ;
        String blockStr = editBlock.getText().toString() ;
        block = Integer.valueOf(blockStr) ;
        block = sector*4 + block ;
        if (hfReader != null ) {
            if (authFlag) {
                readData = hfReader.readM1Block(block) ;
                if (readData != null) {
                    editRead.setText(Tools.Bytes2HexString(readData, readData.length));
                    Util.play(1, 0);
                }else{
                    toastMsg(getResources().getString(R.string.read_fail));
                }
            }else{
                toastMsg(getResources().getString(R.string.please_auth));
            }
        }else{
            toastMsg(getResources().getString(R.string.device_exception));
        }

    }

    //write block data
    private void write() {
        byte[] writeData = null ;
        String sectorStr = editSector.getText().toString() ;
        sector = Integer.valueOf(sectorStr) ;
        String blockStr = editBlock.getText().toString() ;
        block = Integer.valueOf(blockStr) ;
        block = sector*4 + block ;
        String wDataStr = editWrite.getText().toString() ;
        //1 block data length 16bytes
        if (wDataStr == null || wDataStr.length() != 32) {
            toastMsg(getResources().getString(R.string.please_16_byte));
            return ;
        }
        writeData = Tools.HexString2Bytes(wDataStr);
        boolean wFlag  = false  ;
        if (hfReader != null ) {
            if (authFlag) {
                wFlag = hfReader.writeM1Block(block, writeData) ;
                if (wFlag) {
                    toastMsg(getResources().getString(R.string.write_success));
                    Util.play(1, 0);
                }else{
                    toastMsg(getResources().getString(R.string.write_fail));
                }
            }else{
                toastMsg(getResources().getString(R.string.please_auth));
            }
        }else{
            toastMsg(getResources().getString(R.string.device_exception));
        }

    }

}
