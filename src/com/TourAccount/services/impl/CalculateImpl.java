package com.TourAccount.services.impl;

import android.content.Context;
import android.util.Log;
import com.TourAccount.model.*;
import com.TourAccount.model.Currency;
import com.TourAccount.services.Calculate;
import com.TourAccount.sqlite.DatabaseHandler;

import java.util.*;

/**
 * User: User
 * Date: 28.12.14
 * Time: 18:47
 */
public class CalculateImpl implements Calculate {

    // Database Helper
    DatabaseHandler db;
    Context mContext;
    private static final String LOG = "CalculateImpl";

    public void DataBaseHandler(Context mContext) {
        this.mContext = mContext;
        db = new DatabaseHandler(mContext);
    }

    public void CloseDataBase() {
        try {
            db.closeDB();
        } catch (Exception ex) {
            Log.e(LOG, "ex = " + ex.getMessage());
        }

    }

    @Override
    public List<TouristSum> getDeltasSum(List<TouristSum> touristSums, Map<Integer, Float> avgSum) {
        //для каждой валюты суммы, потраченной туристом, посчитать дельту в соот со средней суммой в каждой валюте
        List<TouristSum> result = new ArrayList<TouristSum>();
        for (TouristSum touristSum : touristSums) {
            TouristSum touristSumRes = new TouristSum();
            touristSumRes.setTourist(touristSum.getTourist());
            Map<Integer, Float> deltaSum = new HashMap<Integer, Float>();
            for (Integer currSum : touristSum.getSumsInCur().keySet()) {
                for (Integer currId : avgSum.keySet()) {
                    if (currId == currSum) {
                        deltaSum.put(currId, (touristSum.getSumsInCur().get(currSum) - avgSum.get(currId)));
                    }
                }
            }
            touristSumRes.setSumsInCur(deltaSum);
            result.add(touristSumRes);
        }

        return result;
    }


    @Override
    public List<TouristSum> getTouristSumInCurs(List<TouristSum> touristSums, int tour_id) {
        List<TouristSum> res = new ArrayList<TouristSum>();
        for (TouristSum touristSum : touristSums) {
            TouristSum resSum = new TouristSum();
            resSum.setTourist(touristSum.getTourist());
            resSum.setSumsInCur(GetSumCurrId(touristSum.getSumsInCur(), tour_id));
            res.add(resSum);
        }
        return res;
    }

    public Currency getCurrency(Map<Integer, Float> sumMap) {

        Currency currency = new Currency();
        for (Integer curr : sumMap.keySet()) {
            currency = db.getCurrency(curr);
        }
        return currency;
    }

    public Map<Integer, Float> GetAverageSums(Map<Integer, Float> sumMap, int touristCnt) {
        Map<Integer, Float> resSum = new HashMap<Integer, Float>();
        for (Integer curr : sumMap.keySet()) {
            resSum.put(curr, sumMap.get(curr) / touristCnt);
        }
        return resSum;
    }

    public Map<Integer, Float> GetSumCurrId(Map<Integer, Float> sumMap, int tour_id) {
        Map<Integer, Float> resSum = new HashMap<Integer, Float>();
        for (Integer curr : sumMap.keySet()) {
            //  curr_id = curr;
            float sumInCur = 0F;
            Currency currency = db.getCurrency(curr);

            Log.e(LOG, "Считаем сумму в " + currency.getName());
            for (Integer innerCur : sumMap.keySet()) {
                Currency innerCurrency = db.getCurrency(innerCur);
                //
                if (curr != innerCur) {
                    //Есть еще другая функция ConvertCurrency
                    // float res = db.CurrConvert(tour_id, innerCur, sumMap.get(innerCur), curr);
                    float res = db.ConvertCurrency(tour_id, innerCur, sumMap.get(innerCur), curr);
                    if (res < 0) res = -res;
                    if (sumMap.get(innerCur) > 0)
                        sumInCur += res;
                    else
                        sumInCur -= res;

                    Log.e(LOG, sumMap.get(innerCur) + " " + innerCurrency.getName() + " = " + res + " " + currency.getName());

                } else {
                    sumInCur += sumMap.get(innerCur);
                    // Log.e(LOG, sumMap.get(innerCur) + " " + currency.getName() + " = " + sumMap.get(curr) + " " + innerCurrency.getName());
                }
            }

            resSum.put(curr, sumInCur);
        }
        return resSum;
    }

    public Map<Currency, Float> GetSum(Map<Integer, Float> sumMap, int tour_id) {
        Map<Currency, Float> resSum = new HashMap<Currency, Float>();
        for (Integer curr : sumMap.keySet()) {
            //  curr_id = curr;
            float sumInCur = 0F;
            Currency currency = db.getCurrency(curr);

            Log.e(LOG, "Считаем сумму в " + currency.getName());
            for (Integer innerCur : sumMap.keySet()) {
                Currency innerCurrency = db.getCurrency(innerCur);
                //
                if (curr != innerCur) {
                    //Есть еще другая функция ConvertCurrency
                    // float res = db.CurrConvert(tour_id, innerCur, sumMap.get(innerCur), curr);
                    float res = db.ConvertCurrency(tour_id, innerCur, sumMap.get(innerCur), curr);
                    if (res < 0) res = -res;
                    if (sumMap.get(innerCur) > 0)
                        sumInCur += res;
                    else
                        sumInCur -= res;

                    Log.e(LOG, sumMap.get(innerCur) + " " + innerCurrency.getName() + " = " + res + " " + currency.getName());

                } else {
                    sumInCur += sumMap.get(innerCur);

                    // Log.e(LOG, sumMap.get(innerCur) + " " + currency.getName() + " = " + sumMap.get(curr) + " " + innerCurrency.getName());
                }
            }

            resSum.put(currency, sumInCur);
        }
        return resSum;
    }

    public String makeResSum(Map<Currency, Float> curSums) {
        String resString = "";
        for (Currency currency : curSums.keySet()) {
            resString += " Итого в " + currency.getName() + " = " + curSums.get(currency) + "\n";
        }
        return resString;
    }

    public List<DebtorPair> makePair(Tour tour, int curr_id) {
        Log.d(LOG, "makePair curr_id = " + curr_id);
        //получаем суммы потраченные каждым туристом, сгруппированные по валютам
        List<TouristSum> touristSums = db.getSumTourists(tour.getId());
        Log.d(LOG, "makePair touristSums" + touristSums.toString());
        Map<Integer, Float> sumMap = db.getTourAllSum(tour.getId(), TourEnum.TourItemType.ALL.value);
        Log.d(LOG, "makePair sumMap" + sumMap.toString());
        List<TouristSum> touristSumList = getTouristSumInCurs(touristSums, tour.getId());
        Log.d(LOG, "makePair touristSumList" + touristSumList.toString());
        touristSumList = getDeltasSum(touristSumList,
                GetAverageSums(GetSumCurrId(sumMap, tour.getId()),
                        tour.getTourist_cnt()));
        Log.d(LOG, "makePair touristSumList" + touristSumList.toString());
        Map<Integer, Debtor> debtors = new HashMap<Integer, Debtor>();
        Map<Integer, Debtor> antiDebtors = new HashMap<Integer, Debtor>();
        for (TouristSum touristSum : touristSumList) {

            for (Integer curr : touristSum.getSumsInCur().keySet()) {
                Currency currency = db.getCurrency(curr);
                if (touristSum.getSumsInCur().get(curr).floatValue() < 0) {
                    if (!debtors.containsKey(touristSum.getTourist().getTourist_id())) {
                        //конвертация
                        float res = -touristSum.getSumsInCur().get(curr);
                        if (curr.intValue() != curr_id) {
                            Log.d(LOG, "makePair curr.intValue() = " + curr.intValue());
                            res = db.ConvertCurrency(tour.getId(), curr, -touristSum.getSumsInCur().get(curr),
                                    curr_id);
                        }
                        Debtor debtor = new Debtor();
                        debtor.debtorId = touristSum.getTourist().getTourist_id();
                        debtor.debtorSum = res;
                        debtors.put(touristSum.getTourist().getTourist_id(), debtor);
                    }
                } else {
                    if (!antiDebtors.containsKey(touristSum.getTourist().getTourist_id())) {
                        //конвертация
                        float res = touristSum.getSumsInCur().get(curr);
                        if (curr != curr_id)
                            res = db.ConvertCurrency(tour.getId(), curr, touristSum.getSumsInCur().get(curr), curr_id);
                        Debtor debtor = new Debtor();
                        debtor.debtorId = touristSum.getTourist().getTourist_id();
                        debtor.debtorSum = res;
                        Log.e(LOG, " add antiDebtors " + debtor);
                        antiDebtors.put(touristSum.getTourist().getTourist_id(), debtor);
                    }
                }


            }
        }
        return debtors(debtors, antiDebtors);
    }

    @Override
    public List<DebtorPair> makeResultPairs(int tour_id) {
        List<DebtorPair> result = new ArrayList<DebtorPair>();

        List<TourItem> tourItems = db.getAllTourItems(tour_id);
        for (TourItem tourItem : tourItems) {
            List<DebtorPair> debtorPairs = makePairsOneItem(tourItem.getId());
            for (DebtorPair debtorPair : debtorPairs) {
                if (!result.contains(debtorPair)) {
                    //если еще такой пары не было, то просто добавляем ее в результат
                    result.add(debtorPair);
                } else {
                    //если такая пара уже была, то нужно ее извлечь из коллекции,
                    //конвертировать сумму в ту же валюту, сложить и запилить обратно в коллекуцию
                    int index = result.indexOf(debtorPair);
                    DebtorPair alreadyExistsPair = result.get(index);
                    result.remove(index);

                    //конвертация
                    int curr = alreadyExistsPair.currId;
                    if (curr != debtorPair.currId) {
                        float res = db.ConvertCurrency(tour_id, debtorPair.currId, debtorPair.sum, curr);
                        //res - это сумма в валюте curr, которая изначально была в валюте debtorPair.currId
                        alreadyExistsPair.sum +=res;
                    }
                    result.add(alreadyExistsPair);
                }

            }
        }

        return result;
    }

    @Override
    public List<DebtorPair> makePairsOneItem(int item_id) {
        List<DebtorPair> result = new ArrayList<DebtorPair>();
        DebtorPair debtorPair;// = new DebtorPair();
        TourItem tourItem = db.getTourItem(item_id);
        List<ItemTourist> itemTouristList = db.getAllItemTourist(item_id);
        List<Integer> tourists_ids = db.getAllItemTouristsIdInt(item_id);
        if (tourists_ids.contains(tourItem.getTourist_id())) {
            //если тот, кто расчитывался, есть в списке тех, на кого пилить
            if (tourists_ids.size() == 1) {
                //если чувак сам за себя платил и никто ему не должен

            } else {
                //если чувак платил за себя и за других
                float AmountRest = tourItem.getCurr_amount() - tourItem.getCurr_amount() / tourists_ids.size();
                float AmountDebt = AmountRest / (tourists_ids.size() - 1);
                for (Integer tourist_id : tourists_ids) {
                    if (tourist_id != tourItem.getTourist_id()) {
                        debtorPair = new DebtorPair();
                        debtorPair.sum = AmountDebt;
                        debtorPair.debtorId = tourist_id;
                        debtorPair.antiDebtorId = tourItem.getTourist_id();
                        debtorPair.currId = tourItem.getCurr_id();
                        result.add(debtorPair);
                    }
                }

            }

        } else {
            //если чувак платил исключительно за других, а сам не участвовал
            float AmountRest = tourItem.getCurr_amount();
            float AmountDebt = AmountRest / tourists_ids.size();
            for (Integer tourist_id : tourists_ids) {
                if (tourist_id != tourItem.getTourist_id()) {
                    debtorPair = new DebtorPair();
                    debtorPair.sum = AmountDebt;
                    debtorPair.debtorId = tourist_id;
                    debtorPair.antiDebtorId = tourItem.getTourist_id();
                    debtorPair.currId = tourItem.getCurr_id();
                    result.add(debtorPair);
                }
            }

        }
        return result;
    }


    @Override
    public List<DebtorPair> debtors(Map<Integer, Debtor> debtors,  //те, кто должны
                                    Map<Integer, Debtor> antiDebtors   // те, кому должны
    ) {

        //Queue<Debtor> debtorsQ;  //те, кто должны
        // Queue<Debtor> antiDebtorsQ;   // те, кому должны

        List<DebtorPair> result = new ArrayList<DebtorPair>();
        DebtorPair debtorPair = new DebtorPair();
        for (Integer debtorKey : debtors.keySet()) {
            Debtor debtor = debtors.get(debtorKey);
            Log.e(LOG, "Следующий должник: сумма = " + debtor.debtorSum + "\t ид = " + debtor.debtorId);
            for (Integer antiDebtorKey : antiDebtors.keySet()) {
                Debtor antiDebtor = antiDebtors.get(antiDebtorKey);
                Log.e(LOG, "Следующий кому должны: сумма = " + antiDebtor.debtorSum + "\t ид = " + antiDebtor.debtorId);
                if (debtor.debtorSum > antiDebtor.debtorSum) {
                    debtorPair = new DebtorPair();
                    debtorPair.antiDebtorId = antiDebtor.debtorId;
                    debtorPair.debtorId = debtor.debtorId;
                    debtorPair.sum = antiDebtor.debtorSum;
                    debtor.debtorSum = debtor.debtorSum - antiDebtor.debtorSum;
                    result.add(debtorPair);
                    antiDebtors.remove(antiDebtor);  //удаляем того, кому должны. ему все вернули
                    ;//переходим к следующему КРЕДИТОРУ
                } else if (debtor.debtorSum == antiDebtor.debtorSum) {

                    debtorPair = new DebtorPair();
                    debtorPair.antiDebtorId = antiDebtor.debtorId;
                    debtorPair.debtorId = debtor.debtorId;
                    debtorPair.sum = antiDebtor.debtorSum;
                    result.add(debtorPair);
                    antiDebtors.remove(antiDebtor);   //удаляем того, кому должны. ему все вернули
                    debtors.remove(debtor); // удаляем должника, он все вернул
                    break;//переходим к следующему кому ДОЛЖНИКУ И КРЕДИТОРУ
                } else {
                    debtorPair = new DebtorPair();
                    debtorPair.antiDebtorId = antiDebtor.debtorId;
                    debtorPair.debtorId = debtor.debtorId;
                    debtorPair.sum = debtor.debtorSum;
                    antiDebtor.debtorSum = antiDebtor.debtorSum - debtor.debtorSum;
                    result.add(debtorPair);
                    debtors.remove(debtor);  // удаляем должника, он все вернул
                    break;//переходим к следующему кому ДОЛЖНИКУ
                    //System.out.println( debtors.remove() );
                }
            }
        }
        return result;
    }

}
