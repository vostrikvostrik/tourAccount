package com.TourAccount.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.TourAccount.R;
import com.TourAccount.adapter.ArticleSpinnerAdapter;
import com.TourAccount.adapter.CurrencySpinnerAdapter;
import com.TourAccount.adapter.TouristSpinnerAdapter;
import com.TourAccount.model.*;
import com.TourAccount.sqlite.DatabaseHandler;

import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 20.09.14
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */
public class CreateNewItemActivity extends Activity {

    int tour_id;
    int item_id;
    // Database Helper
    DatabaseHandler db;
    Spinner spinner_curr;
    Spinner spinner_article;
    Spinner spinTourist;
    CurrencySpinnerAdapter currencySpinnerAdapter;
    ArticleSpinnerAdapter articleListAdapter;
    TouristSpinnerAdapter touristSpinnerAdapter;
    Spinner tourItemsTypes;

    //AlertDialog.Builder ad;

    // Logcat tag
    private static final String LOG = "CreateNewItemActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);

        //получить ид тура
        tour_id = getIntent().getIntExtra("tour_id", 0);

        final String type_id = getIntent().getStringExtra("type_id");

        tourItemsTypes = (Spinner) findViewById(R.id.spinner_itemType);

        spinner_article = (Spinner) findViewById(R.id.spinner_article);
        loadSpinnerArticleData();

        //http://felixd.mypage.ru/dva_3g.html
        db = new DatabaseHandler(getApplicationContext());
        TextView textView = (TextView) findViewById(R.id.textV_AllCurses);
        List<CurrRate> currRatesList = db.getTourCurrRates(tour_id);
        Log.e(LOG, "currRatesList.size()=" + currRatesList.size());
        String allCurses = "";
        for (CurrRate currRate : currRatesList)
            allCurses += currRate.getCurr1_id() + " " + currRate.getCurr2_id() + "\n";
        Log.e(LOG, "allCurses=" + allCurses);
        //    textView.setText(allCurses);
        db.closeDB();

        spinner_curr = (Spinner) findViewById(R.id.spinner_curr);
        loadSpinnerCurrData(tour_id);

        spinTourist = (Spinner) findViewById(R.id.spinTourist);
        loadSpinnerTouristData(tour_id);

        final Button but_SaveItem = (Button) findViewById(R.id.but_SaveItem);
        final Button but_Finish = (Button) findViewById(R.id.but_FinishItems);
        final Button but_DeleteItem = (Button) findViewById(R.id.but_DeleteItem);
        final Button but_EditItem = (Button) findViewById(R.id.but_EditItem);

        Log.e(LOG, "type_id = " + type_id);

        if (TourEnum.EditITEM.CREATE_ITEM.toString().equals(type_id)) {
            //Создать новую запись
            but_EditItem.setVisibility(View.GONE);
            but_DeleteItem.setVisibility(View.GONE);
            TextView itemDate= (TextView) findViewById(R.id.textV_itemDate);
            Calendar c = Calendar.getInstance();
            itemDate.append( " : " + c.getTime().toString());
            but_SaveItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // new label input field
                    EditText editText_ItemDesrc = (EditText) findViewById(R.id.editText_ItemDesrc);
                    EditText curr_amount = (EditText) findViewById(R.id.curr_amount);

                    db = new DatabaseHandler(getApplicationContext());

                    TourItem tourItem = new TourItem();
                    tourItem.setTour_descr(editText_ItemDesrc.getText().toString());
                    tourItem.setTour_id(tour_id);
                    Long article_id = spinner_article.getSelectedItemId();
                    Log.e(LOG, "article_id=" + article_id);
                    tourItem.setArticle_id(article_id.intValue());
                    Long curr_id = spinner_curr.getSelectedItemId();
                    Log.e(LOG, "curr_id=" + curr_id);
                    tourItem.setCurr_id(curr_id.intValue());
                    tourItem.setCurr_amount(Float.parseFloat(curr_amount.getText().toString()));
                    tourItem.setItem_type(tourItemsTypes.getSelectedItemPosition());
                    Long tourist_id = spinTourist.getSelectedItemId();
                    tourItem.setTourist_id(tourist_id.intValue());
                    db.createTourItem(tourItem);
                    db.closeDB();

                    finish();
                    startActivity(getIntent());
                }
            });

            but_Finish.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent i = new Intent(CreateNewItemActivity.this, EditTourActivity.class);
                    i.putExtra("tour_id", tour_id);
                    startActivity(i);// Запускаем новую Активность.
                    finish();// Завершить текущую активность.

                }
            });
        } else {
            //Отредактировать существующую запись
            item_id = getIntent().getIntExtra("item_id", 0);

            db = new DatabaseHandler(getApplicationContext());
            TourItem tourItem = db.getTourItem(item_id);
            EditText editText_ItemDesrc = (EditText) findViewById(R.id.editText_ItemDesrc);
            editText_ItemDesrc.setText(tourItem.getTour_descr());
            TextView itemDate= (TextView) findViewById(R.id.textV_itemDate);
            itemDate.append( " : " + tourItem.getTour_date().toString());
            EditText curr_amount = (EditText) findViewById(R.id.curr_amount);
            String amount = String.valueOf(tourItem.getCurr_amount());
            curr_amount.setText(amount.replace(",", "."));

            SetSelectedCurrency(tourItem.getCurr_id());
            SetSelectedArticle(tourItem.getArticle_id());
            SetSelectedTourist(tourItem.getTourist_id());
            SetSelectedItemType(tourItem.getItem_type());

            but_SaveItem.setVisibility(View.GONE);
            but_Finish.setVisibility(View.GONE);


            db.closeDB();

            but_DeleteItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                   /* Intent i = new Intent(CreateNewItemActivity.this, EditTourActivity.class);
                    i.putExtra("tour_id", tour_id);
                    startActivity(i);// Запускаем новую Активность.  */
                    db = new DatabaseHandler(getApplicationContext());

                    TourItem tourItem = db.getTourItem(item_id);
                    db.closeDB();

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewItemActivity.this);
                    builder.setTitle("Вы хотите удалить запись?")
                            .setMessage("Удаление записи " + tourItem.getTour_descr())
                                    //.setIcon(R.drawable.ic_android_cat)
                            .setCancelable(true)
                            .setNegativeButton("Удалить",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //dialog.cancel();
                                            db = new DatabaseHandler(getApplicationContext());
                                            db.deleteTourItem(item_id);
                                            db.closeDB();
                                            finish();// Завершить текущую активность.
                                            //startActivity(getIntent());

                                            Intent i = new Intent(CreateNewItemActivity.this, EditTourActivity.class);
                                            i.putExtra("tour_id", tour_id);
                                            startActivity(i);// Запускаем новую Активность.

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                    //   finish();// Завершить текущую активность.

                }
            });
            but_EditItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // new label input field
                    EditText editText_ItemDesrc = (EditText) findViewById(R.id.editText_ItemDesrc);
                    EditText curr_amount = (EditText) findViewById(R.id.curr_amount);

                    db = new DatabaseHandler(getApplicationContext());

                    TourItem tourItem = db.getTourItem(item_id);
                    tourItem.setTour_descr(editText_ItemDesrc.getText().toString());
                    Long article_id = spinner_article.getSelectedItemId();
                    tourItem.setArticle_id(article_id.intValue());
                    Long curr_id = spinner_curr.getSelectedItemId();
                    tourItem.setCurr_id(curr_id.intValue());
                    tourItem.setCurr_amount(Float.parseFloat(curr_amount.getText().toString()));
                    Long tourist_id = spinTourist.getSelectedItemId();
                    tourItem.setTourist_id(tourist_id.intValue());
                    tourItem.setItem_type(tourItemsTypes.getSelectedItemPosition());
                    db.updateTourItem(tourItem);
                    db.closeDB();
                    finish();// Завершить текущую активность.
                    //startActivity(getIntent());

                    Intent i = new Intent(CreateNewItemActivity.this, EditTourActivity.class);
                    i.putExtra("tour_id", tour_id);
                    startActivity(i);// Запускаем новую Активность.
                }
            });


        }


    }

    /**
     * Function to load the spinner data from SQLite database
     */
    private void loadSpinnerCurrData(int tour_id) {

        db = new DatabaseHandler(getApplicationContext());
        currencySpinnerAdapter = new CurrencySpinnerAdapter(getApplicationContext(), db.getAllCurrencies(tour_id));
        db.closeDB();
        spinner_curr.setAdapter(currencySpinnerAdapter);
    }


    private void loadSpinnerTouristData(int tour_id) {

        db = new DatabaseHandler(getApplicationContext());
        touristSpinnerAdapter = new TouristSpinnerAdapter(getApplicationContext(), db.getTourTourists(tour_id));
        db.closeDB();
        spinTourist.setAdapter(touristSpinnerAdapter);
    }

    private void loadSpinnerArticleData() {
        // database handler
        db = new DatabaseHandler(getApplicationContext());
        articleListAdapter = new ArticleSpinnerAdapter(getApplicationContext(), db.getAllArticles());
        db.closeDB();
        spinner_article.setAdapter(articleListAdapter);

    }

    private void SetSelectedCurrency(int curr_id) {

        for (int i = 0; i < spinner_curr.getCount(); i++) {
            Currency currency = (Currency) spinner_curr.getItemAtPosition(i);
            if (currency.getId() == curr_id) {

                spinner_curr.setSelection(i, true);
            }
        }

    }

    private void SetSelectedArticle(int artcile_id) {

        for (int i = 0; i < spinner_article.getCount(); i++) {
            Article article = (Article) spinner_article.getItemAtPosition(i);
            if (article.getId() == artcile_id) {

                spinner_article.setSelection(i, true);
            }
        }

    }


    private void SetSelectedTourist(int tourist_id) {

        for (int i = 0; i < spinTourist.getCount(); i++) {
            Tourist tourist = (Tourist) spinTourist.getItemAtPosition(i);
            if (tourist.getTourist_id() == tourist_id) {

                spinTourist.setSelection(i, true);
            }
        }

    }


    private void SetSelectedItemType(int itemType) {

        tourItemsTypes.setSelection(itemType, true);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateNewItemActivity.this, EditTourActivity.class);
        i.putExtra("tour_id", tour_id);
        finish();
        startActivity(i);// Запускаем новую Активность.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tour_item_menu, menu);
        /*switch (){

        } */
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent i;
        switch (menuItem.getItemId()) {
            case R.id.action_add_item_tourist:
                // TODO: обработчик нажатия здесь
                i = new Intent(CreateNewItemActivity.this, AddItemTouristActivity.class);
                i.putExtra("tour_id", tour_id);
                i.putExtra("tour_item_id", item_id);
                startActivity(i);// Запускаем новую Активность.
                //finish();
                // startActivity(getIntent());
                return true;
        }
        return true;
    }

}