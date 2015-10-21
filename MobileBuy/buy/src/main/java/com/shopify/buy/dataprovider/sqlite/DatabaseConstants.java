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

package com.shopify.buy.dataprovider.sqlite;

interface DatabaseConstants {

    int DATABASE_VERSION = 1;

    String DATABASE_NAME = "mobile_buy_sdk_sqlite_database";

    String TABLE_COLLECTIONS = "collections";
    String TABLE_PRODUCTS = "products";
    String TABLE_IMAGES = "images";
    String TABLE_OPTIONS = "options";
    String TABLE_PRODUCT_VARIANTS = "product_variants";
    String TABLE_OPTION_VALUES = "option_values";

    interface CollectionsTable {
        String TITLE = "title";
        String HANDLE = "handle";
        String COLLECTION_ID = "collection_id";
        String BODY_HTML = "body_html";
        String PUBLISHED_AT = "published_at";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
        String PUBLISHED = "published";
        String IMAGE_SRC = "image_src";
        String IMAGE_CREATED_AT = "image_created_at";
    }

    interface ProductsTable {
        String PRODUCT_ID = "product_id";
        String CHANNEL_ID = "channel_id";
        String TITLE = "title";
        String HANDLE = "handle";
        String BODY_HTML = "body_html";
        String PUBLISHED_AT = "published_at";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
        String VENDOR = "vendor";
        String PRODUCT_TYPE = "product_type";
        String TAGS = "tags";
        String AVAILABLE = "available";
        String PUBLISHED = "published";
    }

    interface ImagesTable {
        String POSITION = "position";
        String PRODUCT_ID = "product_id";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
        String VARIANT_IDS = "variant_ids";
        String SRC = "src";
    }

    interface OptionsTable {
        String NAME = "name";
        String POSITION = "position";
        String PRODUCT_ID = "product_id";
    }

    interface ProductVariantsTable {
        String ID = "id";
        String TITLE = "title";
        String PRICE = "price";
        String GRAMS = "grams";
        String COMPARE_AT_PRICE = "compare_at_price";
        String SKU = "sku";
        String REQUIRES_SHIPPING = "requires_shipping";
        String TAXABLE = "taxable";
        String POSITION = "position";
        String PRODUCT_ID = "product_id";
        String PRODUCT_TITLE = "product_title";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
        String AVAILABLE = "available";
    }

    interface OptionValuesTable {
        String OPTION_ID = "option_id";
        String NAME = "name";
        String VALUE = "value";
    }

}
