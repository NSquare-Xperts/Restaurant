package com.nsquare.restaurant.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.nsquare.restaurant.R;
import com.usebutton.sdk.internal.views.LoadingGradient;

/**
 * Created by Pushkar on 13-09-2017.
 */

public class GatewayScrollView extends ObservableScrollView implements ObservableScrollView.MyInterface {

    private LinearLayout linearlayout_bottom_view;
    private boolean f12475c = false;
    private String f12476d;
    private Scroller f12477e;
    private boolean f12478f = false;
    private int f12479g;
//    private LinearLayout f12473a;

    public GatewayScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f12477e = new Scroller(context);
        setObservableScrollViewListener(this);
//        org.gamatech.androidclient.app.models.gateway.a.a().register(this);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
//        this.f12473a = (LinearLayout) findViewById(R.id.linearlayout_static_data);

        this.linearlayout_bottom_view = (LinearLayout) findViewById(R.id.linearlayout_bottom_view);
        this.linearlayout_bottom_view.setVisibility(0);
//        this.f12474b.addOnPageChangeListener(new 1(this));
        this.linearlayout_bottom_view.setAlpha(0.0f);
        this.linearlayout_bottom_view.setAlpha(0.0f);
        this.f12479g = getResources().getDimensionPixelSize(R.dimen.standardGap);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.f12475c) {
            return false;
        }
        if (motionEvent.getActionMasked() != 1) {
            return super.onTouchEvent(motionEvent);
        }
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        int top = this.linearlayout_bottom_view.getTop();
        if (!this.f12477e.isFinished() || getScrollY() < 0 || getScrollY() >= top) {
            return onTouchEvent;
        }
        if (getScrollY() <= top / 2) {
            fling(-1);
            return onTouchEvent;
        }
        fling(1);
        return onTouchEvent;
    }

    public void fling(int i) {
        if (getChildCount() != 0) {
            int height = getHeight();
            int height2 = getChildAt(0).getHeight();
            this.f12477e.fling(getScrollX(), getScrollY(), 0, i, 0, 0, 0, Math.max(0, height2 - height));
            int finalY = this.f12477e.getFinalY();
            int top = this.linearlayout_bottom_view.getTop();
            if (finalY < 0 || finalY >= top) {
                this.f12478f = false;
                super.fling(i);
            } else if (i < 0) {
                m18895a(0);
            } else {
                m18895a(top);
            }
        }
    }

    private void m18895a(int i) {
        this.f12478f = true;
        this.f12477e.startScroll(0, getScrollY(), 0, i - getScrollY(), 500);
        postInvalidate();
    }

    public void computeScroll() {
        if (!this.f12478f) {
            super.computeScroll();
        } else if (this.f12477e.computeScrollOffset()) {
            scrollTo(0, this.f12477e.getCurrY());
            postInvalidate();
        } else {
            this.f12478f = false;
        }
    }
//
//    @Subscribe
//    public void onProductionFocusEvent(c cVar) {
//        if (cVar.a() == c.a.b) {
//            this.f12475c = true;
//            m18897a(cVar.b(), cVar.c());
//        } else if (cVar.a() == c.a.c) {
//            this.f12475c = false;
//            m18896a(cVar.c());
//        }
//    }
//
//    @Subscribe
//    public void onGatewayResetEvent(org.gamatech.androidclient.app.models.gateway.a.a aVar) {
//        m18895a(0);
//    }
//
//    private void m18897a(Production production, org.gamatech.androidclient.app.models.c.a aVar) {
//        int i = 0;
//        this.f12475c = true;
//        if (this.f12476d == null || !this.f12476d.equals(production.a())) {
//            if (production != null) {
//                C3898b.m18488b().m18501a("ProductionFocused", production.b(), production.a());
//            }
//            this.f12473a.setModelData(production);
//            this.f12473a.measure(MeasureSpec.makeMeasureSpec(getWidth(), 1073741824), MeasureSpec.makeMeasureSpec(getHeight(), 0));
//            this.f12474b.setPlacementHeight((getHeight() - this.f12473a.getMeasuredHeight()) - this.f12479g);
//            this.f12474b.setProduction(production);
//            this.f12476d = production.a();
//            if (aVar == org.gamatech.androidclient.app.models.c.a.b) {
//                i = -125;
//            } else if (aVar == org.gamatech.androidclient.app.models.c.a.c) {
//                i = 125;
//            }
//            this.f12473a.setX((float) i);
//            this.f12473a.setAlpha(0.0f);
//            this.f12473a.animate().setInterpolator(new DecelerateInterpolator()).alpha(LoadingGradient.HIGHLIGHT_RELATIVE_WIDTH).x(0.0f);
////            this.f12474b.setX((float) (i * 2));
////            this.f12474b.setAlpha(0.0f);
////            this.f12474b.animate().setInterpolator(new DecelerateInterpolator()).alpha(LoadingGradient.HIGHLIGHT_RELATIVE_WIDTH).x(0.0f);
//        }
//    }
//
//    private void m18896a(org.gamatech.androidclient.app.models.c.a aVar) {
//        int i = 0;
//        this.f12475c = false;
//        this.f12476d = null;
//        if (aVar == org.gamatech.androidclient.app.models.c.a.b) {
//            i = 125;
//        } else if (aVar == org.gamatech.androidclient.app.models.c.a.c) {
//            i = -125;
//        }
//        this.f12473a.setX(0.0f);
//        this.f12473a.animate().setInterpolator(new DecelerateInterpolator()).alpha(0.0f).x((float) i);
//        this.f12474b.setX(0.0f);
//        this.f12474b.animate().setInterpolator(new DecelerateInterpolator()).alpha(0.0f).x((float) (i * 2));
//    }

    public void m18898a() {
        m18895a(0);
    }

    protected float getTopFadingEdgeStrength() {
        return 0.0f;
    }

    @Override
    public void mo4966a(int i, int i2) {
        float min;
        int top = this.linearlayout_bottom_view.getTop();
        if (top > 0) {
            min = Math.min((((float) i2) * LoadingGradient.HIGHLIGHT_RELATIVE_WIDTH) / ((float) top), LoadingGradient.HIGHLIGHT_RELATIVE_WIDTH);
        } else {
            min = 0.0f;
        }
//        org.gamatech.androidclient.app.models.gateway.a.a().post(new b(min));
//        findViewById(C3896R.id.sectionHeader).setTranslationY((float) i2);
        findViewById(R.id.wheelCarousel).setTranslationY((float) i2);
        if (i2 >= top) {
            this.linearlayout_bottom_view.setTranslationY((float) (i2 - top));
        } else {
            this.linearlayout_bottom_view.setTranslationY(0.0f);
        }
//        this.linearlayout_bottom_view.setMinPagerHeight(getHeight() - (this.linearlayout_bottom_view.getTop() - top));
//        this.linearlayout_bottom_view.a(top - i2);
//        this.linearlayout_bottom_view.setBackgroundAlpha(0.5f);
//        this.linearlayout_bottom_view.setPercentageRevealed(min);
    }

    @Override
    public void mo4967b(int i, int i2) {
//        this.f12474b.setMinPagerHeight(getHeight() - (this.f12474b.getTop() - this.f12473a.getTop()));
    }
}
