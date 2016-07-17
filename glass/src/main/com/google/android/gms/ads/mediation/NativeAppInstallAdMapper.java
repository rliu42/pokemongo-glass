package com.google.android.gms.ads.mediation;

import com.google.android.gms.ads.formats.NativeAd.Image;
import java.util.List;

public abstract class NativeAppInstallAdMapper extends NativeAdMapper {
    private Image zzKJ;
    private String zzwo;
    private List<Image> zzwp;
    private String zzwq;
    private String zzws;
    private double zzwt;
    private String zzwu;
    private String zzwv;

    public final String getBody() {
        return this.zzwq;
    }

    public final String getCallToAction() {
        return this.zzws;
    }

    public final String getHeadline() {
        return this.zzwo;
    }

    public final Image getIcon() {
        return this.zzKJ;
    }

    public final List<Image> getImages() {
        return this.zzwp;
    }

    public final String getPrice() {
        return this.zzwv;
    }

    public final double getStarRating() {
        return this.zzwt;
    }

    public final String getStore() {
        return this.zzwu;
    }

    public final void setBody(String body) {
        this.zzwq = body;
    }

    public final void setCallToAction(String callToAction) {
        this.zzws = callToAction;
    }

    public final void setHeadline(String headline) {
        this.zzwo = headline;
    }

    public final void setIcon(Image icon) {
        this.zzKJ = icon;
    }

    public final void setImages(List<Image> images) {
        this.zzwp = images;
    }

    public final void setPrice(String price) {
        this.zzwv = price;
    }

    public final void setStarRating(double starRating) {
        this.zzwt = starRating;
    }

    public final void setStore(String store) {
        this.zzwu = store;
    }
}
