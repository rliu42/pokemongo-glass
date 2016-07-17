package com.upsight.android.marketing;

import android.app.Activity;
import android.app.DialogFragment;
import android.view.ViewGroup;
import com.upsight.android.marketing.UpsightBillboard.Dimensions;
import com.upsight.android.marketing.UpsightBillboard.Handler;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import com.upsight.android.marketing.internal.billboard.BillboardFragment;
import java.util.List;
import java.util.Set;
import spacemadness.com.lunarconsole.C1391R;

public final class UpsightBillboardHandlers {
    private static final int STYLE_DIALOG;
    private static final int STYLE_FULLSCREEN = 16974122;

    /* renamed from: com.upsight.android.marketing.UpsightBillboardHandlers.1 */
    static /* synthetic */ class C09531 {
        static final /* synthetic */ int[] f268x10de5d48;

        static {
            f268x10de5d48 = new int[PresentationStyle.values().length];
            try {
                f268x10de5d48[PresentationStyle.Dialog.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f268x10de5d48[PresentationStyle.Fullscreen.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private static abstract class SimpleHandler implements Handler {
        protected Activity mActivity;
        protected BillboardFragment mFragment;

        protected SimpleHandler(Activity activity) {
            this.mFragment = null;
            this.mActivity = activity;
        }

        public void onNextView() {
        }

        public void onDetach() {
            DialogFragment fragment = this.mFragment;
            if (fragment != null) {
                fragment.dismissAllowingStateLoss();
                this.mFragment = null;
            }
        }

        public void onPurchases(List<UpsightPurchase> list) {
        }

        public void onRewards(List<UpsightReward> list) {
        }
    }

    public static class DefaultHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List x0) {
            super.onPurchases(x0);
        }

        public /* bridge */ /* synthetic */ void onRewards(List x0) {
            super.onRewards(x0);
        }

        public DefaultHandler(Activity activity) {
            super(activity);
        }

        public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> dimensions) {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            int style;
            Set<Dimensions> fragmentDimensions = null;
            switch (C09531.f268x10de5d48[presentation.ordinal()]) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    fragmentDimensions = dimensions;
                    style = UpsightBillboardHandlers.STYLE_DIALOG;
                    break;
                default:
                    style = UpsightBillboardHandlers.STYLE_FULLSCREEN;
                    break;
            }
            this.mFragment = BillboardFragment.newInstance(this.mActivity, fragmentDimensions);
            this.mFragment.setStyle(1, style);
            this.mFragment.setCancelable(false);
            this.mFragment.show(this.mActivity.getFragmentManager(), null);
            return this.mFragment.getContentViewContainer();
        }
    }

    public static class DialogHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List x0) {
            super.onPurchases(x0);
        }

        public /* bridge */ /* synthetic */ void onRewards(List x0) {
            super.onRewards(x0);
        }

        public DialogHandler(Activity activity) {
            super(activity);
        }

        public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> dimensions) {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            this.mFragment = BillboardFragment.newInstance(this.mActivity, dimensions);
            this.mFragment.setStyle(1, UpsightBillboardHandlers.STYLE_DIALOG);
            this.mFragment.setCancelable(false);
            this.mFragment.show(this.mActivity.getFragmentManager(), null);
            return this.mFragment.getContentViewContainer();
        }
    }

    private static class EmbeddedHandler extends SimpleHandler {
        protected int mContainerViewId;

        public EmbeddedHandler(Activity activity, int containerViewId) {
            super(activity);
            this.mContainerViewId = containerViewId;
        }

        public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> dimensions) {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            this.mFragment = BillboardFragment.newInstance(this.mActivity, dimensions);
            this.mActivity.getFragmentManager().beginTransaction().add(this.mContainerViewId, this.mFragment).commit();
            return this.mFragment.getContentViewContainer();
        }
    }

    public static class FullscreenHandler extends SimpleHandler {
        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List x0) {
            super.onPurchases(x0);
        }

        public /* bridge */ /* synthetic */ void onRewards(List x0) {
            super.onRewards(x0);
        }

        public FullscreenHandler(Activity activity) {
            super(activity);
        }

        public ViewGroup onAttach(String scope, PresentationStyle presentation, Set<Dimensions> set) {
            if (this.mActivity == null || this.mActivity.isFinishing()) {
                return null;
            }
            this.mFragment = BillboardFragment.newInstance(this.mActivity, null);
            this.mFragment.setStyle(1, UpsightBillboardHandlers.STYLE_FULLSCREEN);
            this.mFragment.setCancelable(false);
            this.mFragment.show(this.mActivity.getFragmentManager(), null);
            return this.mFragment.getContentViewContainer();
        }
    }

    static {
        STYLE_DIALOG = C0952R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_UpsightDialog;
    }

    private static Handler forEmbedded(Activity activity, int containerViewId) {
        return new EmbeddedHandler(activity, containerViewId);
    }

    public static Handler forDialog(Activity activity) {
        return new DialogHandler(activity);
    }

    public static Handler forFullscreen(Activity activity) {
        return new FullscreenHandler(activity);
    }

    public static Handler forDefault(Activity activity) {
        return new DefaultHandler(activity);
    }
}
