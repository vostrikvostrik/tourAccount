package com.TourAccount.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * User: User
 * Date: 06.10.14
 * Time: 14:34
 */
public class TourItemsGridView extends GridView {

    public TourItemsGridView(Context context) {
        super(context);
    }

    public TourItemsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TourItemsGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec + 10;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }


}
