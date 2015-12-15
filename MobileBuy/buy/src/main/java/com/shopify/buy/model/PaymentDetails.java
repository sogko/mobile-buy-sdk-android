
package com.shopify.buy.model;

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
     * @return
     *     The avsResultCode
     */
    public String getAvsResultCode() {
        return avsResultCode;
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
     * @return
     *     The creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * 
     * @return
     *     The creditCardCompany
     */
    public String getCreditCardCompany() {
        return creditCardCompany;
    }

}
