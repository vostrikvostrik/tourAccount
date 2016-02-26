package com.TourAccount.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.TourAccount.R;
import com.TourAccount.model.*;
import com.TourAccount.model.Currency;
import com.TourAccount.services.impl.CalculateImpl;
import com.TourAccount.sqlite.DatabaseHandler;

import java.util.*;

/**
 * User: User
 * Date: 28.12.14
 * Time: 18:20
 */
public class TouristTotalActivity extends Activity {
    // Database Helper
    DatabaseHandler db;
    int tour_id;
    CalculateImpl calculate;

    private static final String LOG = "TouristTotalActivity";

    /**
     * Called when the activity is first created.            .
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_tourist);

        TextView textViewTotalTourist = (TextView) findViewById(R.id.textViewTotalTourist);

        // Принимаем ид тура
        tour_id = getIntent().getIntExtra("tour_id", 0);
        calculate = new CalculateImpl();
        calculate.DataBaseHandler(getApplicationContext());

        db = new DatabaseHandler(getApplicationContext());

        //валюта, в которой будут считаться долги
        Currency deltaCur = null;


        Tour tour = db.getTour(tour_id);

        //получаем все суммы тура, сгруппированные по валютам
        Map<Integer, Float> sumMap = db.getTourAllSum(tour_id, /*TourEnum.TourItemType.ALL.value*/
        TourEnum.TourItemType.OUTGOING.value);
        String total = "Деньги тратил(и): " +
                db.getAllTourTourists(tour_id) + " человек\n";
        total += "В туре было: " + //tour.getTourist_cnt();
                db.getAllTourTourists(tour_id) + " человек\n";
        Map<Integer, Float>  averageSums = calculate.GetAverageSums(calculate.GetSumCurrId(sumMap, tour_id),
                tour.getTourist_cnt());
        total += "\nСредний расход на человека:\n" ;
        for(Integer cur : averageSums.keySet()){
            total+= averageSums.get(cur).toString() +  db.getCurrency(cur).getWord_code()+"\n";
        }

        //получаем суммы потраченные каждым туристом, сгруппированные по валютам
        List<TouristSum> touristSums = db.getSumTourists(tour_id);
        for (TouristSum touristSum : touristSums) {
            total += "\n " + touristSum.getTourist().getTourist_name() + " потратил: ";
            for (Integer curr : touristSum.getSumsInCur().keySet()) {
                Currency currency = db.getCurrency(curr);
                total += "\n\t" + touristSum.getSumsInCur().get(curr) + " " + currency.getWord_code();

                //устанавливаем валюту, в которой будут считаться долги
                if (deltaCur == null)
                    deltaCur = currency;
            }
            total += "\nИтого расходов: \n" + calculate.makeResSum(calculate.GetSum(touristSum.getSumsInCur(), tour_id));
            total += "\n________________";
        }

        Log.e(LOG, "before getTouristSumInCurs");
        List<TouristSum> touristSumList = calculate.getTouristSumInCurs(touristSums, tour_id);
        Log.e(LOG, "touristSumList.size =" + touristSumList.size());
        touristSumList = calculate.getDeltasSum(touristSumList,
                calculate.GetAverageSums(calculate.GetSumCurrId(sumMap, tour_id),
                        tour.getTourist_cnt()));
        Log.e(LOG, "getDeltasSum touristSumList.size =" + touristSumList.size());
        Map<Integer, Debtor> debtors = new HashMap<Integer, Debtor>();
        Map<Integer, Debtor> antiDebtors = new HashMap<Integer, Debtor>();


        Log.e(LOG, "deltaCur = " + deltaCur);

        for (TouristSum touristSum : touristSumList) {
            total += "\n " + touristSum.getTourist().getTourist_name() + " дельта: ";

            for (Integer curr : touristSum.getSumsInCur().keySet()) {
                Currency currency = db.getCurrency(curr);
                total += "\n" + touristSum.getSumsInCur().get(curr) + " " + currency.getWord_code();
                //   Log.e(LOG, " add total " + total);
                if (touristSum.getSumsInCur().get(curr).floatValue() < 0) {
                    if (!debtors.containsKey(touristSum.getTourist().getTourist_id())) {
                        //конвертация
                        Log.e(LOG, "tour_id = " + tour_id);
                        Log.e(LOG, "curr = " + curr);
                        Log.e(LOG, "touristSum.getSumsInCur().get(curr) = " + (-touristSum.getSumsInCur().get(curr)));
                        Log.e(LOG, "deltaCur.getId() = " + deltaCur.getId());
                        float res = -touristSum.getSumsInCur().get(curr);
                        if (curr != deltaCur.getId())
                            res = db.ConvertCurrency(tour_id, curr, -touristSum.getSumsInCur().get(curr), deltaCur.getId());
                        Debtor debtor = new Debtor();
                        debtor.debtorId = touristSum.getTourist().getTourist_id();
                        debtor.debtorSum = res;
                        Log.e(LOG, " add debtors " + debtor);
                        debtors.put(touristSum.getTourist().getTourist_id(), debtor);
                    }
                } else {
                    if (!antiDebtors.containsKey(touristSum.getTourist().getTourist_id())) {
                        //конвертация
                        float res = touristSum.getSumsInCur().get(curr);
                        if (curr != deltaCur.getId())
                            res = db.ConvertCurrency(tour_id, curr, touristSum.getSumsInCur().get(curr), deltaCur.getId());
                        Debtor debtor = new Debtor();
                        debtor.debtorId = touristSum.getTourist().getTourist_id();
                        debtor.debtorSum = res;
                        Log.e(LOG, " add antiDebtors " + debtor);
                        antiDebtors.put(touristSum.getTourist().getTourist_id(), debtor);
                    }
                }


            }


            total += "\n________________";
        }


        List<DebtorPair> debtorPairs = calculate.debtors(debtors, antiDebtors);

        for (DebtorPair debtorPair : debtorPairs) {
            Tourist touristDebtor = db.getTourist(debtorPair.debtorId);
            Tourist touristAntiDebtor = db.getTourist(debtorPair.antiDebtorId);
            total += "\n Должник " + touristDebtor.getTourist_name() + " \t должен " + touristAntiDebtor.getTourist_name() +
                    "\t столько " + debtorPair.sum + " " + deltaCur.getWord_code();
        }

        total += "\n________________";


        textViewTotalTourist.setText(total);
    }
}
