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

    // Shop table
    String TABLE_SHOP = "shop";

    // Collections tables
    String TABLE_COLLECTIONS = "collections";

    // Products tables
    String TABLE_PRODUCTS = "products";
    String TABLE_IMAGES = "images";
    String TABLE_OPTIONS = "options";
    String TABLE_PRODUCT_VARIANTS = "product_variants";
    String TABLE_OPTION_VALUES = "option_values";

    // Cart tables
    String TABLE_LINE_ITEMS = "line_items";
    String TABLE_LINE_ITEM_PROPERTIES = "line_item_properties";

    // Checkouts table
    String TABLE_CHECKOUT_TOKENS = "checkout_tokens";

    interface ShopTable {
        String NAME = "name";
        String CITY = "city";
        String PROVINCE = "province";
        String COUNTRY = "country";
        String CONTACT_EMAIL = "contact_email";
        String CURRENCY = "currency";
        String DOMAIN = "domain";
        String URL = "url";
        String MYSHOPIFY_DOMAIN = "myshopify_domain";
        String DESCRIPTION = "description";
        String SHIPS_TO_COUNTRIES = "ships_to_countries";
        String MONEY_FORMAT = "money_format";
        String PUBLISHED_PRODUCTS_COUNT = "published_products_count";
    }

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
        String ID = "id";
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
        String IMAGE_URL = "image_url";
    }

    interface OptionValuesTable {
        String OPTION_ID = "option_id";
        String VARIANT_ID = "variant_id";
        String PRODUCT_ID = "product_id";
        String NAME = "name";
        String VALUE = "value";
    }

    interface LineItemsTable {
        String USER_ID = "user_id";
        String LINE_ITEM_ID = "line_item_id";
        String QUANTITY = "quantity";
        String PRICE = "price";
        String REQUIRES_SHIPPING = "requires_shipping";
        String VARIANT_ID = "variant_id";
        String TITLE = "title";
        String PRODUCT_ID = "product_id";
        String VARIANT_TITLE = "variant_title";
        String LINE_PRICE = "line_price";
        String COMPARE_AT_PRICE = "compare_at_price";
        String SKU = "sku";
        String TAXABLE = "taxable";
        String GRAMS = "grams";
        String FULFILLMENT_SERVICE = "fulfillment_service";
    }

    interface LineItemPropertiesTable {
        String USER_ID = "user_id";
        String LINE_ITEM_ID = "line_item_id";
        String KEY = "key";
        String VALUE = "value";
    }

    interface CheckoutTokensTable {
        String USER_ID = "user_id";
        String TOKEN = "token";
    }

}
