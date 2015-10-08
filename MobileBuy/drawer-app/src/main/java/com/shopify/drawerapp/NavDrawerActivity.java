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

package com.shopify.drawerapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shopify.buy.model.Cart;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.ui.cart.CartFragment;

/**
 * Base class for all activities in the app. Manages the ProgressDialog that is displayed while network activity is occurring.
 */
public class NavDrawerActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nav_drawer_activity);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navDrawer = (ListView) findViewById(R.id.nav_drawer);
        navDrawer.setAdapter(new ArrayAdapter<>(this, R.layout.nav_drawer_list_item, getResources().getStringArray(R.array.nav_drawer_items)));
        navDrawer.setOnItemClickListener(new DrawerItemClickListener());

        // TODO loadFragment(fragment) with the first fragment
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            // TODO loadFragment(fragment) with new Fragment based on position param

            navDrawer.setItemChecked(position, true);
            drawerLayout.closeDrawer(navDrawer);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

}
