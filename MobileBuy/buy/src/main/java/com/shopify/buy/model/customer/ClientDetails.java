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

package com.shopify.buy.model.customer;

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
     * @return The browserIp.
     */
    public String getBrowserIp() {
        return browserIp;
    }

    /**
     * 
     * @param browserIp The browser_ip.
     */
    public void setBrowserIp(String browserIp) {
        this.browserIp = browserIp;
    }

    /**
     * 
     * @return The acceptLanguage.
     */
    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    /**
     * 
     * @param acceptLanguage
     *     The accept_language
     */
    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    /**
     * 
     * @return
     *     The userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 
     * @param userAgent
     *     The user_agent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * 
     * @return
     *     The sessionHash
     */
    public String getSessionHash() {
        return sessionHash;
    }

    /**
     * 
     * @param sessionHash
     *     The session_hash
     */
    public void setSessionHash(String sessionHash) {
        this.sessionHash = sessionHash;
    }

    /**
     * 
     * @return
     *     The browserWidth
     */
    public Integer getBrowserWidth() {
        return browserWidth;
    }

    /**
     * 
     * @param browserWidth
     *     The browser_width
     */
    public void setBrowserWidth(Integer browserWidth) {
        this.browserWidth = browserWidth;
    }

    /**
     * 
     * @return
     *     The browserHeight
     */
    public Integer getBrowserHeight() {
        return browserHeight;
    }

    /**
     * 
     * @param browserHeight
     *     The browser_height
     */
    public void setBrowserHeight(Integer browserHeight) {
        this.browserHeight = browserHeight;
    }

}
