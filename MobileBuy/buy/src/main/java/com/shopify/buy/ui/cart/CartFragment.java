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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shopify.buy.R;
import com.shopify.buy.dataprovider.CartManager;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.LineItem;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchShopIfNecessary(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                currencyFormat = CurrencyFormatter.getFormatter(Locale.getDefault(), shop.getCurrency());
                showCartIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (CartFragmentView) inflater.inflate(R.layout.cart_fragment, container, false);
        view.setTheme(theme);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showCartIfReady();
    }

    private void showCartIfReady() {
        Activity activity = safelyGetActivity();
        if (activity == null) {
            return;
        }

        if (currencyFormat == null) {
            return;
        }

        final Cart cart = CartManager.getInstance().getCart();

        view.updateSubtotal(cart, currencyFormat);

        final ArrayAdapter<CartLineItem> adapter = new ArrayAdapter<CartLineItem>(getActivity(), R.layout.cart_line_item_view, cart.getLineItems()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.cart_line_item_view, null);
                }

                ((CartLineItemView) view).init(getItem(position), currencyFormat, theme, CartFragment.this);

                return view;
            }
        };

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ListView) getView().findViewById(R.id.cart_list_view)).setAdapter(adapter);
            }
        });
    }

    @Override
    protected Cart getCartForCheckout() {
        return CartManager.getInstance().getCart();
    }

    @Override
    public void onQuantityChanged(LineItem lineItem) {
        view.updateSubtotal(CartManager.getInstance().getCart(), currencyFormat);

        CartManager.getInstance().saveCart(getActivity());
    }

}
