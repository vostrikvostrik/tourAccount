package com.TourAccount.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.TourAccount.model.*;
import com.TourAccount.model.Currency;
import com.google.gson.Gson;

import java.io.File;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHandler";

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Database Version
    private static final int DATABASE_VERSION = 5; //prev 4

    // Database Name
    public static final String DATABASE_NAME = "tourManager";

    // Labels table name
    private static final String TABLE_TOUR = "tour";                    // описание тура
    private static final String TABLE_CURR = "tour_curr";               // валюты
    private static final String TABLE_BUY_ARTICLE = "tour_buy_article"; // статьи расходов
    private static final String TABLE_TOUR_ITEMS = "tour_items";        // туры и их расходы
    private static final String TABLE_TOUR_ITEMS_TOURIST = "tour_items_tourist";        // расходы по каждой позиции тура по человекам
    private static final String TABLE_CURR_RATES = "curr_rates";        // курсы валют
    private static final String TABLE_TOURISTS = "tour_tourists";     // туристы в туре

    // Labels Table TABLE_TOUR Columns names
    private static final String TOUR_ID = "id";
    private static final String TOUR_NAME = "name";
    private static final String TOUR_DATE_BEGIN = "date_begin";
    private static final String TOUR_DATE_END = "date_end";
    private static final String TOUR_TOURIST_CNT = "tourist_cnt";
    //добавление типа тура: коммерческий/товарищеский
    private static final String TOUR_TYPE = "tour_type";   //default 0 - commerce
    private static final String TOUR_ARCHIVE_TYPE = "tour_archive_type";   //default 0 - не архив

    // Labels Table TABLE_CURR Columns names
    private static final String CURR_ID = "id";
    //  private static final String CURR_TOUR_ID = "tour_id";
    private static final String CURR_NAME = "name";
    private static final String CURR_DIGIT_CODE = "digit_code";
    private static final String CURR_WORD_CODE = "word_code";
    //   private static final String CURR_RATE = "curr_rate";

    // Labels Table TABLE_BUY_ARTICLE Columns names
    private static final String ARTICLE_ID = "id";
    private static final String ARTICLE_NAME = "name";

    // Labels Table TABLE_TOUR_ITEMS Columns names
    private static final String TOUR_ITEM_ID = "id";
    private static final String TOUR_ITEM_TOUR_ID = "tour_id";
    private static final String TOUR_ITEM_TOURIST_ID = "tourist_id";
    private static final String TOUR_ITEM_ARTICLE_ID = "tour_article_id";
    private static final String TOUR_ITEM_CURR_ID = "tour_curr_id";
    private static final String TOUR_ITEM_CURR_AMOUNT = "tour_curr_amount";
    private static final String TOUR_ITEM_TOUR_DAY = "tour_day";
    private static final String TOUR_ITEM_TOUR_DATE = "tour_date";
    private static final String TOUR_ITEM_DECRIBE = "tour_descr";
    //добавление типа операции: приход/расход
    private static final String TOUR_ITEM_TYPE = "tour_item_type";  //default 0 - расход

    //Labels Table TABLE_TOUR_ITEMS_TOURIST Columns names
    private static final String TOUR_ITEM_TOURIST_ITEM_ID = "item_id"; //FK from table tour_items
    private static final String TOUR_ITEM_TOURIST_TOURIST_ID = "tour_id";
    private static final String TOUR_ITEM_TOURIST_AMOUNT = "amount";
    private static final String TOUR_ITEM_TOURIST_CURR_AMOUNT = "curr_amount";

    //TABLE_CURR_RATES
    private static final String CURRATE_ID = "currate_id";
    private static final String CURRATE_TOUR_ID = "tour_id";
    private static final String CURR1_ID = "curr1_id";
    private static final String CURR2_ID = "curr2_id";
    private static final String VAL1_ID = "val1_id";
    private static final String VAL2_ID = "val2_id";
    private static final String CURRATE_DATE = "currrate_date";


    // TABLE_TOURISTS
    private static final String TOURIST_TOUR_ID = "tour_id";
    private static final String TOURIST_ID = "tourist_id";
    private static final String TOURIST_NAME = "tourist_name";
    private static final String TOURIST_DESCR = "tourist_descr";


    private File databaseFile;

    public File getDatabasePath() {
        return databaseFile;
    }

   /* public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    } */

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(LOG, "getPackageCodePath \t" + context.getPackageCodePath());
        Log.e(LOG, "getPackageResourcePath \t" + context.getPackageResourcePath());
        Log.e(LOG, "getDatabasePath \t" + context.getDatabasePath(DATABASE_NAME).getPath());
        this.databaseFile = context.getDatabasePath(DATABASE_NAME);


    }

    // Table Create Statements
    // TABLE_TOUR table create statement
    private static final String CREATE_TABLE_TOUR = "CREATE TABLE "
            + TABLE_TOUR + "(" + TOUR_ID + " INTEGER PRIMARY KEY," + TOUR_NAME
            + " TEXT," + TOUR_TOURIST_CNT + " INTEGER," + TOUR_DATE_BEGIN
            + " DATETIME DEFAULT CURRENT_TIMESTAMP, " + TOUR_DATE_END + " DATETIME DEFAULT CURRENT_TIMESTAMP ,"
            + TOUR_TYPE + " INTEGER DEFAULT 0, " +TOUR_ARCHIVE_TYPE+" INTEGER DEFAULT 0)";

    // TABLE_CURR_RATES table create statement
    private static final String CREATE_TABLE_CURR_RATES = "CREATE TABLE " + TABLE_CURR_RATES
            + "(" + CURRATE_ID + " INTEGER PRIMARY KEY," + CURRATE_TOUR_ID + " INTEGER," + CURR1_ID + " INTEGER," + CURR2_ID
            + " INTEGER, " + VAL1_ID + " FLOAT, " + VAL2_ID + " FLOAT, " + CURRATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    // TABLE_CURR table create statement
    private static final String CREATE_TABLE_CURR = "CREATE TABLE " + TABLE_CURR
            + "(" + CURR_ID + " INTEGER PRIMARY KEY," + CURR_NAME + " TEXT,"
            + CURR_DIGIT_CODE + " INTEGER, " + CURR_WORD_CODE + " TEXT)";

    // TABLE_BUY_ARTICLE table create statement
    private static final String CREATE_TABLE_BUY_ARTICLE = "CREATE TABLE " + TABLE_BUY_ARTICLE
            + "(" + ARTICLE_ID + " INTEGER PRIMARY KEY," + ARTICLE_NAME + " TEXT)";

    // TABLE_BUY_ARTICLE table create statement
    private static final String CREATE_TABLE_TOUR_ITEMS = "CREATE TABLE " + TABLE_TOUR_ITEMS
            + "(" + TOUR_ITEM_ID + " INTEGER PRIMARY KEY," + TOUR_ITEM_TOUR_ID + " INTEGER,"
            + TOUR_ITEM_TOURIST_ID + " INTEGER,"
            + TOUR_ITEM_ARTICLE_ID + " INTEGER,"
            + TOUR_ITEM_CURR_ID + " INTEGER,"
            + TOUR_ITEM_CURR_AMOUNT + " FLOAT,"
            + TOUR_ITEM_TOUR_DAY + " INTEGER,"
            + TOUR_ITEM_TOUR_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + TOUR_ITEM_DECRIBE + " TEXT, "
            + TOUR_ITEM_TYPE + " INTEGER DEFAULT 0"
            + ")";

    // TABLE_CURR_RATES table create statement
    private static final String CREATE_TABLE_TOURISTS = "CREATE TABLE " + TABLE_TOURISTS
            + "(" + TOURIST_ID + " INTEGER PRIMARY KEY," + TOURIST_TOUR_ID + " INTEGER," + TOURIST_NAME
            + " TEXT, " + TOURIST_DESCR + " TEXT)";

    // TABLE_TOUR_ITEMS_TOURIST table create statement
    private static final String CREATE_TABLE_TOUR_ITEMS_TOURIST  = "CREATE TABLE " + TABLE_TOUR_ITEMS_TOURIST
            + "(" + TOUR_ITEM_TOURIST_ITEM_ID + " INTEGER," + TOUR_ITEM_TOURIST_TOURIST_ID + " INTEGER,"
            + TOUR_ITEM_TOURIST_AMOUNT + " FLOAT, "+ TOUR_ITEM_TOURIST_CURR_AMOUNT +" INTEGER)";

    private static final String DROP_TABLE_TOUR_ITEMS_TOURIST = " DROP TABLE " + TABLE_TOUR_ITEMS_TOURIST;


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        db.execSQL(CREATE_TABLE_TOUR);
        db.execSQL(CREATE_TABLE_CURR);
        db.execSQL(CREATE_TABLE_BUY_ARTICLE);
        db.execSQL(CREATE_TABLE_TOUR_ITEMS);
        db.execSQL(CREATE_TABLE_CURR_RATES);
        db.execSQL(CREATE_TABLE_TOURISTS);
        db.execSQL(CREATE_TABLE_TOUR_ITEMS_TOURIST);
    }


    public String GetCreateDBScript() {
        String res = "";
        res += " create database " + DATABASE_NAME + "/";
        res += CREATE_TABLE_TOUR + "/";
        res += CREATE_TABLE_CURR + "/";
        res += CREATE_TABLE_BUY_ARTICLE + "/";
        res += CREATE_TABLE_TOUR_ITEMS + "/";
        res += CREATE_TABLE_CURR_RATES + "/";
        res += CREATE_TABLE_TOURISTS + "/";
        res += CREATE_TABLE_TOUR_ITEMS_TOURIST + "/";

        return res;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
      /*  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUY_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURR_RATES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TOURISTS);

        // Create tables again
        onCreate(db);*/
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL("alter table " + TABLE_TOUR_ITEMS + " add column  " + TOUR_ITEM_TYPE + " INTEGER DEFAULT 0;");
            db.execSQL("alter table " + TABLE_TOUR + " add column  " + TOUR_TYPE + " INTEGER DEFAULT 0;");
        }
        if (oldVersion == 2 && newVersion == 3) {
            db.execSQL(" alter table " + TABLE_TOUR + " add column " + TOUR_ARCHIVE_TYPE + " INTEGER DEFAULT 0;");
        }
        if (oldVersion == 3 && newVersion == 4) {
            db.execSQL(CREATE_TABLE_TOUR_ITEMS_TOURIST);
        }
        if (oldVersion == 4 && newVersion == 5) {
            db.execSQL(DROP_TABLE_TOUR_ITEMS_TOURIST);
            db.execSQL(CREATE_TABLE_TOUR_ITEMS_TOURIST);
        }

    }

    // ------------------------ "Tour" table methods ----------------//

    /*
    * Creating Tour
    */
    public int createTour(Tour tour) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TOUR_NAME, tour.getName());
        if (tour.getDate_begin() != null)
            values.put(TOUR_DATE_BEGIN, dateFormat.format(tour.getDate_begin()));
        if (tour.getDate_end() != null)
            values.put(TOUR_DATE_END, dateFormat.format(tour.getDate_end()));
        values.put(TOUR_TOURIST_CNT, tour.getTourist_cnt());
        values.put(TOUR_TYPE, tour.getTour_type());
        // insert row
        Long tour_id = db.insert(TABLE_TOUR, null, values);
        Log.e(LOG, "inserted tour_id=" + tour_id + "; date: " + tour.getDate_end());
        return tour_id.intValue();
    }


    public String getDBScript() {
        SQLiteDatabase db = this.getReadableDatabase();
        String res = "";
        String selectQuery = "SELECT sql FROM sqlite_master  " +
                "WHERE type IN ('table', 'view') AND name NOT LIKE 'sqlite %' " +
                "UNION  ALL " +
                "SELECT sql FROM sqlite_temp_master " +
                "WHERE type IN  ('tаblе', 'view')";
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                res += "/n" + c.getString(0);

            } while (c.moveToNext());
        }
        c.close();
        db.close();

        return res;
    }

    public void SelectFromAllTables(List<String> tableNames) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "";
        for (String table : tableNames) {
            selectQuery = " select  * from " + table;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    //res.add(c.getString(0));
                    int columnCnt = c.getColumnCount();
                    for (int i = 0; i < columnCnt; i++) {

                    }

                } while (c.moveToNext());
            }

            c.close();
            db.close();

        }

    }

    public String SearchAllObjects() {

        List<Tour> tours = getAllTours(TourEnum.TourArchiveType.ALL.value);
        //Log.d(LOG, "getAllTours: " + writeJson(tours));

        List<Tourist> tourists = getAllTourists();
        //Log.d(LOG, "getAllTourists: " + writeJson(tourists));

        List<TourItem> tourItems = getAllTourItems();
        //Log.d(LOG, "getAllTourItems: " + writeJson(tourItems));

        List<Article> articles = getAllArticles();
        //Log.d(LOG, "getAllArticles: " + writeJson(articles));

        List<Currency> currencies = getAllCurrencies();
        //Log.d(LOG, "getAllCurrencies: " + writeJson(currencies));

        List<CurrRate> currRates = getAllCurrRates();
        //Log.d(LOG, "getAllCurrRates: " + writeJson(currRates));

        DataBaseModel dataBaseModel = new DataBaseModel(tours, tourists, tourItems,
                articles, currencies, currRates);

        Log.d(LOG, "dataBaseModel: " + writeJson(dataBaseModel));

        return writeJson(dataBaseModel);
    }

    public String writeJson(Object obj) {

        Gson gson = new Gson();
        String json = gson.toJson(obj);

        return json;
    }

    public List<String> getDBTableNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> res = new ArrayList<String>();
        String selectQuery = "SELECT name FROM sqlite_master  " +
                "WHERE type IN ('table', 'view') AND name NOT LIKE 'sqlite %' " +
                "UNION  ALL " +
                "SELECT name FROM sqlite_temp_master " +
                "WHERE type IN  ('tаblе', 'view')";
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                res.add(c.getString(0));

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return res;
    }

    /*
     * get single Tour
    */
    public Tour getTour(int tour_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " +
                TOUR_ID +
                "," +
                TOUR_NAME +
                "," +
                // "CONVERT(VARCHAR(20), MIN(" + TOUR_DATE_BEGIN + "), 101)" +
                TOUR_DATE_BEGIN +
                "," +
                TOUR_DATE_END +
                "," +
                TOUR_TOURIST_CNT +
                "," +
                TOUR_TYPE +
                "," +
                TOUR_ARCHIVE_TYPE +
                " FROM " + TABLE_TOUR + " WHERE "
                + TOUR_ID + " = " + tour_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();


        Tour t = new Tour();
        //  Log.e(LOG, TOUR_ID + " = " + c.getColumnIndex(TOUR_ID));
        t.setId(c.getInt((c.getColumnIndex(TOUR_ID))));
        t.setName(c.getString(c.getColumnIndex(TOUR_NAME)));
        ParsePosition parsePosition = new ParsePosition(0);

        if (c.getString(c.getColumnIndex(TOUR_DATE_BEGIN)) != null)
            t.setDate_begin(dateFormat.parse(c.getString(c.getColumnIndex(TOUR_DATE_BEGIN)), parsePosition));
        // Date dt= new Date(c.getLong(c.getColumnIndex(TOUR_DATE_BEGIN)));
        // t.setDate_begin(dateFormat.format(dt));
        t.setTour_type(c.getInt(c.getColumnIndex(TOUR_TYPE)));
        if (c.getString(c.getColumnIndex(TOUR_DATE_END)) != null)
            t.setDate_end(dateFormat.parse(c.getString(c.getColumnIndex(TOUR_DATE_END)), parsePosition));
        t.setTourist_cnt(c.getInt(c.getColumnIndex(TOUR_TOURIST_CNT)));
        c.close();
        db.close();
        return t;
    }

    /**
     * getting all Tours
     */
    public List<Tour> getAllTours(int tourarchievetype) {
        List<Tour> tours = new ArrayList<Tour>();
        String selectQuery = "";
        if (tourarchievetype >= 0)
            selectQuery = "SELECT  * FROM " + TABLE_TOUR + " WHERE " + TOUR_ARCHIVE_TYPE + " = " + tourarchievetype
                    + " ORDER BY " + TOUR_ID + " DESC ";
        else
            selectQuery = "SELECT  * FROM " + TABLE_TOUR + " ORDER BY " + TOUR_ID + " DESC ";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tour t = new Tour();
                t.setId(c.getInt((c.getColumnIndex(TOUR_ID))));
                t.setName(c.getString(c.getColumnIndex(TOUR_NAME)));
                ParsePosition parsePosition = new ParsePosition(0);
                t.setDate_begin(dateFormat.parse(c.getString(c.getColumnIndex(TOUR_DATE_BEGIN)), parsePosition));
               /* try{
                t.setDate_end(dateFormat.parse(c.getString(c.getColumnIndex(TOUR_DATE_END)),parsePosition));
                }
                catch (Exception ex){
                    Calendar calendar = Calendar.getInstance();
                    t.setDate_end(calendar.getTime());
                } */
                t.setTourist_cnt(c.getInt(c.getColumnIndex(TOUR_TOURIST_CNT)));
                t.setTour_type(c.getInt(c.getColumnIndex(TOUR_TYPE)));
                // adding to tags list
                tours.add(t);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tours;
    }


    /*
     * Updating a Tour
     */
    public int updateTour(Tour tour) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TOUR_NAME, tour.getName());


        //values.put(TOUR_DATE_BEGIN, dateFormat.format(tour.getDate_begin()));

        //Log.e(LOG, "update Tour. beg_date = " + tour.getDate_begin());
        //values.put(TOUR_DATE_END, dateFormat.format(tour.getDate_end()));
        values.put(TOUR_TOURIST_CNT, tour.getTourist_cnt());
        values.put(TOUR_TYPE, tour.getTour_type());
        values.put(TOUR_ARCHIVE_TYPE, tour.getArchive_type());
        // updating row
        int res = db.update(TABLE_TOUR, values, TOUR_ID + " = ?",
                new String[]{String.valueOf(tour.getId())});

        db.close();

        return res;
    }

    /*
     * Deleting a Tour
     */
    public void deleteTour(int tour_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOUR, TOUR_ID + " = ?",
                new String[]{String.valueOf(tour_id)});

        db.close();
    }


    // ------------------------ "Article" table methods ----------------//
    /*
    * Creating Article
    */
    public int createArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ARTICLE_NAME, article.getName());

        // insert row
        Long article_id = db.insert(TABLE_BUY_ARTICLE, null, values);

        db.close();
        return article_id.intValue();
    }

    /*
    * get single Article
    */
    public Article getArticle(int article_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BUY_ARTICLE + " WHERE "
                + ARTICLE_ID + " = " + article_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Article article = new Article();
        article.setId(c.getInt((c.getColumnIndex(ARTICLE_ID))));
        article.setName(c.getString(c.getColumnIndex(ARTICLE_NAME)));

        c.close();
        db.close();
        return article;
    }

    /**
     * getting all Articles
     */
    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<Article>();
        String selectQuery = "SELECT  * FROM " + TABLE_BUY_ARTICLE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Article t = new Article();
                t.setId(c.getInt((c.getColumnIndex(ARTICLE_ID))));
                t.setName(c.getString(c.getColumnIndex(ARTICLE_NAME)));

                // adding to tags list
                articles.add(t);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return articles;
    }

    /*
     * Updating a Article
     */
    public int updateArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ARTICLE_NAME, article.getName());

        // updating row
        int res = db.update(TABLE_BUY_ARTICLE, values, ARTICLE_ID + " = ?",
                new String[]{String.valueOf(article.getId())});


        db.close();

        return res;
    }

    /*
     * Deleting a Article
     */
    public void deleteArticle(int article_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUY_ARTICLE, ARTICLE_ID + " = ?",
                new String[]{String.valueOf(article_id)});


        db.close();
    }

    public void deleteAllArticles() {
        SQLiteDatabase db = this.getWritableDatabase();


        db.close();
    }

    // ------------------------ "Currency" table methods ----------------//
    /*
    * Creating Currency
    */
    public int createCurr(Currency currency) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CURR_NAME, currency.getName());
        //  values.put(CURR_TOUR_ID, currency.getTour_id());
        values.put(CURR_DIGIT_CODE, currency.getDigit_code());
        values.put(CURR_WORD_CODE, currency.getWord_code());
        //   values.put(CURR_RATE , currency.getCurr_rate());
        //    Log.e(LOG, "createCurr.tour_id="+ currency.getTour_id());

        // insert row
        Long currency_id = db.insert(TABLE_CURR, null, values);

        db.close();
        return currency_id.intValue();
    }

    /*
    * get single Currency
    */
    public Currency getCurrency(int currency_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CURR + " WHERE "
                + CURR_ID + " = " + currency_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Currency currency = new Currency();
        currency.setId(c.getInt((c.getColumnIndex(CURR_ID))));
        currency.setName(c.getString(c.getColumnIndex(CURR_NAME)));
        currency.setDigit_code(c.getInt(c.getColumnIndex(CURR_DIGIT_CODE)));
        currency.setWord_code(c.getString(c.getColumnIndex(CURR_WORD_CODE)));
        c.close();
        db.close();
        return currency;
    }

    /**
     * getting all Currency
     */
    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<Currency>();
        String selectQuery = "SELECT  * FROM " + TABLE_CURR;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Currency currency = new Currency();
                currency.setId(c.getInt((c.getColumnIndex(CURR_ID))));
                currency.setName(c.getString(c.getColumnIndex(CURR_NAME)));
                currency.setDigit_code(c.getInt(c.getColumnIndex(CURR_DIGIT_CODE)));
                currency.setWord_code(c.getString(c.getColumnIndex(CURR_WORD_CODE)));
                //    currency.setCurr_rate(c.getFloat(c.getColumnIndex(CURR_RATE)));
                //    currency.setTour_id(c.getInt(c.getColumnIndex(CURR_TOUR_ID)));

                // adding to tags list
                currencies.add(currency);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return currencies;
    }


    /**
     * getting all Currency
     */

    public List<Currency> getAllCurrencies(int tour_id) {
        List<Currency> currencies = new ArrayList<Currency>();
        String selectQuery = "SELECT distinct * FROM " + TABLE_CURR + " WHERE " + CURR_ID + " in (" +
                " SELECT " + CURR1_ID + " FROM " + TABLE_CURR_RATES + " WHERE " +
                CURRATE_TOUR_ID + " = " + tour_id +
                " union all " +
                " SELECT " + CURR2_ID + " FROM " + TABLE_CURR_RATES + " WHERE " +
                CURRATE_TOUR_ID + " = " + tour_id +
                ") order by " + CURR_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Currency currency = new Currency();
                currency.setId(c.getInt((c.getColumnIndex(CURR_ID))));
                currency.setName(c.getString(c.getColumnIndex(CURR_NAME)));
                currency.setDigit_code(c.getInt(c.getColumnIndex(CURR_DIGIT_CODE)));
                currency.setWord_code(c.getString(c.getColumnIndex(CURR_WORD_CODE)));
                //    currency.setCurr_rate(c.getFloat(c.getColumnIndex(CURR_RATE)));
                //    currency.setTour_id(c.getInt(c.getColumnIndex(CURR_TOUR_ID)));

                // adding to tags list
                currencies.add(currency);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return currencies;
    }

    /*
     * Updating a Currency
     */
    public int updateCurrency(Currency currency) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CURR_NAME, currency.getName());
        values.put(CURR_DIGIT_CODE, currency.getDigit_code());
        values.put(CURR_WORD_CODE, currency.getWord_code());
        //  values.put(CURR_RATE , currency.getCurr_rate());
        // values.put(CURR_TOUR_ID, currency.getTour_id());

        // updating row
        int res = db.update(TABLE_CURR, values, CURR_ID + " = ?",
                new String[]{String.valueOf(currency.getId())});

        db.close();

        return res;
    }

    /*
     * Deleting a Currency
     */
    public void deleteCurrency(int currency_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CURR, CURR_ID + " = ?",
                new String[]{String.valueOf(currency_id)});

        db.close();
    }

    public void deleteAllCurrencies() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CURR, null, null);

        db.close();
    }

    // ------------------------ "Currency" table methods ----------------//
    /*
    * Creating CurrRATE
    */
    public int createCurrRate(CurrRate currRate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CURRATE_TOUR_ID, currRate.getTour_id());
        values.put(CURR1_ID, currRate.getCurr1_id());
        values.put(CURR2_ID, currRate.getCurr2_id());
        values.put(VAL1_ID, currRate.getVal1());
        values.put(VAL2_ID, currRate.getVal2());

        // insert row
        Long currency_id = db.insert(TABLE_CURR_RATES, null, values);

        db.close();
        return currency_id.intValue();
    }

    /*
    * get single Currency
    */
    public CurrRate getCurrRate(int currRateId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CURR_RATES + " WHERE "
                + CURRATE_ID + " = " + currRateId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        CurrRate currRate = new CurrRate();

        currRate.setCurrrate_id(c.getInt(c.getColumnIndex(CURRATE_ID)));
        currRate.setTour_id(c.getInt(c.getColumnIndex(CURRATE_TOUR_ID)));
        currRate.setCurr1_id(c.getInt(c.getColumnIndex(CURR1_ID)));
        currRate.setCurr2_id(c.getInt(c.getColumnIndex(CURR2_ID)));
        currRate.setVal1(c.getFloat(c.getColumnIndex(VAL1_ID)));
        currRate.setVal2(c.getFloat(c.getColumnIndex(VAL2_ID)));

        c.close();
        db.close();
        return currRate;
    }

    /**
     * getting all Currency
     */
    public List<CurrRate> getAllCurrRates() {
        List<CurrRate> currRates = new ArrayList<CurrRate>();
        String selectQuery = "SELECT  * FROM " + TABLE_CURR_RATES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                CurrRate currRate = new CurrRate();

                currRate.setCurrrate_id(c.getInt(c.getColumnIndex(CURRATE_ID)));
                currRate.setTour_id(c.getInt(c.getColumnIndex(CURRATE_TOUR_ID)));
                currRate.setCurr1_id(c.getInt(c.getColumnIndex(CURR1_ID)));
                currRate.setCurr2_id(c.getInt(c.getColumnIndex(CURR2_ID)));
                currRate.setVal1(c.getFloat(c.getColumnIndex(VAL1_ID)));
                currRate.setVal2(c.getFloat(c.getColumnIndex(VAL2_ID)));

                currRates.add(currRate);
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        return currRates;
    }

    public List<CurrRate> getTourCurrRates(int tour_id) {
        List<CurrRate> currRates = new ArrayList<CurrRate>();
        String selectQuery = "SELECT  * FROM " + TABLE_CURR_RATES + " WHERE " + CURRATE_TOUR_ID + " = " + tour_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                CurrRate currRate = new CurrRate();

                currRate.setCurrrate_id(c.getInt(c.getColumnIndex(CURRATE_ID)));
                currRate.setTour_id(c.getInt(c.getColumnIndex(CURRATE_TOUR_ID)));
                currRate.setCurr1_id(c.getInt(c.getColumnIndex(CURR1_ID)));
                currRate.setCurr2_id(c.getInt(c.getColumnIndex(CURR2_ID)));
                currRate.setVal1(c.getFloat(c.getColumnIndex(VAL1_ID)));
                currRate.setVal2(c.getFloat(c.getColumnIndex(VAL2_ID)));

                currRates.add(currRate);
            } while (c.moveToNext());
        }
        Log.e(LOG, "currRates" + currRates.size());
        c.close();
        db.close();
        return currRates;
    }

    /*
     * Updating a Currency
     */
    public int updateCurrRate(CurrRate currRate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VAL1_ID, currRate.getVal1());
        values.put(VAL2_ID, currRate.getVal2());

        /*
        values.put(CURRATE_TOUR_ID, currRate.getTour_id());
        values.put(CURR1_ID , currRate.getCurr1_id());
        values.put(CURR2_ID , currRate.getCurr2_id());
        */

        // updating row
        int res = db.update(TABLE_CURR_RATES, values, CURRATE_TOUR_ID + " = ? and " + CURR1_ID + " = ? and " + CURR2_ID + " = ? ",
                new String[]{
                        String.valueOf(currRate.getTour_id()), String.valueOf(currRate.getCurr1_id()),
                        String.valueOf(currRate.getCurr2_id())});


        db.close();

        return res;
    }

    /*
     * Deleting a Currency
     */
    public void deleteCurrRate(CurrRate currRate) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CURR_RATES, CURRATE_ID + " = ? ",
                new String[]{
                        String.valueOf(currRate.getCurrrate_id())});


        db.close();
    }

    public void deleteTourCurrRates(int tour_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CURR_RATES, CURRATE_TOUR_ID + " = ?", new String[]{
                String.valueOf(tour_id)});

        db.close();
    }

    public void deleteAllCurrRates() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CURR_RATES, null, null);

        db.close();
    }

    // ------------------------ "TourItem" table methods ----------------//
    /*
    * Creating TourItem
    */
    public int createTourItem(TourItem tourItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TOUR_ITEM_TOUR_ID, tourItem.getTour_id());
        values.put(TOUR_ITEM_TOURIST_ID, tourItem.getTourist_id());
        values.put(TOUR_ITEM_ARTICLE_ID, tourItem.getArticle_id());
        values.put(TOUR_ITEM_CURR_ID, tourItem.getCurr_id());
        values.put(TOUR_ITEM_CURR_AMOUNT, tourItem.getCurr_amount());
        values.put(TOUR_ITEM_TOUR_DAY, tourItem.getDay());
        values.put(TOUR_ITEM_TYPE, tourItem.getItem_type());
        // values.put(TOUR_ITEM_TOUR_DATE, tourItem.getTour_date());
        values.put(TOUR_ITEM_DECRIBE, tourItem.getTour_descr());
        // insert row
        Long tour_id = db.insert(TABLE_TOUR_ITEMS, null, values);
        Log.e(LOG, "inserted tourItem = " + tour_id);


        db.close();
        return tour_id.intValue();
    }

    /*
    * get single TourItem
    */
    public TourItem getTourItem(int item_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TOUR_ITEMS + " WHERE "
                + TOUR_ITEM_ID + " = " + item_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TourItem tourItem = new TourItem();
        tourItem.setId(c.getInt((c.getColumnIndex(TOUR_ITEM_ID))));
        tourItem.setTour_id(c.getInt(c.getColumnIndex(TOUR_ITEM_TOUR_ID)));
        tourItem.setTourist_id(c.getInt(c.getColumnIndex(TOUR_ITEM_TOURIST_ID)));
        tourItem.setArticle_id(c.getInt(c.getColumnIndex(TOUR_ITEM_ARTICLE_ID)));
        tourItem.setCurr_id(c.getInt(c.getColumnIndex(TOUR_ITEM_CURR_ID)));
        tourItem.setCurr_amount(c.getFloat(c.getColumnIndex(TOUR_ITEM_CURR_AMOUNT)));
        tourItem.setDay(c.getInt(c.getColumnIndex(TOUR_ITEM_TOUR_DAY)));
        tourItem.setTour_date(c.getString(c.getColumnIndex(TOUR_ITEM_TOUR_DATE)));
        tourItem.setTour_descr(c.getString(c.getColumnIndex(TOUR_ITEM_DECRIBE)));
        tourItem.setItem_type(c.getInt(c.getColumnIndex(TOUR_ITEM_TYPE)));
        c.close();
        db.close();
        return tourItem;
    }

    /**
     * getting all TourItems
     */
    //tourist_id
    public int getAllTourTourists(int tour_id) {
        int touristCnt = 0;
        String selectQuery = "SELECT  count(distinct " + TOUR_ITEM_TOURIST_ID + ") FROM " + TABLE_TOUR_ITEMS + " WHERE "
                + TOUR_ITEM_TOUR_ID + " = " + tour_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                touristCnt = c.getInt(0);
            }
            c.close();
        } finally {
            db.close();
        }
        return touristCnt;
    }

    public List<TourItem> getAllTourItems(int tour_id) {
        List<TourItem> tourItems = new ArrayList<TourItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOUR_ITEMS + " WHERE " + TOUR_ITEM_TOUR_ID + " = " + tour_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TourItem tourItem = new TourItem();
                tourItem.setId(c.getInt((c.getColumnIndex(TOUR_ITEM_ID))));
                tourItem.setTour_id(c.getInt(c.getColumnIndex(TOUR_ITEM_TOUR_ID)));
                tourItem.setTourist_id(c.getInt(c.getColumnIndex(TOUR_ITEM_TOURIST_ID)));
                tourItem.setArticle_id(c.getInt(c.getColumnIndex(TOUR_ITEM_ARTICLE_ID)));
                tourItem.setCurr_id(c.getInt(c.getColumnIndex(TOUR_ITEM_CURR_ID)));
                tourItem.setCurr_amount(c.getFloat(c.getColumnIndex(TOUR_ITEM_CURR_AMOUNT)));
                tourItem.setDay(c.getInt(c.getColumnIndex(TOUR_ITEM_TOUR_DAY)));
                tourItem.setTour_date(c.getString(c.getColumnIndex(TOUR_ITEM_TOUR_DATE)));
                tourItem.setTour_descr(c.getString(c.getColumnIndex(TOUR_ITEM_DECRIBE)));
                tourItem.setItem_type(c.getInt(c.getColumnIndex(TOUR_ITEM_TYPE)));
                Log.e(LOG, "next item = " + tourItem.toString());
                Log.e(LOG, "tourItem.getTourist_id() = "
                        + tourItem.getTourist_id() + ";tourItem.getCurr_amount()="
                        + tourItem.getCurr_amount() + ";tourItem.getCurr_id()=" + tourItem.getCurr_id());
                // adding to tags list
                tourItems.add(tourItem);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tourItems;
    }

    //Not used now
    public Map<Integer, Float> getTourAllSumByCurrencies(int tour_id) {
        Map<Integer, Float> sumMap = new HashMap<Integer, Float>();
        List<Integer> currencies = getTourCurrencies(tour_id);
        Map<Integer, Float> allTourSum = getTourAllSum(tour_id, -1);

        for (Integer currId : currencies) {
            Float summ = 0f;
            for (Integer currName : allTourSum.keySet()) {
                if (currName == currId) {
                    summ += allTourSum.get(currName);
                } else {
                    //конвертирвоание
                    // и потом тоже плюс
                }
            }
        }
        return sumMap;
    }


    //Usage
    public Float ConvertCurrency(int tour_id, Integer curIncome, Float summ, Integer currOutgo) {
        Float res = 0f;

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + CURR1_ID + "," + CURR2_ID + "," + VAL1_ID + "," + VAL2_ID + " FROM " + TABLE_CURR_RATES + " WHERE "
                + CURR1_ID + " = " + curIncome + " and " + CURR2_ID + " = " + currOutgo +
                " and " + CURRATE_TOUR_ID + " = " + tour_id +
                " UNION " +
                "SELECT " + CURR2_ID + "," + CURR1_ID + "," + VAL2_ID + "," + VAL1_ID + " FROM " + TABLE_CURR_RATES + " WHERE "
                + CURR1_ID + " = " + currOutgo + " and " + CURR2_ID + " = " + curIncome +
                " and " + CURRATE_TOUR_ID + " = " + tour_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        float v1 = c.getFloat(2), v2 = c.getFloat(3);


        int curr1 = c.getInt(0), curr2 = c.getInt(1);


        if (curr1 == curIncome) {
            res = summ * v2 / v1;
        } else {
            res = summ * v1 / v2;
        }
        // res = summ / factor;

        Log.e(LOG, "curr1 = " + curr1 + "; curr2 = " + curr2 + "; v1 = " + v1 + "; v2 = " + v2 + "; summIn = " + summ);
        c.close();
        db.close();
        return res;
    }


    //Not used now
    public float CurrConvert(int tour_id, int currIn, float sumIn, int currOut) {
        float res = -1F;

        List<CurrRate> currRates = new ArrayList<CurrRate>();
        String selectQuery = "SELECT  * FROM " + TABLE_CURR_RATES
                + " WHERE " + CURRATE_TOUR_ID + " = " + tour_id + " and " +
                CURR1_ID + " = " + currIn + " and " + CURR2_ID + " = " + currOut
                + " UNION " +
                "SELECT  * FROM " + TABLE_CURR_RATES
                + " WHERE " + CURRATE_TOUR_ID + " = " + tour_id + " and " +
                CURR2_ID + " = " + currIn + " and " + CURR1_ID + " = " + currOut;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            CurrRate currRate = new CurrRate();

            currRate.setCurrrate_id(c.getInt(c.getColumnIndex(CURRATE_ID)));
            currRate.setTour_id(c.getInt(c.getColumnIndex(CURRATE_TOUR_ID)));
            currRate.setCurr1_id(c.getInt(c.getColumnIndex(CURR1_ID)));
            currRate.setCurr2_id(c.getInt(c.getColumnIndex(CURR2_ID)));
            currRate.setVal1(c.getFloat(c.getColumnIndex(VAL1_ID)));
            currRate.setVal2(c.getFloat(c.getColumnIndex(VAL2_ID)));

            if (currRate.getCurr1_id() == currIn) {

                res = sumIn * currRate.getVal2() / currRate.getVal1();
            } else {

                res = sumIn * currRate.getVal1() / currRate.getVal2();
            }

            Log.e(LOG, selectQuery);

        }

        Log.e(LOG, "currRates" + currRates.size());
        c.close();
        db.close();
        return res;
    }

    public List<Float> getArticleValuesByCurr(int tour_id, int curr_id, int article_id) {
        List<Float> values = new ArrayList<Float>();


        String selectQuery = "SELECT " + TOUR_ITEM_CURR_ID + "," + TOUR_ITEM_CURR_AMOUNT + " FROM " + TABLE_TOUR_ITEMS + " WHERE " + TOUR_ITEM_TOUR_ID + " = " + tour_id +
                " AND " + TOUR_ITEM_ARTICLE_ID + " = " + article_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int itemCurr = c.getInt(c.getColumnIndex(TOUR_ITEM_CURR_ID));
                float itemAmount = c.getFloat(c.getColumnIndex(TOUR_ITEM_CURR_AMOUNT));
                if (itemCurr != curr_id)
                    itemAmount = ConvertCurrency(tour_id, itemCurr, itemAmount, curr_id);
                values.add(itemAmount);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return values;
    }

    public Map<Integer, Currency> getTourCurrenciesInfo(int tour_id) {
        Map<Integer, Currency> currencies = new HashMap<Integer, Currency>();
        String selectQuery = "select * from " + TABLE_CURR + " WHERE " + CURR_ID + " in (SELECT  " +
                " " + TOUR_ITEM_CURR_ID + " FROM " + TABLE_TOUR_ITEMS + " items " +
                " WHERE " +
                TOUR_ITEM_TOUR_ID + " = " + tour_id + ")";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Currency currency = new Currency();
                currency.setId(c.getInt((c.getColumnIndex(CURR_ID))));
                currency.setName(c.getString(c.getColumnIndex(CURR_NAME)));
                currency.setDigit_code(c.getInt(c.getColumnIndex(CURR_DIGIT_CODE)));
                currency.setWord_code(c.getString(c.getColumnIndex(CURR_WORD_CODE)));
                currencies.put(currency.getId(), currency);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return currencies;
    }

    public List<Integer> getTourCurrencies(int tour_id) {
        List<Integer> currencies = new LinkedList<Integer>();
        String selectQuery = "SELECT  " +
                "distinct " + TOUR_ITEM_CURR_ID + " FROM " + TABLE_TOUR_ITEMS + " items " +
                " WHERE " +
                TOUR_ITEM_TOUR_ID + " = " + tour_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                currencies.add(c.getInt(0));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return currencies;
    }

    public Map<Integer, Float> getTourArticleSum(int tour_id, int article_id, int tourItemType) {
        Map<Integer, Float> sumMap = new HashMap<Integer, Float>();

        String selectQuery = "";

        if (tourItemType >= 0) {
            selectQuery = "SELECT  " +
                    TOUR_ITEM_CURR_ID +
                    ", sum (items." + TOUR_ITEM_CURR_AMOUNT + ")" +
                    " FROM " + TABLE_TOUR_ITEMS + " items " +
                    " WHERE " +
                    TOUR_ITEM_TOUR_ID + " = " + tour_id +
                    " AND " + TOUR_ITEM_ARTICLE_ID + " = " + article_id +
                    " and " + TOUR_ITEM_TYPE + " = " + tourItemType +
                    " group by items." + TOUR_ITEM_CURR_ID;
        } else {
            selectQuery = "SELECT  " +
                    TOUR_ITEM_CURR_ID +
                    ", sum ( case when " + TOUR_ITEM_TYPE + " >0 then -items." + TOUR_ITEM_CURR_AMOUNT + " else (items." + TOUR_ITEM_CURR_AMOUNT + ") end)" +
                    // ", sum (items." + TOUR_ITEM_CURR_AMOUNT + ")" +
                    " FROM " + TABLE_TOUR_ITEMS + " items " +
                    " WHERE " +
                    TOUR_ITEM_TOUR_ID + " = " + tour_id +
                    " AND " + TOUR_ITEM_ARTICLE_ID + " = " + article_id +

                    " group by items." + TOUR_ITEM_CURR_ID;
        }

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Log.e(LOG, "cur_id=" + c.getInt(0) + "; sum=" + c.getFloat(1));
                sumMap.put(c.getInt(0), c.getFloat(1));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return sumMap;
    }

    public Map<Integer, Float> getTourAllSum(int tour_id, int tourItemType) {
        Map<Integer, Float> sumMap = new HashMap<Integer, Float>();

        String selectQuery = "";

        if (tourItemType >= 0) {
            selectQuery = "SELECT  " +
                    TOUR_ITEM_CURR_ID +
                    ", sum (items." + TOUR_ITEM_CURR_AMOUNT + ")" +
                    " FROM " + TABLE_TOUR_ITEMS + " items " +
                    " WHERE " +
                    TOUR_ITEM_TOUR_ID + " = " + tour_id +
                    " and " + TOUR_ITEM_TYPE + " = " + tourItemType +
                    " group by items." + TOUR_ITEM_CURR_ID;
        } else {
            selectQuery = "SELECT  " +
                    TOUR_ITEM_CURR_ID +
                    ", sum ( case when " + TOUR_ITEM_TYPE + " >0 then -items." + TOUR_ITEM_CURR_AMOUNT + " else (items." + TOUR_ITEM_CURR_AMOUNT + ") end)" +
                    // ", sum (items." + TOUR_ITEM_CURR_AMOUNT + ")" +
                    " FROM " + TABLE_TOUR_ITEMS + " items " +
                    " WHERE " +
                    TOUR_ITEM_TOUR_ID + " = " + tour_id +
                    " group by items." + TOUR_ITEM_CURR_ID;
        }

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Log.e(LOG, "cur_id=" + c.getInt(0) + "; sum=" + c.getFloat(1));
                sumMap.put(c.getInt(0), c.getFloat(1));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return sumMap;
    }

    public String arrayToString(List<Integer> objects) {
        Log.e(LOG, objects.toString());
        String res = "";
        for (Integer object : objects)
            res += object + ",";
        Log.e(LOG, "res1 = " + res);
        if (objects.size() > 0) res = res.substring(0, res.length() - 1);
        Log.e(LOG, "res2 = " + res);
        return res;
    }

    public List<Date> getTourItemsDates(List<Integer> tourIds) {
        List<Date> tourDates = new ArrayList<Date>();

        String selectQuery = "SELECT  " +
                "distinct date(" + TOUR_ITEM_TOUR_DATE + ") FROM " + TABLE_TOUR_ITEMS + " items " +
                " WHERE " +
                TOUR_ITEM_TOUR_ID + " in (" + arrayToString(tourIds) + ")";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Log.e(LOG, "next date = " + c.getString(0));
                try {
                    tourDates.add(new SimpleDateFormat(DATE_FORMAT).parse(c.getString(0)));
                } catch (ParseException e) {
                    Log.e(LOG, "Parse date exception = " + e.getStackTrace());
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tourDates;
    }

    public List<Article> getTourItemsArticles(List<Integer> tourIds) {
        List<Article> tourArticles = new ArrayList<Article>();

        String selectQuery = " SELECT * FROM " + TABLE_BUY_ARTICLE + " WHERE " + ARTICLE_ID + " in (SELECT  " +
                "distinct " + TOUR_ITEM_ARTICLE_ID + " FROM " + TABLE_TOUR_ITEMS + " items " +
                " WHERE " +
                TOUR_ITEM_TOUR_ID + " in (" + arrayToString(tourIds) + "))";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Article t = new Article();
                t.setId(c.getInt((c.getColumnIndex(ARTICLE_ID))));
                t.setName(c.getString(c.getColumnIndex(ARTICLE_NAME)));

                // adding to tags list
                tourArticles.add(t);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tourArticles;
    }


    /**
     * getting all TourItems
     */
    public List<TourItem> getAllTourItems() {
        List<TourItem> tourItems = new ArrayList<TourItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOUR_ITEMS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TourItem tourItem = new TourItem();
                tourItem.setId(c.getInt((c.getColumnIndex(TOUR_ITEM_ID))));
                tourItem.setTour_id(c.getInt(c.getColumnIndex(TOUR_ITEM_TOUR_ID)));
                tourItem.setTourist_id(c.getInt(c.getColumnIndex(TOUR_ITEM_TOURIST_ID)));
                tourItem.setArticle_id(c.getInt(c.getColumnIndex(TOUR_ITEM_ARTICLE_ID)));
                tourItem.setCurr_id(c.getInt(c.getColumnIndex(TOUR_ITEM_CURR_ID)));
                tourItem.setCurr_amount(c.getFloat(c.getColumnIndex(TOUR_ITEM_CURR_AMOUNT)));
                tourItem.setDay(c.getInt(c.getColumnIndex(TOUR_ITEM_TOUR_DAY)));
                tourItem.setTour_date(c.getString(c.getColumnIndex(TOUR_ITEM_TOUR_DATE)));
                tourItem.setTour_descr(c.getString(c.getColumnIndex(TOUR_ITEM_DECRIBE)));
                tourItem.setItem_type(c.getInt(c.getColumnIndex(TOUR_ITEM_TYPE)));
                // adding to tags list
                tourItems.add(tourItem);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tourItems;
    }


    /*
     * Updating a TourItem
     */
    public int updateTourItem(TourItem tourItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //   values.put(TOUR_ITEM_TOUR_ID, tourItem.getTour_id());
        values.put(TOUR_ITEM_ARTICLE_ID, tourItem.getArticle_id());
        values.put(TOUR_ITEM_TOURIST_ID, tourItem.getTourist_id());
        values.put(TOUR_ITEM_CURR_ID, tourItem.getCurr_id());
        values.put(TOUR_ITEM_CURR_AMOUNT, tourItem.getCurr_amount());
        //    values.put(TOUR_ITEM_TOUR_DAY, tourItem.getDay());
        //    values.put(TOUR_ITEM_TOUR_DATE, tourItem.getTour_date());
        values.put(TOUR_ITEM_DECRIBE, tourItem.getTour_descr());
        values.put(TOUR_ITEM_TYPE, tourItem.getItem_type());

        // updating row
        int res = db.update(TABLE_TOUR_ITEMS, values, TOUR_ITEM_ID + " = ?",
                new String[]{String.valueOf(tourItem.getId())});


        db.close();

        return res;
    }

    /*
     * Deleting a TourItem
     */
    public void deleteTourItem(int tourItem_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOUR_ITEMS, TOUR_ITEM_ID + " = ?",
                new String[]{String.valueOf(tourItem_id)});

        db.close();
    }


    // ------------------------ "Currency" table methods ----------------//
    /*
    * Creating CurrRATE
    */
    public int createTourist(Tourist tourist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(TOURIST_ID , tourist.getTourist_id());
        values.put(TOURIST_TOUR_ID, tourist.getTour_id());
        values.put(TOURIST_NAME, tourist.getTourist_name());
        values.put(TOURIST_DESCR, tourist.getDescr());

        // insert row
        Long currency_id = db.insert(TABLE_TOURISTS, null, values);

        db.close();
        return currency_id.intValue();
    }

    /*
    * get single Currency
    */
    public Tourist getTourist(int tourist_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TOURISTS + " WHERE "
                + TOURIST_ID + " = " + tourist_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Tourist tourist = new Tourist();

        tourist.setTourist_id(c.getInt(c.getColumnIndex(TOURIST_ID)));
        tourist.setTour_id(c.getInt(c.getColumnIndex(TOURIST_TOUR_ID)));
        tourist.setTourist_name(c.getString(c.getColumnIndex(TOURIST_NAME)));
        tourist.setDescr(c.getString(c.getColumnIndex(TOURIST_DESCR)));
        c.close();
        db.close();
        return tourist;
    }

    /**
     * getting all Currency
     */
    public List<Tourist> getAllTourists() {
        List<Tourist> tourists = new ArrayList<Tourist>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOURISTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tourist tourist = new Tourist();

                tourist.setTourist_id(c.getInt(c.getColumnIndex(TOURIST_ID)));
                tourist.setTour_id(c.getInt(c.getColumnIndex(TOURIST_TOUR_ID)));
                tourist.setTourist_name(c.getString(c.getColumnIndex(TOURIST_NAME)));
                tourist.setDescr(c.getString(c.getColumnIndex(TOURIST_DESCR)));
                ;

                tourists.add(tourist);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tourists;
    }

    public List<Tourist> getTourTourists(int tour_id) {
        List<Tourist> tourists = new ArrayList<Tourist>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOURISTS + " WHERE " + TOURIST_TOUR_ID + " = " + tour_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                CurrRate currRate = new CurrRate();
                Tourist tourist = new Tourist();

                tourist.setTourist_id(c.getInt(c.getColumnIndex(TOURIST_ID)));
                tourist.setTour_id(c.getInt(c.getColumnIndex(TOURIST_TOUR_ID)));
                tourist.setTourist_name(c.getString(c.getColumnIndex(TOURIST_NAME)));
                tourist.setDescr(c.getString(c.getColumnIndex(TOURIST_DESCR)));
                ;

                tourists.add(tourist);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tourists;
    }

    public List<TouristSum> getSumTourists(int tour_id) {
        List<TouristSum> tourists = new ArrayList<TouristSum>();
        String selectQuery = "SELECT " +
                TOUR_ITEM_TOURIST_ID + ", sum (" + TOUR_ITEM_CURR_AMOUNT + "), " + TOUR_ITEM_CURR_ID +
                " FROM " + TABLE_TOUR_ITEMS + " WHERE " + TOUR_ITEM_TOUR_ID + " = " + tour_id +
                " and " + TOUR_ITEM_TYPE + " = " + " 0 " +
                " GROUP BY " + TOUR_ITEM_TOURIST_ID + ", " + TOUR_ITEM_CURR_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        int tourist_id = 0, prev_id = 0;
        HashMap<Integer, Float> sumMap = new HashMap<Integer, Float>();
        TouristSum touristSum = new TouristSum();
        Tourist tourist;
        Log.e(LOG, "c.getColumnNames().length " + c.getColumnNames().length);
        if (c.moveToFirst()) {

            do {
                tourist_id = c.getInt(0);
                Log.e(LOG, " prev_ id = " + prev_id + "; tourist_id = " + tourist_id);
                Log.e(LOG, " tourist_id = " + c.getInt(0) + " sum = " + c.getFloat(1) + " curr = " + c.getInt(2));

                if (prev_id == 0 || tourist_id == prev_id) {
                    prev_id = c.getInt(0);
                    sumMap.put(c.getInt(2), c.getFloat(1));
                } else {
                    tourist = getTourist(prev_id);
                    touristSum.setTourist(tourist);
                    touristSum.setSumsInCur(sumMap);
                    tourists.add(touristSum);
                    prev_id = tourist_id;
                    sumMap = new HashMap<Integer, Float>();
                    touristSum = new TouristSum();
                    sumMap.put(c.getInt(2), c.getFloat(1));
                }

            } while (c.moveToNext());

            Log.e(LOG, "AFTER LOOP prev_ id = " + prev_id + "; tourist_id = " + tourist_id);
            //if (prev_id != tourist_id)
            {
                tourist = getTourist(prev_id);
                touristSum.setTourist(tourist);
                touristSum.setSumsInCur(sumMap);
                tourists.add(touristSum);
            }
        }
        c.close();
        db.close();
        return tourists;
    }


    /*
     * Updating a Tourist
     */
    public int updateTourist(Tourist tourist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TOURIST_NAME, tourist.getTourist_name());
        values.put(TOURIST_DESCR, tourist.getDescr());

        // updating row
        int res = db.update(TABLE_TOURISTS, values, TOURIST_ID + " = ? ",
                new String[]{
                        String.valueOf(tourist.getTourist_id())});


        db.close();

        return res;
    }

    /*
     * Deleting a Currency
     */
    public void deleteTourist(Tourist tourist) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TOURISTS, TOURIST_ID + " = ? ",
                new String[]{
                        String.valueOf(tourist.getTourist_id())});

        db.close();
    }

    public int createItemTourist(ItemTourist itemTourist){
        //CREATE_TABLE_TOUR_ITEMS_TOURIST
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(TOURIST_ID , tourist.getTourist_id());
        values.put(TOUR_ITEM_TOURIST_ITEM_ID , itemTourist.getTourItem());
        values.put(TOUR_ITEM_TOURIST_TOURIST_ID , itemTourist.getTouristId());
        values.put(TOUR_ITEM_TOURIST_AMOUNT , itemTourist.getTouristAmount());
        values.put(TOUR_ITEM_TOURIST_CURR_AMOUNT, itemTourist.getCurrAmount());

        // insert row
        Long item_tourist_id = db.insert(TABLE_TOUR_ITEMS_TOURIST, null, values);

        db.close();
        return item_tourist_id.intValue();

    }

    public List<ItemTourist> getAllItemTourist(int item_id){
        List<ItemTourist> itemTourists = new ArrayList<ItemTourist>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOUR_ITEMS_TOURIST + " WHERE " + TOUR_ITEM_TOURIST_ITEM_ID + " = " + item_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ItemTourist itemTourist = new ItemTourist();

                itemTourist.setTouristId(c.getInt(c.getColumnIndex(TOUR_ITEM_TOURIST_TOURIST_ID)));
                itemTourist.setTourItem(c.getInt(c.getColumnIndex(TOUR_ITEM_TOURIST_ITEM_ID )));
                itemTourist.setTouristAmount(c.getFloat(c.getColumnIndex(TOUR_ITEM_TOURIST_AMOUNT )));

                itemTourists.add(itemTourist);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return itemTourists;
    }

    public List<String> getAllItemTouristsId(int item_id){
        List<String> itemTourists = new ArrayList<String>();
        String selectQuery = "SELECT " + TOUR_ITEM_TOURIST_TOURIST_ID +" FROM " +
                TABLE_TOUR_ITEMS_TOURIST + " WHERE " + TOUR_ITEM_TOURIST_ITEM_ID + " = " + item_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                itemTourists.add(String.valueOf(c.getInt(c.getColumnIndex(TOUR_ITEM_TOURIST_TOURIST_ID))));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return itemTourists;
    }

    public List<Integer> getAllItemTouristsIdInt(int item_id){
        List<Integer> itemTourists = new ArrayList<Integer>();
        String selectQuery = "SELECT " + TOUR_ITEM_TOURIST_TOURIST_ID +" FROM " +
                TABLE_TOUR_ITEMS_TOURIST + " WHERE " + TOUR_ITEM_TOURIST_ITEM_ID + " = " + item_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                itemTourists.add(c.getInt(c.getColumnIndex(TOUR_ITEM_TOURIST_TOURIST_ID)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return itemTourists;
    }


    public int updateItemTourist(List<String> tourist_ids, int item_id, float touristAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        //StringUtils.join(["a", "b", "c"], ',');
        ContentValues values = new ContentValues();
       // values.put(TOURIST_NAME, tourist.getTourist_name());
        values.put(TOUR_ITEM_TOURIST_AMOUNT, touristAmount);

        // updating row
        int res = db.update(TABLE_TOUR_ITEMS_TOURIST, values, TOUR_ITEM_TOURIST_ITEM_ID + " = " + item_id + " AND " +
                TOUR_ITEM_TOURIST_TOURIST_ID + " = ? ",
                tourist_ids.toArray(new String[tourist_ids.size()]));

        db.close();

        return res;
    }

    public void deleteTourTourists(int tour_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOURISTS, TOURIST_TOUR_ID + " = ? ",
                new String[]{
                        String.valueOf(tour_id)});

        db.close();
    }

    public void deleteAllTourists() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOURISTS, null, null);

        db.close();
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    /**
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
