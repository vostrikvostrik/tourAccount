package com.TourAccount.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.TourAccount.R;
import com.TourAccount.adapter.TouristSpinnerAdapter;
import com.TourAccount.model.ItemTourist;
import com.TourAccount.model.TourItem;
import com.TourAccount.sqlite.DatabaseHandler;

import java.util.List;

/**
 * User: User
 * Date: 29.06.15
 * Time: 14:23
 */
public class AddItemTouristActivity extends Activity {

    int tour_id;
    int item_id;
    DatabaseHandler db;
    TourItem tourItem;
    Spinner spinTourist;
    TouristSpinnerAdapter touristSpinnerAdapter;
    private static final String LOG = "AddItemTouristActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_tourists);
        db = new DatabaseHandler(getApplicationContext());

        // Принимаем ид тура
        tour_id = getIntent().getIntExtra("tour_id", 0);
        /*Toast toast = Toast.makeText(getApplicationContext(), "TOUR ID = " +
                tour_id, Toast.LENGTH_SHORT);
        toast.show();*/
        Log.e(LOG, "tour_id=" + tour_id);

        item_id = getIntent().getIntExtra("tour_item_id", 0);
        tourItem = db.getTourItem(item_id);
        /*toast = Toast.makeText(getApplicationContext(), "ITEM ID = " +
                item_id, Toast.LENGTH_SHORT);
        toast.show();*/
        Log.e(LOG, "item_id=" + item_id);
        //
        spinTourist = (Spinner) findViewById(R.id.spinTourist);
        loadSpinnerTouristData(tour_id);

        final Button btn_AddTourist = (Button) findViewById(R.id.btn_AddTourist);
        final Button btn_FinishAddTourist = (Button) findViewById(R.id.btn_FinishAddTourist);

        final TextView itemTourists = (TextView) findViewById(R.id.itemTourists);
        List<ItemTourist> itemTouristsList = db.getAllItemTourist(item_id);

        itemTourists.setText(itemTouristsList.toString());


        final List<String> touristIds= db.getAllItemTouristsId(item_id);
        //считаем сумму для каждого участника (при добавлении нового на оставшихся нужно пересчитать остаток??)
        final float newAmount=tourItem.getCurr_amount()/(touristIds.size() + 1);
        final TextView textView_sumTourist = (TextView) findViewById(R.id.textView_sumTourist);
        textView_sumTourist.setText("Сумма: " + newAmount);

        btn_AddTourist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ItemTourist itemTourist = new ItemTourist();
                itemTourist.setTourItem(item_id);
                Long tourist_id = spinTourist.getSelectedItemId();
                itemTourist.setTouristId(tourist_id.intValue());
                itemTourist.setCurrAmount(tourItem.getCurr_id());
                //Осталось высчитать сумму
                //
                itemTourist.setTouristAmount(newAmount);

                db = new DatabaseHandler(getApplicationContext());
                db.createItemTourist(itemTourist);
                //проапдейтить суммы у тех, кто уже есть в списке
                db.updateItemTourist(touristIds, item_id, newAmount);
                db.closeDB();

                finish();
                startActivity(getIntent());
            }
        });


        btn_FinishAddTourist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();// Завершить текущую активность.

            }
        });


        db.closeDB();
    }

    private void loadSpinnerTouristData(int tour_id) {

        db = new DatabaseHandler(getApplicationContext());
        touristSpinnerAdapter = new TouristSpinnerAdapter(getApplicationContext(), db.getTourTourists(tour_id));
        db.closeDB();
        spinTourist.setAdapter(touristSpinnerAdapter);
    }

}
