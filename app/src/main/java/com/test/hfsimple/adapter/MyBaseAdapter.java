package com.test.hfsimple.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.hfsimple.R;
import com.test.hfsimple.entity.Card;

import java.util.List;

/**
 * Created by admin on 2017/3/28.
 */
public class MyBaseAdapter extends BaseAdapter {

    private Context context ;
    private List<Card> list ;
    public MyBaseAdapter(Context context, List<Card> list){
        this.context = context ;
        this.list = list ;
    }
    @Override
    public int getCount() {
        if (list != null) {
            return list.size() ;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (list != null) {
            list.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null ;
        if (view == null) {
            holder = new ViewHolder() ;
            view = LayoutInflater.from(context).inflate(R.layout.item_list_uid, null);
            holder.tvCount = (TextView) view.findViewById(R.id.item_count) ;
            holder.tvSN = (TextView) view.findViewById(R.id.item_sn) ;
            holder.tvUid = (TextView) view.findViewById(R.id.item_uid) ;
            holder.tvTypte = (TextView) view.findViewById(R.id.item_card_type) ;
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag() ;
        }
        if (list != null && !list.isEmpty()) {
            Card card = list.get(i);
            int sn = i + 1 ;
            holder.tvSN.setText("" +  sn);
            holder.tvUid.setText("" +  card.cardUid);
            holder.tvTypte.setText("" +  card.cardType);
            holder.tvCount.setText("" +  card.count);

        }
        return view;
    }

    class ViewHolder {
        TextView tvSN ;
        TextView tvUid ;
        TextView tvTypte ;
        TextView tvCount ;
    }
}
