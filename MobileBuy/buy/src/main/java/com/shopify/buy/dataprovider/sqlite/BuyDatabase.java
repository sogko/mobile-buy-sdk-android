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
import android.util.Log;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Image;
import com.shopify.buy.model.Option;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BuyDatabase extends SQLiteOpenHelper implements DatabaseConstants {

    /*
    adb shell
    run-as com.shopify.drawerapp
    cp /data/data/com.shopify.drawerapp/databases/mobile_buy_sdk_sqlite_database /sdcard/
    exit
    exit
    adb pull /sdcard/mobile_buy_sdk_sqlite_database
    sqlite3 mobile_buy_sdk_sqlite_database
    [Ctrl-D to exit sqlite3]
     */

    private static final String LOG_TAG = BuyDatabase.class.getSimpleName();

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

    public List<Collection> getCollections() {
        List<Collection> results = new ArrayList<>();
        Cursor cursor = querySimple(TABLE_COLLECTIONS, null, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    results.add(QueryHelper.collection(cursor));
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Could not query Collection from database", e);
                }
            } while (cursor.moveToNext());
        }
        return results;
    }

    public Collection getCollection(long id) {
        Collection collection = null;
        Cursor cursor = querySimple(TABLE_COLLECTIONS, CollectionsTable.COLLECTION_ID + " = " + id, null);
        if (cursor.moveToFirst()) {
            try {
                collection = QueryHelper.collection(cursor);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Could not get Collection from database", e);
            }
        }
        return collection;
    }

    public void saveCollections(List<Collection> collections) {
        SQLiteDatabase db = getWritableDatabase();

        // Delete old collections
        db.delete(TABLE_COLLECTIONS, null, null);

        // Save new collections
        for (Collection collection : collections) {
            db.insert(TABLE_COLLECTIONS, null, QueryHelper.contentValues(collection));
        }
    }

    public List<Product> getProducts() {
        List<Product> results = new ArrayList<>();
        Cursor cursor = querySimple(TABLE_PRODUCTS, null, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    results.add(buildProduct(cursor));
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Could not get Product from database", e);
                }
            } while (cursor.moveToNext());
        }
        return results;
    }

    public Product getProduct(long id) {
        Product product = null;
        Cursor cursor = querySimple(TABLE_PRODUCTS, ProductsTable.PRODUCT_ID + " = " + id, null);
        if (cursor.moveToFirst()) {
            try {
                product = buildProduct(cursor);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Could not get Product from database", e);
            }
        }
        return product;
    }

    public void saveProducts(List<Product> products) {
        /*
         TODO
         This logic assumes that the entire product list is passed in every time.
         To do this properly, we need to selectively insert or update these products,
         and we need some way to delete products should no longer be visible to the user.
          */

        SQLiteDatabase db = getWritableDatabase();

        // Delete old products
        db.delete(TABLE_PRODUCTS, null, null);
        db.delete(TABLE_IMAGES, null, null);
        db.delete(TABLE_OPTIONS, null, null);
        db.delete(TABLE_PRODUCT_VARIANTS, null, null);
        db.delete(TABLE_OPTION_VALUES, null, null);

        // Save new products
        for (Product product : products) {
            db.insert(TABLE_PRODUCTS, null, QueryHelper.contentValues(product));
            for (Image image : product.getImages()) {
                db.insert(TABLE_IMAGES, null, QueryHelper.contentValues(image));
            }
            for (Option option : product.getOptions()) {
                db.insert(TABLE_OPTIONS, null, QueryHelper.contentValues(option));
            }
            for (ProductVariant variant : product.getVariants()) {
                db.insert(TABLE_PRODUCT_VARIANTS, null, QueryHelper.contentValues(variant));
                for (OptionValue optionValue : variant.getOptionValues()) {
                    db.insert(TABLE_OPTION_VALUES, null, QueryHelper.contentValues(optionValue, variant.getId().toString()));
                }
            }
        }
    }

    public List<Product> searchProducts(String query) {
        List<Product> results = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        // TODO

        return results;
    }

    private Product buildProduct(Cursor cursor) throws ParseException {
        String productId = cursor.getString(cursor.getColumnIndex(ProductsTable.PRODUCT_ID));
        List<Image> images = getProductImages(productId);
        List<ProductVariant> variants = getProductVariants(productId);
        List<Option> options = getProductOptions(productId);
        return QueryHelper.product(cursor, images, variants, options);
    }

    private List<Image> getProductImages(String productId) {
        List<Image> results = new ArrayList<>();
        Cursor cursor = querySimple(TABLE_IMAGES, ImagesTable.PRODUCT_ID + " = " + productId, ImagesTable.POSITION + " ASC");
        if (cursor.moveToFirst()) {
            do {
                results.add(QueryHelper.image(cursor));
            } while (cursor.moveToNext());
        }
        return results;
    }

    private List<ProductVariant> getProductVariants(String productId) {
        List<ProductVariant> results = new ArrayList<>();
        Cursor cursor = querySimple(TABLE_PRODUCT_VARIANTS, ProductVariantsTable.PRODUCT_ID + " = " + productId, ProductVariantsTable.POSITION + " ASC");
        if (cursor.moveToFirst()) {
            do {
                try {
                    String variantId = cursor.getString(cursor.getColumnIndex(ProductVariantsTable.ID));
                    List<OptionValue> optionValues = getVariantOptionValues(variantId);
                    results.add(QueryHelper.productVariant(cursor, optionValues));
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Could not query ProductVariant from database", e);
                }
            } while (cursor.moveToNext());
        }
        return results;
    }

    private List<OptionValue> getVariantOptionValues(String variantId) {
        List<OptionValue> results = new ArrayList<>();
        Cursor cursor = querySimple(TABLE_OPTION_VALUES, OptionValuesTable.VARIANT_ID + " = " + variantId, null);
        if (cursor.moveToFirst()) {
            do {
                results.add(QueryHelper.optionValue(cursor));
            } while (cursor.moveToNext());
        }
        return results;
    }

    private List<Option> getProductOptions(String productId) {
        List<Option> results = new ArrayList<>();
        Cursor cursor = querySimple(TABLE_OPTIONS, OptionsTable.PRODUCT_ID + " = " + productId, OptionsTable.POSITION + " ASC");
        if (cursor.moveToFirst()) {
            do {
                results.add(QueryHelper.option(cursor));
            } while (cursor.moveToNext());
        }
        return results;
    }

    private Cursor querySimple(String table, String where, String orderBy) {
        return getReadableDatabase().query(table, null, where, null, null, null, orderBy, null);
    }

}
