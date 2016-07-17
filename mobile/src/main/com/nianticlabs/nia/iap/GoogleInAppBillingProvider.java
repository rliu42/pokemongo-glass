package com.nianticlabs.nia.iap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.os.EnvironmentCompat;
import com.android.vending.billing.IInAppBillingService;
import com.android.vending.billing.IInAppBillingService.Stub;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.iap.InAppBillingProvider.Delegate;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GoogleInAppBillingProvider implements InAppBillingProvider {
    private static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    private static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    private static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    private static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    private static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
    private static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    private static final int BILLING_RESPONSE_RESULT_NOT_FOUND = 1000;
    private static final int BILLING_RESPONSE_RESULT_OK = 0;
    private static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    private static final int BILLING_SERVICE_VERSION = 3;
    static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    private static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    private static final String ITEM_TYPE_INAPP = "inapp";
    private static final String PACKAGE_NAME_BASE = "com.niantic";
    private static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    private static final String RESPONSE_CODE = "RESPONSE_CODE";
    private static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    private static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    private static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    private static final String UNKNOWN_CURRENCY_STRING = "UNKNOWN";
    private static WeakReference<GoogleInAppBillingProvider> instance;
    private static final Logger log;
    private IInAppBillingService billingService;
    private boolean clientConnected;
    private boolean connectionInProgress;
    private final Context context;
    private Map<String, GetSkuDetailsResponseItem> currentPurchasableItems;
    private Delegate delegate;
    private String itemBeingPurchased;
    private final String packageName;
    private PendingIntent pendingIntent;
    private boolean purchaseSupported;
    private ServiceConnection serviceConnection;
    private int transactionsInProgress;

    /* renamed from: com.nianticlabs.nia.iap.GoogleInAppBillingProvider.1 */
    class C07541 implements ServiceConnection {

        /* renamed from: com.nianticlabs.nia.iap.GoogleInAppBillingProvider.1.1 */
        class C07521 implements Runnable {
            final /* synthetic */ IBinder val$service;

            C07521(IBinder iBinder) {
                this.val$service = iBinder;
            }

            public void run() {
                if (GoogleInAppBillingProvider.this.serviceConnection == null) {
                    GoogleInAppBillingProvider.this.finalizeConnectionResult();
                    return;
                }
                GoogleInAppBillingProvider.this.billingService = Stub.asInterface(this.val$service);
                try {
                    boolean z;
                    int response = GoogleInAppBillingProvider.this.billingService.isBillingSupported(GoogleInAppBillingProvider.BILLING_SERVICE_VERSION, GoogleInAppBillingProvider.this.packageName, GoogleInAppBillingProvider.ITEM_TYPE_INAPP);
                    GoogleInAppBillingProvider googleInAppBillingProvider = GoogleInAppBillingProvider.this;
                    if (response == 0) {
                        z = true;
                    } else {
                        z = GoogleInAppBillingProvider.ENABLE_VERBOSE_LOGS;
                    }
                    googleInAppBillingProvider.purchaseSupported = z;
                } catch (RemoteException e) {
                    GoogleInAppBillingProvider.this.purchaseSupported = GoogleInAppBillingProvider.ENABLE_VERBOSE_LOGS;
                }
                if (GoogleInAppBillingProvider.this.currentPurchasableItems.size() > 0) {
                    new ProcessPurchasedItemsTask().execute(new Void[GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK]);
                } else {
                    GoogleInAppBillingProvider.this.finalizeConnectionResult();
                }
            }
        }

        /* renamed from: com.nianticlabs.nia.iap.GoogleInAppBillingProvider.1.2 */
        class C07532 implements Runnable {
            C07532() {
            }

            public void run() {
                GoogleInAppBillingProvider.this.billingService = null;
                GoogleInAppBillingProvider.this.finalizeConnectionResult();
            }
        }

        C07541() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            ContextService.runOnServiceHandler(new C07521(service));
        }

        public void onServiceDisconnected(ComponentName name) {
            ContextService.runOnServiceHandler(new C07532());
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.GoogleInAppBillingProvider.2 */
    class C07552 implements Runnable {
        C07552() {
        }

        public void run() {
            GoogleInAppBillingProvider.this.context.startActivity(new Intent(GoogleInAppBillingProvider.this.context, PurchaseActivity.class));
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.GoogleInAppBillingProvider.3 */
    class C07563 implements Runnable {
        final /* synthetic */ String val$dataSignature;
        final /* synthetic */ String val$purchaseData;
        final /* synthetic */ int val$responseCode;
        final /* synthetic */ int val$resultCode;

        C07563(int i, int i2, String str, String str2) {
            this.val$resultCode = i;
            this.val$responseCode = i2;
            this.val$purchaseData = str;
            this.val$dataSignature = str2;
        }

        public void run() {
            GoogleInAppBillingProvider.this.processPurchaseResult(this.val$resultCode, this.val$responseCode, this.val$purchaseData, this.val$dataSignature);
        }
    }

    private class ConsumeItemTask extends AsyncTask<Void, Void, Integer> {
        private final IInAppBillingService billingService;
        private final String purchaseToken;

        public ConsumeItemTask(String purchaseToken) {
            this.purchaseToken = purchaseToken;
            this.billingService = GoogleInAppBillingProvider.this.billingService;
        }

        protected Integer doInBackground(Void... params) {
            Integer num = null;
            if (this.billingService != null) {
                try {
                    num = Integer.valueOf(this.billingService.consumePurchase(GoogleInAppBillingProvider.BILLING_SERVICE_VERSION, GoogleInAppBillingProvider.this.packageName, this.purchaseToken));
                } catch (RemoteException e) {
                }
            }
            return num;
        }

        protected void onPostExecute(Integer result) {
            if (result == null || result.intValue() != 0) {
                GoogleInAppBillingProvider.this.finalizePurchaseResult(PurchaseResult.FAILURE);
            } else {
                GoogleInAppBillingProvider.this.finalizePurchaseResult(PurchaseResult.SUCCESS);
            }
        }
    }

    private class GetSkuDetailsTask extends AsyncTask<Void, Void, Bundle> {
        private final IInAppBillingService billingService;
        private final Bundle requestBundle;

        public GetSkuDetailsTask(ArrayList<String> skuIds) {
            this.billingService = GoogleInAppBillingProvider.this.billingService;
            this.requestBundle = new Bundle();
            this.requestBundle.putStringArrayList(GoogleInAppBillingProvider.GET_SKU_DETAILS_ITEM_LIST, skuIds);
        }

        protected Bundle doInBackground(Void... params) {
            Bundle bundle = null;
            if (this.billingService != null) {
                try {
                    bundle = this.billingService.getSkuDetails(GoogleInAppBillingProvider.BILLING_SERVICE_VERSION, GoogleInAppBillingProvider.this.packageName, GoogleInAppBillingProvider.ITEM_TYPE_INAPP, this.requestBundle);
                } catch (RemoteException e) {
                }
            }
            return bundle;
        }

        protected void onPostExecute(Bundle result) {
            ArrayList<PurchasableItemDetails> purchasableItems = new ArrayList();
            GoogleInAppBillingProvider.this.currentPurchasableItems.clear();
            if (result != null && result.containsKey(GoogleInAppBillingProvider.RESPONSE_GET_SKU_DETAILS_LIST)) {
                Iterator it = result.getStringArrayList(GoogleInAppBillingProvider.RESPONSE_GET_SKU_DETAILS_LIST).iterator();
                while (it.hasNext()) {
                    GetSkuDetailsResponseItem jsonItem = GetSkuDetailsResponseItem.fromJson((String) it.next());
                    if (jsonItem != null) {
                        PurchasableItemDetails item = GetSkuDetailsResponseItem.toPurchasableItemDetails(jsonItem);
                        purchasableItems.add(item);
                        GoogleInAppBillingProvider.this.currentPurchasableItems.put(item.getItemId(), jsonItem);
                    }
                }
            }
            GoogleInAppBillingProvider.this.notifyPurchasableItemsResult(purchasableItems);
            new ProcessPurchasedItemsTask().execute(new Void[GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK]);
        }
    }

    static class Logger {
        private final String tag;

        public Logger(Class className) {
            this.tag = className.toString();
        }

        void warning(String format, Object... objects) {
        }

        void error(String format, Object... objects) {
        }

        void severe(String format, Object... objects) {
        }

        void dev(String format, Object... objects) {
        }

        void assertOnServiceThread(String message) {
            if (!ContextService.onServiceThread()) {
                severe(this.tag + ": Must be on the service thread: " + message, new Object[GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK]);
            }
        }
    }

    private class ProcessPurchasedItemsTask extends AsyncTask<Void, Void, Bundle> {
        private final IInAppBillingService billingService;

        /* renamed from: com.nianticlabs.nia.iap.GoogleInAppBillingProvider.ProcessPurchasedItemsTask.1 */
        class C07571 implements Runnable {
            final /* synthetic */ Bundle val$result;

            C07571(Bundle bundle) {
                this.val$result = bundle;
            }

            public void run() {
                ArrayList<String> purchaseDataList = this.val$result.getStringArrayList(GoogleInAppBillingProvider.RESPONSE_INAPP_PURCHASE_DATA_LIST);
                ArrayList<String> signatureList = this.val$result.getStringArrayList(GoogleInAppBillingProvider.RESPONSE_INAPP_SIGNATURE_LIST);
                for (int i = GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK; i < purchaseDataList.size(); i += GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_USER_CANCELED) {
                    GoogleInAppBillingProvider.this.transactionsInProgress = GoogleInAppBillingProvider.this.transactionsInProgress + GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_USER_CANCELED;
                    GoogleInAppBillingProvider.this.processPurchaseResult(-1, GoogleInAppBillingProvider.BILLING_RESPONSE_RESULT_OK, (String) purchaseDataList.get(i), (String) signatureList.get(i));
                }
                GoogleInAppBillingProvider.this.finalizeConnectionResult();
                GoogleInAppBillingProvider.this.maybeDisconnectBillingService();
            }
        }

        public ProcessPurchasedItemsTask() {
            this.billingService = GoogleInAppBillingProvider.this.billingService;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected android.os.Bundle doInBackground(java.lang.Void... r13) {
            /*
            r12 = this;
            r0 = 0;
            r1 = 0;
            r2 = 0;
        L_0x0003:
            r8 = r12.billingService;	 Catch:{ RemoteException -> 0x005f }
            r9 = 3;
            r10 = com.nianticlabs.nia.iap.GoogleInAppBillingProvider.this;	 Catch:{ RemoteException -> 0x005f }
            r10 = r10.packageName;	 Catch:{ RemoteException -> 0x005f }
            r11 = "inapp";
            r3 = r8.getPurchases(r9, r10, r11, r2);	 Catch:{ RemoteException -> 0x005f }
            r5 = com.nianticlabs.nia.iap.GoogleInAppBillingProvider.getResponseCodeFromBundle(r3);	 Catch:{ RemoteException -> 0x005f }
            r8 = "INAPP_PURCHASE_DATA_LIST";
            r4 = r3.getStringArrayList(r8);	 Catch:{ RemoteException -> 0x005f }
            r8 = "INAPP_DATA_SIGNATURE_LIST";
            r7 = r3.getStringArrayList(r8);	 Catch:{ RemoteException -> 0x005f }
            r8 = 5;
            if (r5 != r8) goto L_0x0029;
        L_0x0025:
            if (r0 != 0) goto L_0x0061;
        L_0x0027:
            r6 = 0;
        L_0x0028:
            return r6;
        L_0x0029:
            if (r5 != 0) goto L_0x0025;
        L_0x002b:
            r8 = "INAPP_PURCHASE_DATA_LIST";
            r8 = r3.containsKey(r8);	 Catch:{ RemoteException -> 0x005f }
            if (r8 == 0) goto L_0x0025;
        L_0x0033:
            r8 = "INAPP_DATA_SIGNATURE_LIST";
            r8 = r3.containsKey(r8);	 Catch:{ RemoteException -> 0x005f }
            if (r8 == 0) goto L_0x0025;
        L_0x003b:
            r8 = r4.size();	 Catch:{ RemoteException -> 0x005f }
            r9 = r7.size();	 Catch:{ RemoteException -> 0x005f }
            if (r8 != r9) goto L_0x0025;
        L_0x0045:
            if (r0 != 0) goto L_0x0058;
        L_0x0047:
            r0 = r4;
            r1 = r7;
        L_0x0049:
            r8 = "INAPP_CONTINUATION_TOKEN";
            r2 = r3.getString(r8);	 Catch:{ RemoteException -> 0x005f }
            if (r2 == 0) goto L_0x0025;
        L_0x0051:
            r8 = r2.length();	 Catch:{ RemoteException -> 0x005f }
            if (r8 != 0) goto L_0x0003;
        L_0x0057:
            goto L_0x0025;
        L_0x0058:
            r0.addAll(r4);	 Catch:{ RemoteException -> 0x005f }
            r1.addAll(r7);	 Catch:{ RemoteException -> 0x005f }
            goto L_0x0049;
        L_0x005f:
            r8 = move-exception;
            goto L_0x0025;
        L_0x0061:
            r6 = new android.os.Bundle;
            r6.<init>();
            r8 = "INAPP_PURCHASE_DATA_LIST";
            r6.putStringArrayList(r8, r0);
            r8 = "INAPP_DATA_SIGNATURE_LIST";
            r6.putStringArrayList(r8, r1);
            goto L_0x0028;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nianticlabs.nia.iap.GoogleInAppBillingProvider.ProcessPurchasedItemsTask.doInBackground(java.lang.Void[]):android.os.Bundle");
        }

        protected void onPostExecute(Bundle result) {
            if (result != null) {
                ContextService.runOnServiceHandler(new C07571(result));
                return;
            }
            GoogleInAppBillingProvider.this.finalizeConnectionResult();
            GoogleInAppBillingProvider.this.maybeDisconnectBillingService();
        }
    }

    static {
        instance = null;
        log = new Logger(GoogleInAppBillingProvider.class);
    }

    public static WeakReference<GoogleInAppBillingProvider> getInstance() {
        return instance;
    }

    public GoogleInAppBillingProvider(Context context) {
        this.serviceConnection = null;
        this.billingService = null;
        this.purchaseSupported = ENABLE_VERBOSE_LOGS;
        this.transactionsInProgress = BILLING_RESPONSE_RESULT_OK;
        this.connectionInProgress = ENABLE_VERBOSE_LOGS;
        this.clientConnected = ENABLE_VERBOSE_LOGS;
        this.itemBeingPurchased = null;
        String checkedPackageName = context.getPackageName();
        if (checkedPackageName.startsWith(PACKAGE_NAME_BASE)) {
            this.packageName = checkedPackageName;
        } else {
            this.packageName = "ERROR";
        }
        this.context = context;
        this.currentPurchasableItems = new HashMap();
        instance = new WeakReference(this);
        connectToBillingService();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void onResume() {
        this.clientConnected = true;
        connectToBillingService();
    }

    public void onPause() {
        this.clientConnected = ENABLE_VERBOSE_LOGS;
        maybeDisconnectBillingService();
    }

    public boolean isBillingAvailable() {
        return (this.billingService == null || !this.purchaseSupported) ? ENABLE_VERBOSE_LOGS : true;
    }

    public boolean isTransactionInProgress() {
        return this.transactionsInProgress > 0 ? true : ENABLE_VERBOSE_LOGS;
    }

    private void connectToBillingService() {
        if (!this.connectionInProgress) {
            if (this.billingService != null) {
                finalizeConnectionResult();
                return;
            }
            this.connectionInProgress = true;
            this.serviceConnection = new C07541();
            Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
            if (this.context.getPackageManager().queryIntentServices(serviceIntent, BILLING_RESPONSE_RESULT_OK).isEmpty()) {
                finalizeConnectionResult();
            }
            this.context.bindService(serviceIntent, this.serviceConnection, BILLING_RESPONSE_RESULT_USER_CANCELED);
        }
    }

    private void maybeDisconnectBillingService() {
        if (this.transactionsInProgress <= 0 && !this.connectionInProgress && !this.clientConnected) {
            if (this.serviceConnection != null) {
                this.context.unbindService(this.serviceConnection);
            }
            this.serviceConnection = null;
            this.billingService = null;
            this.transactionsInProgress = BILLING_RESPONSE_RESULT_OK;
        }
    }

    public void getPurchasableItems(ArrayList<String> itemIds) {
        if (isBillingAvailable()) {
            new GetSkuDetailsTask(itemIds).execute(new Void[BILLING_RESPONSE_RESULT_OK]);
        } else {
            notifyPurchasableItemsResult(Collections.emptyList());
        }
    }

    public void purchaseItem(String itemId, String developerPayload) {
        this.transactionsInProgress += BILLING_RESPONSE_RESULT_USER_CANCELED;
        if (!isBillingAvailable()) {
            finalizePurchaseResult(PurchaseResult.BILLING_UNAVAILABLE);
        } else if (this.currentPurchasableItems.keySet().contains(itemId)) {
            try {
                Bundle buyIntentBundle = this.billingService.getBuyIntent(BILLING_SERVICE_VERSION, this.packageName, itemId, ITEM_TYPE_INAPP, developerPayload);
                PendingIntent pendingIntent = (PendingIntent) buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
                if (!handlePurchaseErrorResult(getResponseCodeFromBundle(buyIntentBundle))) {
                    if (pendingIntent == null) {
                        finalizePurchaseResult(PurchaseResult.FAILURE);
                        return;
                    }
                    if (this.transactionsInProgress == BILLING_RESPONSE_RESULT_USER_CANCELED) {
                        this.itemBeingPurchased = itemId;
                    } else {
                        this.itemBeingPurchased = null;
                    }
                    launchPurchaseActivity(pendingIntent);
                }
            } catch (RemoteException e) {
                finalizePurchaseResult(PurchaseResult.FAILURE);
            }
        } else {
            finalizePurchaseResult(PurchaseResult.SKU_NOT_AVAILABLE);
        }
    }

    private void launchPurchaseActivity(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
        ContextService.runOnUiThread(new C07552());
    }

    public void startBuyIntent(Activity activity) {
        try {
            activity.startIntentSenderForResult(this.pendingIntent.getIntentSender(), PurchaseActivity.REQUEST_PURCHASE_RESULT, new Intent(), BILLING_RESPONSE_RESULT_OK, BILLING_RESPONSE_RESULT_OK, BILLING_RESPONSE_RESULT_OK);
        } catch (SendIntentException e) {
            this.itemBeingPurchased = null;
            this.pendingIntent = null;
            finalizePurchaseResult(PurchaseResult.FAILURE);
        }
    }

    public void forwardedOnActivityResult(int resultCode, Intent data) {
        int responseCode;
        String purchaseData;
        String dataSignature;
        if (data != null) {
            responseCode = getResponseCodeFromIntent(data);
            purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
            dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);
        } else {
            responseCode = BILLING_RESPONSE_RESULT_NOT_FOUND;
            purchaseData = null;
            dataSignature = null;
        }
        ContextService.runOnServiceHandler(new C07563(resultCode, responseCode, purchaseData, dataSignature));
    }

    private void processPurchaseResult(int activityResultCode, int responseCode, String purchaseData, String dataSignature) {
        String purchasedItem = this.itemBeingPurchased;
        this.itemBeingPurchased = null;
        if (this.billingService != null) {
            if (responseCode != BILLING_RESPONSE_RESULT_NOT_FOUND && handlePurchaseErrorResult(responseCode)) {
                return;
            }
            if (activityResultCode == 0) {
                finalizePurchaseResult(PurchaseResult.USER_CANCELLED);
            } else if (activityResultCode != -1) {
                finalizePurchaseResult(PurchaseResult.FAILURE);
            } else if (responseCode == BILLING_RESPONSE_RESULT_NOT_FOUND || purchaseData == null || dataSignature == null) {
                finalizePurchaseResult(PurchaseResult.FAILURE);
            } else {
                String currency = UNKNOWN_CURRENCY_STRING;
                String productId = null;
                int pricePaidE6 = BILLING_RESPONSE_RESULT_OK;
                if (purchasedItem != null) {
                    GetSkuDetailsResponseItem itemDetails = (GetSkuDetailsResponseItem) this.currentPurchasableItems.get(purchasedItem);
                    if (itemDetails != null) {
                        productId = itemDetails.getProductId();
                        currency = itemDetails.getCurrencyCode();
                        pricePaidE6 = itemDetails.getPriceE6();
                    }
                }
                if (productId == null) {
                    GoogleInAppPurchaseData parsedPurchaseData = GoogleInAppPurchaseData.fromJson(purchaseData);
                    if (parsedPurchaseData != null) {
                        productId = parsedPurchaseData.getProductId();
                    }
                    if (productId == null) {
                        productId = EnvironmentCompat.MEDIA_UNKNOWN;
                    }
                }
                this.delegate.ProcessReceipt(purchaseData, dataSignature, currency, pricePaidE6);
            }
        }
    }

    public void onProcessedGoogleBillingTransaction(boolean success, String purchaseToken) {
        if (!success) {
            finalizePurchaseResult(PurchaseResult.FAILURE);
        } else if (this.billingService == null) {
            finalizePurchaseResult(PurchaseResult.FAILURE);
        } else if (purchaseToken == null) {
            finalizePurchaseResult(PurchaseResult.FAILURE);
        } else {
            new ConsumeItemTask(purchaseToken).execute(new Void[BILLING_RESPONSE_RESULT_OK]);
        }
    }

    private boolean handlePurchaseErrorResult(int resultCode) {
        switch (resultCode) {
            case BILLING_RESPONSE_RESULT_OK /*0*/:
                return ENABLE_VERBOSE_LOGS;
            case BILLING_RESPONSE_RESULT_USER_CANCELED /*1*/:
                finalizePurchaseResult(PurchaseResult.USER_CANCELLED);
                break;
            case BILLING_SERVICE_VERSION /*3*/:
                finalizePurchaseResult(PurchaseResult.BILLING_UNAVAILABLE);
                break;
            case BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE /*4*/:
                finalizePurchaseResult(PurchaseResult.SKU_NOT_AVAILABLE);
                break;
            case BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED /*7*/:
                new ProcessPurchasedItemsTask().execute(new Void[BILLING_RESPONSE_RESULT_OK]);
                finalizePurchaseResult(PurchaseResult.FAILURE);
                break;
            default:
                finalizePurchaseResult(PurchaseResult.FAILURE);
                break;
        }
        return true;
    }

    private void finalizeConnectionResult() {
        boolean z = ENABLE_VERBOSE_LOGS;
        this.connectionInProgress = ENABLE_VERBOSE_LOGS;
        if (this.delegate != null) {
            Delegate delegate = this.delegate;
            if (this.billingService != null) {
                z = true;
            }
            delegate.onConnectionStateChanged(z);
        }
    }

    private void notifyPurchasableItemsResult(Collection<PurchasableItemDetails> purchasableItems) {
        if (this.delegate != null) {
            this.delegate.purchasableItemsResult(purchasableItems);
        }
    }

    private void finalizePurchaseResult(PurchaseResult result) {
        this.transactionsInProgress--;
        maybeDisconnectBillingService();
        if (this.delegate != null) {
            this.delegate.purchaseResult(result);
        }
    }

    private static int getResponseCodeFromBundle(Bundle bundle) {
        return getResponseCodeFromObject(bundle.get(RESPONSE_CODE));
    }

    private static int getResponseCodeFromIntent(Intent intent) {
        return getResponseCodeFromObject(intent.getExtras().get(RESPONSE_CODE));
    }

    private static int getResponseCodeFromObject(Object responseObject) {
        if (responseObject == null) {
            return BILLING_RESPONSE_RESULT_OK;
        }
        if (responseObject instanceof Integer) {
            return ((Integer) responseObject).intValue();
        }
        if (responseObject instanceof Long) {
            return (int) ((Long) responseObject).longValue();
        }
        return BILLING_RESPONSE_RESULT_ERROR;
    }
}
