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

package com.shopify.buy.ui.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import com.shopify.buy.R;

/**
 * Describes the colors and backgrounds to use for Activities.
 */
public class ShopifyTheme implements Parcelable {

    protected Style style;
    protected int accentColor = -1;
    protected boolean showProductImageBackground;
    protected String pathToCustomFont;

    private Typeface typeface = null;
    private float deltaTextSizePx = 0;

    public enum Style {
        DARK,
        LIGHT
    }

    public ShopifyTheme(Resources res) {
        this(Style.LIGHT, res.getColor(R.color.default_accent), true);
    }

    public ShopifyTheme(Style style, int accentColor, boolean showProductImageBackground) {
        this.style = style;
        this.accentColor = accentColor;
        this.showProductImageBackground = showProductImageBackground;
    }

    public static final Parcelable.Creator<ShopifyTheme> CREATOR = new Parcelable.Creator<ShopifyTheme>() {
        public ShopifyTheme createFromParcel(Parcel in) {
            return new ShopifyTheme(in);
        }

        public ShopifyTheme[] newArray(int size) {
            return new ShopifyTheme[size];
        }
    };

    protected ShopifyTheme(Parcel in) {
        style = Style.values()[in.readInt()];
        accentColor = in.readInt();
        showProductImageBackground = in.readInt() != 0;
        pathToCustomFont = in.readString();
        deltaTextSizePx = in.readFloat();
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
    }

    /**
     * Adds a custom font to this theme.
     *
     * @param pathToCustomFont  The relative path from the assets folder to the font file (e.g. "fonts/my_custom_font.ttf").
     * @param baselineTextSize  The text size of this font (in scaled pixels) equivalent to the default system font at <b>20sp</b>.
     */
    public void setCustomFont(String pathToCustomFont, int baselineTextSize, Context context) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        this.pathToCustomFont = pathToCustomFont;
        this.deltaTextSizePx = (20 - baselineTextSize) * scaledDensity;
    }

    public void setShowProductImageBackground(boolean showProductImageBackground) {
        this.showProductImageBackground = showProductImageBackground;
    }

    public boolean shouldShowProductImageBackground() {
        return showProductImageBackground;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(style.ordinal());
        out.writeInt(accentColor);
        out.writeInt(showProductImageBackground ? 1 : 0);
        out.writeString(pathToCustomFont);
        out.writeFloat(deltaTextSizePx);
    }

    public int getBackgroundColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_background);
            default:
                return res.getColor(R.color.light_background);
        }
    }

    public int getAppBarBackgroundColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_low_contrast_background);
            default:
                return res.getColor(R.color.light_low_contrast_background);
        }
    }

    public int getProductTitleColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_product_title);
            default:
                return res.getColor(R.color.light_product_title);
        }
    }

    public int getVariantOptionNameColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_low_contrast_grey);
            default:
                return res.getColor(R.color.light_low_contrast_grey);
        }
    }

    public int getCompareAtPriceColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.body_grey);
            default:
                return res.getColor(R.color.body_grey);
        }
    }

    public int getProductDescriptionColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.body_grey);
            default:
                return res.getColor(R.color.body_grey);
        }
    }

    public int getDividerColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_low_contrast_grey);
            default:
                return res.getColor(R.color.light_low_contrast_grey);
        }
    }

    public int getDialogTitleColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_dialog_title);
            default:
                return res.getColor(R.color.light_dialog_title);
        }
    }

    public int getVariantBreadcrumbBackgroundColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_low_contrast_grey);
            default:
                return res.getColor(R.color.light_low_contrast_grey);
        }
    }

    public int getDialogListItemColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.body_grey);
            default:
                return res.getColor(R.color.body_grey);
        }
    }

    public int getCheckoutLabelColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_dialog_title);
            default:
                return res.getColor(R.color.light_dialog_title);
        }
    }

    public Drawable getCheckmarkDrawable(Context context) {
        Drawable checkmark;
        switch (style) {
            case DARK:
                checkmark = context.getResources().getDrawable(R.drawable.ic_check_white_24dp);
                break;
            default:
                checkmark = context.getResources().getDrawable(R.drawable.ic_check_black_24dp);
                break;
        }
        checkmark.setColorFilter(new PorterDuffColorFilter(accentColor, PorterDuff.Mode.SRC_IN));
        return checkmark;
    }

    public Drawable getBackgroundSelectorDrawable(Resources res) {
        switch (style) {
            case DARK:
                return res.getDrawable(R.drawable.dark_background_selector);
            default:
                return res.getDrawable(R.drawable.light_background_selector);
        }
    }

    public void applyCustomFont(TextView textView) {
        if (typeface == null && !TextUtils.isEmpty(pathToCustomFont)) {
            typeface = Typeface.createFromAsset(textView.getContext().getAssets(), pathToCustomFont);
        }

        if (textView == null || typeface == null) {
            return;
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() + deltaTextSizePx);
        textView.setTypeface(typeface);
    }

}
