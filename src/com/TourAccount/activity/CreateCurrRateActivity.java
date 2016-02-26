package com.TourAccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.TourAccount.R;
import com.TourAccount.adapter.CurrRateDataAdapter;
import com.TourAccount.adapter.CurrencySpinnerAdapter;
import com.TourAccount.model.CurrRate;
import com.TourAccount.model.Currency;
import com.TourAccount.model.Tour;
import com.TourAccount.sqlite.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 22.09.14
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class CreateCurrRateActivity extends Activity implements
        AdapterView.OnItemSelectedListener {

    // Database Helper
    DatabaseHandler db;

    private TextView tour_view;

    private Tour tour;

    CurrencySpinnerAdapter spinnerCurAdapter;
    CurrRateDataAdapter currRateDataAdapter;

    Spinner spinner_curr1;
    Spinner spinner_curr2;

    int tour_id;

    String type_id;

    GridView g;

    private static final String LOG = "CreateCurrRateActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_curr_rate);
        db = new DatabaseHandler(getApplicationContext());

        //получить ид тура
        tour_id = getIntent().getIntExtra("tour_id", 0);
        type_id = getIntent().getStringExtra("type_id");

        tour = db.getTour(tour_id);


        tour_view = (TextView) findViewById(R.id.textV_tourInfo);
        tour_view.setText("Tур: " + tour.getName());


        //Заполнить спиннеры  (оба для валют)
        spinner_curr1 = (Spinner) findViewById(R.id.spinner_cur1);
        loadSpinnerCurrData(spinner_curr1);
        spinner_curr2 = (Spinner) findViewById(R.id.spinner_cur2);
        loadSpinnerCurrData(spinner_curr2);

        g = (GridView) findViewById(R.id.gridView_currRates);
        currRateDataAdapter = new CurrRateDataAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, db.getTourCurrRates(tour_id));

        db.closeDB();

        g.setAdapter(currRateDataAdapter);
        g.setOnItemSelectedListener(this);


        g.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub

            }
        });


        final Button btn_FinishCurRates = (Button) findViewById(R.id.btn_FinishCurRates);
        btn_FinishCurRates.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CreateCurrRateActivity.this, MainActivity.class);
                startActivity(intent);// Запускаем новую Активность.
                finish();// Завершить текущую активность.

            }
        });

        final Button but_addCurrRate = (Button) findViewById(R.id.btn_SaveCurRates);
        but_addCurrRate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db = new DatabaseHandler(getApplicationContext());

                CurrRate currRate = new CurrRate();
                currRate.setTour_id(tour_id);
                Currency currency1 = (Currency) spinner_curr1.getSelectedItem();
                currRate.setCurr1_id(currency1.getId());
                Currency currency2 = (Currency) spinner_curr2.getSelectedItem();
                currRate.setCurr2_id(currency2.getId());
                EditText editText_val1 = (EditText) findViewById(R.id.editT_curval1);
                EditText editText_val2 = (EditText) findViewById(R.id.editT_curval2);
                currRate.setVal1(Float.parseFloat(editText_val1.getText().toString()));
                currRate.setVal2(Float.parseFloat(editText_val2.getText().toString()));

                int curr_id = db.createCurrRate(currRate);
                Log.e(LOG, "inserted currRate_id = " + curr_id);

                db.closeDB();

                finish();
                startActivity(getIntent());
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //To change body of implemented methods use File | Settings | File Templates.
        tour_view.setText("Tур: " + tour.getName());

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //To change body of implemented methods use File | Settings | File Templates.
        tour_view.setText("Tур: " + tour.getName());
    }

    private void loadSpinnerCurrData(Spinner spinner) {

        db = new DatabaseHandler(getApplicationContext());
        spinnerCurAdapter = new CurrencySpinnerAdapter(getApplicationContext(), db.getAllCurrencies(/*tour_id*/));
        db.closeDB();
        spinner.setAdapter(spinnerCurAdapter);
    }
}
