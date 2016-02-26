package com.TourAccount.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 22.09.14
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class CurrRate {
    int currrate_id;
    int tour_id;
    int curr1_id;
    int curr2_id;
    float val1;
    float val2;

    public int getCurrrate_id() {
        return currrate_id;
    }

    public void setCurrrate_id(int currrate_id) {
        this.currrate_id = currrate_id;
    }

    public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public int getCurr1_id() {
        return curr1_id;
    }

    public void setCurr1_id(int curr1_id) {
        this.curr1_id = curr1_id;
    }

    public int getCurr2_id() {
        return curr2_id;
    }

    public void setCurr2_id(int curr2_id) {
        this.curr2_id = curr2_id;
    }

    public float getVal1() {
        return val1;
    }

    public void setVal1(float val1) {
        this.val1 = val1;
    }

    public float getVal2() {
        return val2;
    }

    public void setVal2(float val2) {
        this.val2 = val2;
    }

    @Override
    public String toString() {
        return curr1_id + " " + curr2_id + " " + val1 + " " + val2;
    }

    public static List<CurrRate> GenerateCurrenciesPairs(List<Integer> currencies) {
        //Map<Integer,Integer> pairs=new HashMap<Integer, Integer>();
        List<CurrRate> currRatesList = new LinkedList<CurrRate>();
        if (currencies.size() == 1) {
            CurrRate currRates = new CurrRate();
            currRates.curr1_id = currencies.get(0);
            currRates.curr2_id = currencies.get(0);
            currRatesList.add(currRates);
        } else {
            for (Integer val1 : currencies)
                for (Integer val2 : currencies) {
                    if (val1 != val2) {
                        //if( pairs.get(val2)==val1)
                        if (containsPair(currRatesList, val1, val2))
                            continue;
                        CurrRate currRates = new CurrRate();
                        currRates.curr1_id = val1;
                        currRates.curr2_id = val2;
                        currRatesList.add(currRates);
                    }
                }
        }
        return currRatesList;
    }

    public static boolean containsPair(List<CurrRate> currRatesList, int cur1, int cur2) {
        boolean contains = false;

        for (CurrRate currRates : currRatesList) {
            if (currRates.curr1_id == cur1 && currRates.curr2_id == cur2) {
                contains = true;
                break;
            }
            if (currRates.curr2_id == cur1 && currRates.curr1_id == cur2) {
                contains = true;
                break;
            }
        }
        //   System.out.println("" + cur1 + " "+ cur2 + " contains " + contains);
        return contains;
    }


}
