package com.TourAccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import com.TourAccount.R;
import com.TourAccount.adapter.TourCurrenciesAdapter;
import com.TourAccount.model.Currency;
import com.TourAccount.sqlite.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 20.09.14
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public class CreateCurrencyActivity extends Activity implements
        AdapterView.OnItemSelectedListener {
    // Database Helper
    DatabaseHandler db;

    private TourCurrenciesAdapter tourCurrenciesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_curr);

        //получить ид тура
        final int tour_id = getIntent().getIntExtra("tour_id", 0);

        db = new DatabaseHandler(getApplicationContext());

        final GridView g = (GridView) findViewById(R.id.gridView_currencies);
        tourCurrenciesAdapter = new TourCurrenciesAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, db.getAllCurrencies(/*tour_id*/));
        g.setAdapter(tourCurrenciesAdapter);
        g.setOnItemSelectedListener(this);
        g.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub

            }
        });
        db.closeDB();

        final Button but_addCurr = (Button) findViewById(R.id.but_addCur);
        but_addCurr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // new label input field
                EditText editText_currName = (EditText) findViewById(R.id.editText_currName);
                EditText editText_currCode = (EditText) findViewById(R.id.editText_currCode);
                //  EditText editText_currRate = (EditText) findViewById(R.id.editText_currRate);
                EditText editText_currWord = (EditText) findViewById(R.id.editText_currWord);

                db = new DatabaseHandler(getApplicationContext());

                Currency currency = new Currency();
                currency.setWord_code(editText_currWord.getText().toString());
                // currency.setCurr_rate(Float.parseFloat(editText_currRate.getText().toString()));
                currency.setDigit_code(Integer.parseInt(editText_currCode.getText().toString()));
                currency.setName(editText_currName.getText().toString());
                // currency.setTour_id(tour_id);
                db.createCurr(currency);

                db.closeDB();

                finish();
                startActivity(getIntent());
            }
        });

        final Button but_Finish = (Button) findViewById(R.id.but_Finish);
        but_Finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(CreateCurrencyActivity.this, MainActivity.class);
                startActivity(i);// Запускаем новую Активность.
                finish();// Завершить текущую активность.

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
