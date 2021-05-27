package com.test.hfsimple;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pda.hf.HFReader;
import com.pda.hf.ISO15693CardInfo;
import com.pda.hf.ISO15693PICC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */
public class Iso15693Activity extends MyBaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

//    private EditText editCardCount;
    private EditText editWrite;
    private EditText editTips;
    private boolean isRegister=false;
//    private TextView tvBlocks;
//    private TextView tvSingleBlockLen;
//    private Spinner spinnerSelectCard;
    //    private Spinner spinnerBlock;
    private Button btnWrite;
//    private Button btnSearch;
//    private Button btnGetCardInfo;
    private Button btnRead;
    private Button btnClear;

    private MyApplication mapp;
    private HFReader hfReader;

    private byte[] uid = null;
    private int flag;
    private int selectBlock = 0;
    private ParaSave para;
    private List<ISO15693CardInfo> list15693 = null;
    private List<String> listUid = null;
    private ISO15693PICC picc = null;
    private String androidId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iso15693);
        setTitle(getResources().getString(R.string.iso15693));
        setContext(this);
        mapp = (MyApplication) getApplication();
        hfReader = mapp.getHfReader();
        Util.initSoundPool(this);
        para = new ParaSave(this);
        initView();
    }

    private void initView() {
        editTips = (EditText) findViewById(R.id.editTextInfo);
//        tvBlocks = (TextView) findViewById(R.id.editTextBlocks);
//        tvSingleBlockLen = (TextView) findViewById(R.id.editTextSingleBlockLen);
//        editCardCount = (EditText) findViewById(R.id.editTextFindCardCount);
        editWrite = (EditText) findViewById(R.id.editTextWriteData15693);
//        spinnerBlock = (Spinner) findViewById(R.id.spinnerBlock);
//        spinnerSelectCard = (Spinner) findViewById(R.id.spinnerSelectCard);
        btnClear = (Button) findViewById(R.id.buttonClear15693);
//        btnSearch = (Button) findViewById(R.id.buttonFind15693);
//        btnGetCardInfo = (Button) findViewById(R.id.buttonGetCardInfo);
        btnRead = (Button) findViewById(R.id.buttonRead15693);
        btnWrite = (Button) findViewById(R.id.buttonWriteData15693);

        btnClear.setOnClickListener(this);
//        btnSearch.setOnClickListener(this);
//        btnGetCardInfo.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnWrite.setOnClickListener(this);
//        spinnerSelectCard.setOnItemSelectedListener(this);
//        spinnerBlock.setOnItemSelectedListener(this);
        //sunlf added
        btnRead.setFocusable(true);
        btnRead.requestFocus();
        btnRead.setFocusableInTouchMode(true);


        androidId = String
                .format("%16s", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID))
                .replace(' ', '0');
//        String reg=Register.encrypt(androidId);
        String reg=para.getRegister();
//        editWrite.setText(androidId+":"+reg);
        para.saveAndroidId(androidId);
//        para.saveRegister(reg);
        String valid=Register.decrypt(reg);
        if(androidId.equals((valid))){
            isRegister=true;
            editTips.append("版本已注册！ \n\n");
            btnRead.setEnabled(true);
            btnWrite.setEnabled(true);
        }else{
            isRegister=false;
            editTips.append("版本未注册,请尽快注册！ \n\n");
            editTips.append("安卓ID: "+androidId);
            btnRead.setEnabled(false);
            btnWrite.setEnabled(false);
        }

//        getResources().getString(R.string.please_input_right_data);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()) {
//            case R.id.spinnerSelectCard:
//                uid = list15693.get(i).getUid();
//                flag = list15693.get(i).getFlags();
//                break;
//            case R.id.spinnerBlock:
//                selectBlock = i;
//                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.buttonFind15693:
//                search();
//                break;
//            case R.id.buttonGetCardInfo:
//                getCardInfo();
//                break;
            case R.id.buttonRead15693:
                read();
                break;
            case R.id.buttonWriteData15693:
                write();
                break;
            case R.id.buttonClear15693:
                editTips.setText("");
                break;

        }
    }

    //search card
    private void search() {
        list15693 = hfReader.search15693Card();
        if (list15693 != null && !list15693.isEmpty()) {
            Util.play(1, 0);
//            editCardCount.setText("" + list15693.size());
//            listUid = new ArrayList<>();
            for (ISO15693CardInfo info : list15693) {
                uid = info.getUid();
//                listUid.add(Tools.Bytes2HexString(uid, uid.length));
//                editTips.append("UID:0x" + Tools.Bytes2HexString(uid, uid.length) + "\n");
//                editTips.append("FLAGS: " + info.getFlags() + "\n");
//                editTips.append("DSFID: " + info.getDsfid() + "\n\n");
            }
//            spinnerSelectCard.setAdapter(new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_dropdown_item, listUid));

//            spinnerSelectCard.setSelection(0);
//            spinnerSelectCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    uid = list15693.get(i).getUid();
//                    flag = list15693.get(i).getFlags();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
            getCardInfo();
        } else {
            toastMsg(getResources().getString(R.string.search_card_fail));
        }
    }

    private void getCardInfo() {
        if (uid == null) {
            toastMsg(getResources().getString(R.string.please_search_card));
            return;
        }
        picc = hfReader.get15693PICC(uid, flag);
        if (picc != null) {
//            Util.play(1, 0);
            List<String> list = new ArrayList<>();
            int blockCount = picc.getBlockCount(); //区块数目
//            tvBlocks.setText("" + blockCount);
//            tvSingleBlockLen.setText("" + (picc.getBlockLen()));

//            editTips.append("UID:0x" + Tools.Bytes2HexString(picc.getUid(), picc.getUid().length) + "\n");
//            editTips.append("FLAGS: " + picc.getFlag() + "\n");
//            editTips.append("DSFID: " + picc.getDsfid() + "\n");
//            editTips.append("AFI: " + picc.getAfi() + "\n");
//            editTips.append("ICReference: " + picc.getICReference() + "\n");
//            editTips.append("InfoFlag: " + picc.getInfoFlag() + "\n\n");
//            for (int i = 0; i < blockCount; i++) {
//                list.add("" + i);
//            }
//            spinnerBlock.setAdapter(new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_dropdown_item, list));
//            spinnerBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    selectBlock = i;
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
        }
    }

    //read block data
    private void read() {
        search();
        if (uid == null) {
            toastMsg(getResources().getString(R.string.please_search_card));
            return;
        }
        selectBlock = 0;
        byte[] readData = hfReader.read15693Block(uid, flag, selectBlock);
        if (readData != null) {
            boolean isend = true;
            byte[] zero = new byte[]{0x00,0x00,0x00,0x00};
            while (isend && selectBlock < 25) {
                selectBlock++;
                byte[] data = hfReader.read15693Block(uid, flag, selectBlock);
                if (Arrays.equals(data,zero)) {
                    isend = false;
                }
                readData = Tools.concat(readData, data);
//                toastMsg("debug: " + Tools.decodeUTF8(readData) + "\n");
            }
        }


        if (readData != null) {
            Util.play(1, 0);
//            editTips.append("UID:0x" + Tools.Bytes2HexString(uid, uid.length) + "\n");
//            editTips.append("Read Hex data:0x" + Tools.Bytes2HexString(readData, readData.length) + "\n");
            String value=Tools.decodeUTF8(readData);
            editTips.append("Data:" + value + "\n\n");
            editWrite.setText(value);
        }else{
            toastMsg(getResources().getString(R.string.please_search_card));
            return;
        }

    }

    //write block data
    private void write() {
        search();
        selectBlock = 0;
        if (uid == null) {
            toastMsg(getResources().getString(R.string.please_search_card));
            return;
        }
        byte[] wData = null;
        String wStr = editWrite.getText().toString();
        //sunlf commented
//        if(wStr == null || wStr.length() != 8){
//            toastMsg(getResources().getString(R.string.please_input_right_data));
//            return;
//        }
//        wData = Tools.HexString2Bytes(wStr);

        if (wStr == null) {
            toastMsg(getResources().getString(R.string.please_input_right_data));
            return;
        }

        boolean wFlag = false;
        //清空25block开始的内容
        int curBlock = 0;
        byte[] writeData = new byte[4];
//        toastMsg(String.valueOf(writeData));
        while (curBlock < 25) {
            wFlag = hfReader.write15693Block(uid, flag, curBlock, writeData);
            curBlock++;
            if (!wFlag) {
                Util.play(1, 0);
                toastMsg("clear data failed！");
                return;
            }
        }

//        String symbol = para.getSymbol();
//        String str = Tools.String2Hex(wStr) + symbol;
        String str = Tools.String2Hex(wStr);
//        toastMsg(str);
        List<String> strings = Tools.getStrList(str, 8);
        for (String string : strings) {
            if (string.length() < 8) {
                string = string + "00000000";
                string = string.substring(0, 8);
            }
            wData = Tools.HexString2Bytes(string);
//            toastMsg(string);
            wFlag = hfReader.write15693Block(uid, flag, selectBlock, wData);
            selectBlock++;
            if (!wFlag) {
                toastMsg(getResources().getString(R.string.write_fail));
                break;
            }
        }

        // for 2K tag
//        boolean wFlag = hfReader.write2K15693Block(selectBlock, wData, wData.length, new byte[8]);
        if (wFlag) {
            Util.play(1, 0);
            toastMsg(getResources().getString(R.string.write_success));
        }
    }

}
