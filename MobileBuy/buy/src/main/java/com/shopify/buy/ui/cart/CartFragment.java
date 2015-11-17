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

package com.shopify.buy.ui.cart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shopify.buy.R;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.common.CheckoutFragment;
import com.shopify.buy.utils.CurrencyFormatter;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CartFragment extends CheckoutFragment implements QuantityPicker.OnQuantityChangedListener {

    protected NumberFormat currencyFormat;
    protected CartFragmentView view;
    protected ArrayAdapter<CartLineItem> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (CartFragmentView) inflater.inflate(R.layout.cart_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view.setTheme(theme);

        showViewIfReady();
    }

    @Override
    protected void fetchDataIfNecessary() {

        if (shop == null) {
            provider.getShop(buyClient, new Callback<Shop>() {
                @Override
                public void success(Shop shop, Response response) {
                    CartFragment.this.shop = shop;
                    currencyFormat = CurrencyFormatter.getFormatter(Locale.getDefault(), shop.getCurrency());
                    showViewIfReady();
                }

                @Override
                public void failure(RetrofitError error) {
                    // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/589
                }
            });
        }

        if (cart == null) {
            provider.getCart(buyClient, userId, new Callback<Cart>() {
                        @Override
                        public void success(Cart cart, Response response) {
                            CartFragment.this.cart = cart;
                            showViewIfReady();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/589
                        }
                    }
            );
        }
    }

    @Override
    protected void showViewIfReady() {
        Activity activity = safelyGetActivity();
        if (activity == null || currencyFormat == null || cart == null || !viewCreated) {
            return;
        }

        view.updateSubtotal(cart, currencyFormat);

        adapter = new ArrayAdapter<CartLineItem>(getActivity(), R.layout.cart_line_item_view, cart.getLineItems()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.cart_line_item_view, null);
                    ((CartLineItemView) view).applyTheme(theme);
                }

                ((CartLineItemView) view).setLineItem(getItem(position), currencyFormat, CartFragment.this);

                return view;
            }
        };

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = ((ListView) getView().findViewById(R.id.cart_list_view));
                listView.setAdapter(adapter);
                registerForContextMenu(listView);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.cart_item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_cart_item) {
            // Delete the line item from the cart
            int lineItemIndex = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
            CartLineItem lineItem = cart.getLineItems().get(lineItemIndex);
            adjustQuantity(lineItem, -(int) lineItem.getQuantity());
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onQuantityDecreased(CartLineItem lineItem) {
        adjustQuantity(lineItem, -1);
    }

    @Override
    public void onQuantityIncreased(CartLineItem lineItem) {
        adjustQuantity(lineItem, +1);
    }

    private void adjustQuantity(CartLineItem lineItem, int delta) {
        cart.setVariantQuantity(lineItem.getVariant(), (int) lineItem.getQuantity() + delta);
        provider.saveCart(cart, buyClient, userId);

        adapter.notifyDataSetChanged();
        view.updateSubtotal(cart, currencyFormat);
    }

}
