/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.buy.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.shopify.buy.utils.ImageUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/***
 * This class is used to create an {@link ImageView} with a fixed aspect ratio.
 * The width of the image in the layout must be resolvable at the time onMeasure() is called, and cannot be WRAP_CONTENT.
 * It will load a properly sized image from the url specified with {@link #setImageUrl(String)}}
 */
public class FixedAspectImageView extends ImageView {
    private static String TAG = FixedAspectImageView.class.getSimpleName();

    private String imageUrl;
    private boolean cropImage = true;
    private float ratio = 1;

    public void setImageUrl(String imageUrl) {
        if (imageUrl != null) {
            this.imageUrl = ImageUtility.stripQueryFromUrl(imageUrl);
        }
    }

    public void setCropImage(boolean cropImage) {
        this.cropImage = cropImage;
    }

    public void setRatio(int width, int height) {
        ratio = (float) height / width;
    }

    public FixedAspectImageView(Context context) {
        super(context);
    }

    public FixedAspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedAspectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void loadImage() {
        if (imageUrl != null) {
            Picasso picasso = Picasso.with(getContext());

            // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/505
            ImageUtility.loadRemoteImageIntoViewWithoutSize(picasso, imageUrl, this, getMeasuredWidth(), getMeasuredHeight(), cropImage, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                }
            });
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = Math.round(widthSize * ratio);
        int newHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightSpec);

        loadImage();
    }
}
