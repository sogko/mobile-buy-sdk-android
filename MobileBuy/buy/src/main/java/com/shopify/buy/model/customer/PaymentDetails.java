
package com.shopify.buy.model.customer;

import com.google.gson.annotations.SerializedName;

public class PaymentDetails {

    @SerializedName("credit_card_bin")
    private String creditCardBin;

    @SerializedName("avs_result_code")
    private String avsResultCode;

    @SerializedName("cvv_result_code")
    private String cvvResultCode;

    @SerializedName("credit_card_number")
    private String creditCardNumber;

    @SerializedName("credit_card_company")
    private String creditCardCompany;

    /**
     * No args constructor for use in serialization.
     */
    PaymentDetails() {}

    /**
     * 
     * @return
     *     The creditCardBin
     */
    public String getCreditCardBin() {
        return creditCardBin;
    }

    /**
     * 
     * @param creditCardBin
     *     The credit_card_bin
     */
    public void setCreditCardBin(String creditCardBin) {
        this.creditCardBin = creditCardBin;
    }

    /**
     * 
     * @return
     *     The avsResultCode
     */
    public String getAvsResultCode() {
        return avsResultCode;
    }

    /**
     * 
     * @param avsResultCode
     *     The avs_result_code
     */
    public void setAvsResultCode(String avsResultCode) {
        this.avsResultCode = avsResultCode;
    }

    /**
     * 
     * @return
     *     The cvvResultCode
     */
    public String getCvvResultCode() {
        return cvvResultCode;
    }

    /**
     * 
     * @param cvvResultCode
     *     The cvv_result_code
     */
    public void setCvvResultCode(String cvvResultCode) {
        this.cvvResultCode = cvvResultCode;
    }

    /**
     * 
     * @return
     *     The creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * 
     * @param creditCardNumber
     *     The credit_card_number
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * 
     * @return
     *     The creditCardCompany
     */
    public String getCreditCardCompany() {
        return creditCardCompany;
    }

    /**
     * 
     * @param creditCardCompany
     *     The credit_card_company
     */
    public void setCreditCardCompany(String creditCardCompany) {
        this.creditCardCompany = creditCardCompany;
    }

}
