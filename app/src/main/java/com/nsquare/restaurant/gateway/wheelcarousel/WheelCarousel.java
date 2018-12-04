package com.nsquare.restaurant.gateway.wheelcarousel;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nsquare.restaurant.activity.MenuListActivity;
import com.nsquare.restaurant.gateway.SectionData;
import com.nsquare.restaurant.DefaultRetryPolicy;
import com.nsquare.restaurant.Direction;
import com.nsquare.restaurant.Production;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.Section;
import com.nsquare.restaurant.activity.MainActivity;


public class WheelCarousel extends FrameLayout implements AnimatorListener, AnimatorUpdateListener {
    private Carousel carousel;
    private int currentOffset = 0;
    private Production currentProduction;
    private String currentSectionId;
    private int currentSectionIndex = 0;
    private GestureType gestureType;
    private boolean hasMoved = false;
    private boolean isFocused = true;
    private RelativeLayout loadingTreatment;
    private RectF rect;
    private Direction scrollDirection;
    private SectionData sectionData;
    private WheelSections sections;
    private double startArcTan;
    private int startIndex = 0;
    private int startOffset = 0;
    private int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private PointerCoords touchStart;
    private ValueAnimator valueAnimator;
    public static UpdateHomepageData updateHomepageData;
    public static UpdateHomepageCategoryTitle updateHomepageCategoryTitle;
    private Context mContext;

    private enum GestureType {
        WHEEL("Wheel"),
        CAROUSEL("Carousel");
        
        private String gestureType;

        private GestureType(String gestureType) {
            this.gestureType = gestureType;
        }

        public String toString() {
            return this.gestureType;
        }
    }

    public WheelCarousel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.carousel = (Carousel) findViewById(R.id.wheelCarousel);
        this.sections = (WheelSections) findViewById(R.id.wheelSections);
        this.loadingTreatment = (RelativeLayout) findViewById(R.id.loadingSpinner);
    }

    public void showLoadingTreatment() {
        this.loadingTreatment.setVisibility(0);
    }

    public void dismissLoadingTreatment() {
        this.loadingTreatment.setVisibility(8);
    }

    public void setSectionData(final SectionData sectionData) {
        this.sectionData = sectionData;
        this.carousel.setSectionData(sectionData);
        this.sections.setSectionData(sectionData);
        if (sectionData.getTotalSize() == 0) {
            findViewById(R.id.emptyContentMessage).setVisibility(0);
            return;
        }
        findViewById(R.id.emptyContentMessage).setVisibility(8);
        int sectionIndex = 0;
        int productionIndex = 0;
        Production production = null;
        if (this.currentSectionId != null && this.currentProduction != null) {
            for (Section section : sectionData.getSectionList()) {
                if (this.currentSectionId.equals(section.getIconURL())) {
                    for (String productionId : section.getProductionIds()) {
                        if (this.currentProduction.getProductionId().equals(productionId)) {
                            production = this.currentProduction;
                            break;
                        }
                        productionIndex++;
                    }
                    if (production != null) {
                        break;
                    }
                } else {
                    productionIndex += section.getProductionIds().size();
                }
                sectionIndex++;
            }
        }
        if (production != null) {
            this.currentOffset = (this.carousel.getScrollableWidth() * productionIndex) / sectionData.getTotalSize();
            this.currentSectionIndex = sectionIndex;
        } else if (this.currentProduction != null) {
            production = getProductionForIndex(0);
            this.currentOffset = 0;
            this.currentSectionIndex = 0;
        } else {
            production = getProductionForIndex(0);
            this.currentOffset = 0;
            this.currentSectionIndex = 0;
        }
        this.sections.setPercentOffset((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getScrollableWidth()), this.currentSectionIndex);
        this.carousel.setOffset(this.currentOffset);
    }

    public void animateSpinForward() {
        if (this.sectionData != null && this.sectionData.getTotalSize() > 1) {
            this.scrollDirection = Direction.LEFT;
            Production focusedProduction = getProductionForIndex(Math.min(6, this.sectionData.getTotalSize()));
            this.valueAnimator = ValueAnimator.ofInt(new int[]{this.currentOffset, this.carousel.getImageCellWidth() * 2});
            this.valueAnimator.addListener(this);
            this.valueAnimator.addUpdateListener(this);
            this.valueAnimator.setDuration(1000);
            this.valueAnimator.start();
        }
    }

    public void animateSpinBack() {
        if (this.sectionData != null && this.sectionData.getTotalSize() > 1) {
            this.scrollDirection = Direction.RIGHT;
            Production focusedProduction = getProductionForIndex(2);
            this.valueAnimator = ValueAnimator.ofInt(new int[]{this.currentOffset, this.carousel.getImageCellWidth() * 2});
            this.valueAnimator.addListener(this);
            this.valueAnimator.addUpdateListener(this);
            this.valueAnimator.setDuration(1000);
            this.valueAnimator.start();
        }
    }

    public void animateSwipeBack() {
        if (this.sectionData != null && this.sectionData.getTotalSize() > 1) {
            this.scrollDirection = Direction.RIGHT;
            Production focusedProduction = getProductionForIndex(Math.round((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getImageCellWidth())) - 1);
            this.valueAnimator = ValueAnimator.ofInt(new int[]{this.currentOffset, this.carousel.getImageCellWidth() * 2});
            this.valueAnimator.addListener(this);
            this.valueAnimator.addUpdateListener(this);
            this.valueAnimator.setDuration(500);
            this.valueAnimator.start();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isFocused || this.sectionData == null || this.sectionData.getTotalSize() == 0) {
            return super.onTouchEvent(event);
        }
        requestDisallowInterceptTouchEvent(true);
        if (event.getAction() == 0) {
            return onTouchStart(event);
        }
        if (event.getAction() == 2) {
            return onTouchMove(event);
        }
        if (event.getAction() == 1) {
            return onTouchEnd(event);
        }
        return super.onTouchEvent(event);
    }

    private boolean onTouchStart(MotionEvent event) {

        this.hasMoved = false;
        if (this.valueAnimator != null) {
            if (this.valueAnimator.isRunning()) {
                this.valueAnimator.cancel();
            }
            this.valueAnimator.removeAllListeners();
            this.valueAnimator.removeAllUpdateListeners();
        }
        this.touchStart = new PointerCoords();
        event.getPointerCoords(0, this.touchStart);
        this.rect = this.sections.getArcRect();
        if (Math.pow((double) (event.getX() - this.rect.centerX()), 2.0d) + Math.pow((double) (event.getY() - this.rect.centerY()), 2.0d) >= Math.pow((double) this.sections.getInnerRadius(), 2.0d)) {
            this.gestureType = GestureType.WHEEL;
            this.startArcTan = Math.atan2((double) (this.touchStart.y - this.rect.centerY()), (double) (this.touchStart.x - this.rect.centerX()));
        } else {
            this.gestureType = GestureType.CAROUSEL;
        }
        this.scrollDirection = Direction.UNKNOWN;
        this.startOffset = this.currentOffset;
        this.startIndex = Math.round((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getImageCellWidth()));
        return true;
    }

    private boolean onTouchMove(MotionEvent event) {
        float lastOffset = (float) this.currentOffset;
        boolean isFirstMove = false;
        if (this.gestureType == GestureType.WHEEL) {
            this.currentOffset = this.startOffset + ((int) ((((double) this.carousel.getScrollableWidth()) * Math.toDegrees(this.startArcTan - Math.atan2((double) (event.getY() - this.rect.centerY()), (double) (event.getX() - this.rect.centerX())))) / 360.0d));
            if (!this.hasMoved) {
                float deltaX = Math.abs(this.touchStart.x - event.getX());
                float deltaY = Math.abs(this.touchStart.y - event.getY());
                if ((deltaX * deltaX) + (deltaY * deltaY) > ((float) (this.touchSlop * this.touchSlop))) {
                    this.hasMoved = true;
                    isFirstMove = true;
                }
            }
        } else {
            this.currentOffset = (int) ((((float) this.startOffset) + this.touchStart.x) - event.getX());
            if (!this.hasMoved && Math.abs(this.touchStart.x - event.getX()) > ((float) this.touchSlop)) {
                this.hasMoved = true;
                isFirstMove = true;
            }
        }
        if (!this.hasMoved) {
            return true;
        }
        if (((float) this.currentOffset) < lastOffset) {
            this.scrollDirection = Direction.LEFT;
        } else if (((float) this.currentOffset) > lastOffset) {
            this.scrollDirection = Direction.RIGHT;
        }
        if (isFirstMove) {
            this.currentOffset = (this.currentOffset + this.carousel.getScrollableWidth()) % this.carousel.getScrollableWidth();
            setSectionIndexFromOffset(this.currentOffset);
            this.sections.setPercentOffset((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getScrollableWidth()), this.currentSectionIndex);
            this.carousel.setOffset(this.currentOffset);
        } else {
            this.currentOffset = (this.currentOffset + this.carousel.getScrollableWidth()) % this.carousel.getScrollableWidth();
            setSectionIndexFromOffset(this.currentOffset);
            this.sections.setPercentOffset((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getScrollableWidth()), this.currentSectionIndex);
            this.carousel.setOffset(this.currentOffset);
        }

        updateHomepageCategoryTitle.updateHomepageCategoryTitle(sectionData.getSectionList().get(this.currentSectionIndex).getTitle());
        System.out.println("WheelCarousel Auto selected title: "+sectionData.getSectionList().get(this.currentSectionIndex).getTitle());
        return true;
    }

    private boolean onTouchEnd(MotionEvent event) {
        int nearestIndex;
        if (this.gestureType == GestureType.CAROUSEL && this.scrollDirection == Direction.LEFT) {
            nearestIndex = (int) Math.floor((double) ((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getImageCellWidth())));
        } else if (this.gestureType == GestureType.CAROUSEL && this.scrollDirection == Direction.RIGHT) {
            nearestIndex = (int) Math.ceil((double) ((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getImageCellWidth())));
        } else {
            nearestIndex = Math.round((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getImageCellWidth()));
        }

        Production production = getProductionForIndex(nearestIndex);
        if (getProductionForIndex(nearestIndex) == null) {
            Log.e("WheelCarousel", "onTouchEnd failed to find production to focus on");
            return true;
        }
        else if (this.scrollDirection != Direction.UNKNOWN) {
            this.valueAnimator = ValueAnimator.ofInt(new int[]{this.currentOffset, this.carousel.getImageCellWidth() * nearestIndex});
            this.valueAnimator.addListener(this);
            this.valueAnimator.addUpdateListener(this);
            this.valueAnimator.setDuration(250);
            this.valueAnimator.start();
        }  else if (this.gestureType == GestureType.CAROUSEL) {
            System.out.println("WheelCarousel click: "+production.getTitle());
            Intent intent = new Intent(getContext(), MenuListActivity.class);
            intent.putExtra(getResources().getString(R.string.key_title), production.getTitle());
            intent.putExtra(getResources().getString(R.string.key_subCategoryId), production.getProductionId());
            MainActivity.myActivity.startActivity(intent);
            MainActivity.myActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            Toast.makeText(getContext(), production.getTitle(), Toast.LENGTH_SHORT).show();
        }else if (this.gestureType != GestureType.CAROUSEL) {
            int startIndex = getSectionStartIndex((this.carousel.getScrollableWidth() + (this.startOffset + ((int) ((((double) this.carousel.getScrollableWidth()) * Math.toDegrees(this.startArcTan + 1.5707963267948966d)) / 360.0d)))) % this.carousel.getScrollableWidth());
            if (getProductionForIndex(startIndex) == null) {
                Log.e("WheelCarousel", "onTouchEnd failed to find production to focus on for section click");
                return true;
            }
            int toOffset = startIndex * this.carousel.getImageCellWidth();
            if (toOffset < this.currentOffset) {
                toOffset += this.carousel.getScrollableWidth();
            }
//            this.valueAnimator = ValueAnimator.ofInt(new int[]{this.currentOffset, toOffset});
//            this.valueAnimator.addListener(this);
//            this.valueAnimator.addUpdateListener(this);
//            this.valueAnimator.setDuration(500);
//            this.valueAnimator.start();


        }
        return true;
    }

    private int getDistance(int startIndex, int endIndex) {
        if ((startIndex >= endIndex || (this.scrollDirection != Direction.RIGHT && this.scrollDirection != Direction.UNKNOWN)) && (startIndex <= endIndex || this.scrollDirection != Direction.LEFT)) {
            return this.carousel.count() - Math.abs(endIndex - startIndex);
        }
        return Math.abs(endIndex - startIndex);
    }

    private int getSectionStartIndex(int offset) {
        float indexOffset = (float) (offset / this.carousel.getImageCellWidth());
        int startOffset = 0;
        for (Section section : this.sectionData.getSectionList()) {
            if (((float) startOffset) <= indexOffset && indexOffset < ((float) (section.getProductionIds().size() + startOffset))) {
                return startOffset;
            }
            startOffset += section.getProductionIds().size();
        }
        return 0;
    }

    private void setSectionIndexFromOffset(int offset) {
        int lastSectionIndex = this.currentSectionIndex;
        float indexOffset = (float) (offset / this.carousel.getImageCellWidth());
        int index = 0;
        int startOffset = 0;
        for (Section section : this.sectionData.getSectionList()) {
            if (((float) startOffset) <= indexOffset && indexOffset < ((float) (section.getProductionIds().size() + startOffset))) {
                this.currentSectionIndex = index;
                this.currentSectionId = section.getIconURL();
                break;
            }
            startOffset += section.getProductionIds().size();
            index++;
        }
        if (lastSectionIndex == this.currentSectionIndex) {
        }
    }

    private Production getProductionForIndex(int index) {
        if (this.sectionData.getTotalSize() == 0) {
            return null;
        }
        int startOffset = 0;
        int targetIndex = (this.sectionData.getTotalSize() + index) % this.sectionData.getTotalSize();
        for (Section section : this.sectionData.getSectionList()) {
            if (targetIndex - startOffset >= 0 && targetIndex - startOffset < section.getProductionIds().size()) {
                return this.sectionData.getProduction((String) section.getProductionIds().get(targetIndex - startOffset));
            }
            startOffset += section.getProductionIds().size();
        }
        return null;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(this.sections, widthMeasureSpec, heightMeasureSpec);
        int width = this.sections.getMeasuredWidth();
        int height = this.sections.getMeasuredHeight();
        measureChildren(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
        setMeasuredDimension(width, height);

    }

    public void onAnimationStart(Animator animation) {

    }

    public void onAnimationUpdate(ValueAnimator animation) {
        this.currentOffset = ((Integer) animation.getAnimatedValue()).intValue();
        this.currentOffset = (this.currentOffset + this.carousel.getScrollableWidth()) % this.carousel.getScrollableWidth();
        setSectionIndexFromOffset(this.currentOffset);
        this.sections.setPercentOffset((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getScrollableWidth()), this.currentSectionIndex);
        this.carousel.setOffset(this.currentOffset);

        ;
    }

    public void onAnimationEnd(Animator animation) {
        Direction focusDirection;
        if (this.gestureType == GestureType.WHEEL) {
            focusDirection = Direction.UNKNOWN;
        } else {
            focusDirection = this.scrollDirection;
        }
        this.currentProduction = getProductionForIndex(Math.round((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.currentOffset)) / ((float) this.carousel.getImageCellWidth())));
        System.out.println("WheelCarousel Auto selected: "+this.currentProduction.getTitle());
        updateHomepageData.updateHomepageData(this.currentProduction);
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public interface UpdateHomepageData{
        public void updateHomepageData(Production production);
    }

    public interface UpdateHomepageCategoryTitle{
        public void updateHomepageCategoryTitle(String title);
    }
}
