
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
     * @return
     *     The price
     */
    public String getPrice() {
        return price;
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
     * @return
     *     The source
     */
    public String getSource() {
        return source;
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
     * @return
     *     The taxLines
     */
    public List<TaxLine> getTaxLines() {
        return taxLines;
    }

}
