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
import com.TourAccount.model.PieChartArticle;
import com.TourAccount.model.TourEnum;
import com.TourAccount.sqlite.DatabaseHandler;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import java.text.DecimalFormat;
import java.util.*;

/**
 * User: User
 * Date: 03.10.14
 * Time: 9:49
 */
public class ModelPlotActivity extends Activity {


    List<PieChartArticle> pieChartArticles;

    DecimalFormat dtime = new DecimalFormat("#.##");

    // Database Helper
    DatabaseHandler db;

    String[] colors = {"#F0F8FF", "#4B0082", "#FF0000", "#7FFF00", "#1E90FF", "#FFD700", "#FF8C00",
            "#191970", "#ADFF2F", "#DCDCDC", "#00FF00", "#00FF7F", "#00BFFF",
            "#FF00FF", "#0000FF", "#FFF8DC", "#7FFFD4", "#7FFF00", "#FF7F50", "#DC143C"};


    private TextView сurrPiePlotLabel;

    private PieChart graficoPartidos;

    TextView donutSizeTextView;

    private static final String LOG = "ModelPlotActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pieChartArticles = new ArrayList<PieChartArticle>();

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

            Float summAll = GetSum(db.getTourAllSum(tour_id,
                    //TourEnum.TourItemType.ALL.value
                    TourEnum.TourItemType.OUTGOING.value
                    )
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
                Log.e(LOG, "Сумма плюс или минус _: "+integerFloatMap.get(innerCur) +
                        " " + innerCurrency.getName() + " =  " + currency.getName());
                if (currency.getId() != innerCur) {
                    //Есть еще другая функция ConvertCurrency
                    // float res = db.CurrConvert(tour_id, innerCur, sumMap.get(innerCur), curr);
                    float res = db.ConvertCurrency(tour_id, innerCur, integerFloatMap.get(innerCur), currency.getId());
                    if (integerFloatMap.get(innerCur) > 0)
                        sumInCur += res;
                    else
                        sumInCur -= res;

                    Log.e(LOG, "Сумма плюс или минус: "+integerFloatMap.get(innerCur) +
                            " " + innerCurrency.getName() + " = " + res + " " + currency.getName());

                } else {
                    sumInCur += integerFloatMap.get(innerCur);
                    /*if (integerFloatMap.get(innerCur) > 0)
                        sumInCur += integerFloatMap.get(innerCur);
                    else
                        sumInCur -= integerFloatMap.get(innerCur);
                      */
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

        Random random = new Random();
        Log.e(LOG, "getItemPercent. tourItemFloatMap.size() = " + tourItemFloatMap.size());
        Log.e(LOG, "getItemPercent. summAll = " + summAll);
        graficoPartidos.setPlotMarginBottom(0);
        graficoPartidos.getPieWidget().setPadding(10, 1, 1, 1);


        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

        for (Article article : tourItemFloatMap.keySet()) {

            PieChartArticle pieChartArticle = new PieChartArticle(article, tourItemFloatMap.get(article) * 100 / summAll,
                    colors[random.nextInt(colors.length - 1)]);
            pieChartArticles.add(pieChartArticle);
            //Article article = db.getArticle(tourItem.getArticle_id());
            //int randomNum = random.nextInt() ;
            Segment segment = new Segment(article.getName(), tourItemFloatMap.get(article) * 100 / summAll);
            SegmentFormatter segmentFormatter =
                    new SegmentFormatter(Color.parseColor(pieChartArticle.getColor()), Color.BLACK, Color.BLACK, Color.BLACK);


            Paint bgPaint = segmentFormatter.getLabelPaint();
            bgPaint.setColor(Color.BLACK);
            // bgPaint.setStyle(Paint.Style.FILL);
            //bgPaint.setLinearText(true);
            //bgPaint.setTextScaleX(20);
            // bgPaint.setAntiAlias(true);
            bgPaint.setFakeBoldText(true);
            // bgPaint.setAlpha(140);

            bgPaint = segmentFormatter.getLabelMarkerPaint();
            bgPaint.setColor(Color.BLACK);
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setAntiAlias(true);
            bgPaint.setFakeBoldText(true);
            bgPaint.setAlpha(140);

            graficoPartidos.addSeries(segment,
                    segmentFormatter);
            Log.e(LOG, "getItemPercent. article = " + article.getName() + " sum = " + tourItemFloatMap.get(article) * 100 / summAll);
        }


        graficoPartidos.setPlotMarginBottom(0);
        graficoPartidos.getBorderPaint().setColor(Color.TRANSPARENT);
        graficoPartidos.getBackgroundPaint().setColor(Color.TRANSPARENT);
        // ChartSeries series = chartView.getSeries().get(0);
        TourPieRenderer tourPieRenderer = (TourPieRenderer) graficoPartidos.getRenderer(TourPieRenderer.class);


        // PieRenderer pieRenderer =  graficoPartidos.getRenderer(PieRenderer.class);
        // pieRenderer.setStartDeg(100);
        // pieRenderer.setEndDeg(100);
        //TourPieRender tourPieRender = (TourPieRender)pieRenderer;
        //   pieRenderer.setDonutSize((float) 0 / 100, PieRenderer.DonutMode.PERCENT);
        Log.e(LOG, "PieRenderer.DonutMode.PERCENT = " + PieRenderer.DonutMode.PERCENT.toString());
        graficoPartidos.getRenderer(PieRenderer.class).setDonutSize((float) 15 / 100, PieRenderer.DonutMode.PERCENT);

        donutSizeTextView = (TextView) findViewById(R.id.donutSizeTextView);
        updateDonutText();


        // view.invalidateViews()

    }

    //
    //Здесь писать для каждого типа расходов количество процентов и его цвет
    //
    protected void updateDonutText() {
        // donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
        donutSizeTextView.setText("");
        for (PieChartArticle pieChartArticle : pieChartArticles) {
            donutSizeTextView.setText(donutSizeTextView.getText() + "\n" + pieChartArticle.getArticle().getName() +
                    " " + dtime.format(pieChartArticle.getPercent()) + "%");
            Log.e(LOG, "donutSizeTextView = " + donutSizeTextView.getText());
        }


    }
}

