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
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.ui.common.ShopifyTheme;
import com.shopify.buy.utils.CollectionUtils;
import com.shopify.buy.utils.ImageUtility;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class CartLineItemView extends LinearLayout {

    protected ImageView image;
    protected TextView title;
    protected TextView variant;
    protected TextView price;
    // TODO quantity picker
    protected QuantityPicker quantityPicker;

    public CartLineItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        image = (ImageView) findViewById(R.id.line_item_image);
        title = (TextView) findViewById(R.id.line_item_title);
        variant = (TextView) findViewById(R.id.line_item_variant);
        price = (TextView) findViewById(R.id.line_item_price);

        // TODO quantity picker
        //quantityPicker = (QuantityPicker) findViewById(R.id.quantity_picker);
    }

    public void applyTheme(ShopifyTheme theme) {
        title.setTextColor(theme.getAccentColor());
        variant.setTextColor(theme.getProductDescriptionColor(getResources()));
        price.setTextColor(theme.getProductDescriptionColor(getResources()));

        theme.applyCustomFont(title);
        theme.applyCustomFont(variant);
        theme.applyCustomFont(price);

        findViewById(R.id.line_item_divider).setBackgroundColor(theme.getDividerColor(getResources()));
    }

    public void setLineItem(final CartLineItem lineItem, NumberFormat currencyFormat, QuantityPicker.OnQuantityChangedListener listener) {
        // TODO - quantity picker
        //quantityPicker.setLineItem(lineItem, listener);

        title.setText(lineItem.getVariant().getProductTitle().toUpperCase());

        List<OptionValue> optionValues = lineItem.getVariant().getOptionValues();
        if (CollectionUtils.isEmpty(optionValues)) {
            variant.setVisibility(View.GONE);
        } else {
            variant.setVisibility(View.VISIBLE);

            StringBuilder breadcrumbs = new StringBuilder();
            for (int i = 0; i < optionValues.size(); i++) {
                breadcrumbs.append(optionValues.get(i).getName());
                breadcrumbs.append(": ");
                breadcrumbs.append(optionValues.get(i).getValue());
                if (i < optionValues.size() - 1) {
                    breadcrumbs.append(" • ");
                }
            }
            variant.setText(breadcrumbs.toString());
        }

        StringBuilder priceWithMultiplier = new StringBuilder();
        priceWithMultiplier.append(currencyFormat.format(Double.parseDouble(lineItem.getPrice())));
        priceWithMultiplier.append(" x ");
        priceWithMultiplier.append(lineItem.getQuantity());
        price.setText(priceWithMultiplier.toString());

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    int height = getHeight();
                    int width = height;

                    ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
                    layoutParams.height = height;
                    layoutParams.width = width;
                    image.setLayoutParams(layoutParams);

                    String imageUrl = ImageUtility.stripQueryFromUrl(lineItem.getVariant().getImageUrl());
                    ImageUtility.loadImageResourceIntoSizedView(Picasso.with(getContext()), imageUrl, image, false, null);
                }
            });
        }
    }

}
