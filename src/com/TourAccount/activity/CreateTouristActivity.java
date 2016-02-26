package com.TourAccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.TourAccount.R;
import com.TourAccount.adapter.AllTouristDataAdapter;
import com.TourAccount.adapter.TouristSpinnerAdapter;
import com.TourAccount.model.Tourist;
import com.TourAccount.sqlite.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 25.09.14
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class CreateTouristActivity extends Activity {

    // Database Helper
    DatabaseHandler db;

    ListView listView_Tourists;
    AllTouristDataAdapter allTouristDataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tourist);

        final int tour_id = getIntent().getIntExtra("tour_id", 0);

        db = new DatabaseHandler(getApplicationContext());


        listView_Tourists = (ListView) findViewById(R.id.listView_Tourists);
        //listView_Tourists
        allTouristDataAdapter = new AllTouristDataAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, db.getTourTourists(tour_id));

        listView_Tourists.setAdapter(allTouristDataAdapter);


        db.closeDB();

        final Button btn_AddTourist = (Button) findViewById(R.id.btn_AddTourist);
        btn_AddTourist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // new label input field


                EditText editT_touristName = (EditText) findViewById(R.id.editT_touristName);
                EditText editT_touristDescr = (EditText) findViewById(R.id.editT_touristDescr);

                db = new DatabaseHandler(getApplicationContext());

                if (editT_touristName.getText() == null ||
                        editT_touristName.getText().length() == 0) {

                    return;
                }


                Tourist tourist1 = new Tourist();
                tourist1.setDescr(editT_touristDescr.getText().toString());
                tourist1.setTourist_name(editT_touristName.getText().toString());
                tourist1.setTour_id(tour_id);

                db.createTourist(tourist1);

                db.closeDB();

                finish();
                startActivity(getIntent());
            }
        });

        final Button btn_FinishAddTourist = (Button) findViewById(R.id.btn_FinishAddTourist);
        btn_FinishAddTourist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(CreateTouristActivity.this, MainActivity.class);
                startActivity(i);// Запускаем новую Активность.
                finish();// Завершить текущую активность.

            }
        });

    }
}
