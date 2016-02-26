package com.TourAccount.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.TourAccount.R;
import com.TourAccount.activity.CreateNewItemActivity;
import com.TourAccount.model.Currency;
import com.TourAccount.model.TourEnum;
import com.TourAccount.model.TourItem;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 20.09.14
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
public class EditTourDataAdapter extends ArrayAdapter<TourItem> {

    Context mContext;
    private Map<Integer, Currency> currencies;
    private int[] colors = new int[]{0x30DD0000, 0x20000000};
    private static final Integer LIST_ITEM = 1;

    private List<TourItem> tourItems;

    Activity activity;

    public List<TourItem> getTourItems() {
        return tourItems;
    }

    public void setTourItems(List<TourItem> tourItems) {
        this.tourItems = tourItems;
    }

    // Конструктор
    public EditTourDataAdapter(Context context, int textViewResourceId,
                               List<TourItem> tourItems, Map<Integer, Currency> currencies, Activity activity) {
        super(context, textViewResourceId, tourItems);
        this.tourItems = tourItems;
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.currencies = currencies;
        this.activity = activity;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        int colorPos = position % colors.length;
        // TextView label = (TextView) convertView;
        View item = convertView;
        int height = parent.getHeight();
        final int pos = position;
        Currency currency = currencies.get(tourItems.get(position).getCurr_id());

        if (convertView == null) {
            //convertView = new TextView(mContext);
            //label = (TextView) convertView;
            item = LayoutInflater.from(mContext).inflate(
                    R.layout.itemview_layout, parent, false);
            item.setTag(LIST_ITEM);
            item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(mContext.getApplicationContext(), CreateNewItemActivity.class);
                    intent.putExtra("tour_id", tourItems.get(pos).getTour_id());
                    intent.putExtra("type_id", TourEnum.EditITEM.EDIT_ITEM);
                    intent.putExtra("item_id", tourItems.get(pos).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.finish();
                    mContext.startActivity(intent);// Запускаем новую Активность.
                }
            });
        }




        View imgBtn = (View) item.findViewById(R.id.btnViewTourItem);
        imgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(animal.wikipedia_url));
                //  context.startActivity(browserIntent);

                // Toast toast = Toast.makeText(mContext,"TEST = " + tourItems.get(pos).getTour_descr() , 2);
                // toast.show();

                Intent intent = new Intent(mContext.getApplicationContext(), CreateNewItemActivity.class);
                intent.putExtra("tour_id", tourItems.get(pos).getTour_id());
                intent.putExtra("type_id", TourEnum.EditITEM.EDIT_ITEM);
                intent.putExtra("item_id", tourItems.get(pos).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.finish();
                mContext.startActivity(intent);// Запускаем новую Активность.
                // label.setText(tourItems.get(position).toString() + " " + currency.getWord_code());


            }
        });

        TextView header = (TextView) item.findViewById(R.id.lv_item_header);
        header.setText(tourItems.get(position).getTour_descr());

        TextView subtext = (TextView) item.findViewById(R.id.lv_item_subtext);
        //subtext.setText(tours.get(position).toString());
        subtext.setText(tourItems.get(position).toString() + " " + currency.getWord_code());

        return (item);
    }

    // возвращает содержимое выделенного элемента списка
    public String GetItem(int position) {
        Currency currency = currencies.get(tourItems.get(position).getCurr_id());
        return tourItems.get(position).toString() + " " + currency.getWord_code() + " Чел: " + tourItems.get(position).getTourist_id();
    }


}
