package com.nianticlabs.nia.iap;

import android.content.Context;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.iap.InAppBillingProvider.Delegate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NianticBillingManager extends ContextService implements Delegate {
    private InAppBillingProvider inAppBillingProvider;
    private boolean initializing;

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.1 */
    class C07581 implements Runnable {
        C07581() {
        }

        public void run() {
            NianticBillingManager.this.nativeInitializeCallback();
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.2 */
    class C07592 implements Runnable {
        final /* synthetic */ ArrayList val$items;

        C07592(ArrayList arrayList) {
            this.val$items = arrayList;
        }

        public void run() {
            NianticBillingManager.this.inAppBillingProvider.getPurchasableItems(this.val$items);
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.3 */
    class C07603 implements Runnable {
        final /* synthetic */ String val$item;
        final /* synthetic */ String val$userIdToken;

        C07603(String str, String str2) {
            this.val$item = str;
            this.val$userIdToken = str2;
        }

        public void run() {
            NianticBillingManager.this.inAppBillingProvider.purchaseItem(this.val$item, this.val$userIdToken);
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.4 */
    class C07614 implements Runnable {
        final /* synthetic */ boolean val$success;
        final /* synthetic */ String val$transactionToken;

        C07614(boolean z, String str) {
            this.val$success = z;
            this.val$transactionToken = str;
        }

        public void run() {
            NianticBillingManager.this.inAppBillingProvider.onProcessedGoogleBillingTransaction(this.val$success, this.val$transactionToken);
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.5 */
    class C07625 implements Runnable {
        final /* synthetic */ boolean val$connected;

        C07625(boolean z) {
            this.val$connected = z;
        }

        public void run() {
            synchronized (NianticBillingManager.this.callbackLock) {
                NianticBillingManager.this.nativeOnConnectionStateChanged(this.val$connected);
            }
        }
    }

    private native void nativeInitializeCallback();

    private native void nativeOnConnectionStateChanged(boolean z);

    private native void nativeProcessReceipt(String str, String str2, String str3, int i);

    private native void nativePurchasableItemsResult(PurchasableItemDetails[] purchasableItemDetailsArr);

    private native void nativePurchaseResult(int i);

    private native void nativeRecordPurchase(boolean z, String str, int i, float f, String str2, String str3);

    public NianticBillingManager(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.inAppBillingProvider = new GoogleInAppBillingProvider(context);
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onPause() {
        this.inAppBillingProvider.onPause();
    }

    public void onResume() {
        this.inAppBillingProvider.onResume();
    }

    public boolean isBillingAvailable() {
        return this.inAppBillingProvider.isBillingAvailable();
    }

    public void initialize() {
        this.initializing = true;
        this.inAppBillingProvider.setDelegate(this);
        ContextService.runOnServiceHandler(new C07581());
    }

    public boolean isTransactionInProgress() {
        return this.inAppBillingProvider.isTransactionInProgress();
    }

    public void getPurchasableItems(String[] purchasableItems) {
        ContextService.runOnServiceHandler(new C07592(new ArrayList(Arrays.asList(purchasableItems))));
    }

    public void purchaseVendorItem(String item, String userIdToken) {
        ContextService.runOnServiceHandler(new C07603(item, userIdToken));
    }

    public void redeemReceiptResult(boolean success, String transactionToken) {
        ContextService.runOnServiceHandler(new C07614(success, transactionToken));
    }

    public void onConnectionStateChanged(boolean connected) {
        ContextService.runOnServiceHandler(new C07625(connected));
    }

    public void purchasableItemsResult(Collection<PurchasableItemDetails> items) {
        PurchasableItemDetails[] item_array = new PurchasableItemDetails[items.size()];
        int index = 0;
        for (PurchasableItemDetails item : items) {
            int index2 = index + 1;
            item_array[index] = item;
            index = index2;
        }
        synchronized (this.callbackLock) {
            nativePurchasableItemsResult(item_array);
        }
    }

    public void purchaseResult(PurchaseResult result) {
        synchronized (this.callbackLock) {
            nativePurchaseResult(result.ordinal());
        }
    }

    public void ProcessReceipt(String receiptBase64, String receiptSignature, String currency, int priceE6) {
        synchronized (this.callbackLock) {
            nativeProcessReceipt(receiptBase64, receiptSignature, currency, priceE6);
        }
    }
}
