package com.TourAccount.model;

/**
 * User: User
 * Date: 26.12.14
 * Time: 15:56
 */
public class PieChartArticle {
    private Article article;
    private float percent;
    private String color;

    public PieChartArticle(Article article, float percent, String color) {
        this.article = article;
        this.percent = percent;
        this.color = color;
    }

    public Article getArticle() {
        return article;
    }

    public float getPercent() {
        return percent;
    }

    public String getColor() {
        return color;
    }
}
