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

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Order extends ShopifyObject {

    @SerializedName("order_number")
    private String orderNumber;

    private String name;

    @SerializedName("processed_at")
    private Date processedAt;

    @SerializedName("customer_url")
    private String customerUrl;

    @SerializedName("order_status_url")
    private String orderStatusUrl;

    private String currency;

    @SerializedName("total_price")
    private String totalPrice;

    private Boolean cancelled;

    @SerializedName("cancel_reason")
    private String cancelReason;

    @SerializedName("cancelled_at")
    private Date cancelledAt;


    @SerializedName("fulfilled_line_items")
    private List<LineItem> fulfilledLineItems;


    @SerializedName("unfulfilled_line_items")
    private List<LineItem> unfulfilledLineItems;


    /**
     * No args constructor for use in serialization.
     */
    public Order() {
    }


    /**
     * @return URL for the website showing the order status.
     */
    public String getOrderStatusUrl() {
        return orderStatusUrl;
    }

    /**
     * @return The name of the Order.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Numerical identifier unique to the shop.
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @return The unique identifier of the Order within Shopify.
     */
    public String getOrderId() {
        return String.valueOf(id);
    }

    /**
     * @return The date and time when the order was processed.
     */
    public Date getProcessedAt() {
        return processedAt;
    }

    /**
     * @return A URL for the customer order status page.
     */
    public String getCustomerUrl() {
        return customerUrl;
    }

    /**
     * @return The currency of the Order.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The total price of the order.
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return true if the Order was cancelled.
     */
    public Boolean isCancelled() {
        return cancelled;
    }

    /**
     * @return The reason the Order was cancelled.
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * @return The date the Order was cancelled.
     */
    public Date getCancelledAt() {
        return cancelledAt;
    }

    /**
     * @return A list of the fulfilled Line Items in an Order
     */
    public List<LineItem> getFulfilledLineItems() {
        return fulfilledLineItems;
    }

    /**
     * @return A List of the unfulfilled Line Items in an Order
     */
    public List<LineItem> getUnfulfilledLineItems() {
        return unfulfilledLineItems;
    }
}
