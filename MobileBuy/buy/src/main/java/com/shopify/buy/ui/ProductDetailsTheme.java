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

package com.shopify.buy.ui;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes the colors and backgrounds to use for the {@link ProductDetailsFragmentView}.
 */
@Deprecated
public class ProductDetailsTheme extends ShopifyTheme implements Parcelable {

    @Deprecated
    public ProductDetailsTheme(Resources res) {
        super(res);
    }

    @Deprecated
    public ProductDetailsTheme(Style style, int accentColor, boolean showProductImageBackground) {
        super(style, accentColor, showProductImageBackground);
    }

    @Deprecated
    private ProductDetailsTheme(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<ProductDetailsTheme> CREATOR = new Parcelable.Creator<ProductDetailsTheme>() {
        public ProductDetailsTheme createFromParcel(Parcel in) {
            return new ProductDetailsTheme(in);
        }

        public ProductDetailsTheme[] newArray(int size) {
            return new ProductDetailsTheme[size];
        }
    };
}
