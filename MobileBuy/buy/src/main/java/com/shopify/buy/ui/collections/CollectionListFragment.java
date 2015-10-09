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

package com.shopify.buy.ui.collections;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopify.buy.R;
import com.shopify.buy.model.Collection;
import com.shopify.buy.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class CollectionListFragment extends BaseFragment {
    CollectionListFragmentView view;

    private List<Collection> collections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        // Retrieve the list of collections if they were provided
        ArrayList<String> collectionsArrayList = bundle.getStringArrayList(CollectionListConfig.EXTRA_SHOP_COLLECTION);
        if (collectionsArrayList.size() > 0) {
            for (String collectionString : collectionsArrayList) {
                collections.add(Collection.fromJson(collectionString));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (CollectionListFragmentView) inflater.inflate(R.layout.fragment_collection_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
    }


    @Override
    public void onStart() {
        super.onStart();

        // TODO fetch the collections if necessary
//
//        // fetch the Shop and Product data if we don't have them already
//        if (product == null && !TextUtils.isEmpty(productId)) {
//            fetchProduct(productId);
//        }
//        if (shop == null) {
//            fetchShop();
//        }
//
//        showProductIfReady();
    }

}
