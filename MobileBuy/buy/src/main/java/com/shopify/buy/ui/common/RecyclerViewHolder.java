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

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.shopify.buy.model.ShopifyObject;
import com.shopify.buy.utils.ImageUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class RecyclerViewHolder<T extends ShopifyObject> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public enum ImageAspectRatio {
        SQUARE,
        SIXTEEN_BY_NINE
    }

    protected final ClickListener clickListener;
    protected final ImageAspectRatio ratio;
    protected final boolean cropImage;

    protected T item;
    protected ImageView imageView;

    public final AtomicBoolean isLayedOut = new AtomicBoolean(false);

    public RecyclerViewHolder(final View itemView, final ImageAspectRatio ratio, final boolean cropImage, final ClickListener clickListener) {
        super(itemView);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        this.clickListener = clickListener;
        this.ratio = ratio;
        this.cropImage = cropImage;
    }

    public abstract String getImageUrl();

    public void setItem(T item) {
        this.item = item;

        if (isLayedOut.get()) {
            loadImage();
        } else {
            waitForLayout();
        }
    }

    private void waitForLayout() {
        ViewTreeObserver viewTreeObserver = itemView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        itemView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    int width = itemView.getWidth();

                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.width = width;

                    switch (ratio) {
                        case SQUARE:
                            layoutParams.height = width;
                            break;
                        case SIXTEEN_BY_NINE:
                            layoutParams.height = width * 9 / 16;
                            break;
                    }

                    imageView.setLayoutParams(layoutParams);

                    loadImage();

                    isLayedOut.set(true);
                }
            });
        }
    }

    private void loadImage() {
        if (getImageUrl() != null) {
            final String imageUrl = ImageUtility.stripQueryFromUrl(getImageUrl());

            ImageUtility.loadImageResourceIntoSizedView(Picasso.with(imageView.getContext()), imageUrl, imageView, cropImage, new Callback() {
                @Override
                public void onSuccess() {
                    // TODO we should have image loading placeholders or spinners
                }

                @Override
                public void onError() {
                    // TODO we should have image loading placeholders or spinners
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            int position = getAdapterPosition();
            clickListener.onItemClick(position, v, item);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (clickListener != null) {
            int position = getAdapterPosition();
            clickListener.onItemLongClick(position, v, item);
            return false;
        }
        return true;
    }

    public interface ClickListener<T extends ShopifyObject> {
        void onItemClick(int position, View v, T t);

        void onItemLongClick(int position, View v, T t);
    }

}
