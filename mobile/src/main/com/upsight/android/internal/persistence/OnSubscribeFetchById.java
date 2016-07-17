package com.upsight.android.internal.persistence;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.upsight.android.UpsightException;
import com.voxelbusters.nativeplugins.defines.Keys;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

class OnSubscribeFetchById implements OnSubscribe<Storable> {
    private final Context mContext;
    private final String[] mIds;
    private final String mType;

    OnSubscribeFetchById(Context context, String type, String... ids) {
        if (context == null) {
            throw new IllegalArgumentException("Provided Context can not be null.");
        } else if (TextUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Provided type can not be empty or null.");
        } else if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("Object identifiers can not be null or empty.");
        } else {
            this.mContext = context;
            this.mType = type;
            this.mIds = ids;
        }
    }

    public void call(Subscriber<? super Storable> subscriber) {
        StringBuffer where = new StringBuffer();
        where.append("_id").append(" IN (");
        for (int i = 0; i < this.mIds.length; i++) {
            where.append("?");
            if (i < this.mIds.length - 1) {
                where.append(",");
            }
        }
        where.append(")");
        Cursor cursor = null;
        try {
            cursor = this.mContext.getContentResolver().query(Content.getContentUri(this.mContext), null, where.toString(), this.mIds, null);
            if (cursor == null) {
                subscriber.onError(new UpsightException("Unable to retrieve stored objects.", new Object[0]));
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor.getCount() != this.mIds.length) {
                subscriber.onError(new UpsightException("Unable to retrieve stored objects. Some ID(s) were not found.", new Object[0]));
                if (cursor != null) {
                    cursor.close();
                }
            } else {
                while (cursor.moveToNext()) {
                    subscriber.onNext(Storable.create(cursor.getString(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex(Keys.TYPE)), cursor.getString(cursor.getColumnIndex(ModelColumns.DATA))));
                }
                subscriber.onCompleted();
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
