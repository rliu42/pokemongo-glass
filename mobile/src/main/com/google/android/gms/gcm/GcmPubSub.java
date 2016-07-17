package com.google.android.gms.gcm;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;
import java.util.regex.Pattern;

public class GcmPubSub {
    private static GcmPubSub zzaCk;
    private static final Pattern zzaCm;
    private InstanceID zzaCl;

    static {
        zzaCm = Pattern.compile("/topics/[a-zA-Z0-9-_.~%]{1,900}");
    }

    private GcmPubSub(Context context) {
        this.zzaCl = InstanceID.getInstance(context);
    }

    public static synchronized GcmPubSub getInstance(Context context) {
        GcmPubSub gcmPubSub;
        synchronized (GcmPubSub.class) {
            if (zzaCk == null) {
                zzaCk = new GcmPubSub(context);
            }
            gcmPubSub = zzaCk;
        }
        return gcmPubSub;
    }

    public void subscribe(String registrationToken, String topic, Bundle extras) throws IOException {
        if (registrationToken == null || registrationToken.isEmpty()) {
            throw new IllegalArgumentException("Invalid appInstanceToken: " + registrationToken);
        } else if (topic == null || !zzaCm.matcher(topic).matches()) {
            throw new IllegalArgumentException("Invalid topic name: " + topic);
        } else {
            if (extras == null) {
                extras = new Bundle();
            }
            extras.putString("gcm.topic", topic);
            this.zzaCl.getToken(registrationToken, topic, extras);
        }
    }

    public void unsubscribe(String registrationToken, String topic) throws IOException {
        Bundle bundle = new Bundle();
        bundle.putString("gcm.topic", topic);
        this.zzaCl.zzb(registrationToken, topic, bundle);
    }
}
