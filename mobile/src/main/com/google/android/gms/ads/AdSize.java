package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzl;

public final class AdSize {
    public static final int AUTO_HEIGHT = -2;
    public static final AdSize BANNER;
    public static final AdSize FLUID;
    public static final AdSize FULL_BANNER;
    public static final int FULL_WIDTH = -1;
    public static final AdSize LARGE_BANNER;
    public static final AdSize LEADERBOARD;
    public static final AdSize MEDIUM_RECTANGLE;
    public static final AdSize SMART_BANNER;
    public static final AdSize WIDE_SKYSCRAPER;
    private final int zznQ;
    private final int zznR;
    private final String zznS;

    static {
        BANNER = new AdSize(320, 50, "320x50_mb");
        FULL_BANNER = new AdSize(468, 60, "468x60_as");
        LARGE_BANNER = new AdSize(320, 100, "320x100_as");
        LEADERBOARD = new AdSize(728, 90, "728x90_as");
        MEDIUM_RECTANGLE = new AdSize(300, 250, "300x250_as");
        WIDE_SKYSCRAPER = new AdSize(160, 600, "160x600_as");
        SMART_BANNER = new AdSize(FULL_WIDTH, AUTO_HEIGHT, "smart_banner");
        FLUID = new AdSize(-3, -4, "fluid");
    }

    public AdSize(int width, int height) {
        this(width, height, (width == FULL_WIDTH ? "FULL" : String.valueOf(width)) + "x" + (height == AUTO_HEIGHT ? "AUTO" : String.valueOf(height)) + "_as");
    }

    AdSize(int width, int height, String formatString) {
        if (width < 0 && width != FULL_WIDTH && width != -3) {
            throw new IllegalArgumentException("Invalid width for AdSize: " + width);
        } else if (height >= 0 || height == AUTO_HEIGHT || height == -4) {
            this.zznQ = width;
            this.zznR = height;
            this.zznS = formatString;
        } else {
            throw new IllegalArgumentException("Invalid height for AdSize: " + height);
        }
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AdSize)) {
            return false;
        }
        AdSize adSize = (AdSize) other;
        return this.zznQ == adSize.zznQ && this.zznR == adSize.zznR && this.zznS.equals(adSize.zznS);
    }

    public int getHeight() {
        return this.zznR;
    }

    public int getHeightInPixels(Context context) {
        switch (this.zznR) {
            case -4:
            case -3:
                return FULL_WIDTH;
            case AUTO_HEIGHT /*-2*/:
                return AdSizeParcel.zzb(context.getResources().getDisplayMetrics());
            default:
                return zzl.zzcF().zzb(context, this.zznR);
        }
    }

    public int getWidth() {
        return this.zznQ;
    }

    public int getWidthInPixels(Context context) {
        switch (this.zznQ) {
            case -4:
            case -3:
                return FULL_WIDTH;
            case FULL_WIDTH /*-1*/:
                return AdSizeParcel.zza(context.getResources().getDisplayMetrics());
            default:
                return zzl.zzcF().zzb(context, this.zznQ);
        }
    }

    public int hashCode() {
        return this.zznS.hashCode();
    }

    public boolean isAutoHeight() {
        return this.zznR == AUTO_HEIGHT;
    }

    public boolean isFluid() {
        return this.zznQ == -3 && this.zznR == -4;
    }

    public boolean isFullWidth() {
        return this.zznQ == FULL_WIDTH;
    }

    public String toString() {
        return this.zznS;
    }
}
