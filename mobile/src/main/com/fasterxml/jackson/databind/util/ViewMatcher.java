package com.fasterxml.jackson.databind.util;

import java.io.Serializable;
import spacemadness.com.lunarconsole.C1391R;

public class ViewMatcher implements Serializable {
    protected static final ViewMatcher EMPTY;
    private static final long serialVersionUID = 1;

    private static final class Multi extends ViewMatcher implements Serializable {
        private static final long serialVersionUID = 1;
        private final Class<?>[] _views;

        public Multi(Class<?>[] v) {
            this._views = v;
        }

        public boolean isVisibleForView(Class<?> activeView) {
            for (Class<?> view : this._views) {
                if (activeView == view || view.isAssignableFrom(activeView)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class Single extends ViewMatcher {
        private static final long serialVersionUID = 1;
        private final Class<?> _view;

        public Single(Class<?> v) {
            this._view = v;
        }

        public boolean isVisibleForView(Class<?> activeView) {
            return activeView == this._view || this._view.isAssignableFrom(activeView);
        }
    }

    static {
        EMPTY = new ViewMatcher();
    }

    public boolean isVisibleForView(Class<?> cls) {
        return false;
    }

    public static ViewMatcher construct(Class<?>[] views) {
        if (views == null) {
            return EMPTY;
        }
        switch (views.length) {
            case C1391R.styleable.AdsAttrs_adSize /*0*/:
                return EMPTY;
            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return new Single(views[0]);
            default:
                return new Multi(views);
        }
    }
}
