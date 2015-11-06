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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.shopify.buy.model.ShopifyObject;
import com.shopify.buy.utils.ImageUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public abstract class RecyclerViewHolder<T extends ShopifyObject> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    protected final ClickListener clickListener;
    protected final boolean cropImage;

    protected T item;
    protected ImageView imageView;

    public RecyclerViewHolder(final View itemView, final boolean cropImage, final ClickListener clickListener) {
        super(itemView);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        this.clickListener = clickListener;
        this.cropImage = cropImage;
    }

    public abstract String getImageUrl();

    public void setItem(T item) {
        this.item = item;
        loadImage();
    }

    private void loadImage() {
        if (getImageUrl() != null) {
            final String imageUrl = ImageUtility.stripQueryFromUrl(getImageUrl());

            Picasso picasso = Picasso.with(imageView.getContext());

            // TODO - find a way to load smaller images if we can
            ImageUtility.loadRemoteImageIntoViewWithoutSize(picasso, imageUrl, imageView, ImageUtility.SIZE_UNKNOWN, ImageUtility.SIZE_UNKNOWN, cropImage, new Callback() {
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
