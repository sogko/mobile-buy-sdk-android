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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Image;
import com.shopify.buy.model.Option;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages the SQLite database that stores the shop's collections and products.
 */
public class BuyDatabase extends SQLiteOpenHelper implements DatabaseConstants {

    /*
    Terminal commands for pulling the database off of the device and inspecting it in sqlite3:

    adb shell
    run-as com.shopify.merchantapp
    cp /data/data/com.shopify.merchantapp/databases/mobile_buy_sdk_sqlite_database /sdcard/
    exit
    exit
    adb pull /sdcard/mobile_buy_sdk_sqlite_database
    sqlite3 mobile_buy_sdk_sqlite_database
    [Ctrl-D to exit sqlite3]
     */

    public BuyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(QueryHelper.createCollectionsTable());
        db.execSQL(QueryHelper.createProductsTable());
        db.execSQL(QueryHelper.createImagesTable());
        db.execSQL(QueryHelper.createOptionsTable());
        db.execSQL(QueryHelper.createOptionValuesTable());
        db.execSQL(QueryHelper.createProductVariantsTable());
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION_VALUES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_VARIANTS);
        onCreate(db);
    }

    /**
     * @return A list of all the {@link Collection} objects in the database.
     */
    public List<Collection> getCollections() {
        List<Collection> results = new ArrayList<>();

        Cursor cursor = querySimple(TABLE_COLLECTIONS, null, null);
        if (cursor.moveToFirst()) {
            do {
                results.add(QueryHelper.collection(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return results;
    }

    /**
     * Inserts a list of {@link Collection} objects into the database.
     *
     * @param collections The collections to save.
     */
    public void saveCollections(List<Collection> collections) {
        SQLiteDatabase db = getWritableDatabase();

        // Delete old collections
        db.delete(TABLE_COLLECTIONS, null, null);

        // Save new collections
        for (Collection collection : collections) {
            db.insert(TABLE_COLLECTIONS, null, QueryHelper.contentValues(collection));
        }
    }

    /**
     * @param productIds The list of IDs for the products we want to fetch from the database.
     * @return A list of {@link Product} objects matching the specified IDs.
     */
    public List<Product> getProducts(List<String> productIds) {
        List<Product> results = new ArrayList<>();

        if (CollectionUtils.isEmpty(productIds)) {
            return results;
        }

        String where = String.format("%s IN (%s)", ProductsTable.PRODUCT_ID, TextUtils.join(",", productIds.toArray()));
        Cursor cursor = querySimple(TABLE_PRODUCTS, where, null);
        results.addAll(buildProducts(cursor, new AtomicBoolean(false)));
        cursor.close();

        return results;
    }

    /**
     * Inserts a list of {@link Product} objects into the database.
     *
     * @param products The products to save.
     */
    public void saveProducts(List<Product> products) {
        // TODO We need some way to delete products should no longer be visible to the user.

        SQLiteDatabase db = getWritableDatabase();

        // Save new products (deleting old content first)
        for (Product product : products) {
            deleteProduct(db, product);

            db.insert(TABLE_PRODUCTS, null, QueryHelper.contentValues(product));

            // need to populate each of the product sub-tables
            for (Image image : product.getImages()) {
                db.insert(TABLE_IMAGES, null, QueryHelper.contentValues(image));
            }
            for (Option option : product.getOptions()) {
                db.insert(TABLE_OPTIONS, null, QueryHelper.contentValues(option));
            }
            for (ProductVariant variant : product.getVariants()) {
                db.insert(TABLE_PRODUCT_VARIANTS, null, QueryHelper.contentValues(variant));
                for (OptionValue optionValue : variant.getOptionValues()) {
                    db.insert(TABLE_OPTION_VALUES, null, QueryHelper.contentValues(optionValue, variant.getId().toString(), product.getProductId()));
                }
            }
        }
    }

    /**
     * @param query       Term to search for.
     * @param isCancelled This AtomicBoolean will be checked periodically to see whether this search should be cancelled.
     * @return A list of {@link Product} objects that match the serch term.
     */
    public List<Product> searchProducts(final String query, final AtomicBoolean isCancelled) {
        List<Product> results = new ArrayList<>();
        if (!TextUtils.isEmpty(query)) {
            Cursor cursor = querySimple(TABLE_PRODUCTS, QueryHelper.searchProductsWhereClause(query), null);
            if (!isCancelled.get()) {
                results.addAll(buildProducts(cursor, isCancelled));
            }
            cursor.close();
        }
        return results;
    }

    /**
     * Deletes a product from the database, including all sub-tables.
     */
    private void deleteProduct(SQLiteDatabase db, Product product) {
        final String productId = product.getProductId();

        db.delete(TABLE_PRODUCTS, String.format("%s = \'%s\'", ProductsTable.PRODUCT_ID, productId), null);
        db.delete(TABLE_IMAGES, String.format("%s = \'%s\'", ImagesTable.PRODUCT_ID, productId), null);
        db.delete(TABLE_OPTIONS, String.format("%s = \'%s\'", OptionsTable.PRODUCT_ID, productId), null);
        db.delete(TABLE_OPTION_VALUES, String.format("%s = \'%s\'", OptionValuesTable.PRODUCT_ID, productId), null);
        db.delete(TABLE_PRODUCT_VARIANTS, String.format("%s = \'%s\'", ProductVariantsTable.PRODUCT_ID, productId), null);
    }

    /**
     * Helper function for building a list of products from a simple query on the products table. This will handle all
     * of the sub-table querying that is required to get the images, options, and variants associated with each product.
     *
     * @param productsCursor Contains the row data from a query on the products table.
     * @param isCancelled    This AtomicBoolean will be checked periodically to see whether this search should be cancelled.
     * @return A list of fully populated {@link Product} objects from the original cursor rows.
     */
    private List<Product> buildProducts(Cursor productsCursor, final AtomicBoolean isCancelled) {
        // If the cursor from the Products table doesn't have any rows, return an empty list
        if (!productsCursor.moveToFirst()) {
            return Collections.EMPTY_LIST;
        }

        // Extract a list of the Product IDs that we can use to query the image, option, variant, and option value tables
        List<String> productIds = new ArrayList<>();
        do {
            productIds.add(productsCursor.getString(productsCursor.getColumnIndex(ProductsTable.PRODUCT_ID)));
        } while (productsCursor.moveToNext());

        if (isCancelled.get()) return Collections.EMPTY_LIST;

        // Map each Product ID to the list of Images for that Product
        Map<String, List<Image>> imagesByProduct = new CursorAdapter<Image>().buildMap(TABLE_IMAGES, ImagesTable.PRODUCT_ID, productIds, ImagesTable.POSITION, getReadableDatabase(), new CursorAdapter.ObjectBuilder<Image>() {
            @Override
            public Image build(Cursor cursor) {
                return QueryHelper.image(cursor);
            }
        });

        if (isCancelled.get()) return Collections.EMPTY_LIST;

        // Map each Product ID to the list of Options for that Product
        Map<String, List<Option>> optionsByProduct = new CursorAdapter<Option>().buildMap(TABLE_OPTIONS, OptionsTable.PRODUCT_ID, productIds, OptionsTable.POSITION, getReadableDatabase(), new CursorAdapter.ObjectBuilder<Option>() {
            @Override
            public Option build(Cursor cursor) {
                return QueryHelper.option(cursor);
            }
        });

        if (isCancelled.get()) return Collections.EMPTY_LIST;

        // Map each Product ID to the list of OptionsValues for that Product (OptionValues are actually associated with ProductVariants, but we'll get to that)
        Map<String, List<ModelFactory.DBOptionValue>> optionValuesByProduct = new CursorAdapter<ModelFactory.DBOptionValue>().buildMap(TABLE_OPTION_VALUES, OptionValuesTable.PRODUCT_ID, productIds, null, getReadableDatabase(), new CursorAdapter.ObjectBuilder<ModelFactory.DBOptionValue>() {
            @Override
            public ModelFactory.DBOptionValue build(Cursor cursor) {
                return QueryHelper.optionValue(cursor);
            }
        });

        if (isCancelled.get()) return Collections.EMPTY_LIST;

        // Map each Product ID to a nested map, which itself maps each ProductVariant ID to the list of OptionValues for that ProductVariant
        final Map<String, Map<String, List<ModelFactory.DBOptionValue>>> optionValuesByProductByVariant = new HashMap<>();
        for (String productId : optionsByProduct.keySet()) {

            // Get the list of all OptionValues the current Product ID (includes all OptionValues for all ProductVariants for this Product)
            List<ModelFactory.DBOptionValue> optionValues = optionValuesByProduct.get(productId);

            // If our outer map doesn't contain this Product ID as a key yet, add it
            if (!optionValuesByProductByVariant.containsKey(productId)) {
                optionValuesByProductByVariant.put(productId, new HashMap<String, List<ModelFactory.DBOptionValue>>());
            }

            // Map each ProductVariant ID to the list of OptionValues for that ProductVariant (this is building our nested map)
            for (ModelFactory.DBOptionValue optionValue : optionValues) {
                String variantId = optionValue.getVariantId();

                // If our nested map doesn't contain this ProductVariant ID as a key yet, add it
                List<ModelFactory.DBOptionValue> optionValuesForVariant = optionValuesByProductByVariant.get(productId).get(variantId);
                if (optionValuesForVariant == null) {
                    optionValuesForVariant = new ArrayList<>();
                }
                optionValuesForVariant.add(optionValue);

                // Update the OptionValue list in the nested map to include the current OptionValue
                optionValuesByProductByVariant.get(productId).put(variantId, optionValuesForVariant);
            }
        }

        if (isCancelled.get()) return Collections.EMPTY_LIST;

        // Map each Product ID to the list of ProductVariants for that Product
        Map<String, List<ProductVariant>> variantsByProduct = new CursorAdapter<ProductVariant>().buildMap(TABLE_PRODUCT_VARIANTS, ProductVariantsTable.PRODUCT_ID, productIds, null, getReadableDatabase(), new CursorAdapter.ObjectBuilder<ProductVariant>() {
            @Override
            public ProductVariant build(Cursor cursor) {
                // The QueryHelper needs a list of OptionValues to build a ProductVariant, so we extract it out of the nested map we built earlier
                String productId = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.PRODUCT_ID));
                String variantId = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.ID));
                List<OptionValue> optionValues = new ArrayList<>();
                // The first get(productId) returns a map of ProductVariant IDs to OptionValue lists, and then get(variantId) returns the list of OptionValues
                optionValues.addAll(optionValuesByProductByVariant.get(productId).get(variantId));
                return QueryHelper.productVariant(cursor, optionValues);
            }
        });

        if (isCancelled.get()) return Collections.EMPTY_LIST;

        // Finally, use the original cursor from the Products table along with all of our maps to create fully populated Product objects
        List<Product> products = new ArrayList<>();
        productsCursor.moveToFirst();
        do {
            String productId = productsCursor.getString(productsCursor.getColumnIndex(ProductsTable.PRODUCT_ID));
            products.add(QueryHelper.product(productsCursor, imagesByProduct.get(productId), variantsByProduct.get(productId), optionsByProduct.get(productId)));
        } while (productsCursor.moveToNext());

        return products;
    }

    private Cursor querySimple(String table, String where, String orderBy) {
        return getReadableDatabase().query(table, null, where, null, null, null, orderBy, null);
    }

}
