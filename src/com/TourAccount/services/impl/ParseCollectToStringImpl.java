package com.TourAccount.services.impl;

import com.TourAccount.model.DebtorPair;
import com.TourAccount.model.Tourist;
import com.TourAccount.services.ParseCollectToString;
import com.TourAccount.sqlite.DatabaseHandler;

import java.util.List;

/**
 * User: User
 * Date: 03.07.15
 * Time: 18:21
 */
public class ParseCollectToStringImpl implements ParseCollectToString {
    // Database Helper
    DatabaseHandler db;

    public ParseCollectToStringImpl(DatabaseHandler db){
        this.db = db;
    }

    @Override
    public String parseDebtorPairsToString(List<DebtorPair> debtorPairs) {
        String result="";
        for(DebtorPair debtorPair : debtorPairs){
            //Получить имя должника
            Tourist touristDebtor = db.getTourist(debtorPair.debtorId);
            //Получить имя кому должны
            Tourist touristAntiDebtor = db.getTourist(debtorPair.antiDebtorId);
            if(touristAntiDebtor !=null && touristDebtor!=null)
            result += touristDebtor.getTourist_name() + " должен "+ touristAntiDebtor.getTourist_name() + "  = " + debtorPair.sum+"\n";
        }

        return result;
    }
}
