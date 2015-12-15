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

public class ClientDetails {

    @SerializedName("browser_ip")
    private String browserIp;

    @SerializedName("accept_language")
    private String acceptLanguage;

    @SerializedName("user_agent")
    private String userAgent;

    @SerializedName("session_hash")
    private String sessionHash;

    @SerializedName("browser_width")
    private Integer browserWidth;

    @SerializedName("browser_height")
    private Integer browserHeight;

    /**
     * No args constructor for use in serialization.
     */
    public ClientDetails() {
    }

    /**
     * @return The browser IP address.
     */
    public String getBrowserIp() {
        return browserIp;
    }

    /**
     * @return The acceptLanguage.
     */
    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    /**
     * @return  The userAgent.
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @return The sessionHash.
     */
    public String getSessionHash() {
        return sessionHash;
    }

    /**
     * @return The browser screen width in pixels, if available.
     *
     */
    public Integer getBrowserWidth() {
        return browserWidth;
    }

    /**
     * 
     * @return The browser screen height in pixels, if available.
     *
     */
    public Integer getBrowserHeight() {
        return browserHeight;
    }

}
