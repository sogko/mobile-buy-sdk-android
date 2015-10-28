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
import android.content.res.ColorStateList;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.dataprovider.CartManager;
import com.shopify.buy.model.Cart;
import com.shopify.buy.ui.ShopifyTheme;

import java.text.NumberFormat;

public class CartFragmentView extends RelativeLayout {

    private TextView subtotalValue;

    public CartFragmentView(Context context) {
        super(context);
    }

    public CartFragmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTheme(ShopifyTheme theme) {
        // TODO make sure the color state list stuff works on old and new devices
        
        findViewById(R.id.footer_divider).setBackgroundColor(theme.getDividerColor(getResources()));

        setBackgroundColor(theme.getBackgroundColor(getResources()));

        ((TextView) findViewById(R.id.subtotal_label)).setTextColor(theme.getProductTitleColor(getResources()));
        ((TextView) findViewById(R.id.shipping_and_taxes)).setTextColor(theme.getProductDescriptionColor(getResources()));

        subtotalValue = (TextView) findViewById(R.id.subtotal_value);
        subtotalValue.setTextColor(theme.getAccentColor());

        Button checkoutButton = (Button) findViewById(R.id.checkout_button);
        checkoutButton.setBackgroundColor(theme.getAccentColor());
        checkoutButton.setTextColor(theme.getBackgroundColor(getResources()));

        int disabledTextAlpha = getResources().getInteger(R.integer.disabled_text_alpha);
        int textColor = getResources().getColor(R.color.light_dialog_title);
        checkoutButton.setTextColor(new ColorStateList(
                new int[][]{new int[]{-android.R.attr.state_enabled}, new int[]{android.R.attr.state_enabled}},
                new int[]{ColorUtils.setAlphaComponent(textColor, disabledTextAlpha), textColor,}
        ));
    }

    public void updateSubtotal(Cart cart, NumberFormat currencyFormat) {
        subtotalValue.setText(currencyFormat.format(cart.getSubtotal()));
    }

}
