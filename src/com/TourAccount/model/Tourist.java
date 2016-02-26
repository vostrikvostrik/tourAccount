package com.TourAccount.model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 25.09.14
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class Tourist {
    int tourist_id;
    int tour_id;
    String tourist_name;
    String descr;

    public int getTourist_id() {
        return tourist_id;
    }

    public void setTourist_id(int tourist_id) {
        this.tourist_id = tourist_id;
    }

    public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public String getTourist_name() {
        return tourist_name;
    }

    public void setTourist_name(String tourist_name) {
        this.tourist_name = tourist_name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public String toString() {
        return tourist_name + "(" + descr + ")";
    }

    public static  Tourist search(ArrayList<Tourist> tourists, int tourist_id){

        for(Tourist tourist : tourists)
        {
            if(tourist.getTourist_id() == tourist_id)
                return  tourist;
        }
        return  null;
    }
}
