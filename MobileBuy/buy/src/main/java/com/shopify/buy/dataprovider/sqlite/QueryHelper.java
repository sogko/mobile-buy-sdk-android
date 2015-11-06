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

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Image;
import com.shopify.buy.model.Option;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.model.internal.CollectionImage;
import com.shopify.buy.utils.DateUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryHelper implements DatabaseConstants {

    static String createCollectionsTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_COLLECTIONS)
                .append(" (")
                .append(CollectionsTable.COLLECTION_ID).append(" TEXT PRIMARY KEY, ")
                .append(CollectionsTable.TITLE).append(" TEXT, ")
                .append(CollectionsTable.BODY_HTML).append(" TEXT, ")
                .append(CollectionsTable.HANDLE).append(" TEXT, ")
                .append(CollectionsTable.PUBLISHED).append(" INTEGER, ")
                .append(CollectionsTable.PUBLISHED_AT).append(" TEXT, ")
                .append(CollectionsTable.CREATED_AT).append(" TEXT, ")
                .append(CollectionsTable.UPDATED_AT).append(" TEXT, ")
                .append(CollectionsTable.IMAGE_CREATED_AT).append(" TEXT, ")
                .append(CollectionsTable.IMAGE_SRC).append(" TEXT")
                .append(")");
        return sql.toString();
    }

    static ContentValues contentValues(Collection collection) {
        ContentValues values = new ContentValues();

        values.put(CollectionsTable.COLLECTION_ID, collection.getCollectionId());
        values.put(CollectionsTable.TITLE, collection.getTitle());
        values.put(CollectionsTable.BODY_HTML, collection.getHtmlDescription());
        values.put(CollectionsTable.HANDLE, collection.getHandle());
        values.put(CollectionsTable.PUBLISHED, collection.isPublished() ? 1 : 0);
        values.put(CollectionsTable.PUBLISHED_AT, DateUtility.toString(collection.getPublishedAtDate()));
        values.put(CollectionsTable.CREATED_AT, DateUtility.toString(collection.getCreatedAtDate()));
        values.put(CollectionsTable.UPDATED_AT, DateUtility.toString(collection.getUpdatedAtDate()));

        CollectionImage image = collection.getImage();
        if (image != null) {
            values.put(CollectionsTable.IMAGE_CREATED_AT, image.getCreatedAt());
            values.put(CollectionsTable.IMAGE_SRC, image.getSrc());
        }

        return values;
    }

    static Collection collection(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(CollectionsTable.TITLE));
        String htmlDescription = cursor.getString(cursor.getColumnIndex(CollectionsTable.BODY_HTML));
        String handle = cursor.getString(cursor.getColumnIndex(CollectionsTable.HANDLE));
        boolean published = cursor.getInt(cursor.getColumnIndex(CollectionsTable.PUBLISHED)) == 1;
        String collectionId = cursor.getString(cursor.getColumnIndex(CollectionsTable.COLLECTION_ID));
        Date createdAtDate = DateUtility.toDate(cursor.getString(cursor.getColumnIndex(CollectionsTable.CREATED_AT)));
        Date updatedAtDate = DateUtility.toDate(cursor.getString(cursor.getColumnIndex(CollectionsTable.UPDATED_AT)));
        Date publishedAtDate = DateUtility.toDate(cursor.getString(cursor.getColumnIndex(CollectionsTable.PUBLISHED_AT)));
        String imageCreatedAt = cursor.getString(cursor.getColumnIndex(CollectionsTable.IMAGE_CREATED_AT));
        String imageSrc = cursor.getString(cursor.getColumnIndex(CollectionsTable.IMAGE_SRC));

        return new ModelFactory.DBCollection(title, htmlDescription, handle, published, collectionId, createdAtDate, updatedAtDate, publishedAtDate, imageCreatedAt, imageSrc);
    }

    static String createProductsTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_PRODUCTS)
                .append(" (")
                .append(ProductsTable.PRODUCT_ID).append(" TEXT PRIMARY KEY, ")
                .append(ProductsTable.CHANNEL_ID).append(" TEXT, ")
                .append(ProductsTable.TITLE).append(" TEXT, ")
                .append(ProductsTable.HANDLE).append(" TEXT, ")
                .append(ProductsTable.BODY_HTML).append(" TEXT, ")
                .append(ProductsTable.PUBLISHED_AT).append(" TEXT, ")
                .append(ProductsTable.CREATED_AT).append(" TEXT, ")
                .append(ProductsTable.UPDATED_AT).append(" TEXT, ")
                .append(ProductsTable.VENDOR).append(" TEXT, ")
                .append(ProductsTable.PRODUCT_TYPE).append(" TEXT, ")
                .append(ProductsTable.TAGS).append(" TEXT, ")
                .append(ProductsTable.AVAILABLE).append(" INTEGER, ")
                .append(ProductsTable.PUBLISHED).append(" INTEGER")
                .append(")");
        return sql.toString();
    }

    static ContentValues contentValues(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductsTable.PRODUCT_ID, product.getProductId());
        values.put(ProductsTable.CHANNEL_ID, product.getChannelId());
        values.put(ProductsTable.TITLE, product.getTitle());
        values.put(ProductsTable.HANDLE, product.getHandle());
        values.put(ProductsTable.BODY_HTML, product.getBodyHtml());
        values.put(ProductsTable.PUBLISHED_AT, DateUtility.toString(product.getPublishedAtDate()));
        values.put(ProductsTable.CREATED_AT, DateUtility.toString(product.getCreatedAtDate()));
        values.put(ProductsTable.UPDATED_AT, DateUtility.toString(product.getUpdatedAtDate()));
        values.put(ProductsTable.VENDOR, product.getVendor());
        values.put(ProductsTable.PRODUCT_TYPE, product.getProductType());
        values.put(ProductsTable.TAGS, TextUtils.join(",", product.getTags().toArray()));
        values.put(ProductsTable.AVAILABLE, product.isAvailable() ? 1 : 0);
        values.put(ProductsTable.PUBLISHED, product.isPublished() ? 1 : 0);
        return values;
    }

    static Product product(Cursor cursor, List<Image> images, List<ProductVariant> variants, List<Option> options) {
        String productId = cursor.getString(cursor.getColumnIndex(ProductsTable.PRODUCT_ID));
        String channelId = cursor.getString(cursor.getColumnIndex(ProductsTable.CHANNEL_ID));
        String title = cursor.getString(cursor.getColumnIndex(ProductsTable.TITLE));
        String handle = cursor.getString(cursor.getColumnIndex(ProductsTable.HANDLE));
        String bodyHtml = cursor.getString(cursor.getColumnIndex(ProductsTable.BODY_HTML));
        Date createdAtDate = DateUtility.toDate(cursor.getString(cursor.getColumnIndex(ProductsTable.CREATED_AT)));
        Date updatedAtDate = DateUtility.toDate(cursor.getString(cursor.getColumnIndex(ProductsTable.UPDATED_AT)));
        Date publishedAtDate = DateUtility.toDate(cursor.getString(cursor.getColumnIndex(ProductsTable.PUBLISHED_AT)));
        String vendor = cursor.getString(cursor.getColumnIndex(ProductsTable.VENDOR));
        String productType = cursor.getString(cursor.getColumnIndex(ProductsTable.PRODUCT_TYPE));
        boolean available = cursor.getInt(cursor.getColumnIndex(ProductsTable.AVAILABLE)) == 1;
        boolean published = cursor.getInt(cursor.getColumnIndex(ProductsTable.PUBLISHED)) == 1;

        String tagCSV = cursor.getString(cursor.getColumnIndex(ProductsTable.TAGS));
        Set<String> tags = new HashSet<>();
        if (!TextUtils.isEmpty(tagCSV)) {
            tags.addAll(Arrays.asList(tagCSV.split(",")));
        }

        return new ModelFactory.DBProduct(productId, channelId, title, handle, bodyHtml, publishedAtDate, createdAtDate, updatedAtDate, vendor, productType, images, variants, options, tags, available, published);
    }

    static String searchProductsWhereClause(String query) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("LOWER(").append(ProductsTable.TITLE).append(") LIKE \'%").append(query.toLowerCase()).append("%\'");
        return whereClause.toString();
    }

    static String createImagesTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_IMAGES)
                .append(" (")
                .append(ImagesTable.PRODUCT_ID).append(" TEXT, ")
                .append(ImagesTable.POSITION).append(" INTEGER, ")
                .append(ImagesTable.CREATED_AT).append(" TEXT, ")
                .append(ImagesTable.UPDATED_AT).append(" TEXT, ")
                .append(ImagesTable.VARIANT_IDS).append(" TEXT, ")
                .append(ImagesTable.SRC).append(" TEXT, ")
                .append("PRIMARY KEY (").append(ImagesTable.PRODUCT_ID).append(", ").append(ImagesTable.POSITION).append(")")
                .append(")");
        return sql.toString();
    }

    static ContentValues contentValues(Image image) {
        ContentValues values = new ContentValues();
        values.put(ImagesTable.PRODUCT_ID, image.getProductId());
        values.put(ImagesTable.CREATED_AT, image.getCreatedAt());
        values.put(ImagesTable.POSITION, image.getPosition());
        values.put(ImagesTable.SRC, image.getSrc());
        values.put(ImagesTable.UPDATED_AT, image.getUpdatedAt());
        values.put(ImagesTable.VARIANT_IDS, TextUtils.join(",", image.getVariantIds().toArray()));
        return values;
    }

    static Image image(Cursor cursor) {
        long productId = Long.parseLong(cursor.getString(cursor.getColumnIndex(ImagesTable.PRODUCT_ID)));
        String createdAt = cursor.getString(cursor.getColumnIndex(ImagesTable.CREATED_AT));
        int position = cursor.getInt(cursor.getColumnIndex(ImagesTable.POSITION));
        String updatedAt = cursor.getString(cursor.getColumnIndex(ImagesTable.UPDATED_AT));
        String src = cursor.getString(cursor.getColumnIndex(ImagesTable.SRC));

        String variantIdsCSV = cursor.getString(cursor.getColumnIndex(ImagesTable.VARIANT_IDS));
        List<Long> variantIds = null;
        if (!TextUtils.isEmpty(variantIdsCSV)) {
            variantIds = new ArrayList<>();
            for (String id : variantIdsCSV.split(",")) {
                variantIds.add(Long.parseLong(id));
            }
        }

        return new ModelFactory.DBImage(createdAt, position, updatedAt, productId, variantIds, src);
    }

    static String createOptionsTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_OPTIONS)
                .append(" (")
                .append(OptionsTable.ID).append(" INTEGER, ")
                .append(OptionsTable.PRODUCT_ID).append(" TEXT, ")
                .append(OptionsTable.POSITION).append(" INTEGER, ")
                .append(OptionsTable.NAME).append(" TEXT, ")
                .append("PRIMARY KEY (").append(OptionsTable.PRODUCT_ID).append(", ").append(OptionsTable.POSITION).append(")")
                .append(")");
        return sql.toString();
    }

    static ContentValues contentValues(Option option) {
        ContentValues values = new ContentValues();
        values.put(OptionsTable.ID, option.getId());
        values.put(OptionsTable.PRODUCT_ID, option.getProductId());
        values.put(OptionsTable.NAME, option.getName());
        values.put(OptionsTable.POSITION, option.getPosition());
        return values;
    }

    static Option option(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(OptionsTable.ID));
        String name = cursor.getString(cursor.getColumnIndex(OptionsTable.NAME));
        int position = cursor.getInt(cursor.getColumnIndex(OptionsTable.POSITION));
        String productId = cursor.getString(cursor.getColumnIndex(OptionsTable.PRODUCT_ID));

        return new ModelFactory.DBOption(id, name, position, productId);
    }

    static String createProductVariantsTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_PRODUCT_VARIANTS)
                .append(" (")
                .append(ProductVariantsTable.ID).append(" TEXT PRIMARY KEY, ")
                .append(ProductVariantsTable.TITLE).append(" TEXT, ")
                .append(ProductVariantsTable.PRICE).append(" TEXT, ")
                .append(ProductVariantsTable.GRAMS).append(" INTEGER, ")
                .append(ProductVariantsTable.COMPARE_AT_PRICE).append(" TEXT, ")
                .append(ProductVariantsTable.SKU).append(" TEXT, ")
                .append(ProductVariantsTable.REQUIRES_SHIPPING).append(" INTEGER, ")
                .append(ProductVariantsTable.TAXABLE).append(" INTEGER, ")
                .append(ProductVariantsTable.POSITION).append(" INTEGER, ")
                .append(ProductVariantsTable.PRODUCT_ID).append(" TEXT, ")
                .append(ProductVariantsTable.PRODUCT_TITLE).append(" TEXT, ")
                .append(ProductVariantsTable.CREATED_AT).append(" TEXT, ")
                .append(ProductVariantsTable.UPDATED_AT).append(" TEXT, ")
                .append(ProductVariantsTable.AVAILABLE).append(" INTEGER, ")
                .append(ProductVariantsTable.IMAGE_URL).append(" TEXT")
                .append(")");
        return sql.toString();
    }

    static ContentValues contentValues(ProductVariant variant) {
        ContentValues values = new ContentValues();
        values.put(ProductVariantsTable.ID, variant.getId());
        values.put(ProductVariantsTable.TITLE, variant.getTitle());
        values.put(ProductVariantsTable.PRICE, variant.getPrice());
        values.put(ProductVariantsTable.GRAMS, variant.getGrams());
        values.put(ProductVariantsTable.COMPARE_AT_PRICE, variant.getCompareAtPrice());
        values.put(ProductVariantsTable.SKU, variant.getSku());
        values.put(ProductVariantsTable.REQUIRES_SHIPPING, variant.isRequiresShipping() ? 1 : 0);
        values.put(ProductVariantsTable.TAXABLE, variant.isTaxable() ? 1 : 0);
        values.put(ProductVariantsTable.POSITION, variant.getPosition());
        values.put(ProductVariantsTable.PRODUCT_ID, variant.getProductId());
        values.put(ProductVariantsTable.PRODUCT_TITLE, variant.getProductTitle());
        values.put(ProductVariantsTable.CREATED_AT, DateUtility.toString(variant.getCreatedAtDate()));
        values.put(ProductVariantsTable.UPDATED_AT, DateUtility.toString(variant.getUpdatedAtDate()));
        values.put(ProductVariantsTable.AVAILABLE, variant.isAvailable() ? 1 : 0);
        values.put(ProductVariantsTable.IMAGE_URL, variant.getImageUrl());
        return values;
    }

    static ProductVariant productVariant(Cursor cursor, List<OptionValue> optionValues) {
        long id = Long.parseLong(cursor.getString(cursor.getColumnIndex(ProductVariantsTable.ID)));
        String title = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.TITLE));
        String price = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.PRICE));
        long grams = cursor.getLong(cursor.getColumnIndex(ProductVariantsTable.GRAMS));
        String compareAtPrice = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.COMPARE_AT_PRICE));
        String sku = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.SKU));
        boolean requiresShipping = cursor.getInt(cursor.getColumnIndex(ProductVariantsTable.REQUIRES_SHIPPING)) == 1;
        boolean taxable = cursor.getInt(cursor.getColumnIndex(ProductVariantsTable.TAXABLE)) == 1;
        int position = cursor.getInt(cursor.getColumnIndex(ProductVariantsTable.POSITION));
        long productId = Long.parseLong(cursor.getString(cursor.getColumnIndex(ProductVariantsTable.PRODUCT_ID)));
        String productTitle = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.PRODUCT_TITLE));
        Date createdAtDate = DateUtility.toDate(cursor.getString(cursor.getColumnIndex(ProductVariantsTable.CREATED_AT)));
        Date updatedAtDate = DateUtility.toDate(cursor.getString(cursor.getColumnIndex(ProductVariantsTable.UPDATED_AT)));
        boolean available = cursor.getInt(cursor.getColumnIndex(ProductVariantsTable.AVAILABLE)) == 1;
        String imageUrl = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.IMAGE_URL));

        return new ModelFactory.DBProductVariant(id, title, price, optionValues, grams, compareAtPrice, sku, requiresShipping, taxable, position, productId, productTitle, createdAtDate, updatedAtDate, available, imageUrl);
    }

    static String createOptionValuesTable() {
        // TODO it might speed up searching to create an index on the PRODUCT_ID column
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_OPTION_VALUES)
                .append(" (")
                .append(OptionValuesTable.OPTION_ID).append(" TEXT, ")
                .append(OptionValuesTable.VARIANT_ID).append(" TEXT, ")
                .append(OptionValuesTable.PRODUCT_ID).append(" TEXT, ")
                .append(OptionValuesTable.NAME).append(" TEXT, ")
                .append(OptionValuesTable.VALUE).append(" TEXT, ")
                .append("PRIMARY KEY (").append(OptionValuesTable.OPTION_ID).append(", ").append(OptionValuesTable.VARIANT_ID).append(")")
                .append(")");
        return sql.toString();
    }

    static ContentValues contentValues(OptionValue optionValue, String variantId, String productId) {
        ContentValues values = new ContentValues();
        values.put(OptionValuesTable.OPTION_ID, optionValue.getOptionId());
        values.put(OptionValuesTable.VARIANT_ID, variantId);
        values.put(OptionValuesTable.PRODUCT_ID, productId);
        values.put(OptionValuesTable.NAME, optionValue.getName());
        values.put(OptionValuesTable.VALUE, optionValue.getValue());
        return values;
    }

    // This needs to return the DBOptionValue class so that we have access to the variant id
    static ModelFactory.DBOptionValue optionValue(Cursor cursor) {
        String optionId = cursor.getString(cursor.getColumnIndex(OptionValuesTable.OPTION_ID));
        String name = cursor.getString(cursor.getColumnIndex(OptionValuesTable.NAME));
        String value = cursor.getString(cursor.getColumnIndex(OptionValuesTable.VALUE));
        String variantId = cursor.getString(cursor.getColumnIndex(OptionValuesTable.VARIANT_ID));

        return new ModelFactory.DBOptionValue(optionId, name, value, variantId);
    }

}
