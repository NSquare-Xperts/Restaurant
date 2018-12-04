package com.nsquare.restaurant.gateway.wheelcarousel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.nsquare.restaurant.DefaultRetryPolicy;
import com.nsquare.restaurant.Section;
import com.nsquare.restaurant.gateway.SectionData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WheelSections extends Wheel {
    final Set<Target> protectedFromGarbageCollectorTargets = new HashSet<>();
    private int activeSectionIndex;
    private float currentRotation = 0.0f;
    private SectionData sectionData;
    private Paint separatorPaint = new Paint(1);
    private Paint trianglePaint;
    private Path trianglePath;
    private List<WheelSection> wheelSectionList;

    private class WheelSection implements Target {
        private final float iconAngle = ((90.0f + this.startAngle) + (this.sweepAngle / 2.0f));
        private Bitmap iconBitmap;
        private Paint primaryArcPaint = new Paint(1);
        private Paint secondaryArcPaint;
        private float startAngle;
        private float sweepAngle;

        WheelSection(int startOffset, int totalSize, Section section) {
            int endOffset = startOffset + section.getProductionIds().size();
            this.startAngle = 270.0f + ((((float) startOffset) * 360.0f) / ((float) totalSize));
            this.sweepAngle = (((float) (endOffset - startOffset)) * 360.0f) / ((float) totalSize);
            this.primaryArcPaint.setStyle(Style.STROKE);
//            this.primaryArcPaint.setColor(getResources().getColor(section.getPrimaryColor()));
//            this.primaryArcPaint.setColor(section.getPrimaryColor());
            this.primaryArcPaint.setColor(Color.parseColor(section.getPrimaryColor()));
            this.secondaryArcPaint = new Paint(1);
            this.secondaryArcPaint.setStyle(Style.STROKE);
//            this.secondaryArcPaint.setColor(section.getSecondaryColor());
//            this.secondaryArcPaint.setColor(getResources().getColor(section.getSecondaryColor()));
            this.secondaryArcPaint.setColor(Color.parseColor(section.getSecondaryColor()));
            System.out.println("****************----------------- "+section.getTitle());

//            this.iconBitmap = BitmapFactory.decodeResource(WheelSections.this.getContext().getResources(), section.getImage());
//            final ImageView imageView = new ImageView(WheelSections.this.getContext());
//
//            Target bitmapTarget = new BitmapTarget();
//            protectedFromGarbageCollectorTargets.add(bitmapTarget);
//            Picasso.with(WheelSections.this.getContext()).load(section.getIconURL()).into(bitmapTarget);
//            imageView.setTag(bitmapTarget);
//            try {
//                URL url = new URL(section.getIconURL());
//                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                iconBitmap = image;
//            } catch(IOException e) {
//                System.out.println("****************----------------- "+e);
//            }
            Picasso.with(WheelSections.this.getContext()).load(section.getIconURL()).resize(0, (int) (((double) WheelSections.this.strokeWidth) * 0.6d)).into((Target) this);
        }

        public float getStartAngle() {
            return this.startAngle;
        }

        public float getSweepAngle() {
            return this.sweepAngle;
        }

        public float getIconAngle() {
            return this.iconAngle;
        }

        public void setStrokeWidth(int strokeWidth) {
            this.primaryArcPaint.setStrokeWidth((float) strokeWidth);
            this.secondaryArcPaint.setStrokeWidth((float) strokeWidth);
        }

        public Paint getPrimaryArcPaint() {
            return this.primaryArcPaint;
        }

        public Paint getSecondaryArcPaint() {
            return this.secondaryArcPaint;
        }

        public Bitmap getIconBitmap() {
            return this.iconBitmap;
        }

        public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadedFrom) {

//            System.out.println("****************----------------- here "+loadedFrom.toString());
            this.iconBitmap = bitmap;
            WheelSections.this.invalidate();
        }

        public void onBitmapFailed(Drawable drawable) {
//            System.out.println("****************----------------- failed ");
        }

        public void onPrepareLoad(Drawable drawable) {
        }
    }

    public WheelSections(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        this.separatorPaint.setStyle(Style.STROKE);
        this.separatorPaint.setColor(-1);
        this.trianglePaint = new Paint(1);
        this.trianglePaint.setStyle(Style.FILL);
        this.trianglePaint.setColor(-1);
    }

    public void setSectionData(SectionData sectionData) {
        this.sectionData = sectionData;
        initializeWheelSectionList();
    }

    private void initializeWheelSectionList() {
        if (this.sectionData != null && getWidth() != 0) {
            this.wheelSectionList = new ArrayList(this.sectionData.getSectionList().size());
            int startOffset = 0;
            for (Section section : this.sectionData.getSectionList()) {
                this.wheelSectionList.add(new WheelSection(startOffset, this.sectionData.getTotalSize(), section));
                startOffset += section.getProductionIds().size();
            }
            setPercentOffset(0.0f, 0);
        }
    }

    public void setPercentOffset(float percentOffset, int currentSectionIndex) {
        this.activeSectionIndex = currentSectionIndex;
        this.currentRotation = 360.0f - (360.0f * percentOffset);
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float triangleWidth = ((float) this.verticalMargin) * 1.5f;
        this.trianglePath = new Path();
        this.trianglePath.setFillType(FillType.EVEN_ODD);
        this.trianglePath.moveTo(this.arcRect.centerX(), (float) (this.verticalMargin + 5));
        this.trianglePath.lineTo(this.arcRect.centerX() - (triangleWidth / 2.0f), (float) 5);
        this.trianglePath.lineTo((this.arcRect.centerX() + DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) + (triangleWidth / 2.0f), (float) 5);
        this.trianglePath.lineTo(this.arcRect.centerX() + DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, (float) (this.verticalMargin + 5));
        this.trianglePath.close();
        this.separatorPaint.setStrokeWidth((float) this.strokeWidth);
        if (this.wheelSectionList == null) {
            initializeWheelSectionList();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.wheelSectionList != null && this.wheelSectionList.size() != 0) {
            WheelSection activeWheelSection = (WheelSection) this.wheelSectionList.get(this.activeSectionIndex);
            activeWheelSection.setStrokeWidth(this.strokeWidth);
            float activeSectionStart = (this.currentRotation + activeWheelSection.getStartAngle()) % 360.0f;
            canvas.drawArc(this.arcRect, activeSectionStart, activeWheelSection.getSweepAngle(), false, activeWheelSection.getSecondaryArcPaint());
            canvas.drawArc(this.arcRect, 270.0f, (activeWheelSection.getSweepAngle() + activeSectionStart) - 270.0f, false, activeWheelSection.getPrimaryArcPaint());
            canvas.drawPath(this.trianglePath, this.trianglePaint);
            canvas.drawArc(this.arcRect, 270.0f, 0.25f, false, this.separatorPaint);
            int index = 0;
            for (WheelSection section : this.wheelSectionList) {
                if (!(index == this.activeSectionIndex || index == (this.activeSectionIndex + 1) % this.wheelSectionList.size())) {
                    canvas.drawArc(this.arcRect, section.getStartAngle() + this.currentRotation, 0.25f, false, this.separatorPaint);
                }
                index++;
            }
            for (WheelSection section2 : this.wheelSectionList) {
                if (section2.getIconBitmap() != null) {
                    canvas.save();
                    canvas.rotate(this.currentRotation + section2.getIconAngle(), this.arcRect.centerX(), this.arcRect.centerY());
                    canvas.drawBitmap(section2.getIconBitmap(), this.arcRect.centerX() - ((float) (section2.getIconBitmap().getWidth() / 2)), this.arcRect.top - ((float) (section2.getIconBitmap().getHeight() / 2)), null);
                    canvas.restore();
                }
            }
        }
    }

    public class BitmapTarget implements Target {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            System.out.println("****************----------------- here "+loadedFrom.toString());
            //handle bitmap
            protectedFromGarbageCollectorTargets.remove(this);
        }



    @Override
    public void onBitmapFailed(Drawable drawable) {
        protectedFromGarbageCollectorTargets.remove(this);
        System.out.println("****************----------------- here failed ");
    }

    @Override
    public void onPrepareLoad(Drawable drawable) {

    }
}
}
