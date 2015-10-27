package com.shopify.buy.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.shopify.buy.R;

/**
 * Describes the colors and backgrounds to use for Activities.
 */
public class ShopifyTheme implements Parcelable {

    protected Style style;
    protected int accentColor = -1;
    protected boolean showProductImageBackground;

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
}
