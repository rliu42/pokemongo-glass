package com.nianticlabs.nia.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nianticlabs.nia.account.NianticAccountManager.Status;
import java.io.IOException;
import java.lang.ref.WeakReference;
import spacemadness.com.lunarconsole.BuildConfig;

public class AccountsActivity extends Activity {
    static final String AUTH_TOKEN_SCOPE_PREFIX = "audience:server:client_id:";
    static String EXTRA_OAUTH_CLIENT_ID = null;
    private static final int REQUEST_CHOOSE_ACCOUNT = 1;
    private static final int REQUEST_GET_AUTH = 2;
    private static String TAG;
    private NianticAccountManager accountManager;
    private boolean authInProgress;

    /* renamed from: com.nianticlabs.nia.account.AccountsActivity.1 */
    class C07411 extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ String val$accountName;

        C07411(String str) {
            this.val$accountName = str;
        }

        protected Void doInBackground(Void... params) {
            AccountsActivity.getAuthTokenBlocking(AccountsActivity.this, this.val$accountName);
            return null;
        }
    }

    /* renamed from: com.nianticlabs.nia.account.AccountsActivity.2 */
    class C07422 implements Runnable {
        final /* synthetic */ UserRecoverableAuthException val$e;

        C07422(UserRecoverableAuthException userRecoverableAuthException) {
            this.val$e = userRecoverableAuthException;
        }

        public void run() {
            AccountsActivity.this.startActivityForResult(this.val$e.getIntent(), AccountsActivity.REQUEST_GET_AUTH);
        }
    }

    /* renamed from: com.nianticlabs.nia.account.AccountsActivity.3 */
    class C07433 implements Runnable {
        C07433() {
        }

        public void run() {
            AccountsActivity.this.finish();
        }
    }

    public AccountsActivity() {
        this.authInProgress = false;
    }

    static {
        TAG = "AccountsActivity";
        EXTRA_OAUTH_CLIENT_ID = "oauthClientId";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("accounts_activity", "layout", getPackageName()));
        this.accountManager = null;
        WeakReference<NianticAccountManager> weakAccountManager = NianticAccountManager.getInstance();
        if (weakAccountManager != null) {
            this.accountManager = (NianticAccountManager) weakAccountManager.get();
        }
        if (this.accountManager == null) {
            throw new RuntimeException("Unable to locate NianticAccountManager");
        }
    }

    protected void onResume() {
        super.onResume();
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != 0) {
            Log.e(TAG, "Google Play Services not available, need to do something. Error code: " + resultCode);
            this.accountManager.setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR);
            finish();
        } else if (!this.authInProgress) {
            this.authInProgress = true;
            getAuthOrAccount();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String error = "Unexpected requestCode " + requestCode;
        switch (requestCode) {
            case REQUEST_CHOOSE_ACCOUNT /*1*/:
                if (resultCode == 0) {
                    failAuth(Status.USER_CANCELED_LOGIN, "User decided to cancel account selection.");
                } else if (data == null) {
                    failAuth(Status.NON_RECOVERABLE_ERROR, "Attempt to choose null account, resultCode: " + resultCode);
                } else {
                    String accountName = data.getStringExtra("authAccount");
                    if (accountName == null || BuildConfig.FLAVOR.equals(accountName)) {
                        failAuth(Status.NON_RECOVERABLE_ERROR, "Attempt to choose unnamed account, resultCode: " + resultCode);
                        return;
                    }
                    this.accountManager.setAccountName(accountName);
                    getAuth(accountName);
                }
            case REQUEST_GET_AUTH /*2*/:
                getAuthOrAccount();
            default:
                Log.e(TAG, error);
        }
    }

    private void getAuthOrAccount() {
        String accountName = this.accountManager.getAccountName();
        if (accountName != null) {
            getAuth(accountName);
            return;
        }
        String[] strArr = new String[REQUEST_CHOOSE_ACCOUNT];
        strArr[0] = GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE;
        startActivityForResult(AccountPicker.newChooseAccountIntent(null, null, strArr, false, null, null, null, null), REQUEST_CHOOSE_ACCOUNT);
    }

    private void getAuth(String accountName) {
        new C07411(accountName).execute(new Void[0]);
    }

    private static void getAuthTokenBlocking(AccountsActivity activity, String accountName) {
        try {
            Log.d(TAG, "Authenticating with account: " + accountName);
            String clientId = activity.getIntent().getStringExtra(EXTRA_OAUTH_CLIENT_ID);
            Log.i(TAG, "Authenticating with client id: " + clientId);
            String scope = AUTH_TOKEN_SCOPE_PREFIX + clientId;
            Log.i(TAG, "Authenticating with scope: " + scope);
            activity.accountManager.setAuthToken(Status.OK, GoogleAuthUtil.getToken((Context) activity, accountName, scope));
            activity.postFinish();
        } catch (UserRecoverableAuthException userAuthEx) {
            activity.askUserToRecover(userAuthEx);
        } catch (IOException transientEx) {
            Log.e(TAG, "Unable to get authToken at this time.", transientEx);
            activity.accountManager.setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR);
            activity.postFinish();
        } catch (GoogleAuthException authEx) {
            Log.e(TAG, "User cannot be authenticated.", authEx);
            activity.accountManager.setAuthToken(Status.NON_RECOVERABLE_ERROR, BuildConfig.FLAVOR);
            activity.postFinish();
        }
    }

    private void failAuth(Status status, String error) {
        Log.e(TAG, error);
        this.accountManager.setAuthToken(status, BuildConfig.FLAVOR);
        finish();
    }

    private void askUserToRecover(UserRecoverableAuthException e) {
        runOnUiThread(new C07422(e));
    }

    private void postFinish() {
        runOnUiThread(new C07433());
    }
}
