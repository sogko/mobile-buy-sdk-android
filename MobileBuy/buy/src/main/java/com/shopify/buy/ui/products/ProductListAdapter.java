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

package com.shopify.buy.ui.products;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.common.RecyclerViewHolder;
import com.shopify.buy.ui.common.ShopifyTheme;
import com.shopify.buy.utils.CurrencyFormatter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    final Context context;
    final ShopifyTheme theme;

    List<Product> products;
    Shop shop;
    NumberFormat currencyFormatter;

    // Listener used to pass click events back to the fragment or adapter
    private RecyclerViewHolder.ClickListener<Product> clickListener;

    public ProductListAdapter(Context context, ShopifyTheme theme) {
        super();
        this.context = context;
        this.theme = theme;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, viewGroup, false);

        ProductViewHolder viewHolder = new ProductViewHolder(view, theme, clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int index) {
        viewHolder.setItem(products.get(index));
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (products != null) {
            size = products.size();
        }
        return size;
    }

    public void setClickListener(RecyclerViewHolder.ClickListener<Product> clickListener) {
        this.clickListener = clickListener;
    }

    class ProductViewHolder extends RecyclerViewHolder<Product> {

        public TextView titleView;
        public TextView priceView;

        public ProductViewHolder(View itemView, ShopifyTheme theme, ClickListener<Product> clickListener) {
            super(itemView, false, clickListener);

            titleView = (TextView) itemView.findViewById(R.id.item_title);
            priceView = (TextView) itemView.findViewById(R.id.item_price);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);

            if (theme != null) {
                titleView.setTextColor(theme.getAccentColor());

                theme.applyCustomFont(titleView);
                theme.applyCustomFont(priceView);
            }
        }

        @Override
        public void setItem(Product product) {
            super.setItem(product);

            titleView.setText(product.getTitle());

            // Set the product price.  If there are multiple prices show the minimum
            Set<String> prices = product.getPrices();

            String productPrice = currencyFormatter.format(Double.parseDouble(product.getMinimumPrice()));
            if (prices.size() > 1) {
                productPrice = context.getString(R.string.from) + " " + productPrice;
            }
            priceView.setText(productPrice);
        }

        @Override
        public String getImageUrl() {
            if (item.getImages() != null && item.getImages().size() > 0) {
                return item.getImages().get(0).getSrc();
            }
            return null;
        }
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
        currencyFormatter = CurrencyFormatter.getFormatter(Locale.getDefault(), shop.getCurrency());
    }

}

