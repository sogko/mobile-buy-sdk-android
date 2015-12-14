
package com.shopify.buy.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.shopify.buy.model.TaxLine;

public class ShippingLine {

    private String title;

    private String price;

    private String code;

    private String source;

    private Object phone;

    @SerializedName("tax_lines")
    private List<TaxLine> taxLines = new ArrayList<>();

    /**
     * No args constructor for use in serialization.
     */
    public ShippingLine() {
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * 
     * @param price
     *     The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The source
     */
    public String getSource() {
        return source;
    }

    /**
     * 
     * @param source
     *     The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 
     * @return
     *     The phone
     */
    public Object getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone
     *     The phone
     */
    public void setPhone(Object phone) {
        this.phone = phone;
    }

    /**
     * 
     * @return
     *     The taxLines
     */
    public List<TaxLine> getTaxLines() {
        return taxLines;
    }

    /**
     * 
     * @param taxLines
     *     The tax_lines
     */
    public void setTaxLines(List<TaxLine> taxLines) {
        this.taxLines = taxLines;
    }

}
