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

package com.shopify.buy.ui.cart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.model.CartLineItem;

public class QuantityPicker extends LinearLayout {

    interface OnQuantityChangedListener {
        void onQuantityIncreased(CartLineItem lineItem);
        void onQuantityDecreased(CartLineItem lineItem);
    }

    protected CartLineItem lineItem;
    protected TextView quantityTextView;
    protected OnQuantityChangedListener listener;

    public QuantityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLineItem(CartLineItem lineItem, OnQuantityChangedListener listener) {
        this.lineItem = lineItem;
        this.listener = listener;
        quantityTextView.setText(Long.toString(lineItem.getQuantity()));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        quantityTextView = (TextView) findViewById(R.id.quantity_text_view);

        findViewById(R.id.minus_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onQuantityDecreased(lineItem);
                }
            }
        });

        findViewById(R.id.plus_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onQuantityIncreased(lineItem);
                }
            }
        });
    }

}
