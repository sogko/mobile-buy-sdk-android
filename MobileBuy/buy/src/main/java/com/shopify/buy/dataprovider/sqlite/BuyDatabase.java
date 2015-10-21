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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;

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
        // TODO
        return null;
    }

    public Collection getCollection(long id) {
        // TODO
        return null;
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
        // TODO
        return null;
    }

    public Product getProduct(long id) {
        // TODO
        return null;
    }

    public void saveProducts(List<Product> products) {
        // TODO
    }

}
