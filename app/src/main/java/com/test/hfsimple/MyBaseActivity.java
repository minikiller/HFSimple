package com.test.hfsimple;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/1/13.
 */
public class MyBaseActivity extends Activity {

    private TextView tvTitle ;
    private Button btnOperation ;

    private PopupWindow popupWindow ;
    private ListView lv ;
    private String[] arrayItems ;
    private Context context ;

    private MyApplication mapp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        tvTitle = (TextView) findViewById(R.id.textView_title);
        btnOperation = (Button) findViewById(R.id.button_operation);

        btnOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPopupwindow();
            }
        });
    }

    private void addPopupwindow() {
        if (popupWindow == null) {
//            arrayItems = context.getResources().getStringArray(R.array.operations);
//            arrayItems = new String[] {"adfasdf", "adfas", "adfas", "adfas", "adfas", "adfas"} ;
            String basic = context.getResources().getString(R.string.basic_search) ;
            String m1 = context.getResources().getString(R.string.m1) ;
            String iso15693 = context.getResources().getString(R.string.iso15693) ;
            String settings = context.getResources().getString(R.string.settings) ;
            String about = context.getResources().getString(R.string.about_soft);
            String exit = context.getResources().getString(R.string.exit);
            arrayItems = new String[] {basic, m1,iso15693,settings , about, exit} ;
            View view = LayoutInflater.from(MyBaseActivity.this).inflate(R.layout.item_list_popupwindow, null) ;
            lv = (ListView) view.findViewById(R.id.listView_popup) ;
            lv.setAdapter(new ArrayAdapter<String>(MyBaseActivity.this,  android.R.layout.simple_list_item_1,arrayItems));
            popupWindow = new PopupWindow(view, 260, 500);
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(btnOperation, 10, 4);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null ;
                Activity activity = (Activity) context ;
                switch (position) {
                    case 0://to basic search card
                        intent = new Intent(activity, BasicSearchActivity.class);
                        break ;
                    case 1://to m1
                        intent = new Intent(activity, M1Activity.class);
                        break ;
                    case 2://to 15693
                        intent = new Intent(activity, Iso15693Activity.class);
                        break ;
                    case 3://to settings
                        intent = new Intent(activity, SettingsActivity.class);
                        break ;
                    case 4://about soft
                        createDialog() ;
                        return ;
                    case 5://exit soft
                        mapp.exit();
                        return ;
                }

                activity.startActivity(intent);
                popupWindow.dismiss();
            }
        });

    }

    /**
     * create dialog for view about soft information
     */
    private void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        //version code + finish date
        String info = getResources().getString(R.string.current_version) + getVersion() + "\n"
                + getResources().getString(R.string.finish_date) + "2021-05-01"  ;
        builder.setTitle(getResources().getString(R.string.about_soft)) ;
        builder.setMessage(info) ;
        builder.setNegativeButton(getResources().getString(R.string.cancle), null) ;
        builder.create().show();


    }

    /**
     * get current version
     * @return
     */
    public String getVersion() {
             try {
                    PackageManager manager = this.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                    String version = info.versionName;
                 return "" + version;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
         }


    private String getCurrentDate(){
        String dateStr = ""  ;
        String reg = "yyyy-MM-dd" ;
        Date date = new Date()  ;
        SimpleDateFormat sf = new SimpleDateFormat(reg) ;
        dateStr = sf.format(date) ;
        return dateStr ;
    }


    private long startTime = 0L;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - startTime > 2000){
                startTime = System.currentTimeMillis() ;
                toastMsg(getResources().getString(R.string.please_press_again_exit));
                return false;
            }
            mapp.exit();

        }
        return super.onKeyDown(keyCode, event);
    }

    public void setContext(Context context){
        this.context = context ;
        mapp = (MyApplication) getApplication() ;
        mapp.addActivity((Activity) context);
    }

    /**
     * set title
     * @param text
     */
    public void setTitle(String text) {
        tvTitle.setText(text);
    }

    /****
     * show toast Message
     * @param msg
     */
    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
