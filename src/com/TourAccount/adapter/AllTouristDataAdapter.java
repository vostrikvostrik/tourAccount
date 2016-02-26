package com.TourAccount.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.TourAccount.model.Tourist;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 26.09.14
 * Time: 10:03
 * To change this template use File | Settings | File Templates.
 */
public class AllTouristDataAdapter extends ArrayAdapter<Tourist> {
    Context mContext;
    private List<Tourist> tourists;

    private static final String LOG = "AllTouristDataAdapter";

    public List<Tourist> getTourists() {
        return tourists;
    }

    public void setTourists(List<Tourist> tourists) {
        this.tourists = tourists;
    }

    // Конструктор
    public AllTouristDataAdapter(Context context, int textViewResourceId, List<Tourist> tourists) {
        super(context, textViewResourceId, tourists);
        this.tourists = tourists;
        // TODO Auto-generated constructor stub
        this.mContext = context;
        Log.e(LOG, "AllTouristDataAdapter constructor");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        TextView label = (TextView) convertView;

        if (convertView == null) {
            convertView = new TextView(mContext);
            label = (TextView) convertView;
        }
        label.setText(tourists.get(position).toString());
        return (convertView);
    }

    // возвращает содержимое выделенного элемента списка
    public String GetItem(int position) {

        return tourists.get(position).toString();
    }

}
