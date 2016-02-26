package com.TourAccount.model;

import java.util.List;

/**
 * User: User
 * Date: 21.02.15
 * Time: 12:20
 */
public class DebtorPair implements Comparable<DebtorPair> {
    public int debtorId;
    public int antiDebtorId;
    public float sum;
    public int currId;

    public DebtorPair() {
    }

    @Override
    public int compareTo(DebtorPair another) {
        //return 0;  //To change body of implemented methods use File | Settings | File Templates.

        if (another.debtorId == debtorId && another.antiDebtorId == antiDebtorId)
            return 0;
        else
            return 1;
    }

    @Override
    public String toString() {
        return "Этот id = " + debtorId + " должен этому  id = " + antiDebtorId + " вот столько sum = " + sum;
    }


    /*public static <E> int indexOf(List<E> list, E searchItem) {
        int index = 0;
        for (E item : list) {
            if (item == searchItem)
                return index;
            index += 1;
        }
        return -1;
    } */

    public static int indexOf(List<DebtorPair> list, DebtorPair searchItem) {
        int index = 0;
        for (DebtorPair item : list) {
            if (item.equals(searchItem))
                return index;
            index += 1;
        }
        return -1;
    }

    //@Override
    //public boolean equals(DebtorPair debtorPair) {
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(DebtorPair.class)) {
            DebtorPair debtorPair = (DebtorPair) obj;
            if (debtorPair.debtorId == debtorId && debtorPair.antiDebtorId == antiDebtorId)
                return true;
            else
                return false;
            //return this.name.equals(obj.name); //Naive implementation just to show
            //equals is based on the name field.
        }
        return false;
    }
}

