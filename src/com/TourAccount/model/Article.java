package com.TourAccount.model;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 18.09.14
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
public class Article {
    int id;
    String name;

    public Article() {

    }

    public Article(String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
