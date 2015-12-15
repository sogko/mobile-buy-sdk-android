
package com.shopify.buy.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.shopify.buy.dataprovider.BuyClient;

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

    private Integer number;

    private String note;

    private String token;

    private String gateway;

    private Boolean test;

    @SerializedName("total_price")
    private String totalPrice;

    @SerializedName("subtotal_price")
    private String subtotalPrice;

    @SerializedName("total_weight")
    private Integer totalWeight;

    @SerializedName("total_tax")
    private String totalTax;

    @SerializedName("taxes_included")
    private Boolean taxesIncluded;

    private String currency;

    @SerializedName("financial_status")
    private String financialStatus;

    private Boolean confirmed;

    @SerializedName("total_discounts")
    private String totalDiscounts;

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

    @SerializedName("total_price_usd")
    private String totalPriceUsd;

    @SerializedName("checkout_token")
    private String checkoutToken;

    @SerializedName("user_id")
    private Object userId;

    @SerializedName("location_id")
    private Object locationId;

    @SerializedName("source_identifier")
    private Object sourceIdentifier;

    @SerializedName("source_url")
    private String sourceUrl;

    @SerializedName("processed_at")
    private String processedAt;

    @SerializedName("device_id")
    private Object deviceId;

    @SerializedName("browser_ip")
    private String browserIp;

    @SerializedName("landing_site_ref")
    private Object landingSiteRef;

    @SerializedName("order_number")
    private Integer orderNumber;

    @SerializedName("discount_codes")
    private List<Discount> discountCodes = new ArrayList<>();

    @SerializedName("note_attributes")
    private List<Object> noteAttributes = new ArrayList<Object>();

    @SerializedName("payment_gateway_names")
    private List<String> paymentGatewayNames = new ArrayList<String>();

    @SerializedName("processing_method")
    private String processingMethod;

    @SerializedName("checkout_id")
    private Integer checkoutId;

    @SerializedName("source_name")
    private String sourceName;

    @SerializedName("fulfillment_status")
    private String fulfillmentStatus;

    @SerializedName("tax_lines")
    private List<TaxLine> taxLines = new ArrayList<TaxLine>();

    private String tags;

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

    private List<Object> fulfillments = new ArrayList<Object>();

    @SerializedName("client_details")
    private ClientDetails clientDetails;

    private List<Object> refunds = new ArrayList<Object>();

    @SerializedName("payment_details")
    private PaymentDetails paymentDetails;

    private Customer customer;

    /**
     * No args constructor for use in serialization
     */
    public Order() {
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return The closedAt
     */
    public Object getClosedAt() {
        return closedAt;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @return The number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @return The note
     */
    public Object getNote() {
        return note;
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return The gateway
     */
    public String getGateway() {
        return gateway;
    }

    /**
     * @return The test
     */
    public Boolean getTest() {
        return test;
    }

    /**
     * @return The totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return The subtotalPrice
     */
    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    /**
     * @return The totalWeight
     */
    public Integer getTotalWeight() {
        return totalWeight;
    }

    /**
     * @return The totalTax
     */
    public String getTotalTax() {
        return totalTax;
    }

    /**
     * @return The taxesIncluded
     */
    public Boolean getTaxesIncluded() {
        return taxesIncluded;
    }

    /**
     * @return The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The financialStatus
     */
    public String getFinancialStatus() {
        return financialStatus;
    }

    /**
     * @return The confirmed
     */
    public Boolean getConfirmed() {
        return confirmed;
    }

    /**
     * @return The totalDiscounts
     */
    public String getTotalDiscounts() {
        return totalDiscounts;
    }

    /**
     * @return The totalLineItemsPrice
     */
    public String getTotalLineItemsPrice() {
        return totalLineItemsPrice;
    }

    /**
     * @return The cartToken
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
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @return The referringSite
     */
    public String getReferringSite() {
        return referringSite;
    }

    /**
     * @return The landingSite
     */
    public String getLandingSite() {
        return landingSite;
    }

    /**
     * @return The date and time when the order was cancelled.
     */
    public Object getCancelledAt() {
        return cancelledAt;
    }

    /**
     * @return The reason why the order was cancelled. If the order was not cancelled, this value is null.
     */
    public Object getCancelReason() {
        return cancelReason;
    }

    /**
     * @return The totalPriceUsd
     */
    public String getTotalPriceUsd() {
        return totalPriceUsd;
    }

    /**
     * @return The checkoutToken
     */
    public String getCheckoutToken() {
        return checkoutToken;
    }

    /**
     * @return The reference
     */
    public Object getReference() {
        return reference;
    }

    /**
     * @return The userId
     */
    public Object getUserId() {
        return userId;
    }

    /**
     * @return The locationId
     */
    public Object getLocationId() {
        return locationId;
    }

    /**
     * @return The sourceIdentifier
     */
    public Object getSourceIdentifier() {
        return sourceIdentifier;
    }

    /**
     * @return The sourceUrl
     */
    public Object getSourceUrl() {
        return sourceUrl;
    }

    /**
     * @return The processedAt
     */
    public String getProcessedAt() {
        return processedAt;
    }

    /**
     * @return The deviceId
     */
    public Object getDeviceId() {
        return deviceId;
    }

    /**
     * @return The IP address of the browser used by the customer when placing the order.
     */
    public String getBrowserIp() {
        return browserIp;
    }

    /**
     * @return The landingSiteRef
     */
    public Object getLandingSiteRef() {
        return landingSiteRef;
    }

    /**
     * @return The orderNumber
     */
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * @return The discountCodes
     */
    public List<Object> getDiscountCodes() {
        return discountCodes;
    }

    /**
     * @return The noteAttributes
     */
    public List<Object> getNoteAttributes() {
        return noteAttributes;
    }

    /**
     * @return The paymentGatewayNames
     */
    public List<String> getPaymentGatewayNames() {
        return paymentGatewayNames;
    }

    /**
     * @return The processingMethod
     */
    public String getProcessingMethod() {
        return processingMethod;
    }

    /**
     * @return The checkoutId
     */
    public Integer getCheckoutId() {
        return checkoutId;
    }

    /**
     * @return The sourceName
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * @return The fulfillmentStatus
     */
    public Object getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    /**
     * @return The taxLines
     */
    public List<TaxLine> getTaxLines() {
        return taxLines;
    }

    /**
     * @return The tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @return The contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @return The lineItems
     */
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    /**
     * @return The shippingLines
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
     * @return The shippingAddress
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /**
     * @return The fulfillments
     */
    public List<Object> getFulfillments() {
        return fulfillments;
    }

    /**
     * @return The clientDetails
     */
    public ClientDetails getClientDetails() {
        return clientDetails;
    }

    /**
     * @return The refunds
     */
    public List<Object> getRefunds() {
        return refunds;
    }

    /**
     * @return The paymentDetails
     */
    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    /**
     * @return The customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @return URL for the website showing the order status. This is only available for orders that are part of a complete {@link Checkout} returned using {@link BuyClient#getCheckout(String, Callback)}
     */
    public String getStatusUrl() {
        return statusUrl;
    }
}
