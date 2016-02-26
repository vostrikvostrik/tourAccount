package com.TourAccount.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.TourAccount.model.Article;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 22.09.14
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
public class ArticleSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    Context mContext;
    private List<Article> articles;
    // Logcat tag
    private static final String LOG = "ArticleSpinnerAdapter";

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    // Конструктор
    public ArticleSpinnerAdapter(Context context, List<Article> articles) {
        //  super(context, textViewResourceId, currencies);
        this.articles = articles;
        Log.e(LOG, "this.currencies.size=" + this.articles.size());
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return articles.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Article getItem(int i) {
        return articles.get(i);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int i) {
        return articles.get(i).getId();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        TextView text = new TextView(mContext);
        text.setTextColor(Color.BLACK);
        text.setText(articles.get(position).toString());
        Log.e(LOG, "getView=" + articles.get(position).toString());
        return text;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView text = new TextView(mContext);
        text.setTextColor(Color.BLACK);
        text.setText(articles.get(position).toString());
        Log.e(LOG, "getDropDownView=" + articles.get(position).toString());
        return text;
    }

    // возвращает содержимое выделенного элемента списка

    public String GetItem(int position) {
        return articles.get(position).toString();
    }
}
