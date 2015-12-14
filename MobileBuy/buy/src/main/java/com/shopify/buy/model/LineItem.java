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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Line Item, containing a {@link ProductVariant} and an associated quantity
 */
public class LineItem {

    protected long quantity;

    protected String id;

    protected String price;

    @SerializedName("requires_shipping")
    protected boolean requiresShipping;

    @SerializedName("variant_id")
    protected Long variantId;

    protected String title;

    @SerializedName("product_id")
    protected String productId;

    @SerializedName("variant_title")
    protected String variantTitle;

    @SerializedName("line_price")
    protected String linePrice;

    @SerializedName("compare_at_price")
    protected String compareAtPrice;

    protected String sku;

    protected boolean taxable;

    protected long grams;

    @SerializedName("fulfillment_service")
    protected String fulfillmentService;

    @SerializedName("fulfillment_status")
    protected String fulfillmentStatus;

    @SerializedName("fulfillable_quantity")
    protected Long fulfillableQuantity;

    protected Map<String, String> properties;

    protected String vendor;

    @SerializedName("gift_card")
    protected boolean giftCard;

    protected String name;

    @SerializedName("total_discount")
    protected String totalDiscount;

    @SerializedName("tax_lines")
    protected List<TaxLine> taxLines;

    protected LineItem() {
    }

    public LineItem(ProductVariant variant) {
        variantId = variant.getId();
        price = variant.getPrice();
        title = variant.getTitle();
        requiresShipping = variant.isRequiresShipping();
        quantity = 1;
    }

    /**
     * @return The title for the {@link ProductVariant} on this line item.
     */
    public String getVariantTitle() {
        return variantTitle;
    }

    /**
     * @return The name for the {@link ProductVariant} on this line item.
     */
    public String getName() { return name; }

    /**
     * @return The line price of the item (price * quantity).
     */
    public String getLinePrice() {
        return linePrice;
    }

    /**
     * @return The competitor's price for the same item. You need to set this value on the {@link Product} in your shop admin portal.
     */
    public String getCompareAtPrice() {
        return compareAtPrice;
    }

    /**
     * @return The unique SKU for the line item.
     */
    public String getSku() {
        return sku;
    }

    /**
     * @return {@code true} if this line item is taxable, {@code false} otherwise.
     */
    public boolean isTaxable() {
        return taxable;
    }

    /**
     * @return The id of the {@link Product} in this line item.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return The weight of the {@link ProductVariant} in this line item (in grams).
     */
    public long getGrams() {
        return grams;
    }

    /**
     * @return Name of the service provider that is doing the fulfillment.
     */
    public String getFulfillmentService() {
        return fulfillmentService;
    }

    /**
     *
     * @return The amount available to fulfill.
     */
    public Long getFulfillableQuantity() { return fulfillableQuantity; }


    /**
     * @return Custom properties set on this line item.
     */
    public Map<String, String> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    /**
     * @return The quantity of the {@link ProductVariant} being purchased in this line item.
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * @return The price of the line item. This price does not need to match the product variant.
     */
    public String getPrice() {
        return price;
    }

    /**
     * @return The title of the line item. The title does not need to match the product variant.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The unique identifier for the {@link ProductVariant} being purchased in this line item.
     */
    public Long getVariantId() {
        return variantId;
    }

    /**
     * @return The unique identifier of this line item.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The vendor supplying the product.
     */
    public String getVendor() { return vendor; }

    /**
     * * @return How far along an order is in terms line items fulfilled. Valid values are: fulfilled, null or partial.
     */
    public String getFulfillmentStatus() { return fulfillmentStatus; }

    /**
     * @return A list of tax_line objects, each of which details the taxes applicable to this line item.
     */
    public List<TaxLine> getTaxLines() { return taxLines; }

    /**
     * @return  States whether or not the line_item is a gift card. If so, the item is not taxed or considered for shipping charges.
     */
    public boolean isGiftCard() { return giftCard; }

    /**
     * @param quantity The quantity of the {@link ProductVariant} being purchased in this line item.
     */
    void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return {@code true} if the product that is being purchased in this line item requires shipping, {@code false} otherwise.
     */
    public boolean isRequiresShipping() {
        return requiresShipping;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LineItem) {
            Long otherVariantId = ((LineItem) o).getVariantId();
            return (otherVariantId != null && otherVariantId.equals(variantId));
        }
        return false;
    }
}
