package com.TourAccount.model;

/**
 * User: User
 * Date: 21.02.15
 * Time: 12:20
 */
public class Debtor implements Comparable<Debtor> {
    public int debtorId;
    public float debtorSum;

    public Debtor() {
    }

    @Override
    public int compareTo(Debtor another) {
        return 0;
    }

    @Override
    public String toString() {
        return " debtorId: " + debtorId + "; sum = " + debtorSum;
    }
}
