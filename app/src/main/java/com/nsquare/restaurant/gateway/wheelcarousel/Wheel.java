package com.nsquare.restaurant.gateway.wheelcarousel;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Wheel extends View {
    protected RectF arcRect = new RectF();
    protected int horizontalMargin;
    protected int innerRadius;
    protected int strokeWidth;
    protected int verticalMargin;

    public Wheel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectF getArcRect() {
        return this.arcRect;
    }

    public int getInnerRadius() {
        return this.innerRadius;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.strokeWidth = (int) (0.08d * ((double) w));
        this.horizontalMargin = (int) (((double) w) * 0.032d);
        this.verticalMargin = (int) (((double) w) * 0.032d);
        this.innerRadius = ((w / 2) - this.strokeWidth) - this.horizontalMargin;
        int horizontalOffset = this.horizontalMargin + (this.strokeWidth / 2);
        int verticalOffset = this.verticalMargin + (this.strokeWidth / 2);
        this.arcRect.set((float) horizontalOffset, (float) verticalOffset, (float) (w - horizontalOffset), (float) (h - verticalOffset));
    }
}
