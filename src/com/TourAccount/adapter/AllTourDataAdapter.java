package com.TourAccount.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.TourAccount.R;
import com.TourAccount.activity.EditTourActivity;
import com.TourAccount.model.Tour;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 19.09.14
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */
public class AllTourDataAdapter extends ArrayAdapter<Tour> {

    Context mContext;
    private List<Tour> tours;
    private String[] colors = {"#009688", "#CFD8DC"};
    public static final int HDR_POS1 = 0;
    public static final int HDR_POS2 = 5;
    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;


    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    // Конструктор
    public AllTourDataAdapter(Context context, int textViewResourceId, List<Tour> tours) {
        super(context, textViewResourceId, tours);
        this.tours = tours;
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        int colorPos = position % colors.length;


        //TextView label = (TextView) convertView;

        View item = convertView;

        if (convertView == null) {
            // convertView = new TextView(mContext);

            item = LayoutInflater.from(mContext).inflate(
                    R.layout.lv_layout, parent, false);

            item.setTag(LIST_ITEM);

            item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //   textView_selected.setText("Выбранный элемент: "
                    //           + mAdapter.GetItem(position));

                    Intent intent = new Intent(mContext, EditTourActivity.class);
                    // указываем первым параметром ключ, а второе значение
                    // по ключу мы будем получать значение с Intent
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tour_id", tours.get(position).getId());
                    getContext().startActivity(intent);// Запускаем новую Активность.
                }

            });
        }

        TextView header = (TextView) item.findViewById(R.id.lv_item_header);
        header.setText(tours.get(position).getName());

        TextView subtext = (TextView) item.findViewById(R.id.lv_item_subtext);
        subtext.setText(tours.get(position).toString());

        //Set last divider in a sublist invisible
        View divider = item.findViewById(R.id.item_separator);
        if (position == HDR_POS2 - 1) {
            divider.setVisibility(View.INVISIBLE);
        }

        View imgBtn = item.findViewById(R.id.btnViewTourItems);

        imgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //   textView_selected.setText("Выбранный элемент: "
                //           + mAdapter.GetItem(position));

                Intent intent = new Intent(mContext, EditTourActivity.class);
                // указываем первым параметром ключ, а второе значение
                // по ключу мы будем получать значение с Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("tour_id", tours.get(position).getId());
                getContext().startActivity(intent);// Запускаем новую Активность.
            }

        });


        /*label = (TextView) convertView;
        label.setText(tours.get(position).toString());
        label.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        label.setBackgroundColor(Color.parseColor(colors[colorPos]));
        label.setTextColor(Color.BLACK);*/
        return (item);
    }

    // возвращает содержимое выделенного элемента списка
    public String GetItem(int position) {

        return tours.get(position).toString();
    }

}