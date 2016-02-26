package com.TourAccount.model;

import java.util.List;

/**
 * User: User
 * Date: 19.02.15
 * Time: 15:05
 */
public class DataBaseModel {

    private List<Tour> tours;
    private List<Tourist> tourists;
    private List<TourItem> tourItems;
    private List<Article> articles;
    private List<Currency> currencies;
    private List<CurrRate> currRates;

    public DataBaseModel(List<Tour> tours, List<Tourist> tourists, List<TourItem> tourItems,
                         List<Article> articles, List<Currency> currencies, List<CurrRate> currRates) {
        this.articles = articles;
        this.currencies = currencies;
        this.currRates = currRates;
        this.tourists = tourists;
        this.tourItems = tourItems;
        this.tours = tours;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public List<Tourist> getTourists() {
        return tourists;
    }

    public void setTourists(List<Tourist> tourists) {
        this.tourists = tourists;
    }

    public List<TourItem> getTourItems() {
        return tourItems;
    }

    public void setTourItems(List<TourItem> tourItems) {
        this.tourItems = tourItems;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public List<CurrRate> getCurrRates() {
        return currRates;
    }

    public void setCurrRates(List<CurrRate> currRates) {
        this.currRates = currRates;
    }
}
