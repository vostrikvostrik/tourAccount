package com.TourAccount.model;

import java.util.Map;

/**
 * User: User
 * Date: 28.12.14
 * Time: 19:47
 */
public class TouristSum {
    Tourist tourist;
    Map<Integer, Float> sumsInCur;

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public Map<Integer, Float> getSumsInCur() {
        return sumsInCur;
    }

    public void setSumsInCur(Map<Integer, Float> sumsInCur) {
        this.sumsInCur = sumsInCur;
    }
}
