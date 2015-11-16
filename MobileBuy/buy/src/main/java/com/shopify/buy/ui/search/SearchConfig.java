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

package com.shopify.buy.ui.search;

import android.os.Bundle;
import android.text.TextUtils;

import com.shopify.buy.ui.common.BaseConfig;

/***
 * Used to serialize data for the {@link SearchFragment}.
 */
class SearchConfig extends BaseConfig {

    public static final String EXTRA_SEARCH_QUERY = "com.shopify.buy.ui.SEARCH_QUERY";
    public static final String EXTRA_SEARCH_RESULTS = "com.shopify.buy.ui.SEARCH_RESULTS";

    private String query;

    public String getSearchQuery() {
        return query;
    }

    public void setSearchQuery(String query) {
        this.query = query;
    }

    public Bundle toBundle() {
        Bundle bundle = super.toBundle();

        if (!TextUtils.isEmpty(query)) {
            bundle.putString(EXTRA_SEARCH_QUERY, query);
        }

        return bundle;
    }

}
