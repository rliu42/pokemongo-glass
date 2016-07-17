package com.upsight.android.marketing.internal.billboard;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.upsight.android.marketing.C0952R;
import com.upsight.android.marketing.UpsightBillboard.Dimensions;
import com.upsight.android.marketing.UpsightBillboard.Dimensions.LayoutOrientation;
import java.util.Set;
import spacemadness.com.lunarconsole.C1391R;

public final class BillboardFragment extends DialogFragment {
    private static final String BUNDLE_KEY_LANDSCAPE_HEIGHT = "landscapeHeight";
    private static final String BUNDLE_KEY_LANDSCAPE_WIDTH = "landscapeWidth";
    private static final String BUNDLE_KEY_PORTRAIT_HEIGHT = "portraitHeight";
    private static final String BUNDLE_KEY_PORTRAIT_WIDTH = "portraitWidth";
    private ViewGroup mContentViewContainer;
    private ViewGroup mRootView;

    /* renamed from: com.upsight.android.marketing.internal.billboard.BillboardFragment.1 */
    static /* synthetic */ class C09561 {
        static final /* synthetic */ int[] f270x3f5ad3de;

        static {
            f270x3f5ad3de = new int[LayoutOrientation.values().length];
            try {
                f270x3f5ad3de[LayoutOrientation.Portrait.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f270x3f5ad3de[LayoutOrientation.Landscape.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public BillboardFragment() {
        this.mRootView = null;
        this.mContentViewContainer = null;
    }

    public static BillboardFragment newInstance(Context context, Set<Dimensions> dimensions) {
        BillboardFragment fragment = new BillboardFragment();
        Bundle args = new Bundle();
        if (dimensions != null) {
            for (Dimensions dimension : dimensions) {
                if (dimension.width > 0 && dimension.height > 0) {
                    switch (C09561.f270x3f5ad3de[dimension.layout.ordinal()]) {
                        case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            args.putInt(BUNDLE_KEY_PORTRAIT_WIDTH, dimension.width);
                            args.putInt(BUNDLE_KEY_PORTRAIT_HEIGHT, dimension.height);
                            break;
                        case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                            args.putInt(BUNDLE_KEY_LANDSCAPE_WIDTH, dimension.width);
                            args.putInt(BUNDLE_KEY_LANDSCAPE_HEIGHT, dimension.height);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        fragment.mContentViewContainer = new LinearLayout(context.getApplicationContext());
        return fragment;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.mContentViewContainer != null) {
            this.mRootView = (ViewGroup) inflater.inflate(C0952R.layout.upsight_fragment_billboard, container, false);
            this.mRootView.addView(this.mContentViewContainer, new LayoutParams(-1, -1));
        }
        return this.mRootView;
    }

    public void onStart() {
        super.onStart();
        if (this.mContentViewContainer == null) {
            dismiss();
        }
    }

    public void onResume() {
        int orientation = getResources().getConfiguration().orientation;
        Bundle args = getArguments();
        if (orientation == 1 && args.containsKey(BUNDLE_KEY_PORTRAIT_WIDTH)) {
            setDialogSize(args.getInt(BUNDLE_KEY_PORTRAIT_WIDTH), args.getInt(BUNDLE_KEY_PORTRAIT_HEIGHT));
        } else if (orientation == 2 && args.containsKey(BUNDLE_KEY_LANDSCAPE_WIDTH)) {
            setDialogSize(args.getInt(BUNDLE_KEY_LANDSCAPE_WIDTH), args.getInt(BUNDLE_KEY_LANDSCAPE_HEIGHT));
        }
        super.onResume();
    }

    public void onDestroyView() {
        if (this.mContentViewContainer != null) {
            this.mRootView.removeView(this.mContentViewContainer);
        }
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public ViewGroup getContentViewContainer() {
        return this.mContentViewContainer;
    }

    private void setDialogSize(int width, int height) {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = height;
        window.setAttributes(params);
    }
}
