package com.upsight.android.managedvariables.internal.type;

import com.upsight.android.UpsightException;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import com.upsight.android.managedvariables.internal.type.UxmSchema.BaseSchema;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import com.upsight.android.managedvariables.type.UpsightManagedVariable.Listener;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import com.upsight.android.persistence.annotation.Removed;
import com.upsight.android.persistence.annotation.Updated;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;

public class ManagedVariableManager {
    private static final Map<Class<? extends ManagedVariable>, Class<? extends ManagedVariableModel>> sModelMap;
    private final Map<String, ManagedVariable> mCache;
    private Scheduler mCallbackScheduler;
    private UpsightDataStore mDataStore;
    private UxmSchema mUxmSchema;

    /* renamed from: com.upsight.android.managedvariables.internal.type.ManagedVariableManager.1 */
    static class C09291 extends HashMap<Class<? extends ManagedVariable>, Class<? extends ManagedVariableModel>> {
        C09291() {
            put(UpsightManagedString.class, Model.class);
            put(UpsightManagedBoolean.class, Model.class);
            put(UpsightManagedInt.class, Model.class);
            put(UpsightManagedFloat.class, Model.class);
        }
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.ManagedVariableManager.2 */
    class C09302 implements Action1<T> {
        final /* synthetic */ Listener val$listener;

        C09302(Listener listener) {
            this.val$listener = listener;
        }

        public void call(T managedVariable) {
            this.val$listener.onSuccess(managedVariable);
        }
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.ManagedVariableManager.3 */
    class C09313 implements Action1<ManagedVariableModel> {
        final /* synthetic */ Class val$clazz;
        final /* synthetic */ Listener val$listener;
        final /* synthetic */ String val$tag;

        C09313(String str, Listener listener, Class cls) {
            this.val$tag = str;
            this.val$listener = listener;
            this.val$clazz = cls;
        }

        public void call(ManagedVariableModel model) {
            synchronized (ManagedVariableManager.this.mCache) {
                ManagedVariable cachedVariable = (ManagedVariable) ManagedVariableManager.this.mCache.get(this.val$tag);
                if (cachedVariable != null) {
                    this.val$listener.onSuccess(cachedVariable);
                } else {
                    T managedVariable = ManagedVariableManager.this.fromModel(this.val$clazz, this.val$tag, model);
                    if (managedVariable != null) {
                        ManagedVariableManager.this.mCache.put(this.val$tag, managedVariable);
                        this.val$listener.onSuccess(managedVariable);
                    } else {
                        this.val$listener.onFailure(new UpsightException("Invalid managed variable tag", new Object[0]));
                    }
                }
            }
        }
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.ManagedVariableManager.4 */
    class C09324 implements Action1<Throwable> {
        final /* synthetic */ Listener val$listener;

        C09324(Listener listener) {
            this.val$listener = listener;
        }

        public void call(Throwable throwable) {
            this.val$listener.onFailure(new UpsightException(throwable));
        }
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.ManagedVariableManager.5 */
    class C09335 implements Func1<ManagedVariableModel, Boolean> {
        final /* synthetic */ Class val$clazz;
        final /* synthetic */ String val$tag;

        C09335(Class cls, String str) {
            this.val$clazz = cls;
            this.val$tag = str;
        }

        public Boolean call(ManagedVariableModel model) {
            return Boolean.valueOf(ManagedVariableManager.this.mUxmSchema.get(this.val$clazz, this.val$tag).tag.equals(model.getTag()));
        }
    }

    static {
        sModelMap = new C09291();
    }

    ManagedVariableManager(Scheduler callbackScheduler, UpsightDataStore dataStore, UxmSchema uxmSchema) {
        this.mCache = new HashMap();
        this.mCallbackScheduler = callbackScheduler;
        this.mDataStore = dataStore;
        this.mUxmSchema = uxmSchema;
        dataStore.subscribe(this);
    }

    public <T extends ManagedVariable> T fetch(Class<T> clazz, String tag) {
        T managedVariable;
        synchronized (this.mCache) {
            T cachedVariable = (ManagedVariable) this.mCache.get(tag);
            if (cachedVariable != null) {
                managedVariable = cachedVariable;
            } else {
                managedVariable = fromModel(clazz, tag, (ManagedVariableModel) fetchDataStoreObservable(clazz, tag).toBlocking().first());
                if (managedVariable != null) {
                    this.mCache.put(tag, managedVariable);
                }
            }
        }
        return managedVariable;
    }

    public <T extends ManagedVariable> UpsightSubscription fetch(Class<T> clazz, String tag, Listener<T> listener) {
        UpsightSubscription subscription;
        synchronized (this.mCache) {
            ManagedVariable cachedVariable = (ManagedVariable) this.mCache.get(tag);
            if (cachedVariable != null) {
                subscription = Subscriptions.from(Observable.just(cachedVariable).observeOn(this.mCallbackScheduler).subscribe(new C09302(listener)));
            } else {
                subscription = Subscriptions.from(fetchDataStoreObservable(clazz, tag).subscribe(new C09313(tag, listener, clazz), new C09324(listener)));
            }
        }
        return subscription;
    }

    @Created
    @Updated
    public void handleManagedVariableUpdate(Model model) {
        updateValue(UpsightManagedString.class, model.getTag(), model.getValue());
    }

    @Created
    @Updated
    public void handleManagedVariableUpdate(Model model) {
        updateValue(UpsightManagedBoolean.class, model.getTag(), model.getValue());
    }

    @Created
    @Updated
    public void handleManagedVariableUpdate(Model model) {
        updateValue(UpsightManagedInt.class, model.getTag(), model.getValue());
    }

    @Created
    @Updated
    public void handleManagedVariableUpdate(Model model) {
        updateValue(UpsightManagedFloat.class, model.getTag(), model.getValue());
    }

    @Removed
    public void handleManagedVariableRemoval(Model model) {
        resetValue(UpsightManagedString.class, model.getTag());
    }

    @Removed
    public void handleManagedVariableRemoval(Model model) {
        resetValue(UpsightManagedBoolean.class, model.getTag());
    }

    @Removed
    public void handleManagedVariableRemoval(Model model) {
        resetValue(UpsightManagedInt.class, model.getTag());
    }

    @Removed
    public void handleManagedVariableRemoval(Model model) {
        resetValue(UpsightManagedFloat.class, model.getTag());
    }

    private <T extends ManagedVariable> Observable<? extends ManagedVariableModel> fetchDataStoreObservable(Class<T> clazz, String tag) {
        return this.mDataStore.fetchObservable((Class) sModelMap.get(clazz)).filter(new C09335(clazz, tag)).defaultIfEmpty(null);
    }

    private <T extends ManagedVariable> T fromModel(Class<T> clazz, String tag, ManagedVariableModel model) {
        T t = null;
        T managedVariable = null;
        BaseSchema schemaObject = this.mUxmSchema.get(clazz, tag);
        if (schemaObject == null) {
            return null;
        }
        if (UpsightManagedString.class.equals(clazz)) {
            String defaultValue = schemaObject.defaultValue;
            if (model != null) {
                t = model.getValue();
            }
            managedVariable = new ManagedString(tag, defaultValue, (String) t);
        } else if (UpsightManagedBoolean.class.equals(clazz)) {
            Boolean defaultValue2 = schemaObject.defaultValue;
            if (model != null) {
                t = model.getValue();
            }
            managedVariable = new ManagedBoolean(tag, defaultValue2, (Boolean) t);
        } else if (UpsightManagedInt.class.equals(clazz)) {
            Integer defaultValue3 = schemaObject.defaultValue;
            if (model != null) {
                t = model.getValue();
            }
            managedVariable = new ManagedInt(tag, defaultValue3, (Integer) t);
        } else if (UpsightManagedFloat.class.equals(clazz)) {
            Float defaultValue4 = schemaObject.defaultValue;
            if (model != null) {
                t = model.getValue();
            }
            managedVariable = new ManagedFloat(tag, defaultValue4, (Float) t);
        }
        return managedVariable;
    }

    private <T extends ManagedVariable> void updateValue(Class<T> clazz, String tag, Object value) {
        synchronized (this.mCache) {
            ManagedVariable managedVariable = (ManagedVariable) this.mCache.get(tag);
            if (managedVariable != null && clazz.isInstance(managedVariable)) {
                managedVariable.set(value);
            }
        }
    }

    private <T extends ManagedVariable> void resetValue(Class<T> clazz, String tag) {
        synchronized (this.mCache) {
            ManagedVariable managedVariable = (ManagedVariable) this.mCache.get(tag);
            if (managedVariable != null && clazz.isInstance(managedVariable)) {
                managedVariable.set(null);
            }
        }
    }
}
