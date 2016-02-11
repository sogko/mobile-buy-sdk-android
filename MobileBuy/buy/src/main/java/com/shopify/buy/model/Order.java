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

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;

public class Order extends ShopifyObject {

    private String email;

    @SerializedName("status_url")
    private String statusUrl;

    @SerializedName("closed_at")
    private Date closedAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    private Long number;

    private String note;

    private String token;

    @SerializedName("total_price")
    private String totalPrice;

    @SerializedName("subtotal_price")
    private String subtotalPrice;

    @SerializedName("total_discount")
    private String totalDiscounts;

    @SerializedName("total_weight")
    private Long totalWeight;

    @SerializedName("total_tax")
    private String totalTax;

    @SerializedName("taxes_included")
    private Boolean taxesIncluded;

    private String currency;

    @SerializedName("financial_status")
    private String financialStatus;

    @SerializedName("total_line_items_price")
    private String totalLineItemsPrice;

    @SerializedName("cart_token")
    private String cartToken;

    @SerializedName("buyer_accepts_marketing")
    private Boolean buyerAcceptsMarketing;

    private String name;

    @SerializedName("referring_site")
    private String referringSite;

    @SerializedName("landing_site")
    private String landingSite;

    @SerializedName("cancelled_at")
    private Date cancelledAt;

    @SerializedName("cancel_reason")
    private String cancelReason;

    @SerializedName("checkout_token")
    private String checkoutToken;

    @SerializedName("processed_at")
    private Date processedAt;

    @SerializedName("browser_ip")
    private String browserIp;

    @SerializedName("order_number")
    private Long orderNumber;

    @SerializedName("discount_codes")
    private List<String> discountCodes = new ArrayList<>();

    @SerializedName("payment_gateway_names")
    private List<String> paymentGatewayNames = new ArrayList<String>();

    @SerializedName("processing_method")
    private String processingMethod;

    @SerializedName("checkout_id")
    private Long checkoutId;

    @SerializedName("source_name")
    private String sourceName;

    @SerializedName("fulfillment_status")
    private String fulfillmentStatus;

    @SerializedName("tax_lines")
    private List<TaxLine> taxLines = new ArrayList<TaxLine>();

    private String tags;
    private Set<String> tagSet;

    @SerializedName("contact_email")
    private String contactEmail;

    @SerializedName("line_items")
    private List<LineItem> lineItems = new ArrayList<LineItem>();

    @SerializedName("shipping_lines")
    private List<ShippingLine> shippingLines = new ArrayList<ShippingLine>();

    @SerializedName("billing_address")
    private Address billingAddress;

    @SerializedName("shipping_address")
    private Address shippingAddress;

    private Customer customer;

    /**
     * No args constructor for use in serialization
     */
    public Order() {
    }

    /**
     * @return The unique identifier of this object within the Shopify platform.
     */
    public String getOrderId() {
        return String.valueOf(id);
    }

    /**
     * @return The customer's email address. Is required when a billing address is present.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return The date and time when the order was closed. {@code null} if the order is not closed.
     */
    public Date getClosedAt() {
        return closedAt;
    }

    /**
     * @return The date and time when the order was created.
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @return The date and time when the order was last modified.
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @return Numerical identifier unique to the shop.
     */
    public Long getNumber() {
        return number;
    }

    /**
     * @return The text of an optional note that a shop owner can attach to the order.
     */
    public String getNote() {
        return note;
    }

    /**
     * @return Unique identifier for a particular order.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return The sum of all the prices of all the items in the order, taxes and discounts included (must be positive).
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return Price of the order before shipping and taxes.
     */
    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    /**
     * @return The sum of all the weights of the line items in the order, in grams.
     */
    public Long getTotalWeight() {
        return totalWeight;
    }

    /**
     * @return The sum of all the taxes applied to the order (must be positive).
     */
    public String getTotalTax() {
        return totalTax;
    }

    /**
     * @return States whether or not taxes are included in the order subtotal.
     */
    public Boolean getTaxesIncluded() {
        return taxesIncluded;
    }

    /**
     * @return The three letter code (ISO 4217) for the currency used for the payment.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The financialStatus of the order.
     */
    public String getFinancialStatus() {
        return financialStatus;
    }

    /**
     * @return The total amount of the discounts to be applied to the price of the order.
     */
    public String getTotalDiscounts() {
        return totalDiscounts;
    }

    /**
     * @return The sum of all the prices of all the items in the order.
     */
    public String getTotalLineItemsPrice() {
        return totalLineItemsPrice;
    }

    /**
     * @return Unique identifier for a particular {@link Cart} that is attached to this order.
     */
    public String getCartToken() {
        return cartToken;
    }

    /**
     * @return Indicates whether or not the person who placed the order would like to receive email updates from the shop.
     */
    public Boolean getBuyerAcceptsMarketing() {
        return buyerAcceptsMarketing;
    }

    /**
     * @return The customer's order name as represented by a number.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The website that the customer clicked on to come to the shop.
     */
    public String getReferringSite() {
        return referringSite;
    }

    /**
     * @return The URL for the page where the buyer landed when entering the shop.
     */
    public String getLandingSite() {
        return landingSite;
    }

    /**
     * @return The date and time when the order was cancelled.
     */
    public Date getCancelledAt() {
        return cancelledAt;
    }

    /**
     * @return The reason why the order was cancelled. If the order was not cancelled, this value is null.
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * @return The checkoutToken associated with this order.
     */
    public String getCheckoutToken() {
        return checkoutToken;
    }

    /**
     * @return The date and time when the order was imported.
     */
    public Date getProcessedAt() {
        return processedAt;
    }

    /**
     * @return The IP address of the browser used by the customer when placing the order.
     */
    public String getBrowserIp() {
        return browserIp;
    }

    /**
     * @return A unique numeric identifier for the order.
     */
    public Long getOrderNumber() {
        return orderNumber;
    }

    /**
     * @return The list of all payment gateways used for the order.
     */
    public List<String> getPaymentGatewayNames() {
        return paymentGatewayNames;
    }

    /**
     * @return States the type of payment processing method. Valid values are: checkout, direct, manual, offsite or express.
     */
    public String getProcessingMethod() {
        return processingMethod;
    }

    /**
     * @return The checkoutId associated with this order.
     */
    public Long getCheckoutId() {
        return checkoutId;
    }

    /**
     * @return Where the order originated.
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * @return The fulfillment status of the order. Will be one of fulfilled, partial, or null.
     */
    public String getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    /**
     * @return An list of tax_line objects, each of which details the total taxes applicable to the order.
     */
    public List<TaxLine> getTaxLines() {
        return taxLines;
    }

    /**
     * @return The tags associated with this order.
     */
    public Set<String> getTags() {
        return tagSet;
    }

    /**
     * @return The contact email for the merchant.
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @return A list of {@link LineItem}, each one containing information about an item in the order.
     */
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    /**
     * @return A list of {@link ShippingLine} objects, each of which details the shipping methods used.
     */
    public List<ShippingLine> getShippingLines() {
        return shippingLines;
    }

    /**
     * @return The mailing address associated with the payment method. This address is an optional field that will not be available on orders that do not require one.
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * @return The mailing address to where the order will be shipped. This address is optional and will not be available on orders that do not require one.
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /**
     * @return URL for the website showing the order status. This is only available for orders that are part of a complete {@link Checkout} returned using {@link BuyClient#getCheckout(String, Callback)}
     */
    public String getStatusUrl() {
        return statusUrl;
    }


    public static class OrderDeserializer implements JsonDeserializer<Order> {
        @Override
        public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return fromJson(json.toString());
        }
    }

    /**
     * An Order object created using the values in the JSON string.
     */
    public static Order fromJson(String json) {
        Gson gson = BuyClientFactory.createDefaultGson(Order.class);
        Order order = gson.fromJson(json, Order.class);

        // Create the tagSet.
        order.tagSet = new HashSet<>();

        // Populate the tagSet from the comma separated list.
        if (!TextUtils.isEmpty(order.tags)) {
            for (String tag : order.tags.split(",")) {
                String myTag = tag.trim();
                order.tagSet.add(myTag);
            }
        }

        return order;
    }
}
