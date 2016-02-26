package com.TourAccount.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.TourAccount.model.Tourist;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 25.09.14
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class TouristSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    Context mContext;
    private List<Tourist> tourists;
    // Logcat tag
    private static final String LOG = "TouristSpinnerAdapter";

    public List<Tourist> getTourists() {
        return tourists;
    }

    public void setTourists(List<Tourist> tourists) {
        this.tourists = tourists;
    }

    // Конструктор
    public TouristSpinnerAdapter(Context context, List<Tourist> tourists) {
        //  super(context, textViewResourceId, currencies);
        this.tourists = tourists;
        Log.e(LOG, "this.tourists.size=" + this.tourists.size());
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return tourists.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tourist getItem(int i) {
        return tourists.get(i);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int i) {
        return tourists.get(i).getTourist_id();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        TextView text = new TextView(mContext);
        text.setTextColor(Color.BLACK);
        text.setText(tourists.get(position).toString());
        Log.e(LOG, "getView=" + tourists.get(position).toString());
        return text;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView text = new TextView(mContext);
        text.setTextColor(Color.BLACK);
        text.setText(tourists.get(position).toString());
        Log.e(LOG, "getDropDownView=" + tourists.get(position).toString());
        return text;
    }

    // возвращает содержимое выделенного элемента списка

    public String GetItem(int position) {
        return tourists.get(position).toString();
    }
}
