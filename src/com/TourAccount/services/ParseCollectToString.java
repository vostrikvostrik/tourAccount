package com.TourAccount.services;

import com.TourAccount.model.DebtorPair;

import java.util.List;

/**
 * User: User
 * Date: 03.07.15
 * Time: 18:20
 */
public interface ParseCollectToString {

    String parseDebtorPairsToString(List<DebtorPair> debtorPairs);
}
