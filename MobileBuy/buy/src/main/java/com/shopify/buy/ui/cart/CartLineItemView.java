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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.model.CartLineItem;

public class CartLineItemView extends LinearLayout {

    protected CartLineItem lineItem;
    protected TextView titleTextView;
    protected TextView priceTextView;
    protected TextView variantTextView;
    protected QuantityPicker quantityPicker;

    public CartLineItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLineItem(CartLineItem lineItem) {
        this.lineItem = lineItem;

        quantityPicker.setLineItem(lineItem);

        titleTextView.setText("Kick Scooter");
        priceTextView.setText("$15.00");
        variantTextView.setText("Adult • Mikado • 5\" Wheel Radius");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        titleTextView = (TextView) findViewById(R.id.title_text_view);
        priceTextView = (TextView) findViewById(R.id.price_text_view);
        variantTextView = (TextView) findViewById(R.id.variant_text_view);

        quantityPicker = (QuantityPicker) findViewById(R.id.quantity_picker);
    }
}
