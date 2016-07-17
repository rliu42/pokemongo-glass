package android.support.v4.media.session;

import android.media.Rating;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompatApi14.Callback;
import com.google.android.gms.ads.AdRequest;

public class MediaSessionCompatApi19 {
    private static final long ACTION_SET_RATING = 128;
    private static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    private static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    private static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";

    static class OnMetadataUpdateListener<T extends Callback> implements android.media.RemoteControlClient.OnMetadataUpdateListener {
        protected final T mCallback;

        public OnMetadataUpdateListener(T callback) {
            this.mCallback = callback;
        }

        public void onMetadataUpdate(int key, Object newValue) {
            if (key == 268435457 && (newValue instanceof Rating)) {
                this.mCallback.onSetRating(newValue);
            }
        }
    }

    public static void setTransportControlFlags(Object rccObj, long actions) {
        ((RemoteControlClient) rccObj).setTransportControlFlags(getRccTransportControlFlagsFromActions(actions));
    }

    public static Object createMetadataUpdateListener(Callback callback) {
        return new OnMetadataUpdateListener(callback);
    }

    public static void setMetadata(Object rccObj, Bundle metadata, long actions) {
        MetadataEditor editor = ((RemoteControlClient) rccObj).editMetadata(true);
        MediaSessionCompatApi14.buildOldMetadata(metadata, editor);
        addNewMetadata(metadata, editor);
        if ((ACTION_SET_RATING & actions) != 0) {
            editor.addEditableKey(268435457);
        }
        editor.apply();
    }

    public static void setOnMetadataUpdateListener(Object rccObj, Object onMetadataUpdateObj) {
        ((RemoteControlClient) rccObj).setMetadataUpdateListener((android.media.RemoteControlClient.OnMetadataUpdateListener) onMetadataUpdateObj);
    }

    static int getRccTransportControlFlagsFromActions(long actions) {
        int transportControlFlags = MediaSessionCompatApi18.getRccTransportControlFlagsFromActions(actions);
        if ((ACTION_SET_RATING & actions) != 0) {
            return transportControlFlags | AdRequest.MAX_CONTENT_URL_LENGTH;
        }
        return transportControlFlags;
    }

    static void addNewMetadata(Bundle metadata, MetadataEditor editor) {
        if (metadata != null) {
            if (metadata.containsKey(METADATA_KEY_YEAR)) {
                editor.putLong(8, metadata.getLong(METADATA_KEY_YEAR));
            }
            if (metadata.containsKey(METADATA_KEY_RATING)) {
                editor.putObject(101, metadata.getParcelable(METADATA_KEY_RATING));
            }
            if (metadata.containsKey(METADATA_KEY_USER_RATING)) {
                editor.putObject(268435457, metadata.getParcelable(METADATA_KEY_USER_RATING));
            }
        }
    }
}
