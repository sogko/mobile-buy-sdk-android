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

package com.shopify.buy.ui.collections;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.model.Collection;
import com.shopify.buy.utils.ImageUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.ViewHolder> {

    List<Collection> collections;
    Context context;

    // Listener used to pass click events back to the fragment or adapter
    private ClickListener clickListener;

    public CollectionListAdapter(Activity context) {
        super();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collection_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.collection = collections.get(index);
        viewHolder.collectionNameView.setText(viewHolder.collection.getTitle());

        if (viewHolder.isLayedOut.get()) {
            viewHolder.loadImage();
        } else {
            viewHolder.waitForLayout();
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (collections != null) {
            size = collections.size();
        }
        return size;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public Collection collection;
        public TextView collectionNameView;
        public ImageView collectionImageView;

        public final AtomicBoolean isLayedOut = new AtomicBoolean(false);

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            collectionNameView = (TextView) itemView.findViewById(R.id.collection_name);
            collectionImageView = (ImageView) itemView.findViewById(R.id.collection_image);
        }

        public void waitForLayout() {
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
                        int height = width * 9 / 16;

                        ViewGroup.LayoutParams layoutParams = collectionImageView.getLayoutParams();
                        layoutParams.height = height;
                        layoutParams.width = width;
                        collectionImageView.setLayoutParams(layoutParams);

                        loadImage();

                        isLayedOut.set(true);
                    }
                });
            }
        }

        public void loadImage() {
            if (collection.getImageUrl() != null) {
                final String imageUrl = ImageUtility.stripQueryFromUrl(collection.getImageUrl());

                ImageUtility.loadImageResourceIntoSizedView(Picasso.with(context), imageUrl, collectionImageView, true, new Callback() {
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
                clickListener.onItemClick(position, v, collections.get(position));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (clickListener != null) {
                int position = getAdapterPosition();
                clickListener.onItemLongClick(position, v, collections.get(position));
                return false;
            }
            return true;
        }
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public interface ClickListener {
        void onItemClick(int position, View v, Collection collection);
        void onItemLongClick(int position, View v, Collection collection);
    }
}

