package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;
import java.util.concurrent.TimeUnit;

public final class BatchResult implements Result {
    private final Status zzSC;
    private final PendingResult<?>[] zzaaB;

    BatchResult(Status status, PendingResult<?>[] pendingResults) {
        this.zzSC = status;
        this.zzaaB = pendingResults;
    }

    public Status getStatus() {
        return this.zzSC;
    }

    public <R extends Result> R take(BatchResultToken<R> resultToken) {
        zzx.zzb(resultToken.mId < this.zzaaB.length, (Object) "The result token does not belong to this batch");
        return this.zzaaB[resultToken.mId].await(0, TimeUnit.MILLISECONDS);
    }
}
