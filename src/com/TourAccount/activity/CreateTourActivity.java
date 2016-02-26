package com.TourAccount.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.TourAccount.R;
import com.TourAccount.adapter.CurrencyDataAdapter;
import com.TourAccount.model.Tour;
import com.TourAccount.model.TourEnum;
import com.TourAccount.sqlite.DatabaseHandler;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 19.09.14
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
public class CreateTourActivity extends Activity {

    // Database Helper
    DatabaseHandler db;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    ParsePosition parsePosition = new ParsePosition(0);

    int tour_id;

    String type_id;

    CurrencyDataAdapter currencyDataAdapter;

    Spinner spinnerSelCur;

    private static final String LOG = "CreateTourActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tour);
        EditText editText_Cnt = (EditText) findViewById(R.id.editText_Cnt);
        editText_Cnt.setText("1");

        type_id = getIntent().getStringExtra("type_id");


        final Button save_btn = (Button) findViewById(R.id.but_NextStep);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // new label input field
                EditText editText_Name = (EditText) findViewById(R.id.editText_Name);
                EditText editText_Cnt = (EditText) findViewById(R.id.editText_Cnt);
                DatePicker datePick_dateBeg = (DatePicker) findViewById(R.id.datePick_dateBeg);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //  Date date = datePick_dateBeg.
                // ContentValues initialValues = new ContentValues();
                // initialValues.put("date_created", dateFormat.format(date));
                if (editText_Cnt.getText().length() == 0) {
                    Context context = getApplicationContext();
                    CharSequence text = "Необходимо задать количество людей в туре";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }

                if (editText_Name.getText().length() == 0) {
                    Context context = getApplicationContext();
                    CharSequence text = "Необходимо задать название тура";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }


                Tour tour = new Tour(editText_Name.getText().toString(), Integer.parseInt(editText_Cnt.getText().toString()),
                                   dateFormat.parse(
                                           datePick_dateBeg.getYear() + "-" +
                                                   (datePick_dateBeg.getMonth() + 1) +
                                                   "-" + datePick_dateBeg.getDayOfMonth(), parsePosition)
                                // + " 00:00:00"


                );

                db = new DatabaseHandler(getApplicationContext());
                int tour_id = db.createTour(tour);
                db.closeDB();


                // Intent i = new Intent(CreateTourActivity.this, CreateCurrencyActivity.class);
                Intent i = new Intent(CreateTourActivity.this, SelectCurrenciesForTourActivity.class);
                i.putExtra("tour_id", tour_id);
                startActivity(i);
                finish();  // Завершить текущую активность.
            }
        });


        if (TourEnum.EditITEM.EDIT_ITEM.toString().equals(type_id)) {


            // Принимаем ид тура
            tour_id = getIntent().getIntExtra("tour_id", 0);

            db = new DatabaseHandler(getApplicationContext());

            Tour tour = db.getTour(tour_id);
            db.closeDB();


            Log.e(LOG, "tour_id =" + tour_id);
            Log.e(LOG, "type_id =" + type_id);
            Log.e(LOG, "tour =" + tour);
            Log.e(LOG, "tour.cnt =" + tour.getTourist_cnt());

            editText_Cnt = (EditText) findViewById(R.id.editText_Cnt);
            editText_Cnt.setText(String.valueOf(tour.getTourist_cnt()));

            EditText editText_Name = (EditText) findViewById(R.id.editText_Name);
            editText_Name.setText(tour.getName());

            DatePicker datePick_dateBeg = (DatePicker) findViewById(R.id.datePick_dateBeg);


            Date date = null;
            try {
                date = tour.getDate_begin();//dateFormat.parse(tour.getDate_begin());
                Log.e(LOG, "EDIT_ITEM tour.getDate_begin() " + tour.getDate_begin());
                Log.e(LOG, "EDIT_ITEM tour.getDate_end()" + tour.getDate_end());
                Log.e(LOG, "EDIT_ITEM dateFormat.parse date: " + date);
                //date.getTime()
                Log.e(LOG, "EDIT_ITEM dateFormat.parse datePick_dateBeg : " + "'" + date.getYear() + "-" +
                        (date.getMonth() + 1) +
                        "-" + date.getDay() +
                        " 00:00:00'");
                datePick_dateBeg.init(date.getYear() + 1900, date.getMonth(), date.getDay(),
                        new DatePicker.OnDateChangedListener() {
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar c = Calendar.getInstance();
                                c.set(year, monthOfYear, dayOfMonth);
                                //System.out.println("TEST");
                            }
                        }
                );
                Log.e(LOG, "EDIT_ITEM dateFormat.parse datePick_dateBeg : " + "'" + datePick_dateBeg.getYear() + "-" +
                        String.format("%2s", datePick_dateBeg.getMonth() + 1).replace(" ", "0") +
                        "-" + String.format("%2s", datePick_dateBeg.getDayOfMonth()).replace(" ", "0") +
                        " 00:00:00'");
                /*datePick_dateBeg.updateDate(date.getYear(), date.getMonth() + 1, date.getDay());
                Log.e(LOG, "EDIT_ITEM dateFormat.parse datePick_dateBeg: " + "'" + datePick_dateBeg.getYear() + "-" +
                        String.format("%2s", datePick_dateBeg.getMonth()+1).replace(" ","0") +
                        "-" + String.format("%2s", datePick_dateBeg.getDayOfMonth()).replace(" ","0") +
                        " 00:00:00'");
                                            */


            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Log.e(LOG, "dateFormat exception: " + e.getMessage());
            }

            save_btn.setVisibility(View.GONE);
            final Button but_Back = (Button) findViewById(R.id.but_Back);
            but_Back.setVisibility(View.GONE);
            //editTour_save


            final Button editTour_save = (Button) findViewById(R.id.editTour_save);
            editTour_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    // new label input field
                    EditText editText_Name = (EditText) findViewById(R.id.editText_Name);
                    EditText editText_Cnt = (EditText) findViewById(R.id.editText_Cnt);
                    DatePicker datePick_dateBeg = (DatePicker) findViewById(R.id.datePick_dateBeg);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    if (editText_Cnt.getText().length() == 0) {
                        Context context = getApplicationContext();
                        CharSequence text = "Необходимо задать количество людей в туре";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return;
                    }

                    if (editText_Name.getText().length() == 0) {
                        Context context = getApplicationContext();
                        CharSequence text = "Необходимо задать название тура";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return;
                    }



                  /*  = new Tour(editText_Name.getText().toString(), Integer.parseInt(editText_Cnt.getText().toString()),
                            "'" + datePick_dateBeg.getYear() + "-" +
                                    String.format("%2s", datePick_dateBeg.getMonth()+1).replace(" ","0") +
                                    "-" + String.format("%2s", datePick_dateBeg.getDayOfMonth()).replace(" ","0") +
                                    " 00:00:00'"
                    );   */


                    Tour tour = db.getTour(tour_id);
                    tour.setDate_begin(/*datePick_dateBeg.getYear() + "-" +
                            String.format("%2s", datePick_dateBeg.getMonth()+1).replace(" ","0") +
                            "-" + String.format("%2s", datePick_dateBeg.getDayOfMonth()).replace(" ","0") +
                            " 00:00:00"*/
                            dateFormat.parse(
                            datePick_dateBeg.getYear() + "-" +
                                    (datePick_dateBeg.getMonth() + 1) +
                                    "-" + datePick_dateBeg.getDayOfMonth(), parsePosition)
                                   // + " 00:00:00"
                    );
                    tour.setTourist_cnt(Integer.parseInt(editText_Cnt.getText().toString()));
                    tour.setName(editText_Name.getText().toString());

                    db = new DatabaseHandler(getApplicationContext());
                    // int tour_id = db.createTour(tour);
                    db.updateTour(tour);
                    db.closeDB();

                    Log.e(LOG, "tour.getDate_begin() " + tour.getDate_begin());
                    Log.e(LOG, "tour.getDate_end()" + tour.getDate_end());

                    Log.e(LOG, "dateFormat.parse: " + "'" + datePick_dateBeg.getYear() + "-" +
                            String.format("%2s", datePick_dateBeg.getMonth() + 1).replace(" ", "0") +
                            "-" + String.format("%2s", datePick_dateBeg.getDayOfMonth()).replace(" ", "0") +
                            " 00:00:00'");
                  /*  // Intent i = new Intent(CreateTourActivity.this, CreateCurrencyActivity.class);
                    Intent i = new Intent(CreateTourActivity.this, SelectCurrenciesForTourActivity.class);
                    i.putExtra("tour_id", tour_id);
                    startActivity(i);       */
                    finish();  // Завершить текущую активность.
                }
            });

        } else {
            final Button editTour_save = (Button) findViewById(R.id.editTour_save);
            editTour_save.setVisibility(View.GONE);
            //editTour_save
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
