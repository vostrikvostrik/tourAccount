package com.TourAccount.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.TourAccount.R;
import com.TourAccount.model.CurrRate;
import com.TourAccount.model.Currency;
import com.TourAccount.model.TourEnum;
import com.TourAccount.model.Tourist;
import com.TourAccount.sqlite.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 24.09.14
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
public class SetCurrRatesActivity extends Activity {
    // Database Helper
    DatabaseHandler db;

    List<CurrRate> currRateList;

    String type_id;
    int tour_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_curr_rates);

        tour_id = getIntent().getIntExtra("tour_id", 0);
        type_id = getIntent().getStringExtra("type_id");


        if (TourEnum.EditITEM.EDIT_ITEM.toString().equals(type_id)) {

            //для переданных валют сделать нужное количество строк:
            TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);

            db = new DatabaseHandler(getApplicationContext());

            currRateList = db.getTourCurrRates(tour_id);
            int index = 1;
            for (CurrRate currRate : currRateList) {
                Currency currency1 = db.getCurrency(currRate.getCurr1_id());
                Currency currency2 = db.getCurrency(currRate.getCurr2_id());
                addRow(currency1.getName(), currency2.getName(), index, currRate, currRate.getVal1(), currRate.getVal2());
                index++;
            }
        } else {

            // Принимаем ид тура
            final String selected_currs = getIntent().getStringExtra("selected_currs");
            ArrayList<Integer> selectedIds = getIntent().getIntegerArrayListExtra("currRatesIds");

            TextView textV_selectedCurr = (TextView) findViewById(R.id.textV_selectedCurr);
            textV_selectedCurr.setText(selected_currs);


            //для переданных валют сделать нужное количество строк:
            TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);

            db = new DatabaseHandler(getApplicationContext());

            currRateList = new ArrayList<CurrRate>();

            int index = 1;
            for (int i = 0; i < selectedIds.size(); i++) {

                CurrRate currRate = db.getCurrRate(selectedIds.get(i));
                currRateList.add(currRate);
                Currency currency1 = db.getCurrency(currRate.getCurr1_id());
                Currency currency2 = db.getCurrency(currRate.getCurr2_id());
                addRow(currency1.getName(), currency2.getName(), index, currRate, 1, 1);
                index++;
            }
        }
        Button btn_save = (Button) findViewById(R.id.btn_saveCurrRates);
        btn_save.setText("Далее");
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = new DatabaseHandler(getApplicationContext());


                if (!TourEnum.EditITEM.EDIT_ITEM.toString().equals(type_id)) {
                    //Добавить организатора тура
                    Tourist tourist = new Tourist();
                    tourist.setTour_id(tour_id);
                    tourist.setTourist_name("Огранизтор");
                    tourist.setDescr("я");
                    db.createTourist(tourist);
                    db.closeDB();

                    Intent i = new Intent(SetCurrRatesActivity.this, CreateTouristActivity.class);
                    i.putExtra("tour_id", tour_id);
                    startActivity(i);  // Запускаем новую Активность.
                } else {
                    Intent i = new Intent(SetCurrRatesActivity.this, EditTourActivity.class);
                    i.putExtra("tour_id", tour_id);
                    startActivity(i);  // Запускаем новую Активность.
                }
                finish();  // Завершить текущую активность.


            }
        });

        db.closeDB();
    }

    public void addRow(String cell0, String cell1, int index, CurrRate currRate, float val1, float val2) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow tr = (TableRow) inflater.inflate(R.layout.curr_rate, null);

        TextView tv = (TextView) tr.getChildAt(0);
        tv.setText(cell0);
        tv = (TextView) tr.getChildAt(1);
        tv.setText(cell1);
        EditText editT_val1 = (EditText) tr.getChildAt(2);// editT_val1
        editT_val1.setText(String.valueOf(val1));
        editT_val1.addTextChangedListener(new EditTextWatcher(editT_val1, currRate, 0));
        EditText editT_val2 = (EditText) tr.getChildAt(3);// editT_val1
        editT_val2.setText(String.valueOf(val2));
        editT_val2.addTextChangedListener(new EditTextWatcher(editT_val2, currRate, 1));

        tableLayout.addView(tr, index);
    }


    private class EditTextWatcher implements TextWatcher {

        EditText v;
        CurrRate currRate;
        int type;

        public EditTextWatcher(EditText view, CurrRate currRate, int type) {
            this.v = view;
            this.currRate = currRate;
            this.type = type;
        }

        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                db = new DatabaseHandler(getApplicationContext());
                if (type == 0)
                    this.currRate.setVal1(Float.parseFloat(s.toString()));
                else
                    this.currRate.setVal2(Float.parseFloat(s.toString()));
                db.updateCurrRate(this.currRate);
                db.closeDB();
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {


        }

    }

}
