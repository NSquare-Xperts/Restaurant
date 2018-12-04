package com.nsquare.restaurant.util;

/**
 * Created by Pushkar on 13-09-2017.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class ObservableScrollView extends ScrollView {
    private List<MyInterface> f16755a;

    public interface MyInterface {
        void mo4966a(int i, int i2);

        void mo4967b(int i, int i2);
    }

    public ObservableScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        for (MyInterface a : this.f16755a) {
            a.mo4966a(i, i2);
        }
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        for (MyInterface b : this.f16755a) {
            b.mo4967b(i, i2);
        }
    }

    public void setObservableScrollViewListener(MyInterface c4755a) {
        if (this.f16755a == null) {
            this.f16755a = new ArrayList();
        }
        this.f16755a.add(c4755a);
    }
}
