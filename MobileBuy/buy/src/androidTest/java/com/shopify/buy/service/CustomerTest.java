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

package com.shopify.buy.service;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerWrapper;
import com.shopify.buy.model.Order;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomerTest extends ShopifyAndroidTestCase {

    private static final boolean ENABLED = false;

    private Customer customer;
    private String token;

    public void testCustomerCreation() throws InterruptedException {
        if (!ENABLED) {
            return;
        }

        final CountDownLatch latch = new CountDownLatch(1);
        final Customer customer = getCustomer();

        buyClient.createCustomer(customer, "password", new Callback<CustomerWrapper>() {
            @Override
            public void success(CustomerWrapper customerWrapper, Response response) {
                assertNotNull(customerWrapper);
                assertNotNull(customerWrapper.getCustomer());
                assertEquals(false, customerWrapper.getToken().isEmpty());
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    public void testCustomerLogin() throws InterruptedException {
        if (!ENABLED) {
            return;
        }

        customer = getCustomer();

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.loginCustomer(customer.getEmail(), "password", new Callback<CustomerWrapper>() {
            @Override
            public void success(CustomerWrapper customerWrapper, Response response) {
                assertNotNull(customerWrapper);
                assertNotNull(customerWrapper.getCustomer());
                assertEquals(false, customerWrapper.getToken().isEmpty());

                CustomerTest.this.customer = customerWrapper.getCustomer();
                CustomerTest.this.token = customerWrapper.getToken();

                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    public void testCustomerUpdate() throws InterruptedException {
        if (!ENABLED) {
            return;
        }

        testCustomerLogin();

        customer.setLastName("Foo");

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.updateCustomer(token, customer, new Callback<Customer>() {
            @Override
            public void success(Customer customer, Response response) {
                assertNotNull(customer);
                assertEquals("Foo", customer.getLastName());
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    public void testGetCustomerOrders() throws InterruptedException {
        if (!ENABLED) {
            return;
        }

        testCustomerLogin();

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getOrders(token, new Callback<List<Order>>() {
            @Override
            public void success(List<Order> orders, Response response) {
                assertNotNull(orders);
                assertEquals(true, orders.size() > 0);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }

        });
        latch.await();
    }

    public void testGetCustomer() throws InterruptedException {
        if (!ENABLED) {
            return;
        }

        testCustomerLogin();

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getCustomer(token, new Callback<Customer>() {
            @Override
            public void success(Customer customer, Response response) {
                assertNotNull(customer);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setEmail("krisorr2@gmail.com");
        customer.setFirstName("Kristopher");
        customer.setLastName("Orr");

        return customer;
    }
}
