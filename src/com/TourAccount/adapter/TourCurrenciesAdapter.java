package com.TourAccount.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.TourAccount.model.Currency;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 20.09.14
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */
public class TourCurrenciesAdapter extends ArrayAdapter<Currency> {

    Context mContext;
    private List<Currency> currencies;

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    // Конструктор
    public TourCurrenciesAdapter(Context context, int textViewResourceId, List<Currency> currencies) {
        super(context, textViewResourceId, currencies);
        this.currencies = currencies;
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
        label.setText(currencies.get(position).toString());
        return (convertView);
    }

    // возвращает содержимое выделенного элемента списка
    public String GetItem(int position) {
        return currencies.get(position).toString();
    }
}
