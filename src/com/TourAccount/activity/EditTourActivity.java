package com.TourAccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.TourAccount.R;
import com.TourAccount.activity.plot.ModelPlotActivity;
import com.TourAccount.adapter.CurrRateDataAdapter;
import com.TourAccount.adapter.EditTourDataAdapter;
import com.TourAccount.model.Currency;
import com.TourAccount.model.Tour;
import com.TourAccount.model.TourEnum;
import com.TourAccount.model.TourItem;
import com.TourAccount.sqlite.DatabaseHandler;
import com.TourAccount.services.impl.CalculateImpl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 20.09.14
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class EditTourActivity extends Activity implements
        AdapterView.OnItemSelectedListener {

    // Database Helper
    DatabaseHandler db;

    Tour tour;

    CalculateImpl calculate;

    private int curr_id;

    int tour_id;

    List<TourItem> items;

    Map<Integer, Currency> currencies;

    String address = "vostrikovaen@gmail.com", subject = "Отчет по туру", emailtext = " тур";

    private EditTourDataAdapter editTourDataAdapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private CurrRateDataAdapter currRateDataAdapter;

    private static final String LOG = "EditTourActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_tour);

        // Принимаем ид тура
        tour_id = getIntent().getIntExtra("tour_id", 0);

        db = new DatabaseHandler(getApplicationContext());
        calculate = new CalculateImpl();
        calculate.DataBaseHandler(getApplicationContext());

        currencies = db.getTourCurrenciesInfo(tour_id);
        items = db.getAllTourItems(tour_id);

        //final TourItemsGridView g = (TourItemsGridView) findViewById(R.id.gridView_tourItems);
        final GridView g = (GridView) findViewById(R.id.gridView_tourItems);
        editTourDataAdapter = new EditTourDataAdapter(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, items, currencies, this);
        // g.setMinimumHeight(items.size() * 20);
        Log.e(LOG, "this.getParent() = " + this);
        //  Log.e(LOG, "this.getParent() = " + this);
        g.setAdapter(editTourDataAdapter);


        g.setOnItemSelectedListener(this);
        //add view for new item row
        TextView label = (TextView) findViewById(R.id.textView_tourName);
        tour = db.getTour(tour_id);
        label.setText("Тур: " + tour.getName() +
                (tour.getDate_begin()!=null ? "\nДата начала: " + dateFormat.format(tour.getDate_begin()): "")
                + "\n Кол-во участников: " + tour.getTourist_cnt());

        //add view for new item row
        TextView textItog = (TextView) findViewById(R.id.textItog);

        //получить все Йтемы ,все суммы по ним
        //сначала учесть все расходы
        Log.e(LOG, "расходы\n");
        Map<Integer, Float> sumMap = db.getTourAllSum(tour_id, TourEnum.TourItemType.OUTGOING.value);
        String sum = "";
        Float outgoing = 0F, incoming = 0F;


        sum += "\n";
        sum += "Итого расходов: \n" + calculate.makeResSum(calculate.GetSum(sumMap, tour_id));
        Log.e(LOG, "\nприходы\n");
        sumMap = db.getTourAllSum(tour_id, TourEnum.TourItemType.INCOMING.value);


        sum += "\n";
        sum += "Итого приход: " + calculate.makeResSum(calculate.GetSum(sumMap, tour_id));

        Log.e(LOG, "\nобщий итог\n");
        sumMap = db.getTourAllSum(tour_id, TourEnum.TourItemType.ALL.value);


        sum += "\n";
        sum += "Итого: " + calculate.makeResSum(calculate.GetSum(sumMap, tour_id));

        textItog.setText(sum);

        try {
            db.closeDB();
        } catch (Exception ex) {
            Log.e(LOG, "ex = " + ex.getMessage());
        }

        calculate.CloseDataBase();

        g.setOnItemSelectedListener(this);
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub

            }
        });


        final ImageButton totalTourist = (ImageButton) findViewById(R.id.btnViewTourist);
        totalTourist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(EditTourActivity.this, TouristTotalActivity.class);
                intent.putExtra("tour_id", tour_id);
                startActivity(intent);// Запускаем новую Активность.

            }
        });

        final Button btn_getPlot = (Button) findViewById(R.id.btn_getPlot);
        btn_getPlot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(EditTourActivity.this, ModelPlotActivity.class);
                intent.putExtra("tour_id", tour_id);
                intent.putExtra("plot_type", 1);
                intent.putExtra("curr_id", curr_id);

                startActivity(intent);// Запускаем новую Активность.


                finish();// Завершить текущую активность.

                //  startActivity(getIntent());
            }
        });

        final Button button1 = (Button) findViewById(R.id.btn_newTourItem);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(EditTourActivity.this, CreateNewItemActivity.class);
                intent.putExtra("tour_id", tour_id);
                intent.putExtra("type_id", TourEnum.EditITEM.CREATE_ITEM);

                startActivity(intent);// Запускаем новую Активность.


                finish();// Завершить текущую активность.

                //   startActivity(getIntent());

            }
        });

        final Button btn_sendMailTour = (Button) findViewById(R.id.btn_sendMailTour);
        btn_sendMailTour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                // Кому
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{address});
                // Зачем
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        subject);

                emailtext = "\n";

                db = new DatabaseHandler(getApplicationContext());


                for (TourItem item : items) {
                    Currency currency = currencies.get(item.getCurr_id());
                    emailtext += "\n " + item.toString() + " " + currency.getWord_code();
                }

                /*Map<Integer, Float> sumMap = db.getTourAllSum(tour_id);

                emailtext += "\n";

                //получить итоговую сумму по курсам
                for (Integer curr : sumMap.keySet()) {
                    float sumInCur = 0F;
                    Currency currency = db.getCurrency(curr);

                    Log.e(LOG, "Считаем сумму в " + currency.getName());
                    for (Integer innerCur : sumMap.keySet()) {
                        Currency innerCurrency = db.getCurrency(innerCur);
                        //
                        if (curr != innerCur) {
                            //Есть еще другая функция ConvertCurrency
                            // float res = db.CurrConvert(tour_id, innerCur, sumMap.get(innerCur), curr);
                            float res = db.ConvertCurrency(tour_id, innerCur, sumMap.get(innerCur), curr);
                            sumInCur += res;

                            Log.e(LOG, sumMap.get(innerCur) + " " + innerCurrency.getName() + " = " + res + " " + currency.getName());

                        } else {
                            sumInCur += sumMap.get(innerCur);
                            // Log.e(LOG, sumMap.get(innerCur) + " " + currency.getName() + " = " + sumMap.get(curr) + " " + innerCurrency.getName());
                        }
                    }

                    emailtext += " Итого в " + currency.getName() + " = " + sumInCur + "\n";
                }*/

                Map<Integer, Float> sumMap = db.getTourAllSum(tour_id, TourEnum.TourItemType.OUTGOING.value);
                //String sum = "";
                //Float outgoing=0F, incoming=0F;
              /*  for (Integer curr : sumMap.keySet()) {
                    Currency currency = db.getCurrency(curr);
                    // sum += currency.getName() + " = " + sumMap.get(curr);
                }*/

                emailtext += "\n";
                emailtext += "Итого расходов: " + calculate.makeResSum(calculate.GetSum(sumMap, tour_id));

                sumMap = db.getTourAllSum(tour_id, TourEnum.TourItemType.INCOMING.value);

               /* for (Integer curr : sumMap.keySet()) {
                    Currency currency = db.getCurrency(curr);
                    // sum += currency.getName() + " = " + sumMap.get(curr);
                }*/

                emailtext += "\n";
                emailtext += "Итого приход: " + calculate.makeResSum(calculate.GetSum(sumMap, tour_id));

                sumMap = db.getTourAllSum(tour_id, TourEnum.TourItemType.ALL.value);


                emailtext += "\n";
                emailtext += "Итого: " + calculate.makeResSum(calculate.GetSum(sumMap, tour_id));


                db.closeDB();
                // О чём
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        emailtext);
                // С чем

                // Поехали!
                emailIntent.putExtra("hello", "hello world!");

                Log.e(LOG, "before sending email");
                EditTourActivity.this.startActivity(Intent.createChooser(emailIntent,
                        "Отправка письма..."));
                Log.e(LOG, "after sending email");
            }
        });


        btn_sendMailTour.setVisibility(View.GONE);
        button1.setVisibility(View.GONE);
        btn_getPlot.setVisibility(View.GONE);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tour_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_archive);
        switch (tour.getArchive_type()){
            case 0:{
                menuItem.setTitle("В архив");
                break;
            }
            case 1:{
                menuItem.setTitle("В актуальные");
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent i;
        switch (menuItem.getItemId()) {
            case R.id.action_curr_rates:
                // TODO: обработчик нажатия здесь
                i = new Intent(EditTourActivity.this, SetCurrRatesActivity.class);
                i.putExtra("tour_id", tour_id);
                i.putExtra("type_id", TourEnum.EditITEM.EDIT_ITEM);
                startActivity(i);// Запускаем новую Активность.
                finish();
                // startActivity(getIntent());
                return true;
            case R.id.action_edit_tour: {
                // TODO: обработчик нажатия здесь
                i = new Intent(EditTourActivity.this, CreateTourActivity.class);
                i.putExtra("tour_id", tour_id);
                i.putExtra("type_id", TourEnum.EditITEM.EDIT_ITEM);
                startActivity(i);// Запускаем новую Активность.
                finish();
                // startActivity(getIntent());
                return true;
            }
            case R.id.action_tour_curr:
                // TODO: обработчик нажатия здесь
                return true;
            case R.id.action_new_item: {
                // TODO: обработчик нажатия здесь
                i = new Intent(EditTourActivity.this, CreateNewItemActivity.class);
                i.putExtra("tour_id", tour_id);
                i.putExtra("type_id", TourEnum.EditITEM.CREATE_ITEM);

                startActivity(i);// Запускаем новую Активность.


                finish();// Завершить текущую активность.

                //   startActivity(getIntent());
                return true;
            }
            case R.id.action_mail_item: {
                // TODO: обработчик нажатия здесь
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                // Кому
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{address});
                // Зачем
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        subject);

                emailtext = "\n";

                db = new DatabaseHandler(getApplicationContext());


                for (TourItem item : items) {
                    Currency currency = currencies.get(item.getCurr_id());
                    emailtext += "\n " + item.toString() + " " + currency.getWord_code();
                }

                //First all outgoing money
                Map<Integer, Float> sumMap = db.getTourAllSum(tour_id, TourEnum.TourItemType.OUTGOING.value);

                emailtext += "\n";

                //получить итоговую сумму по курсам
                for (Integer curr : sumMap.keySet()) {
                    float sumInCur = 0F;
                    Currency currency = db.getCurrency(curr);

                    Log.e(LOG, "Считаем сумму в " + currency.getName());
                    for (Integer innerCur : sumMap.keySet()) {
                        Currency innerCurrency = db.getCurrency(innerCur);
                        //
                        if (curr != innerCur) {
                            //Есть еще другая функция ConvertCurrency
                            // float res = db.CurrConvert(tour_id, innerCur, sumMap.get(innerCur), curr);
                            float res = db.ConvertCurrency(tour_id, innerCur, sumMap.get(innerCur), curr);
                            sumInCur += res;

                            Log.e(LOG, sumMap.get(innerCur) + " " + innerCurrency.getName() + " = " + res + " " + currency.getName());

                        } else {
                            sumInCur += sumMap.get(innerCur);
                            // Log.e(LOG, sumMap.get(innerCur) + " " + currency.getName() + " = " + sumMap.get(curr) + " " + innerCurrency.getName());
                        }
                    }

                    emailtext += " Итого в " + currency.getName() + " = " + sumInCur + "\n";
                }


                db.closeDB();
                // О чём
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        emailtext);
                // С чем

                // Поехали!
                emailIntent.putExtra("hello", "hello world!");

                Log.e(LOG, "before sending email");
                EditTourActivity.this.startActivity(Intent.createChooser(emailIntent,
                        "Отправка письма..."));
                Log.e(LOG, "after sending email");
                return true;
            }
            case R.id.action_tourists:
                // TODO: обработчик нажатия здесь
                return true;
            case R.id.action_graf: {
                // TODO: обработчик нажатия здесь
                Intent intent = new Intent(EditTourActivity.this, ModelPlotActivity.class);
                intent.putExtra("tour_id", tour_id);
                intent.putExtra("plot_type", 1);
                intent.putExtra("curr_id", curr_id);

                startActivity(intent);// Запускаем новую Активность.


                //  finish();// Завершить текущую активность.
                return true;
            }
            case R.id.action_archive:
                // TODO: обработчик нажатия здесь
                switch (tour.getArchive_type()){
                    case 0:{
                        tour.setArchive_type(1);
                        break;
                    }
                    case 1:{
                        tour.setArchive_type(0);
                        break;
                    }
                }

                db.updateTour(tour);
                return true;

        }



       /* Context context = getApplicationContext();
        CharSequence text = item.getTitle();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
         */
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        db = new DatabaseHandler(getApplicationContext());
        items = db.getAllTourItems(tour_id);
        db.closeDB();
    }


}
