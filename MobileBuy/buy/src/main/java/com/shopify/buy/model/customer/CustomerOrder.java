
package com.shopify.buy.model.customer;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.ShopifyObject;
import com.shopify.buy.model.TaxLine;

public class CustomerOrder extends ShopifyObject {

    private String email;

    @SerializedName("closed_at")
    private Object closedAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    private Integer number;

    private Object note;

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
    private Object cancelledAt;

    @SerializedName("cancel_reason")
    private Object cancelReason;

    @SerializedName("total_price_usd")
    private String totalPriceUsd;

    @SerializedName("checkout_token")
    private String checkoutToken;

    private Object reference;

    @SerializedName("user_id")
    private Object userId;

    @SerializedName("location_id")
    private Object locationId;

    @SerializedName("source_identifier")
    private Object sourceIdentifier;

    @SerializedName("source_url")
    private Object sourceUrl;

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
    private List<Object> discountCodes = new ArrayList<Object>();

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
    private Object fulfillmentStatus;

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
    public CustomerOrder() {
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The closedAt
     */
    public Object getClosedAt() {
        return closedAt;
    }

    /**
     * @param closedAt The closed_at
     */
    public void setClosedAt(Object closedAt) {
        this.closedAt = closedAt;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return The number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number The number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return The note
     */
    public Object getNote() {
        return note;
    }

    /**
     * @param note The note
     */
    public void setNote(Object note) {
        this.note = note;
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return The gateway
     */
    public String getGateway() {
        return gateway;
    }

    /**
     * @param gateway The gateway
     */
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    /**
     * @return The test
     */
    public Boolean getTest() {
        return test;
    }

    /**
     * @param test The test
     */
    public void setTest(Boolean test) {
        this.test = test;
    }

    /**
     * @return The totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice The total_price
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return The subtotalPrice
     */
    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    /**
     * @param subtotalPrice The subtotal_price
     */
    public void setSubtotalPrice(String subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    /**
     * @return The totalWeight
     */
    public Integer getTotalWeight() {
        return totalWeight;
    }

    /**
     * @param totalWeight The total_weight
     */
    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

    /**
     * @return The totalTax
     */
    public String getTotalTax() {
        return totalTax;
    }

    /**
     * @param totalTax The total_tax
     */
    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    /**
     * @return The taxesIncluded
     */
    public Boolean getTaxesIncluded() {
        return taxesIncluded;
    }

    /**
     * @param taxesIncluded The taxes_included
     */
    public void setTaxesIncluded(Boolean taxesIncluded) {
        this.taxesIncluded = taxesIncluded;
    }

    /**
     * @return The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return The financialStatus
     */
    public String getFinancialStatus() {
        return financialStatus;
    }

    /**
     * @param financialStatus The financial_status
     */
    public void setFinancialStatus(String financialStatus) {
        this.financialStatus = financialStatus;
    }

    /**
     * @return The confirmed
     */
    public Boolean getConfirmed() {
        return confirmed;
    }

    /**
     * @param confirmed The confirmed
     */
    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * @return The totalDiscounts
     */
    public String getTotalDiscounts() {
        return totalDiscounts;
    }

    /**
     * @param totalDiscounts The total_discounts
     */
    public void setTotalDiscounts(String totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    /**
     * @return The totalLineItemsPrice
     */
    public String getTotalLineItemsPrice() {
        return totalLineItemsPrice;
    }

    /**
     * @param totalLineItemsPrice The total_line_items_price
     */
    public void setTotalLineItemsPrice(String totalLineItemsPrice) {
        this.totalLineItemsPrice = totalLineItemsPrice;
    }

    /**
     * @return The cartToken
     */
    public String getCartToken() {
        return cartToken;
    }

    /**
     * @param cartToken The cart_token
     */
    public void setCartToken(String cartToken) {
        this.cartToken = cartToken;
    }

    /**
     * @return The buyerAcceptsMarketing
     */
    public Boolean getBuyerAcceptsMarketing() {
        return buyerAcceptsMarketing;
    }

    /**
     * @param buyerAcceptsMarketing The buyer_accepts_marketing
     */
    public void setBuyerAcceptsMarketing(Boolean buyerAcceptsMarketing) {
        this.buyerAcceptsMarketing = buyerAcceptsMarketing;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The referringSite
     */
    public String getReferringSite() {
        return referringSite;
    }

    /**
     * @param referringSite The referring_site
     */
    public void setReferringSite(String referringSite) {
        this.referringSite = referringSite;
    }

    /**
     * @return The landingSite
     */
    public String getLandingSite() {
        return landingSite;
    }

    /**
     * @param landingSite The landing_site
     */
    public void setLandingSite(String landingSite) {
        this.landingSite = landingSite;
    }

    /**
     * @return The cancelledAt
     */
    public Object getCancelledAt() {
        return cancelledAt;
    }

    /**
     * @param cancelledAt The cancelled_at
     */
    public void setCancelledAt(Object cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    /**
     * @return The cancelReason
     */
    public Object getCancelReason() {
        return cancelReason;
    }

    /**
     * @param cancelReason The cancel_reason
     */
    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * @return The totalPriceUsd
     */
    public String getTotalPriceUsd() {
        return totalPriceUsd;
    }

    /**
     * @param totalPriceUsd The total_price_usd
     */
    public void setTotalPriceUsd(String totalPriceUsd) {
        this.totalPriceUsd = totalPriceUsd;
    }

    /**
     * @return The checkoutToken
     */
    public String getCheckoutToken() {
        return checkoutToken;
    }

    /**
     * @param checkoutToken The checkout_token
     */
    public void setCheckoutToken(String checkoutToken) {
        this.checkoutToken = checkoutToken;
    }

    /**
     * @return The reference
     */
    public Object getReference() {
        return reference;
    }

    /**
     * @param reference The reference
     */
    public void setReference(Object reference) {
        this.reference = reference;
    }

    /**
     * @return The userId
     */
    public Object getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(Object userId) {
        this.userId = userId;
    }

    /**
     * @return The locationId
     */
    public Object getLocationId() {
        return locationId;
    }

    /**
     * @param locationId The location_id
     */
    public void setLocationId(Object locationId) {
        this.locationId = locationId;
    }

    /**
     * @return The sourceIdentifier
     */
    public Object getSourceIdentifier() {
        return sourceIdentifier;
    }

    /**
     * @param sourceIdentifier The source_identifier
     */
    public void setSourceIdentifier(Object sourceIdentifier) {
        this.sourceIdentifier = sourceIdentifier;
    }

    /**
     * @return The sourceUrl
     */
    public Object getSourceUrl() {
        return sourceUrl;
    }

    /**
     * @param sourceUrl The source_url
     */
    public void setSourceUrl(Object sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    /**
     * @return The processedAt
     */
    public String getProcessedAt() {
        return processedAt;
    }

    /**
     * @param processedAt The processed_at
     */
    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }

    /**
     * @return The deviceId
     */
    public Object getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId The device_id
     */
    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return The browserIp
     */
    public String getBrowserIp() {
        return browserIp;
    }

    /**
     * @param browserIp The browser_ip
     */
    public void setBrowserIp(String browserIp) {
        this.browserIp = browserIp;
    }

    /**
     * @return The landingSiteRef
     */
    public Object getLandingSiteRef() {
        return landingSiteRef;
    }

    /**
     * @param landingSiteRef The landing_site_ref
     */
    public void setLandingSiteRef(Object landingSiteRef) {
        this.landingSiteRef = landingSiteRef;
    }

    /**
     * @return The orderNumber
     */
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber The order_number
     */
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @return The discountCodes
     */
    public List<Object> getDiscountCodes() {
        return discountCodes;
    }

    /**
     * @param discountCodes The discount_codes
     */
    public void setDiscountCodes(List<Object> discountCodes) {
        this.discountCodes = discountCodes;
    }

    /**
     * @return The noteAttributes
     */
    public List<Object> getNoteAttributes() {
        return noteAttributes;
    }

    /**
     * @param noteAttributes The note_attributes
     */
    public void setNoteAttributes(List<Object> noteAttributes) {
        this.noteAttributes = noteAttributes;
    }

    /**
     * @return The paymentGatewayNames
     */
    public List<String> getPaymentGatewayNames() {
        return paymentGatewayNames;
    }

    /**
     * @param paymentGatewayNames The payment_gateway_names
     */
    public void setPaymentGatewayNames(List<String> paymentGatewayNames) {
        this.paymentGatewayNames = paymentGatewayNames;
    }

    /**
     * @return The processingMethod
     */
    public String getProcessingMethod() {
        return processingMethod;
    }

    /**
     * @param processingMethod The processing_method
     */
    public void setProcessingMethod(String processingMethod) {
        this.processingMethod = processingMethod;
    }

    /**
     * @return The checkoutId
     */
    public Integer getCheckoutId() {
        return checkoutId;
    }

    /**
     * @param checkoutId The checkout_id
     */
    public void setCheckoutId(Integer checkoutId) {
        this.checkoutId = checkoutId;
    }

    /**
     * @return The sourceName
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * @param sourceName The source_name
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     * @return The fulfillmentStatus
     */
    public Object getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    /**
     * @param fulfillmentStatus The fulfillment_status
     */
    public void setFulfillmentStatus(Object fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    /**
     * @return The taxLines
     */
    public List<TaxLine> getTaxLines() {
        return taxLines;
    }

    /**
     * @param taxLines The tax_lines
     */
    public void setTaxLines(List<TaxLine> taxLines) {
        this.taxLines = taxLines;
    }

    /**
     * @return The tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return The contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @param contactEmail The contact_email
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * @return The lineItems
     */
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    /**
     * @param lineItems The line_items
     */
    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    /**
     * @return The shippingLines
     */
    public List<ShippingLine> getShippingLines() {
        return shippingLines;
    }

    /**
     * @param shippingLines The shipping_lines
     */
    public void setShippingLines(List<ShippingLine> shippingLines) {
        this.shippingLines = shippingLines;
    }

    /**
     * @return The billingAddress
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * @param billingAddress The billing_address
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * @return The shippingAddress
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /**
     * @param shippingAddress The shipping_address
     */
    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * @return The fulfillments
     */
    public List<Object> getFulfillments() {
        return fulfillments;
    }

    /**
     * @param fulfillments The fulfillments
     */
    public void setFulfillments(List<Object> fulfillments) {
        this.fulfillments = fulfillments;
    }

    /**
     * @return The clientDetails
     */
    public ClientDetails getClientDetails() {
        return clientDetails;
    }

    /**
     * @param clientDetails The client_details
     */
    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    /**
     * @return The refunds
     */
    public List<Object> getRefunds() {
        return refunds;
    }

    /**
     * @param refunds The refunds
     */
    public void setRefunds(List<Object> refunds) {
        this.refunds = refunds;
    }

    /**
     * @return The paymentDetails
     */
    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    /**
     * @param paymentDetails The payment_details
     */
    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    /**
     * @return The customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer The customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
