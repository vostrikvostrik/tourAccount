package com.TourAccount.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 18.09.14
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public class Tour {

    private static final String LOG = "Tour";

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    int id;
    String name;
    Date date_begin;
    Date date_end;
    int tourist_cnt;
    int main_cur;
    int tour_type;
    int archive_type;

    // constructors
    public Tour() {
    }

    public Tour(String name, int tourist_cnt) {
        this.name = name;
        this.tourist_cnt = tourist_cnt;
    }

    public Tour(String name, int tourist_cnt, Date date_begin) {
        //this.id = id;
        this.name = name;
        this.tourist_cnt = tourist_cnt;
        this.date_begin = date_begin;
    }

    public Tour(int id, String name, int tourist_cnt) {
        this.id = id;
        this.name = name;
        this.tourist_cnt = tourist_cnt;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate_begin() {
        return date_begin;
    }

    public void setDate_begin(Date date_begin) {
        this.date_begin = date_begin;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public int getTourist_cnt() {
        return tourist_cnt;
    }

    public void setTourist_cnt(int tourist_cnt) {
        this.tourist_cnt = tourist_cnt;
    }

    public int getMain_cur() {
        return main_cur;
    }

    public void setMain_cur(int main_cur) {
        this.main_cur = main_cur;
    }

    public int getTour_type() {
        return tour_type;
    }

    public void setTour_type(int tour_type) {
        this.tour_type = tour_type;
    }

    public int getArchive_type() {
        return archive_type;
    }

    public void setArchive_type(int archive_type) {
        this.archive_type = archive_type;
    }

    @Override
    public String toString() {
        try{
        Log.e(LOG, "date_begin = " + date_begin);
        Log.e(LOG, "date_begin = " + date_begin.toString());
        return name + "; " + dateFormat.format(date_begin)
                + ";" + tourist_cnt + "чел.";
     //   return name + " " + date_begin + " " + date_end + " " + tourist_cnt;
        }
        catch (Exception ex){
            ex.printStackTrace();
                                 return name + "; " + tourist_cnt + "чел.";
        }
    }
}
