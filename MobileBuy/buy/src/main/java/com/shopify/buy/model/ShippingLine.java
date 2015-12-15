
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

    @SerializedName("tax_lines")
    private List<TaxLine> taxLines = new ArrayList<>();

    /**
     * No args constructor for use in serialization.
     */
    public ShippingLine() {
    }

    /**
     * @return The title of the shipping method.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The price of this shipping method.
     */
    public String getPrice() {
        return price;
    }

    /**
     * @return A reference to the shipping method.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The source of the shipping method.
     */
    public String getSource() {
        return source;
    }

    /**
     * @return A list of {@link TaxLine}, each of which details the taxes applicable to this shipping_line.
     */
    public List<TaxLine> getTaxLines() {
        return taxLines;
    }

}
