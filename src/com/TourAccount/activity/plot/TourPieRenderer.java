package com.TourAccount.activity.plot;

import android.graphics.*;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import java.util.Set;

/**
 * User: User
 * Date: 26.12.14
 * Time: 11:41
 */
public class TourPieRenderer extends //SeriesRenderer<PieChart, Segment, SegmentFormatter> {
        PieRenderer {

    // starting angle to use when drawing the first radial line of the first segment.
    @SuppressWarnings("FieldCanBeLocal")
    private float startDeg = 0;
    private float endDeg = 360;

    // TODO: express donut in units other than px.
    private float donutSize = 0.5f;
    private DonutMode donutMode = DonutMode.PERCENT;

    public enum DonutMode {
        PERCENT,
        DP,
        PIXELS
    }


    public TourPieRenderer(PieChart plot) {
        super(plot);
    }

    @Override
    public float getRadius(RectF rect) {
        return rect.width() < rect.height() ? rect.width() / 2 : rect.height() / 2;
    }

    @Override
    public void onRender(Canvas canvas, RectF plotArea) throws PlotRenderException {

        float radius = getRadius(plotArea);
        PointF origin = new PointF(plotArea.centerX(), plotArea.centerY());

        double[] values = getValues();
        double scale = calculateScale(values);
        float offset = startDeg;
        Set<Segment> segments = getPlot().getSeriesSet();

        //PointF lastRadial = calculateLineEnd(origin, radius, offset);

        RectF rec = new RectF(origin.x - radius, origin.y - radius, origin.x + radius, origin.y + radius);

        int i = 0;
        for (Segment segment : segments) {
            float lastOffset = offset;
            float sweep = (float) (scale * (values[i]) * 360);
            offset += sweep;
            //PointF radial = calculateLineEnd(origin, radius, offset);
            drawSegment(canvas, rec, segment, getPlot().getFormatter(segment, getClass()),
                    radius, lastOffset, sweep);
            //lastRadial = radial;
            i++;
        }
    }

    @Override
    protected void drawSegment(Canvas canvas, RectF bounds, Segment seg, SegmentFormatter f,
                               float rad, float startAngle, float sweep) {
        canvas.save();

        float cx = bounds.centerX();
        float cy = bounds.centerY();

        float donutSizePx;
        switch (donutMode) {
            case PERCENT:
                donutSizePx = donutSize * rad;
                break;
            case PIXELS:
                donutSizePx = (donutSize > 0) ? donutSize : (rad + donutSize);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented.");
        }

        // do we have a pie chart of less than 100%
        if (Math.abs(sweep - 360f) > Float.MIN_VALUE) {
            // vertices of the first radial:
            PointF r1Outer = calculateLineEnd(cx, cy, rad, startAngle);
            PointF r1Inner = calculateLineEnd(cx, cy, donutSizePx, startAngle);

            // vertices of the second radial:
            PointF r2Outer = calculateLineEnd(cx, cy, rad, startAngle + sweep);
            PointF r2Inner = calculateLineEnd(cx, cy, donutSizePx, startAngle + sweep);

            Path clip = new Path();

            //float outerStroke = f.getOuterEdgePaint().getStrokeWidth();
            //float halfOuterStroke = outerStroke / 2;

            // leave plenty of room on the outside for stroked borders;
            // necessary because the clipping border is ugly
            // and cannot be easily anti aliased.  Really we only care about masking off the
            // radial edges.
            clip.arcTo(new RectF(bounds.left - rad,
                    bounds.top - rad,
                    bounds.right + rad,
                    bounds.bottom + rad),
                    startAngle, sweep);
            clip.lineTo(cx, cy);
            clip.close();
            canvas.clipPath(clip);

            Path p = new Path();
            p.arcTo(bounds, startAngle, sweep);
            p.lineTo(r2Inner.x, r2Inner.y);

            // sweep back to original angle:
            p.arcTo(new RectF(
                    cx - donutSizePx,
                    cy - donutSizePx,
                    cx + donutSizePx,
                    cy + donutSizePx),
                    startAngle + sweep, -sweep);

            p.close();

            // fill segment:
            canvas.drawPath(p, f.getFillPaint());

            // draw radial lines
            canvas.drawLine(r1Inner.x, r1Inner.y, r1Outer.x, r1Outer.y, f.getRadialEdgePaint());
            canvas.drawLine(r2Inner.x, r2Inner.y, r2Outer.x, r2Outer.y, f.getRadialEdgePaint());
        } else {
            canvas.save(Canvas.CLIP_SAVE_FLAG);
            Path chart = new Path();
            chart.addCircle(cx, cy, rad, Path.Direction.CW);
            Path inside = new Path();
            inside.addCircle(cx, cy, donutSizePx, Path.Direction.CW);
            canvas.clipPath(inside, Region.Op.DIFFERENCE);
            canvas.drawPath(chart, f.getFillPaint());
            canvas.restore();
        }

        // draw inner line:
        canvas.drawCircle(cx, cy, donutSizePx, f.getInnerEdgePaint());

        // draw outer line:
        canvas.drawCircle(cx, cy, rad, f.getOuterEdgePaint());
        canvas.restore();

    /*    PointF labelOrigin = calculateLineEnd(cx, cy,
                (rad-((rad- donutSizePx)/2)), startAngle + (sweep/2));
              */
        float offset = 0; // radial offset from the center of the segment
        PointF labelOrigin = calculateLineEnd(cx, cy,
                (rad + offset - ((rad + offset - donutSizePx) / 2)), startAngle + (sweep / 2));
        drawSegmentLabel(canvas, labelOrigin, seg, f);
    }

    protected void drawSegmentLabel(Canvas canvas, PointF origin,
                                    Segment seg, SegmentFormatter f) {
        canvas.drawText(seg.getTitle(), origin.x, origin.y, f.getLabelPaint());

    }

    @Override
    protected void doDrawLegendIcon(Canvas canvas, RectF rect, SegmentFormatter formatter) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Determines how many counts there are per cent of whatever the
     * pie chart is displaying as a fraction, 1 being 100%.
     */
    @Override
    protected double calculateScale(double[] values) {
        double total = 0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }

        return (1d / total);
    }

    @Override
    protected double[] getValues() {
        Set<Segment> segments = getPlot().getSeriesSet();
        double[] result = new double[segments.size()];
        int i = 0;
        for (Segment seg : getPlot().getSeriesSet()) {
            result[i] = seg.getValue().doubleValue();
            i++;
        }
        return result;
    }

    @Override
    protected PointF calculateLineEnd(float x, float y, float rad, float deg) {
        return calculateLineEnd(new PointF(x, y), rad, deg);
    }

    @Override
    protected PointF calculateLineEnd(PointF origin, float rad, float deg) {

        double radians = deg * Math.PI / 180F;
        double x = rad * Math.cos(radians);
        double y = rad * Math.sin(radians);

        // convert to screen space:
        return new PointF(origin.x + (float) x, origin.y + (float) y);
    }

    //@Override
    public void setDonutSize(float size, DonutMode mode) {
        switch (mode) {
            case PERCENT:
                if (size < 0 || size > 1) {
                    throw new IllegalArgumentException(
                            "Size parameter must be between 0 and 1 when operating in PERCENT mode.");
                }
                break;
            case PIXELS:
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented.");
        }
        donutMode = mode;
        donutSize = size;
    }

    /**
     * Retrieve the segment containing the specified point.  This current implementation
     * only matches against angle; clicks outside of the pie/donut inner/outer boundaries
     * will still trigger a match on the segment whose begining and ending angle contains
     * the angle of the line drawn between the pie chart's center point and the clicked point.
     *
     * @param point The clicked point
     * @return Segment containing the clicked point.
     */
    @Override
    public Segment getContainingSegment(PointF point) {

        RectF plotArea = getPlot().getPieWidget().getWidgetDimensions().marginatedRect;
        // figure out the angle in degrees of the line between the clicked point
        // and the origin of the plotArea:
        PointF origin = new PointF(plotArea.centerX(), plotArea.centerY());
        float dx = point.x - origin.x;
        float dy = point.y - origin.y;
        double theta = Math.atan2(dy, dx);
        double angle = (theta * (180f / Math.PI));
        if (angle < 0) {
            // convert angle to 0-360 range with 0 being in the
            // traditional "westerly" orientation:
            angle += 360;
        }

        // find the segment whose starting and ending angle (degs) contains
        // the angle calculated above
        Set<Segment> segments = getPlot().getSeriesSet();
        int i = 0;
        double[] values = getValues();
        double scale = calculateScale(values);
        float offset = startDeg;
        for (Segment segment : segments) {
            float lastOffset = offset;
            float sweep = (float) (scale * (values[i]) * 360);
            offset += sweep;

            if (angle >= lastOffset && angle <= offset) {
                return segment;
            }
            i++;
        }
        return null;
    }

    public void setStartDeg(float deg) {
        startDeg = deg;
    }

    public float getStartDeg() {
        return startDeg;
    }

    public void setEndDeg(float deg) {
        endDeg = deg;
    }

    public float getEndDeg() {
        return endDeg;
    }
}
