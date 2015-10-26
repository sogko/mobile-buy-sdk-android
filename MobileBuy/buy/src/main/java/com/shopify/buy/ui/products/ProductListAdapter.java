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
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.model.Image;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;
import com.shopify.buy.utils.CurrencyFormatter;
import com.shopify.buy.utils.ImageUtility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// TODO we should create a base class for our recycler view adapters and view holders.
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    List<Product> products;
    Context context;
    Shop shop;
    NumberFormat currencyFormatter;

    // Listener used to pass click events back to the fragment or adapter
    private ClickListener clickListener;

    public ProductListAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Product product = products.get(i);
        viewHolder.product = product;

        viewHolder.productTitleView.setText(product.getTitle());

        // Set the product price.  If there are multiple prices show the minimum
        Set<String> prices = product.getPrices();

        String productPrice = currencyFormatter.format(Double.parseDouble(product.getMinimumPrice()));
        if (prices.size() > 1) {
            productPrice = context.getString(R.string.from) + " " + productPrice;
        }
        viewHolder.productPriceView.setText(productPrice);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (products != null) {
            size = products.size();
        }
        return size;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView productTitleView;
        public TextView productPriceView;
        public ImageView productImageView;
        public Product product;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            productTitleView = (TextView)itemView.findViewById(R.id.item_title);
            productPriceView = (TextView)itemView.findViewById(R.id.product_price);
            productImageView = (ImageView)itemView.findViewById(R.id.item_image);

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
                        int height = width;

                        ViewGroup.LayoutParams layoutParams = productImageView.getLayoutParams();
                        layoutParams.height = height;
                        layoutParams.width = width;
                        productImageView.setLayoutParams(layoutParams);

                        if (product.getImages() != null && product.getImages().size() > 0) {

                            Image image = product.getImages().get(0);
                            String imageUrl = ImageUtility.stripQueryFromUrl(image.getSrc());

                            ImageUtility.loadImageResourceIntoSizedView(Picasso.with(context), imageUrl, productImageView, false, new Callback() {
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
                });
            }}


        @Override
        public void onClick(View v){
            if (clickListener != null) {
                int position = getAdapterPosition();
                clickListener.onItemClick(position, v, products.get(position));
            }
        }

        @Override
        public boolean onLongClick(View v){
            if (clickListener != null) {
                int position = getAdapterPosition();
                clickListener.onItemLongClick(position, v, products.get(position));
                return false;
            }
            return true;
        }
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
        currencyFormatter = CurrencyFormatter.getFormatter(Locale.getDefault(), shop.getCurrency());
    }

    public interface ClickListener {
        void onItemClick(int position, View v, Product product);
        void onItemLongClick(int position, View v, Product product);
    }
}

