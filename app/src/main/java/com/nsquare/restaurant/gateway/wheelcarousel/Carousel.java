package com.nsquare.restaurant.gateway.wheelcarousel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.nsquare.restaurant.DefaultRetryPolicy;
import com.nsquare.restaurant.Production;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.Section;
import com.nsquare.restaurant.gateway.SectionData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Carousel extends View implements Target {
    private static final float IMAGE_ASPECT_RATIO = 0.6666667f;
    private static final float IMAGE_CELL_WIDTH_PERCENTAGE = 0.4f;
    private CarouselAdapter carouselAdapter;
    private int centerIndex;
    private Bitmap focusedBitmap;
    private String focusedProductionId;
    private int imageCellWidth;
    private int imageHeight;
    private int imageWidth;
    private int leftOffset = 0;
    private Paint paint;
    private Bitmap placeholder;
    private Rect rect = new Rect();
    private SectionData sectionData;
    private int smallImageHeight;
    private int smallImageWidth;

    private class CarouselAdapter {
        private List<CarouselItem> carouselItemList;

        public CarouselAdapter(SectionData sectionData) {
            this.carouselItemList = new ArrayList(sectionData.getTotalSize());
            for (Section section : sectionData.getSectionList()) {
                for (String productionId : section.getProductionIds()) {
                    Production production = sectionData.getProduction(productionId);
                    System.out.println("getCategories Production "+production.getProductionId()+" "+production.getCoverImageURL());
                    this.carouselItemList.add(new CarouselItem(production.getProductionId(), production.getCoverImageURL()));
                }
            }
        }

        CarouselItem getCarouselItem(int index) {
            CarouselItem carouselItem = (CarouselItem) this.carouselItemList.get((this.carouselItemList.size() + index) % this.carouselItemList.size());
            carouselItem.prepareBitmap();
            return carouselItem;
        }

        void setActiveIndexRange(int startIndex, int endIndex) {
            for (int i = startIndex; i < this.carouselItemList.size() + startIndex; i++) {
                if (i > endIndex) {
                    ((CarouselItem) this.carouselItemList.get((this.carouselItemList.size() + i) % this.carouselItemList.size())).freeBitmap();
                }
            }
        }
    }

    private class CarouselItem implements Target {
        private Bitmap bitmap;
        private boolean hasActiveRequest = false;
        private String imageURL;
        private String productionId;

        public CarouselItem(String productionId, String imageURL) {
            this.productionId = productionId;
            this.imageURL = imageURL;
        }

        public void prepareBitmap() {
            if (!this.hasActiveRequest && this.bitmap == null) {
                this.hasActiveRequest = true;
                if (this.imageURL == null) {
                    return;
                }
                int cloudinaryImageHeight;
                if (((double) Carousel.this.getResources().getDisplayMetrics().density) >= 1.5d) {
                    cloudinaryImageHeight = Carousel.this.smallImageHeight / 2;
                    int access$100 = Carousel.this.smallImageWidth / 2;
                    return;
                }
                cloudinaryImageHeight = Carousel.this.smallImageHeight;
//                Carousel.this.smallImageWidth;
            }
        }

        public void freeBitmap() {
            if (this.hasActiveRequest) {
                Picasso.with(Carousel.this.getContext()).cancelRequest((Target) this);
            }
            this.bitmap = null;
            this.hasActiveRequest = false;
        }

        public Bitmap getBitmap() {
            if (this.bitmap != null) {
                return this.bitmap;
            }
            return Carousel.this.placeholder;
        }

        public String getProductionId() {
            return this.productionId;
        }

        public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadedFrom) {
            this.bitmap = bitmap;
            this.hasActiveRequest = false;
            Carousel.this.invalidate();
        }

        public void onBitmapFailed(Drawable drawable) {
        }

        public void onPrepareLoad(Drawable drawable) {
        }
    }

    public Carousel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        this.imageHeight = (int) (((float) metrics.widthPixels) * 0.7f);
        this.imageWidth = (int) (((float) this.imageHeight) * IMAGE_ASPECT_RATIO);
        this.smallImageHeight = (int) (((float) metrics.widthPixels) * 0.35f);
        this.smallImageWidth = (int) (((float) this.smallImageHeight) * IMAGE_ASPECT_RATIO);
        this.imageCellWidth = (int) (((float) metrics.widthPixels) * IMAGE_CELL_WIDTH_PERCENTAGE);
        this.paint = new Paint(2);
        this.paint.setAntiAlias(true);
        Options bitmapOptions = new Options();
        bitmapOptions.inPreferredConfig = Config.RGB_565;
//        URL url = new URL("http://....");
//        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        this.placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.pub_two, bitmapOptions);
    }

    public int count() {
        if (this.carouselAdapter != null) {
            return this.carouselAdapter.carouselItemList.size();
        }
        return 0;
    }

    public void setSectionData(SectionData sectionData) {
        this.sectionData = sectionData;
        if (sectionData.getTotalSize() > 0) {
            this.carouselAdapter = new CarouselAdapter(sectionData);
            setOffset(0);
            return;
        }
        this.carouselAdapter = null;
        invalidate();
    }

    public int getScrollableWidth() {
        return this.sectionData.getTotalSize() * this.imageCellWidth;
    }

    public int getImageCellWidth() {
        return this.imageCellWidth;
    }

    public void setOffset(int offset) {
        this.leftOffset = offset;
        this.centerIndex = Math.round((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.leftOffset)) / ((float) this.imageCellWidth));
        this.carouselAdapter.setActiveIndexRange(this.centerIndex - 2, this.centerIndex + 2);
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.carouselAdapter != null) {
            int endIndex = this.centerIndex + 2;
            for (int i = this.centerIndex - 2; i <= endIndex; i++) {
                CarouselItem carouselItem = this.carouselAdapter.getCarouselItem(i);
                if (carouselItem.getBitmap() != null) {
                    int left = (-this.leftOffset) + (this.imageCellWidth * i);
                    if (left <= (-this.imageCellWidth) || left >= this.imageCellWidth) {
                        updateRect(left, this.smallImageWidth, this.smallImageHeight);
                    } else {
                        float scale = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT - ((((float) (this.imageCellWidth - Math.abs(left))) * DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) / ((float) this.imageCellWidth));
                        updateRect(left, (int) (((float) this.imageWidth) - (((float) this.smallImageWidth) * scale)), (int) (((float) this.imageHeight) - (((float) this.smallImageHeight) * scale)));
                    }
                    if (this.focusedBitmap != null && this.focusedProductionId.equals(carouselItem.getProductionId())) {
                        canvas.drawBitmap(this.focusedBitmap, null, this.rect, this.paint);
                    } else if (carouselItem.getBitmap() != null) {
                        canvas.drawBitmap(carouselItem.getBitmap(), null, this.rect, this.paint);
                    }
                }
            }
        }
    }

    private void updateRect(int leftOffset, int width, int height) {
        int left = (getWidth() - width) / 2;
        int top = (getHeight() - height) / 2;
        this.rect.set(left + leftOffset, top, (left + leftOffset) + width, top + height);
    }

    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
        if (this.focusedProductionId != null) {
            this.focusedBitmap = bitmap;
            invalidate();
        }
    }

    public void onBitmapFailed(Drawable errorDrawable) {
    }

    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
}
