package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.otto.Bus;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.UpsightManagedVariablesExtension;
import com.upsight.android.analytics.event.uxm.UpsightUxmEnumerateEvent;
import com.upsight.android.analytics.internal.action.Action;
import com.upsight.android.analytics.internal.action.ActionContext;
import com.upsight.android.analytics.internal.action.ActionFactory;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.UpsightManagedVariablesComponent;
import com.upsight.android.persistence.UpsightDataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import rx.Observable;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public final class UxmContentActions {
    private static final Map<String, InternalFactory> FACTORY_MAP;

    private interface InternalFactory {
        Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonNode jsonNode);
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1 */
    static class C09401 extends HashMap<String, InternalFactory> {

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.1 */
        class C09351 implements InternalFactory {
            C09351() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonNode actionParams) {
                return new UxmEnumerate(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.2 */
        class C09362 implements InternalFactory {
            C09362() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonNode actionParams) {
                return new SetBundleId(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.3 */
        class C09373 implements InternalFactory {
            C09373() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonNode actionParams) {
                return new ModifyValue(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.4 */
        class C09384 implements InternalFactory {
            C09384() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonNode actionParams) {
                return new NotifyUxmValuesSynchronized(actionType, actionParams, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.5 */
        class C09395 implements InternalFactory {
            C09395() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, String actionType, JsonNode actionParams) {
                return new Destroy(actionType, actionParams, null);
            }
        }

        C09401() {
            put("action_uxm_enumerate", new C09351());
            put("action_set_bundle_id", new C09362());
            put("action_modify_value", new C09373());
            put("action_notify_uxm_values_synchronized", new C09384());
            put("action_destroy", new C09395());
        }
    }

    static class Destroy extends Action<UxmContent, UxmContentActionContext> {
        private Destroy(UxmContentActionContext actionContext, String type, JsonNode params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            Bus bus = ((UxmContentActionContext) getActionContext()).mBus;
            content.signalActionCompleted(bus);
            content.signalActionMapCompleted(bus);
        }
    }

    static class ModifyValue extends Action<UxmContent, UxmContentActionContext> {
        private static final String MATCH = "match";
        private static final String OPERATOR = "operator";
        private static final String OPERATOR_SET = "set";
        private static final String PROPERTY_NAME = "property_name";
        private static final String PROPERTY_VALUE = "property_value";
        private static final String TYPE = "type";
        private static final String VALUES = "values";

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.1 */
        class C09411 implements Func1<T, JsonNode> {
            final /* synthetic */ ObjectMapper val$mapper;

            C09411(ObjectMapper objectMapper) {
                this.val$mapper = objectMapper;
            }

            public JsonNode call(T model) {
                return this.val$mapper.valueToTree(model);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.2 */
        class C09422 implements Func1<ObjectNode, Boolean> {
            final /* synthetic */ String val$propertyName;
            final /* synthetic */ JsonNode val$propertyValue;

            C09422(String str, JsonNode jsonNode) {
                this.val$propertyName = str;
                this.val$propertyValue = jsonNode;
            }

            public Boolean call(ObjectNode model) {
                return Boolean.valueOf(model.path(this.val$propertyName).equals(this.val$propertyValue));
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.3 */
        class C09433 implements Func1<ObjectNode, ObjectNode> {
            final /* synthetic */ String val$propertyName;
            final /* synthetic */ JsonNode val$propertyValue;

            C09433(String str, JsonNode jsonNode) {
                this.val$propertyName = str;
                this.val$propertyValue = jsonNode;
            }

            public ObjectNode call(ObjectNode model) {
                model.replace(this.val$propertyName, this.val$propertyValue);
                return model;
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4 */
        class C09474 implements Action1<ObjectNode> {
            final /* synthetic */ UxmContentActionContext val$actionContext;
            final /* synthetic */ Class val$clazz;
            final /* synthetic */ UxmContent val$content;
            final /* synthetic */ UpsightDataStore val$dataStore;
            final /* synthetic */ UpsightLogger val$logger;
            final /* synthetic */ ObjectMapper val$mapper;

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.1 */
            class C09441 implements Action1<T> {
                final /* synthetic */ ObjectNode val$modelNode;

                C09441(ObjectNode objectNode) {
                    this.val$modelNode = objectNode;
                }

                public void call(T t) {
                    C09474.this.val$logger.m197d(Upsight.LOG_TAG, "Modified managed variable of class " + C09474.this.val$clazz + " with value " + this.val$modelNode, new Object[0]);
                }
            }

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.2 */
            class C09452 implements Action1<Throwable> {
                C09452() {
                }

                public void call(Throwable t) {
                    C09474.this.val$logger.m200e(Upsight.LOG_TAG, t, "Failed to modify managed variable of class " + C09474.this.val$clazz, new Object[0]);
                }
            }

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.3 */
            class C09463 implements Action0 {
                C09463() {
                }

                public void call() {
                    C09474.this.val$content.signalActionCompleted(C09474.this.val$actionContext.mBus);
                }
            }

            C09474(UpsightDataStore upsightDataStore, ObjectMapper objectMapper, Class cls, UxmContentActionContext uxmContentActionContext, UpsightLogger upsightLogger, UxmContent uxmContent) {
                this.val$dataStore = upsightDataStore;
                this.val$mapper = objectMapper;
                this.val$clazz = cls;
                this.val$actionContext = uxmContentActionContext;
                this.val$logger = upsightLogger;
                this.val$content = uxmContent;
            }

            public void call(ObjectNode modelNode) {
                try {
                    this.val$dataStore.storeObservable(this.val$mapper.treeToValue(modelNode, this.val$clazz)).subscribeOn(this.val$actionContext.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(this.val$actionContext.mUpsight.getCoreComponent().observeOnScheduler()).subscribe(new C09441(modelNode), new C09452(), new C09463());
                } catch (JsonProcessingException e) {
                    this.val$logger.m200e(Upsight.LOG_TAG, e, "Failed to parse managed variable of class " + this.val$clazz, new Object[0]);
                    this.val$content.signalActionCompleted(this.val$actionContext.mBus);
                }
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.5 */
        class C09485 implements Action1<Throwable> {
            final /* synthetic */ UxmContentActionContext val$actionContext;
            final /* synthetic */ Class val$clazz;
            final /* synthetic */ UxmContent val$content;
            final /* synthetic */ UpsightLogger val$logger;

            C09485(UpsightLogger upsightLogger, Class cls, UxmContent uxmContent, UxmContentActionContext uxmContentActionContext) {
                this.val$logger = upsightLogger;
                this.val$clazz = cls;
                this.val$content = uxmContent;
                this.val$actionContext = uxmContentActionContext;
            }

            public void call(Throwable throwable) {
                this.val$logger.m200e(Upsight.LOG_TAG, throwable, "Failed to fetch managed variable of class " + this.val$clazz, new Object[0]);
                this.val$content.signalActionCompleted(this.val$actionContext.mBus);
            }
        }

        private ModifyValue(UxmContentActionContext actionContext, String type, JsonNode params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            boolean isSync = true;
            ActionContext actionContext = getActionContext();
            if (content.shouldApplyBundle()) {
                String type = optParamString(TYPE);
                ArrayNode matchers = optParamJsonArray(MATCH);
                ArrayNode values = optParamJsonArray(VALUES);
                if (!(TextUtils.isEmpty(type) || matchers == null || values == null)) {
                    Class<?> clazz = null;
                    if ("com.upsight.uxm.string".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.boolean".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.integer".equals(type)) {
                        clazz = Model.class;
                    } else if ("com.upsight.uxm.float".equals(type)) {
                        clazz = Model.class;
                    }
                    if (clazz != null) {
                        modifyValue(content, clazz, matchers, values);
                        isSync = false;
                    } else {
                        actionContext.mLogger.m199e(Upsight.LOG_TAG, "Failed to execute action_modify_value due to unknown managed variable type " + type, new Object[0]);
                    }
                }
            }
            if (isSync) {
                content.signalActionCompleted(actionContext.mBus);
            }
        }

        private <T> void modifyValue(UxmContent content, Class<T> clazz, JsonNode matchers, JsonNode values) {
            UxmContentActionContext actionContext = (UxmContentActionContext) getActionContext();
            ObjectMapper mapper = actionContext.mMapper;
            UpsightLogger logger = actionContext.mUpsight.getLogger();
            UpsightDataStore dataStore = actionContext.mUpsight.getDataStore();
            Observable<ObjectNode> fetchObservable = dataStore.fetchObservable(clazz).map(new C09411(mapper)).cast(ObjectNode.class);
            ObjectNode seedNode = mapper.createObjectNode();
            Iterator i$ = matchers.iterator();
            while (i$.hasNext()) {
                JsonNode matcher = (JsonNode) i$.next();
                String propertyName = matcher.path(PROPERTY_NAME).asText();
                JsonNode propertyValue = matcher.path(PROPERTY_VALUE);
                fetchObservable = fetchObservable.filter(new C09422(propertyName, propertyValue));
                seedNode.replace(propertyName, propertyValue);
            }
            fetchObservable = fetchObservable.defaultIfEmpty(seedNode);
            i$ = values.iterator();
            while (i$.hasNext()) {
                JsonNode value = (JsonNode) i$.next();
                String operator = value.path(OPERATOR).asText();
                propertyName = value.path(PROPERTY_NAME).asText();
                propertyValue = value.path(PROPERTY_VALUE);
                if (OPERATOR_SET.equals(operator)) {
                    fetchObservable = fetchObservable.map(new C09433(propertyName, propertyValue));
                }
            }
            Observable observeOn = fetchObservable.subscribeOn(actionContext.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(actionContext.mUpsight.getCoreComponent().observeOnScheduler());
            r24.subscribe(new C09474(dataStore, mapper, clazz, actionContext, logger, content), new C09485(logger, clazz, content, actionContext));
        }
    }

    static class NotifyUxmValuesSynchronized extends Action<UxmContent, UxmContentActionContext> {
        private static final String TAGS = "tags";

        private NotifyUxmValuesSynchronized(UxmContentActionContext actionContext, String type, JsonNode params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            List<String> synchronizedTags = new ArrayList();
            ArrayNode tagNodes = optParamJsonArray(TAGS);
            if (content.shouldApplyBundle() && tagNodes != null) {
                Iterator i$ = tagNodes.iterator();
                while (i$.hasNext()) {
                    JsonNode tagNode = (JsonNode) i$.next();
                    if (tagNode.isTextual()) {
                        synchronizedTags.add(tagNode.asText());
                    }
                }
            }
            Bus bus = ((UxmContentActionContext) getActionContext()).mBus;
            bus.post(new ScheduleSyncNotificationEvent(synchronizedTags, null));
            content.signalActionCompleted(bus);
        }
    }

    public static class ScheduleSyncNotificationEvent {
        public final String mId;
        public final List<String> mTags;

        private ScheduleSyncNotificationEvent(String id, List<String> tags) {
            this.mId = id;
            this.mTags = tags;
        }
    }

    static class SetBundleId extends Action<UxmContent, UxmContentActionContext> {
        private static final String BUNDLE_ID = "bundle.id";

        private SetBundleId(UxmContentActionContext actionContext, String type, JsonNode params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            if (content.shouldApplyBundle()) {
                PreferencesHelper.putString(((UxmContentActionContext) getActionContext()).mUpsight, UxmContent.PREFERENCES_KEY_UXM_BUNDLE_ID, optParamString(BUNDLE_ID));
            }
            content.signalActionCompleted(((UxmContentActionContext) getActionContext()).mBus);
        }
    }

    public static class UxmContentActionContext extends ActionContext {
        public UxmContentActionContext(UpsightContext upsight, Bus bus, ObjectMapper mapper, Clock clock, Worker mainWorker, UpsightLogger logger) {
            super(upsight, bus, mapper, clock, mainWorker, logger);
        }
    }

    public static class UxmContentActionFactory implements ActionFactory<UxmContent, UxmContentActionContext> {
        public static final String TYPE = "datastore_factory";

        public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext actionContext, JsonNode actionJSON) throws UpsightException {
            if (actionJSON == null) {
                throw new UpsightException("Failed to create Action. JSON is null.", new Object[0]);
            }
            String actionType = actionJSON.get(ActionFactory.KEY_ACTION_TYPE).asText();
            JsonNode actionParams = actionJSON.get(ActionFactory.KEY_ACTION_PARAMS);
            InternalFactory factory = (InternalFactory) UxmContentActions.FACTORY_MAP.get(actionType);
            if (factory != null) {
                return factory.create(actionContext, actionType, actionParams);
            }
            throw new UpsightException("Failed to create Action. Unknown action type.", new Object[0]);
        }
    }

    static class UxmEnumerate extends Action<UxmContent, UxmContentActionContext> {
        private UxmEnumerate(UxmContentActionContext actionContext, String type, JsonNode params) {
            super(actionContext, type, params);
        }

        public void execute(UxmContent content) {
            ActionContext actionContext = getActionContext();
            try {
                UpsightUxmEnumerateEvent.createBuilder(new JSONArray(((UpsightManagedVariablesComponent) actionContext.mUpsight.getUpsightExtension(UpsightManagedVariablesExtension.EXTENSION_NAME).getComponent()).uxmSchema().mSchemaJsonString)).record(actionContext.mUpsight);
            } catch (JSONException e) {
                actionContext.mUpsight.getLogger().m200e(Upsight.LOG_TAG, e, "Failed to send UXM enumerate event", new Object[0]);
            }
            content.signalActionCompleted(actionContext.mBus);
        }
    }

    private UxmContentActions() {
    }

    static {
        FACTORY_MAP = new C09401();
    }
}
