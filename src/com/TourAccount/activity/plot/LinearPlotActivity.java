package com.TourAccount.activity.plot;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.TourAccount.R;
import com.TourAccount.model.Article;
import com.TourAccount.model.Currency;
import com.TourAccount.model.TourEnum;
import com.TourAccount.sqlite.DatabaseHandler;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import java.util.*;

/**
 * User: User
 * Date: 26.12.14
 * Time: 12:27
 */
public class LinearPlotActivity extends Activity {
    // Database Helper
    DatabaseHandler db;

    private TextView сurrPiePlotLabel;

    private PieChart graficoPartidos;

    private static final String LOG = "LinearPlotActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(getApplicationContext());

        setContentView(R.layout.gistaplot);

        //получить ид тура
        final int plot_type = getIntent().getIntExtra("plot_type", 0);

        Log.e(LOG, "check plot type = " + plot_type);

        if (plot_type == TourEnum.PlotDataType.TOURITEMS.value) {
            //по туру
            final Integer tour_id = getIntent().getIntExtra("tour_id", 0);
            Log.e(LOG, "tour_id = " + tour_id);
            final Integer curr_id = getIntent().getIntExtra("curr_id", 0);
            Log.e(LOG, "curr_id = " + curr_id);
            ArrayList<Integer> tourIds = new ArrayList();
            Log.e(LOG, "tourIds.1 = " + tourIds.size());
            tourIds.add(tour_id);
            Log.e(LOG, "tourIds.2 = " + tourIds.size());
            List<Date> dates = db.getTourItemsDates(tourIds);
            List<Article> articleList = db.getTourItemsArticles(tourIds);

            List<Currency> currencies = db.getAllCurrencies(tour_id);
            Currency currency = currencies.get(0);
            сurrPiePlotLabel = (TextView) findViewById(R.id.сurrPiePlot);

            Float summAll = GetSum(db.getTourAllSum(tour_id, TourEnum.TourItemType.ALL.value)
                    , tour_id, currency.getId());

            сurrPiePlotLabel.setText("Общая сумма " + summAll + " " + currency.getName());

            getItemPercent(getArticleSum(tour_id, currency), summAll);


        } else if (plot_type == TourEnum.PlotDataType.TOURS.value) {
            //по турам
            final List<Integer> tourIds = getIntent().getIntegerArrayListExtra("tourIds");

        }

        db.closeDB();
    }


    private Float GetSum(Map<Integer, Float> sumMap, int tour_id, int curr_id) {

        Currency currency = db.getCurrency(curr_id);
        Float sumInCur = 0F;
        Log.e(LOG, "Считаем сумму в " + currency.getName());
        for (Integer innerCur : sumMap.keySet()) {
            Currency innerCurrency = db.getCurrency(innerCur);
            //
            if (curr_id != innerCur) {
                //Есть еще другая функция ConvertCurrency
                // float res = db.CurrConvert(tour_id, innerCur, sumMap.get(innerCur), curr);
                float res = db.ConvertCurrency(tour_id, innerCur, sumMap.get(innerCur), curr_id);
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

        return sumInCur;
    }


    public Map<Article, Float> getArticleSum(int tour_id, Currency currency) {
        Map<Article, Float> articleFloatMap = new HashMap<Article, Float>();

        List<Article> articles = db.getTourItemsArticles(Arrays.asList(tour_id));

        for (Article article : articles) {
            Float sumInCur = 0F;
            Map<Integer, Float> integerFloatMap = db.getTourArticleSum(tour_id, article.getId(), TourEnum.TourItemType.OUTGOING.value);
            for (Integer innerCur : integerFloatMap.keySet()) {
                Currency innerCurrency = db.getCurrency(innerCur);
                //
                if (currency.getId() != innerCur) {
                    //Есть еще другая функция ConvertCurrency
                    // float res = db.CurrConvert(tour_id, innerCur, sumMap.get(innerCur), curr);
                    float res = db.ConvertCurrency(tour_id, innerCur, integerFloatMap.get(innerCur), currency.getId());
                    if (integerFloatMap.get(innerCur) > 0)
                        sumInCur += res;
                    else
                        sumInCur -= res;

                    Log.e(LOG, integerFloatMap.get(innerCur) + " " + innerCurrency.getName() + " = " + res + " " + currency.getName());

                } else {
                    sumInCur += integerFloatMap.get(innerCur);
                    // Log.e(LOG, sumMap.get(innerCur) + " " + currency.getName() + " = " + sumMap.get(curr) + " " + innerCurrency.getName());
                }
            }
            Log.e(LOG, "article = " + article.getName() + " sum = " + sumInCur);
            articleFloatMap.put(article, sumInCur);
        }

        return articleFloatMap;
    }

    public void getItemPercent(Map<Article, Float> tourItemFloatMap, Float summAll) {

        graficoPartidos = (PieChart) findViewById(R.id.piePlot);


        // detect segment clicks:
        graficoPartidos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PointF click = new PointF(motionEvent.getX(), motionEvent.getY());
                if (graficoPartidos.getPieWidget().containsPoint(click)) {
                    Segment segment = graficoPartidos.getRenderer(PieRenderer.class).getContainingSegment(click);
                    if (segment != null) {
                        // handle the segment click...for now, just print
                        // the clicked segment's title to the console:
                        //System.out.println("Clicked Segment: " + segment.getTitle());
                    }
                }
                return false;
            }
        });
        // graficoPartidos.setPadding(0,0,0,0);
        graficoPartidos.getBackgroundPaint().setColor(Color.BLACK);
        graficoPartidos.setVerticalScrollBarEnabled(true);
        graficoPartidos.setTitle("На что потрачены деньги");

        Random random = new Random(10);
        Log.e(LOG, "getItemPercent. tourItemFloatMap.size() = " + tourItemFloatMap.size());
        Log.e(LOG, "getItemPercent. summAll = " + summAll);
        graficoPartidos.setPlotMarginBottom(0);
        graficoPartidos.getPieWidget().setPadding(10, 1, 1, 1);

        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

        for (Article article : tourItemFloatMap.keySet()) {
            //Article article = db.getArticle(tourItem.getArticle_id());
            Segment segment = new Segment(article.getName(), tourItemFloatMap.get(article) * 100 / summAll);
            SegmentFormatter segmentFormatter = new SegmentFormatter(Color.rgb(50 * random.nextInt(),
                    100 * random.nextInt(),
                    1 * random.nextInt()), Color.BLACK, Color.BLACK, Color.BLACK);

            segmentFormatter.getFillPaint();
            Paint bgPaint = new Paint();
            bgPaint.setColor(Color.BLACK);
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setLinearText(true);
            bgPaint.setTextScaleX(100);
            bgPaint.setStrikeThruText(true);
            bgPaint.setAlpha(140);
            //segmentFormatter.setRadialEdgePaint(bgPaint);
            segmentFormatter.setLabelMarkerPaint(bgPaint);
            //segmentFormatter.getLabelPaint().setTextScaleX(200F);

            // segmentFormatter.getFillPaint().setMaskFilter(emf);

            graficoPartidos.addSeries(segment,
                    segmentFormatter);
            Log.e(LOG, "getItemPercent. article = " + article.getName() + " sum = " + tourItemFloatMap.get(article) * 100 / summAll);
        }


        graficoPartidos.setPlotMarginBottom(0);
        graficoPartidos.getBorderPaint().setColor(Color.TRANSPARENT);
        graficoPartidos.getBackgroundPaint().setColor(Color.TRANSPARENT);
        // ChartSeries series = chartView.getSeries().get(0);
        PieRenderer pieRenderer = graficoPartidos.getRenderer(PieRenderer.class);
        //TourPieRender tourPieRender = (TourPieRender)pieRenderer;
        pieRenderer.setDonutSize((float) 0 / 100, PieRenderer.DonutMode.PERCENT);

    }

}

