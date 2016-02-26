package com.TourAccount.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.TourAccount.model.Currency;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 20.09.14
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */
public class CurrencySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    Context mContext;
    private List<Currency> currencies;
    // Logcat tag
    private static final String LOG = "CurrencySpinnerAdapter";

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    // Конструктор
    public CurrencySpinnerAdapter(Context context, List<Currency> currencies) {
        //  super(context, textViewResourceId, currencies);

        this.currencies = currencies;

        Log.e(LOG, "this.currencies.size=" + this.currencies.size());
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return currencies.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Currency getItem(int i) {
        return currencies.get(i);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int i) {
        return currencies.get(i).getId();  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

       /* TextView label = (TextView) convertView;

        if (convertView == null) {
            convertView = new TextView(mContext);
            label = (TextView) convertView;
        }
        label.setText(currencies.get(position).toString());
        return (convertView); */

        TextView text = new TextView(mContext);
        text.setTextColor(Color.BLACK);
        text.setText(currencies.get(position).toString());
        Log.e(LOG, "getView=" + currencies.get(position).toString());
        return text;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView text = new TextView(mContext);
        text.setTextColor(Color.BLACK);
        text.setText(currencies.get(position).toString());
        Log.e(LOG, "getDropDownView=" + currencies.get(position).toString());
        return text;
    }

    // возвращает содержимое выделенного элемента списка

    public String GetItem(int position) {
        return currencies.get(position).toString();
    }
}
