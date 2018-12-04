package com.nsquare.restaurant.gateway.wheelcarousel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

public class WheelBackground extends Wheel {
    private Paint circlePaint = new Paint(1);

    public WheelBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
//        this.circlePaint.setColor(-12303292);
        this.circlePaint.setColor(-12303292);
        this.circlePaint.setStyle(Style.STROKE);
        this.circlePaint.setShadowLayer(25.0f, 0.0f, 0.0f, -872415232);
        setLayerType(1, this.circlePaint);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.circlePaint.setStrokeWidth((float) this.strokeWidth);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(this.arcRect, 0.0f, 360.0f, false, this.circlePaint);
    }
}
