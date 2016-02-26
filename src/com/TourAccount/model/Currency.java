package com.TourAccount.model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 18.09.14
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class Currency {

    int id;
    //  int tour_id;
    String name;
    int digit_code;
    String word_code;
    //   float curr_rate;

    // constructors
    public Currency() {
    }

    public Currency(String name, int digit_code, String word_code) {
        // this.id = id;
        this.name = name;
        this.digit_code = digit_code;
        this.word_code = word_code;
    }

    // getters and setters
   /* public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }   */

  /*  public float getCurr_rate() {
        return curr_rate;
    }

    public void setCurr_rate(float curr_rate) {
        this.curr_rate = curr_rate;
    }   */

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

    public int getDigit_code() {
        return digit_code;
    }

    public void setDigit_code(int digit_code) {
        this.digit_code = digit_code;
    }

    public String getWord_code() {
        return word_code;
    }

    public void setWord_code(String word_code) {
        this.word_code = word_code;
    }

    @Override
    public String toString() {
        return name + "(" + word_code + ")";
    }

    public static  Currency search(ArrayList<Currency> currencies, int curr_id){

        for(Currency currency1 : currencies)
        {
            if(currency1.getId() == curr_id)
                return  currency1;
        }
        return  null;
    }

    public boolean equals(Currency currency) {
        //return this.getId().equals(currency.getId());
        if(this.getId() == currency.getId())
            return true;
        else
            return false;
    }
}
