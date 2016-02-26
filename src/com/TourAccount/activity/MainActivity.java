package com.TourAccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.TourAccount.R;
import com.TourAccount.adapter.AllTourDataAdapter;
import com.TourAccount.model.Article;
import com.TourAccount.model.Currency;
import com.TourAccount.model.Tour;
import com.TourAccount.model.TourEnum;
import com.TourAccount.services.AttachFilesWork;
import com.TourAccount.services.impl.AttachFilesWorkImpl;
import com.TourAccount.sqlite.DatabaseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class MainActivity extends Activity implements
        AdapterView.OnItemSelectedListener {

    private static final String LOG = "MainActivity";

    //final int DB_VERSION = 2; // версия БД

    String address = "vostrikovaen@gmail.com", subject = "отчет", emailtext = " body-body";

    private String filePdf = "tour.pdf";
    private String fileDB = "data.txt";

    private int archive = 0;


    // Database Helper
    DatabaseHandler db;

    AttachFilesWork attachFilesWork;

    private TextView textView_selected;
    private AllTourDataAdapter mAdapter;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        db = new DatabaseHandler(getApplicationContext());

        attachFilesWork = new AttachFilesWorkImpl();
        attachFilesWork.setApplContext(getApplicationContext());
        attachFilesWork.setDb(db);
        attachFilesWork.setDbFileName(fileDB);
        attachFilesWork.setPdfFileName(filePdf);


        //db.close();

        Log.e(LOG, "DB=" + db.getReadableDatabase().getPath());
        List<Currency> currencies = db.getAllCurrencies();
        if (currencies.size() == 0) {
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
            currency = new Currency("Шведские кроны",752 , "SEK");
            curr_id = db.createCurr(currency);
            Log.d("Currency Count", "Currency Count: " + db.getAllCurrencies().size());

        }

        List<Article> articles = db.getAllArticles();
        if (articles.size() == 0) {
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

        }

        for (Currency curr : currencies)
            Log.d("Currencies ", "Currency: getId=" + curr.getId() + "; getName=" + curr.getName());


        // Don't forget to close database connection
        db.closeDB();

    }


    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        // String currentDBPath = db. getDatabasePath();
        String backupDBPath = "/data/data/app/" + db.DATABASE_NAME;
        File currentDB = //new File(data, currentDBPath);
                db.getDatabasePath();
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG, e.getStackTrace().toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        db = new DatabaseHandler(getApplicationContext());

        //что делать, если не передан параметр
        archive = getIntent().getIntExtra("archive", 0);

        textView_selected = (TextView) findViewById(R.id.textView_selected);
        final GridView g = (GridView) findViewById(R.id.gridView_Tours);
        mAdapter = new AllTourDataAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, db.getAllTours(archive));
        g.setAdapter(mAdapter);
        g.setOnItemSelectedListener(this);
        g.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(MainActivity.this, EditTourActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // указываем первым параметром ключ, а второе значение
                // по ключу мы будем получать значение с Intent
                intent.putExtra("tour_id", mAdapter.getItem(position).getId());
                startActivity(intent);// Запускаем новую Активность.
            }
        });
        db.closeDB();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        // TODO Auto-generated method stub
        //   textView_selected.setText("Выбранный элемент: " + mAdapter.GetItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
        //   textView_selected.setText("Выбранный элемент: ничего");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        switch (archive){
            case 1:// 1 - это выбраны архивные туры
            {
            MenuItem item = menu.findItem(R.id.action_viewarchive);
            item.setVisible(false);
                break;
            }
            case 0:// 0 - это актуальные туры
            {
                MenuItem item = menu.findItem(R.id.action_viewactual);
                item.setVisible(false);
                break;
            }
            default:{
                MenuItem item = menu.findItem(R.id.action_viewall);
                item.setVisible(false);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // TODO: обработчик нажатия здесь
                return true;
            case R.id.action_newtour: {
                // TODO: обработчик нажатия здесь
                Intent i = new Intent(MainActivity.this, CreateTourActivity.class);
                startActivity(i);// Запускаем новую Активность.
                return true;
            }
            case R.id.action_sendmail: {
                // TODO: обработчик нажатия здесь
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                // Кому
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{address});
                // Зачем
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        subject);

                emailtext = "прошедшие туры:\n";

                db = new DatabaseHandler(getApplicationContext());

                for (Tour tour : db.getAllTours(TourEnum.TourArchiveType.ALL.value)) {
                    emailtext += tour.getName() + ". Дата начала: " +
                            tour.getDate_begin() + ". Кол-во человек: " +
                            tour.getTourist_cnt() + "\n";
                }

                db.closeDB();
                // О чём
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        emailtext);

                attachFilesWork.saveDBFile();
                //-----------------------
                // File file = getApplicationContext().getFileStreamPath(Environment.getExternalStorageDirectory().getPath() + "/" + "data.txt");


                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + filePdf);


                if (file.exists()) {

                    Toast toast = Toast.makeText(getApplicationContext(), "FILE EXISTS" +
                            file.getPath(), Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "FILE NOT! EXISTS" +
                            file.getPath(), Toast.LENGTH_SHORT);
                    toast.show();
                }
                emailIntent.putExtra(Intent.EXTRA_STREAM,
                        Uri.fromFile(file));


                Log.e(LOG, "before sending email");
                MainActivity.this.startActivity(Intent.createChooser(emailIntent,
                        "Отправка письма..."));
                Log.e(LOG, "after sending email");
                return true;
            }
            case R.id.action_graf: {
                // TODO: обработчик нажатия здесь
                Intent i = new Intent(MainActivity.this, ShowPloatActivity.class);
                startActivity(i);// Запускаем новую Активность.
                return true;
            }
            case R.id.action_viewarchive: {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.putExtra("archive", 1);//признак показывать только архивные туры
                finish();
                startActivity(i);// Запускаем новую Активность.
                return true;
            }
            case R.id.action_viewactual: {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.putExtra("archive", 0);//признак показывать только архивные туры
                finish();
                startActivity(i);// Запускаем новую Активность.
                return true;
            }
            case R.id.action_viewall: {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.putExtra("archive", -1);//признак показывать только архивные туры
                finish();
                startActivity(i);// Запускаем новую Активность.
                return true;
            }


            case R.id.action_extractdb: {
                // TODO: обработчик нажатия здесь
                Log.e(LOG, "before action_extractdb");


                attachFilesWork.saveDBFile();
                Log.e(LOG, "after action_extractdb");
                return true;
            }

        }


        return true;
    }


}
