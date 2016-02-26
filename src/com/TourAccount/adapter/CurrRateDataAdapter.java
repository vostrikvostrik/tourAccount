package com.TourAccount.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.TourAccount.model.CurrRate;
import com.TourAccount.model.Tour;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 22.09.14
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class CurrRateDataAdapter extends ArrayAdapter<CurrRate> {

    Context mContext;
    private List<CurrRate> currRates;

    public List<CurrRate> getCurrRates() {
        return currRates;
    }

    public void setCurrRates(List<CurrRate> currRates) {
        this.currRates = currRates;
    }

    // Конструктор
    public CurrRateDataAdapter(Context context, int textViewResourceId, List<CurrRate> currRates) {
        super(context, textViewResourceId, currRates);
        this.currRates = currRates;
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        TextView label = (TextView) convertView;

        if (convertView == null) {
            convertView = new TextView(mContext);
            label = (TextView) convertView;
        }
        label.setText(currRates.get(position).toString());
        return (convertView);
    }

    // возвращает содержимое выделенного элемента списка
    public String GetItem(int position) {
        return currRates.get(position).toString();
    }
}

