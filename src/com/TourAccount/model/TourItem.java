package com.TourAccount.model;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 18.09.14
 * Time: 20:18
 * To change this template use File | Settings | File Templates.
 */
public class TourItem {
    int id;
    int tour_id;
    int tourist_id;
    int article_id;
    int curr_id;
    float curr_amount;
    int day;
    String tour_date;
    String tour_descr;
    int tourist_cnt;
    int item_type;

    public TourItem() {
    }

    public TourItem(int id, int tour_id, int article_id, int curr_id, float curr_amount, int day, String tour_date, String tour_descr, int item_type) {
        this.id = id;
        this.tour_id = tour_id;
        this.article_id = article_id;
        this.curr_id = curr_id;
        this.curr_amount = curr_amount;
        this.day = day;
        this.tour_date = tour_date;
        this.tour_descr = tour_descr;
        this.item_type = item_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public int getTourist_id() {
        return tourist_id;
    }

    public void setTourist_id(int tourist_id) {
        this.tourist_id = tourist_id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getCurr_id() {
        return curr_id;
    }

    public void setCurr_id(int curr_id) {
        this.curr_id = curr_id;
    }

    public float getCurr_amount() {
        return curr_amount;
    }

    public void setCurr_amount(float curr_amount) {
        this.curr_amount = curr_amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTour_date() {
        return tour_date;
    }

    public void setTour_date(String tour_date) {
        this.tour_date = tour_date;
    }

    public String getTour_descr() {
        return tour_descr;
    }

    public void setTour_descr(String tour_descr) {
        this.tour_descr = tour_descr;
    }

    public int getTourist_cnt() {
        return tourist_cnt;
    }

    public void setTourist_cnt(int tourist_cnt) {
        this.tourist_cnt = tourist_cnt;
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    @Override
    public String toString() {
        return //tour_descr+
                (item_type == 0 ? "расход." : "приход.") + ": " + curr_amount + " ";
    }
}
