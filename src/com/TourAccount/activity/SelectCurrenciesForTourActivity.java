package com.TourAccount.activity;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.SparseBooleanArray;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.ListView;

import android.widget.Toast;
import com.TourAccount.R;
import com.TourAccount.adapter.CurrencyDataAdapter;
import com.TourAccount.model.CurrRate;
import com.TourAccount.model.Currency;
import com.TourAccount.sqlite.DatabaseHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SelectCurrenciesForTourActivity extends Activity {


    ListView myList;

    Button getChoice;


    /**
     * Called when the activity is first created.
     */

    DatabaseHandler db;


    @Override

    public void onCreate(Bundle savedInstanceState) {

        final int tour_id = getIntent().getIntExtra("tour_id", 0);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_currencies);

        myList = (ListView) findViewById(R.id.list);

        getChoice = (Button) findViewById(R.id.getchoice);


        db = new DatabaseHandler(getApplicationContext());
        CurrencyDataAdapter currencyDataAdapter
                = new CurrencyDataAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice, db.getAllCurrencies());
        currencyDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        db.closeDB();

        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        myList.setAdapter(currencyDataAdapter);


        getChoice.setOnClickListener(new Button.OnClickListener() {


            @Override

            public void onClick(View v) {

                // TODO Auto-generated method stub

                String selected = "";


                int cntChoice = myList.getCount();

                SparseBooleanArray sparseBooleanArray = myList.getCheckedItemPositions();
                ArrayList<Integer> selectedId = new ArrayList<Integer>();

                for (int i = 0; i < cntChoice; i++) {

                    if (sparseBooleanArray.get(i)) {

                        selected += myList.getItemAtPosition(i).toString() + "\n";

                        Currency currency = (Currency) myList.getItemAtPosition(i);

                        selectedId.add(currency.getId());

                    }

                }

                if (selectedId.size() == 0) {

                    Context context = getApplicationContext();
                    CharSequence text = "Необходимо выбрать хотя бы одну валюту";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }

                ArrayList<Integer> currRatesIds = new ArrayList<Integer>();
                db = new DatabaseHandler(getApplicationContext());
                for (CurrRate currRate : CurrRate.GenerateCurrenciesPairs(selectedId)) {
                    currRate.setTour_id(tour_id);
                    currRate.setVal1(1f);
                    currRate.setVal2(1f);
                    int currRateId = db.createCurrRate(currRate);
                    currRatesIds.add(currRateId);
                }
                db.closeDB();

                //Передать список в следующее окно
                if (selectedId.size() == 1) {
                    //textV_selectedCurr
                    Intent intent = new Intent(SelectCurrenciesForTourActivity.this, CreateTouristActivity.class);
                    intent.putExtra("tour_id", tour_id);

                    startActivity(intent);// Запускаем новую Активность.

                    finish();
                } else {
                    //textV_selectedCurr
                    Intent intent = new Intent(SelectCurrenciesForTourActivity.this, SetCurrRatesActivity.class);
                    intent.putExtra("selected_currs", selected);
                    intent.putExtra("tour_id", tour_id);
                    intent.putIntegerArrayListExtra("currRatesIds", currRatesIds);

                    startActivity(intent);// Запускаем новую Активность.

                    finish();
                }

              /*  Toast.makeText(SelectCurrenciesForTourActivity.this,

                        selected,

                        Toast.LENGTH_LONG).show();       */

            }
        });


    }

}