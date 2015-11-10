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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.model.Collection;
import com.shopify.buy.ui.common.RecyclerViewHolder;
import com.shopify.buy.ui.common.ShopifyTheme;
import com.shopify.buy.ui.common.FixedAspectImageView;

import java.util.List;

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.CollectionViewHolder> {

    List<Collection> collections;

    final Context context;
    final ShopifyTheme theme;

    // Listener used to pass click events back to the fragment or adapter
    private RecyclerViewHolder.ClickListener<Collection> clickListener;

    public CollectionListAdapter(Context context, ShopifyTheme theme) {
        super();
        this.context = context;
        this.theme = theme;
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collection_list_item, viewGroup, false);
        CollectionViewHolder viewHolder = new CollectionViewHolder(view, theme, clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder viewHolder, int index) {
        viewHolder.setItem(collections.get(index));
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (collections != null) {
            size = collections.size();
        }
        return size;
    }

    public void setClickListener(RecyclerViewHolder.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class CollectionViewHolder extends RecyclerViewHolder<Collection> {

        private TextView nameText;

        public CollectionViewHolder(View itemView, ShopifyTheme theme, ClickListener<Collection> clickListener) {
            super(itemView, true, clickListener);

            nameText = (TextView) itemView.findViewById(R.id.collection_name);
            imageView = (FixedAspectImageView) itemView.findViewById(R.id.collection_image);

            if (theme != null) {
                theme.applyCustomFont(nameText);
            }
        }

        @Override
        public void setItem(Collection collection) {
            super.setItem(collection);

            nameText.setText(collection.getTitle());
            imageView.setRatio(16, 9);
        }

        @Override
        public String getImageUrl() {
            return item.getImageUrl();
        }
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

}

