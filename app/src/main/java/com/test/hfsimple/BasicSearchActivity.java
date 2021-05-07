package com.test.hfsimple;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.pda.hf.HFReader;
import com.pda.hf.ISO15693CardInfo;
import com.test.hfsimple.adapter.MyBaseAdapter;
import com.test.hfsimple.entity.Card;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicSearchActivity extends MyBaseActivity implements View.OnClickListener {

    private ListView lv;
    private Button btnStart;
    private Button btnClear;
    private TextView tvTips;

    private MyApplication mapp;
    private HFReader hfReader;

    private Set<String> uidSet  = null;
    private HashMap<String , Integer> uidLation = null;
    private List<Card> listUid = null;

    private MyBaseAdapter adapter;
    private ParaSave para;

    private final int MSG_CARD = 1101;
    private int index = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CARD:
                    String uid = msg.getData().getString("uid");
                    String cardType = msg.getData().getString("cardType");
//                    Util.play(1, 0 );
                    mSoundPoolMgr.play(1);
                    Card card;
                    if (uidSet == null) {
                        //first add
                        uidSet = new HashSet<>();
                        uidLation = new HashMap<>();
                        listUid = new ArrayList<>();
                        uidSet.add(uid);
                        index = 0;
                        uidLation.put(uid, index);
                        card = new Card();
                        card.count = 1;
                        card.cardUid = uid;
                        card.cardType = cardType;
                        listUid.add(card);
                    }else{
                        if (uidSet.contains(uid)) {
                            int location = uidLation.get(uid);
//                            card = listUid.get(location);
                            card = new Card();
                            int mCount = listUid.get(location).count;
                            card.count = mCount + 1;
                            card.cardUid = uid;
                            card.cardType = cardType;
                            listUid.set(location, card);
                        }else{
                            uidSet.add(uid);
                            index++;
                            uidLation.put(uid, index);
                            card = new Card();
                            card.count = 1;
                            card.cardUid = uid;
                            card.cardType = cardType;
                            listUid.add(card);
                        }
                    }
                    //update listview
                    updataListView();
                    break;
            }
        }
    };
    private SoundPoolMgr mSoundPoolMgr;


    private void updataListView(){
        adapter = new MyBaseAdapter(this, listUid);
        lv.setAdapter(adapter);
//        if (adapter == null) {
//            adapter = new MyBaseAdapter(this, listUid);
//            lv.setAdapter(adapter);
//
//        }else{
//            adapter.notifyDataSetChanged();
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_search);
        setTitle(this.getResources().getString(R.string.basic_search));
        setContext(this);
        mapp = (MyApplication) getApplication();
        hfReader = mapp.getHfReader();
//        Util.initSoundPool(this);
        mSoundPoolMgr = SoundPoolMgr.getInstance(this);
        initView();
    }



    public double div(double a, double b){
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.listView_uid);
        btnStart = (Button) findViewById(R.id.button_start_search);
        btnClear = (Button) findViewById(R.id.button_clear);
        tvTips = (TextView) findViewById(R.id.textView_para);
        btnStart.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        para = new ParaSave(this);
        tvTips.setText("port "+ para.getSerial() + ";buadrate = " + 115200 + "; power = " + para.getPower());

        if(!running) {
            running = true;
            new Thread(readTask).start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        running = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundPoolMgr.release();
        mSoundPoolMgr = null;
    }

    boolean running = false;
    boolean isStart = false;
    //read thread
    private Runnable readTask = new Runnable() {
        byte[] uid14443 =  null;
        List<ISO15693CardInfo> listCard15693 = null;
        byte[] uid15693 = null;
        byte[] uidZhCNid = new byte[16];
        @Override
        public void run() {
            while(running){
                if(isStart && hfReader != null){

                    //zh-CN ID card
                    int ret ;
//                    int ret = hfReader.getidCard(uidZhCNid);
//                    if (ret > 0) {
//                        sendMSG(Tools.Bytes2HexString(uidZhCNid, ret), "zh-CN ID", MSG_CARD);
////                        sendMSG(new String(uidZhCNid, 0, ret, StandardCharsets.US_ASCII), "zh-CN ID", MSG_CARD);
//                    }
                    //14443A
                    uid14443 = hfReader.search14443Acard();
                    if (uid14443 != null) {
                        sendMSG(Tools.Bytes2HexString(uid14443, uid14443.length), "14443A", MSG_CARD);
//                        sendMSG(new String(uid14443, 0, uid14443.length, StandardCharsets.US_ASCII), "14443A", MSG_CARD);
                    }else{
                        //15693
                        listCard15693 = hfReader.search15693Card();
                        if (listCard15693 != null && !listCard15693.isEmpty()) {
                            for (ISO15693CardInfo card : listCard15693) {
                                uid15693 = card.getUid();
                                sendMSG(Tools.Bytes2HexString(uid15693, uid15693.length), "15693", MSG_CARD);
//                                sendMSG(new String(uid15693, 0, uid15693.length, StandardCharsets.US_ASCII), "15693", MSG_CARD);
                            }
                        }else{

                            //zh-CN ID card
                            ret = hfReader.getidCard(uidZhCNid);
                            if (ret > 0) {
                                sendMSG(Tools.Bytes2HexString(uidZhCNid, ret), "zh-CN ID", MSG_CARD);
//                                sendMSG(new String(uidZhCNid, 0, ret, StandardCharsets.US_ASCII), "zh-CN ID", MSG_CARD);
                            }
                        }
                    }
                }
            }
        }
    };

    //send the result to handler
    private void sendMSG(String cardUid, String cardType, int msgCode) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", cardUid);
        bundle.putString("cardType", cardType);
        Message msg = new Message();
        msg.setData(bundle);
        msg.what = msgCode;
        handler.sendMessage(msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_search:
                if (!isStart) {
                    isStart = true;
                    btnStart.setText(getResources().getString(R.string.stop_search_card));
                }else{
                    isStart = false;
                    btnStart.setText(getResources().getString(R.string.start_search_card));
                }
                break;
            case R.id.button_clear:
                uidSet = null;
                lv.setAdapter(null);
                break;
        }
    }
}
