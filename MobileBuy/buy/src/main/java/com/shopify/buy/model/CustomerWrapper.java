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

package com.shopify.buy.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import com.shopify.buy.dataprovider.BuyClientFactory;

import java.lang.reflect.Type;

public class CustomerWrapper {

    private Customer customer;

    private String password;

    @SerializedName("access_token")
    private String token;

    CustomerWrapper() {}

    public CustomerWrapper(Customer customer) {
        this.customer = customer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The up to date customer.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @return The access token to use for subsequent customer api calls.
     */
    public String getToken() {
        return token;
    }

    /**
     * Custom serializer that add the password to the outgoing Customer json.
     */
    public static class CustomerWrapperSerializer implements JsonSerializer<CustomerWrapper> {

        @Override
        public JsonElement serialize(final CustomerWrapper customerWrapper, final Type typeOfSrc, final JsonSerializationContext context) {
            Gson gson = BuyClientFactory.createDefaultGson(Order.class);

            Customer customer = customerWrapper.getCustomer();
            String password = customerWrapper.getPassword();

            JsonObject customerJsonObject = gson.toJsonTree(customer).getAsJsonObject();
            customerJsonObject.addProperty("password", password);
            customerJsonObject.addProperty("password_confirmation", password);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("customer", customerJsonObject);

            return jsonObject;
        }
    }

}
