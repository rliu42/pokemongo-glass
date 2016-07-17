package com.upsight.android.internal.persistence;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.upsight.android.UpsightException;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.lang.ref.WeakReference;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

class OnSubscribeFetchByType implements OnSubscribe<Storable> {
    private final WeakReference<Context> reference;
    private final String type;

    OnSubscribeFetchByType(Context context, String type) {
        if (context == null) {
            throw new IllegalArgumentException("Provided Context can not be null.");
        } else if (TextUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Provided type can not be empty or null.");
        } else {
            this.reference = new WeakReference(context);
            this.type = type;
        }
    }

    public void call(Subscriber<? super Storable> subscriber) {
        Context context = (Context) this.reference.get();
        if (context == null) {
            subscriber.onError(new IllegalStateException("Context has been reclaimed by Android."));
            return;
        }
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Content.getContentTypeUri(context, this.type), null, null, null, null);
            if (cursor == null) {
                subscriber.onError(new UpsightException("Unable to retrieve stored objects.", new Object[0]));
                if (cursor != null) {
                    cursor.close();
                    return;
                }
                return;
            }
            while (cursor.moveToNext()) {
                subscriber.onNext(Storable.create(cursor.getString(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex(Keys.TYPE)), cursor.getString(cursor.getColumnIndex(ModelColumns.DATA))));
            }
            subscriber.onCompleted();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
