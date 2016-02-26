package com.TourAccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.TourAccount.R;
import com.TourAccount.model.Article;
import com.TourAccount.model.Currency;
import com.TourAccount.sqlite.DatabaseHandler;

/**
 * User: User
 * Date: 02.10.14
 * Time: 17:40
 */
public class TourAccountActivity extends Activity {

    // Database Helper
    DatabaseHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_account);


        DBWork();

        Button btn_tours = (Button) findViewById(R.id.btn_tours);
        btn_tours.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(TourAccountActivity.this, MainActivity.class);
                startActivity(i);// Запускаем новую Активность.

            }
        });

        Button btn_plots = (Button) findViewById(R.id.btn_plots);
        btn_plots.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(TourAccountActivity.this, ShowPloatActivity.class);
                startActivity(i);// Запускаем новую Активность.

            }
        });

        Button btn_settings = (Button) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(TourAccountActivity.this, MainActivity.class);
                startActivity(i);// Запускаем новую Активность.

            }
        });
    }

    public void DBWork() {

        db = new DatabaseHandler(getApplicationContext());

        // Creating currencies
        db.deleteAllCurrencies();

        Currency currency = new Currency("Руб.", 810, "RUR");
        long curr_id = db.createCurr(currency);
        currency = new Currency("Дол. США", 840, "USD");
        curr_id = db.createCurr(currency);
        currency = new Currency("Евро", 978, "EUR");
        curr_id = db.createCurr(currency);
        currency = new Currency("Непальские руппи", 524, "NPR");
        curr_id = db.createCurr(currency);
        currency = new Currency("Норвежская крона", 578, "NOK");
        curr_id = db.createCurr(currency);
        currency = new Currency("Лари", 981, "GEL");
        curr_id = db.createCurr(currency);
        currency = new Currency("Гривна", 980, "UAH");
        curr_id = db.createCurr(currency);
        currency = new Currency("Австралийский доллар", 036, "AUD");
        curr_id = db.createCurr(currency);
        currency = new Currency("Дирхам", 784, "AED");
        curr_id = db.createCurr(currency);
        currency = new Currency("Доллар Гонконг", 344, "HKD");
        curr_id = db.createCurr(currency);

        Log.d("Currency Count", "Currency Count: " + db.getAllCurrencies().size());

        //Creating Articles


        db.deleteAllArticles();

        Article article = new Article("еда");
        int article_id = db.createArticle(article);
        article = new Article("жилье");
        article_id = db.createArticle(article);
        article = new Article("дорога");
        article_id = db.createArticle(article);
        article = new Article("пермиты");
        article_id = db.createArticle(article);
        article = new Article("персонал");
        article_id = db.createArticle(article);
        article = new Article("другое");
        article_id = db.createArticle(article);

        Log.d("Article Count", "Article Count: " + db.getAllArticles().size());

        db.closeDB();
    }
}
