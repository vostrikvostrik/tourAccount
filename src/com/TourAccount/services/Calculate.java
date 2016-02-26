package com.TourAccount.services;

import android.content.Context;
import com.TourAccount.model.Currency;
import com.TourAccount.model.Debtor;
import com.TourAccount.model.DebtorPair;
import com.TourAccount.model.TouristSum;

import java.util.List;
import java.util.Map;

/**
 * User: User
 * Date: 28.12.14
 * Time: 18:48
 */
public interface Calculate {

    //инициализируем контекст
    void DataBaseHandler(Context mContext);

    //Возвращаем список сумм итого, потраченных по туру, в каждой валюте тура
    Map<Currency, Float> GetSum(Map<Integer, Float> sumMap, int tour_id);

    //получаем строку: сумма в каждой валюте из списка
    String makeResSum(Map<Currency, Float> curSums);

    //закрываем соединение с базой данных
    void CloseDataBase();

    //для каждой валюты суммы, потраченной туристом, посчитать дельту в соот со средней суммой в каждой валюте
    List<TouristSum> getDeltasSum(List<TouristSum> touristSum, Map<Integer, Float> avgSum);

    //
    List<TouristSum> getTouristSumInCurs(List<TouristSum> touristSums, int tour_id);

    //Получения списка пар, кто кому сколько должен отдатьв  конце тура
    //Метод используется, если расходы несло несколько человек
    List<DebtorPair> debtors(Map<Integer, Debtor> debtors,  //те, кто должны
                             Map<Integer, Debtor> antiDebtors   // те, кому должны
    );

    //поиск пар должников в контексте одной записи по туру
    List<DebtorPair> makePairsOneItem(int item_id);

    List<DebtorPair> makeResultPairs(int tour_id);
}
