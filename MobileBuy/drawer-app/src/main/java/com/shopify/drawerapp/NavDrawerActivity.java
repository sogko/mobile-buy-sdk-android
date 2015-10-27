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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.ui.ProductDetailsBuilder;
import com.shopify.buy.ui.ShopifyTheme;
import com.shopify.buy.ui.cart.CartBuilder;
import com.shopify.buy.ui.cart.CartFragment;
import com.shopify.buy.ui.collections.CollectionListBuilder;
import com.shopify.buy.ui.collections.CollectionListFragment;
import com.shopify.buy.ui.products.ProductListBuilder;
import com.shopify.buy.ui.products.ProductListFragment;

/**
 * Base class for all activities in the app. Manages the ProgressDialog that is displayed while network activity is occurring.
 */
public class NavDrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView navDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nav_drawer_activity);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // TODO pull the color from the theme
        drawerLayout.setBackgroundColor(getResources().getColor(R.color.light_background));

        navDrawer = (ListView) findViewById(R.id.nav_drawer);
        navDrawer.setAdapter(new ArrayAdapter<>(this, R.layout.nav_drawer_list_item, getResources().getStringArray(R.array.nav_drawer_items)));
        navDrawer.setOnItemClickListener(new DrawerItemClickListener());

        toolbar = (Toolbar) findViewById(R.id.drawer_toolbar);
        // TODO this should be using the appbar colors, text colors from the theme
        toolbar.setBackgroundColor(getResources().getColor(R.color.light_low_contrast_grey));

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_closed) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //  getActionBar().setTitle("drawer is closed");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //  getActionBar().setTitle("drawer is open");
            }
        };

        // Enable the default drawer icon
        drawerToggle.setDrawerIndicatorEnabled(true);

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);


        // TODO loadFragment(fragment) with the first fragment
        // TODO this is temporarily loading the collection list
        CollectionListFragment fragment = new CollectionListBuilder(this)
                .setApiKey(getString(R.string.shopify_api_key))
                .setChannelid(getString(R.string.channel_id))
                .setShopDomain(getString(R.string.shop_url))
                .setApplicationName(getString(R.string.app_name))
                .buildFragment(null, new CollectionListListener());
        loadFragment(fragment);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // TODO handle any other actionbar items here
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            // TODO loadFragment(fragment) with new Fragment based on position param

            navDrawer.setItemChecked(position, true);
            drawerLayout.closeDrawer(navDrawer);

            switch (position) {
                case 0: {
                    CollectionListFragment fragment = new CollectionListBuilder(NavDrawerActivity.this)
                            .setApiKey(getString(R.string.shopify_api_key))
                            .setChannelid(getString(R.string.channel_id))
                            .setShopDomain(getString(R.string.shop_url))
                            .setApplicationName(getString(R.string.app_name))
                            .buildFragment(null, new CollectionListListener());
                    loadFragment(fragment);
                    toolbar.setTitle(getString(R.string.collection_list_fragment_title));
                    break;
                }
                case 2: {
                    CartFragment fragment = new CartBuilder(NavDrawerActivity.this)
                            .setApiKey(getString(R.string.shopify_api_key))
                            .setChannelid(getString(R.string.channel_id))
                            .setShopDomain(getString(R.string.shop_url))
                            .setApplicationName(getString(R.string.app_name))
                                    // TODO checkout listener
                            .buildFragment(null);
                    loadFragment(fragment);
                    break;
                }
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


    private class CollectionListListener implements CollectionListFragment.Listener {

        @Override
        public void onItemClick(Collection collection) {
            ProductListFragment fragment = new ProductListBuilder(NavDrawerActivity.this)
                    .setApiKey(getString(R.string.shopify_api_key))
                    .setChannelid(getString(R.string.channel_id))
                    .setShopDomain(getString(R.string.shop_url))
                    .setApplicationName(getString(R.string.app_name))
                    .setCollection(collection)
                    .buildFragment(null, new ProductListListener());
            loadFragment(fragment);

            toolbar.setTitle(collection.getTitle());

        }

        @Override
        public void onItemLongClick(Collection collection) {
            // do nothing
        }

    }

    private class ProductListListener implements ProductListFragment.Listener {
        // TODO get the shop somewhere central and pass in here

        // TODO make the theme in a central place
        ShopifyTheme theme = new ShopifyTheme(getResources());

        @Override
        public void onItemClick(Product product) {
            Intent intent = new ProductDetailsBuilder(NavDrawerActivity.this)
                    .setApiKey(getString(R.string.shopify_api_key))
                    .setChannelid(getString(R.string.channel_id))
                    .setShopDomain(getString(R.string.shop_url))
                    .setApplicationName(getString(R.string.app_name))
                    .setProduct(product)
                    .setTheme(theme)
                            // TODO determine which url/scheme we want to return to
//                    .setWebReturnToUrl(getString(R.string.web_return_to_url))
//                    .setWebReturnToLabel(getString(R.string.web_return_to_label))
                    .build();

            // TODO handle the result
            NavDrawerActivity.this.startActivityForResult(intent, 1);
        }

        @Override
        public void onItemLongClick(Product product) {
            // do nothing
        }

    }

}
