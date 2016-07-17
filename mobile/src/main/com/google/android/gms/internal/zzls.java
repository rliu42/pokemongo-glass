package com.google.android.gms.internal;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import spacemadness.com.lunarconsole.C1391R;

public final class zzls extends Drawable implements Callback {
    private int mFrom;
    private long zzNY;
    private boolean zzaea;
    private int zzaeh;
    private int zzaei;
    private int zzaej;
    private int zzaek;
    private int zzael;
    private boolean zzaem;
    private zzb zzaen;
    private Drawable zzaeo;
    private Drawable zzaep;
    private boolean zzaeq;
    private boolean zzaer;
    private boolean zzaes;
    private int zzaet;

    private static final class zza extends Drawable {
        private static final zza zzaeu;
        private static final zza zzaev;

        private static final class zza extends ConstantState {
            private zza() {
            }

            public int getChangingConfigurations() {
                return 0;
            }

            public Drawable newDrawable() {
                return zza.zzaeu;
            }
        }

        static {
            zzaeu = new zza();
            zzaev = new zza();
        }

        private zza() {
        }

        public void draw(Canvas canvas) {
        }

        public ConstantState getConstantState() {
            return zzaev;
        }

        public int getOpacity() {
            return -2;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }
    }

    static final class zzb extends ConstantState {
        int zzaew;
        int zzaex;

        zzb(zzb com_google_android_gms_internal_zzls_zzb) {
            if (com_google_android_gms_internal_zzls_zzb != null) {
                this.zzaew = com_google_android_gms_internal_zzls_zzb.zzaew;
                this.zzaex = com_google_android_gms_internal_zzls_zzb.zzaex;
            }
        }

        public int getChangingConfigurations() {
            return this.zzaew;
        }

        public Drawable newDrawable() {
            return new zzls(this);
        }
    }

    public zzls(Drawable drawable, Drawable drawable2) {
        this(null);
        if (drawable == null) {
            drawable = zza.zzaeu;
        }
        this.zzaeo = drawable;
        drawable.setCallback(this);
        zzb com_google_android_gms_internal_zzls_zzb = this.zzaen;
        com_google_android_gms_internal_zzls_zzb.zzaex |= drawable.getChangingConfigurations();
        if (drawable2 == null) {
            drawable2 = zza.zzaeu;
        }
        this.zzaep = drawable2;
        drawable2.setCallback(this);
        com_google_android_gms_internal_zzls_zzb = this.zzaen;
        com_google_android_gms_internal_zzls_zzb.zzaex |= drawable2.getChangingConfigurations();
    }

    zzls(zzb com_google_android_gms_internal_zzls_zzb) {
        this.zzaeh = 0;
        this.zzaej = MotionEventCompat.ACTION_MASK;
        this.zzael = 0;
        this.zzaea = true;
        this.zzaen = new zzb(com_google_android_gms_internal_zzls_zzb);
    }

    public boolean canConstantState() {
        if (!this.zzaeq) {
            boolean z = (this.zzaeo.getConstantState() == null || this.zzaep.getConstantState() == null) ? false : true;
            this.zzaer = z;
            this.zzaeq = true;
        }
        return this.zzaer;
    }

    public void draw(Canvas canvas) {
        int i = 1;
        int i2 = 0;
        switch (this.zzaeh) {
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                this.zzNY = SystemClock.uptimeMillis();
                this.zzaeh = 2;
                break;
            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                if (this.zzNY >= 0) {
                    float uptimeMillis = ((float) (SystemClock.uptimeMillis() - this.zzNY)) / ((float) this.zzaek);
                    if (uptimeMillis < 1.0f) {
                        i = 0;
                    }
                    if (i != 0) {
                        this.zzaeh = 0;
                    }
                    float min = Math.min(uptimeMillis, 1.0f);
                    this.zzael = (int) ((min * ((float) (this.zzaei - this.mFrom))) + ((float) this.mFrom));
                    break;
                }
                break;
        }
        i2 = i;
        i = this.zzael;
        boolean z = this.zzaea;
        Drawable drawable = this.zzaeo;
        Drawable drawable2 = this.zzaep;
        if (i2 != 0) {
            if (!z || i == 0) {
                drawable.draw(canvas);
            }
            if (i == this.zzaej) {
                drawable2.setAlpha(this.zzaej);
                drawable2.draw(canvas);
                return;
            }
            return;
        }
        if (z) {
            drawable.setAlpha(this.zzaej - i);
        }
        drawable.draw(canvas);
        if (z) {
            drawable.setAlpha(this.zzaej);
        }
        if (i > 0) {
            drawable2.setAlpha(i);
            drawable2.draw(canvas);
            drawable2.setAlpha(this.zzaej);
        }
        invalidateSelf();
    }

    public int getChangingConfigurations() {
        return (super.getChangingConfigurations() | this.zzaen.zzaew) | this.zzaen.zzaex;
    }

    public ConstantState getConstantState() {
        if (!canConstantState()) {
            return null;
        }
        this.zzaen.zzaew = getChangingConfigurations();
        return this.zzaen;
    }

    public int getIntrinsicHeight() {
        return Math.max(this.zzaeo.getIntrinsicHeight(), this.zzaep.getIntrinsicHeight());
    }

    public int getIntrinsicWidth() {
        return Math.max(this.zzaeo.getIntrinsicWidth(), this.zzaep.getIntrinsicWidth());
    }

    public int getOpacity() {
        if (!this.zzaes) {
            this.zzaet = Drawable.resolveOpacity(this.zzaeo.getOpacity(), this.zzaep.getOpacity());
            this.zzaes = true;
        }
        return this.zzaet;
    }

    public void invalidateDrawable(Drawable who) {
        if (zzmx.zzqu()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.invalidateDrawable(this);
            }
        }
    }

    public Drawable mutate() {
        if (!this.zzaem && super.mutate() == this) {
            if (canConstantState()) {
                this.zzaeo.mutate();
                this.zzaep.mutate();
                this.zzaem = true;
            } else {
                throw new IllegalStateException("One or more children of this LayerDrawable does not have constant state; this drawable cannot be mutated.");
            }
        }
        return this;
    }

    protected void onBoundsChange(Rect bounds) {
        this.zzaeo.setBounds(bounds);
        this.zzaep.setBounds(bounds);
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (zzmx.zzqu()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.scheduleDrawable(this, what, when);
            }
        }
    }

    public void setAlpha(int alpha) {
        if (this.zzael == this.zzaej) {
            this.zzael = alpha;
        }
        this.zzaej = alpha;
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter cf) {
        this.zzaeo.setColorFilter(cf);
        this.zzaep.setColorFilter(cf);
    }

    public void startTransition(int durationMillis) {
        this.mFrom = 0;
        this.zzaei = this.zzaej;
        this.zzael = 0;
        this.zzaek = durationMillis;
        this.zzaeh = 1;
        invalidateSelf();
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (zzmx.zzqu()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.unscheduleDrawable(this, what);
            }
        }
    }

    public Drawable zzoF() {
        return this.zzaep;
    }
}
