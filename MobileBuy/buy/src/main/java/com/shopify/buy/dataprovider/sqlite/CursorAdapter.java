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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CursorAdapter<T> {

    interface ObjectBuilder<T> {
        T build(Cursor cursor);
    }

    // TODO need serious documentation here, shouldn't merge without it
    public Map<String, List<T>> buildMap(String table, String lookupColumn, List<String> lookupValues, String sortColumn, SQLiteDatabase db, ObjectBuilder<T> builder) {
        Map<String, List<T>> results = new HashMap<>();

        String where = String.format("%s IN (%s)", lookupColumn, TextUtils.join(",", lookupValues.toArray()));
        String orderBy = sortColumn != null ? String.format("%s ASC", sortColumn) : null;
        Cursor cursor = db.query(table, null, where, null, null, null, orderBy, null);

        if (cursor.moveToFirst()) {
            do {
                T item = builder.build(cursor);
                String key = cursor.getString(cursor.getColumnIndex(lookupColumn));
                List<T> items = results.get(key);
                if (items == null) {
                    items = new ArrayList<>();
                }
                items.add(item);
                results.put(key, items);
            } while (cursor.moveToNext());
        }

        return results;
    }

}
