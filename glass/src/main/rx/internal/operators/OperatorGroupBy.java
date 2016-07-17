package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import org.apache.commons.io.FileUtils;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observable.Operator;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public class OperatorGroupBy<T, K, R> implements Operator<GroupedObservable<K, R>, T> {
    private static final Func1<Object, Object> IDENTITY;
    private static final Object NULL_KEY;
    final Func1<? super T, ? extends K> keySelector;
    final Func1<? super T, ? extends R> valueSelector;

    /* renamed from: rx.internal.operators.OperatorGroupBy.1 */
    static class C12251 implements Func1<Object, Object> {
        C12251() {
        }

        public Object call(Object t) {
            return t;
        }
    }

    static final class GroupBySubscriber<K, T, R> extends Subscriber<T> {
        static final AtomicLongFieldUpdater<GroupBySubscriber> BUFFERED_COUNT;
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> COMPLETION_EMITTED_UPDATER;
        private static final int MAX_QUEUE_SIZE = 1024;
        static final AtomicLongFieldUpdater<GroupBySubscriber> REQUESTED;
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> TERMINATED_UPDATER;
        private static final int TERMINATED_WITH_COMPLETED = 1;
        private static final int TERMINATED_WITH_ERROR = 2;
        private static final int UNTERMINATED = 0;
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> WIP_FOR_UNSUBSCRIBE_UPDATER;
        private static final NotificationLite<Object> nl;
        volatile long bufferedCount;
        final Subscriber<? super GroupedObservable<K, R>> child;
        volatile int completionEmitted;
        final Func1<? super T, ? extends R> elementSelector;
        private final ConcurrentHashMap<Object, GroupState<K, T>> groups;
        final Func1<? super T, ? extends K> keySelector;
        volatile long requested;
        final GroupBySubscriber<K, T, R> self;
        volatile int terminated;
        volatile int wipForUnsubscribe;

        /* renamed from: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.1 */
        class C12261 implements Action0 {
            C12261() {
            }

            public void call() {
                if (GroupBySubscriber.WIP_FOR_UNSUBSCRIBE_UPDATER.decrementAndGet(GroupBySubscriber.this.self) == 0) {
                    GroupBySubscriber.this.self.unsubscribe();
                }
            }
        }

        /* renamed from: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.2 */
        class C12302 implements OnSubscribe<R> {
            final /* synthetic */ GroupState val$groupState;
            final /* synthetic */ Object val$key;

            /* renamed from: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.2.1 */
            class C12271 implements Producer {
                C12271() {
                }

                public void request(long n) {
                    GroupBySubscriber.this.requestFromGroupedObservable(n, C12302.this.val$groupState);
                }
            }

            /* renamed from: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.2.2 */
            class C12282 extends Subscriber<T> {
                final /* synthetic */ Subscriber val$o;
                final /* synthetic */ AtomicBoolean val$once;

                C12282(Subscriber x0, Subscriber subscriber, AtomicBoolean atomicBoolean) {
                    this.val$o = subscriber;
                    this.val$once = atomicBoolean;
                    super(x0);
                }

                public void onStart() {
                }

                public void onCompleted() {
                    this.val$o.onCompleted();
                    if (this.val$once.compareAndSet(false, true)) {
                        GroupBySubscriber.this.cleanupGroup(C12302.this.val$key);
                    }
                }

                public void onError(Throwable e) {
                    this.val$o.onError(e);
                    if (this.val$once.compareAndSet(false, true)) {
                        GroupBySubscriber.this.cleanupGroup(C12302.this.val$key);
                    }
                }

                public void onNext(T t) {
                    try {
                        this.val$o.onNext(GroupBySubscriber.this.elementSelector.call(t));
                    } catch (Throwable e) {
                        onError(OnErrorThrowable.addValueAsLastCause(e, t));
                    }
                }
            }

            /* renamed from: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.2.3 */
            class C12293 implements Action0 {
                final /* synthetic */ AtomicBoolean val$once;

                C12293(AtomicBoolean atomicBoolean) {
                    this.val$once = atomicBoolean;
                }

                public void call() {
                    if (this.val$once.compareAndSet(false, true)) {
                        GroupBySubscriber.this.cleanupGroup(C12302.this.val$key);
                    }
                }
            }

            C12302(GroupState groupState, Object obj) {
                this.val$groupState = groupState;
                this.val$key = obj;
            }

            public void call(Subscriber<? super R> o) {
                o.setProducer(new C12271());
                AtomicBoolean once = new AtomicBoolean();
                this.val$groupState.getObservable().doOnUnsubscribe(new C12293(once)).unsafeSubscribe(new C12282(o, o, once));
            }
        }

        private static class GroupState<K, T> {
            private final Queue<Object> buffer;
            private final AtomicLong count;
            private final AtomicLong requested;
            private final Subject<T, T> f917s;

            private GroupState() {
                this.f917s = BufferUntilSubscriber.create();
                this.requested = new AtomicLong();
                this.count = new AtomicLong();
                this.buffer = new ConcurrentLinkedQueue();
            }

            public Observable<T> getObservable() {
                return this.f917s;
            }

            public Observer<T> getObserver() {
                return this.f917s;
            }
        }

        static {
            WIP_FOR_UNSUBSCRIBE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "wipForUnsubscribe");
            nl = NotificationLite.instance();
            COMPLETION_EMITTED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "completionEmitted");
            TERMINATED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "terminated");
            REQUESTED = AtomicLongFieldUpdater.newUpdater(GroupBySubscriber.class, "requested");
            BUFFERED_COUNT = AtomicLongFieldUpdater.newUpdater(GroupBySubscriber.class, "bufferedCount");
        }

        public GroupBySubscriber(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends R> elementSelector, Subscriber<? super GroupedObservable<K, R>> child) {
            this.self = this;
            this.wipForUnsubscribe = TERMINATED_WITH_COMPLETED;
            this.groups = new ConcurrentHashMap();
            this.terminated = 0;
            this.keySelector = keySelector;
            this.elementSelector = elementSelector;
            this.child = child;
            child.add(Subscriptions.create(new C12261()));
        }

        public void onStart() {
            REQUESTED.set(this, FileUtils.ONE_KB);
            request(FileUtils.ONE_KB);
        }

        public void onCompleted() {
            if (TERMINATED_UPDATER.compareAndSet(this, 0, TERMINATED_WITH_COMPLETED)) {
                for (GroupState<K, T> group : this.groups.values()) {
                    emitItem(group, nl.completed());
                }
                if (this.groups.isEmpty() && COMPLETION_EMITTED_UPDATER.compareAndSet(this, 0, TERMINATED_WITH_COMPLETED)) {
                    this.child.onCompleted();
                }
            }
        }

        public void onError(Throwable e) {
            if (TERMINATED_UPDATER.compareAndSet(this, 0, TERMINATED_WITH_ERROR)) {
                for (GroupState<K, T> group : this.groups.values()) {
                    emitItem(group, nl.error(e));
                }
                try {
                    this.child.onError(e);
                } finally {
                    unsubscribe();
                }
            }
        }

        void requestFromGroupedObservable(long n, GroupState<K, T> group) {
            BackpressureUtils.getAndAddRequest(group.requested, n);
            if (group.count.getAndIncrement() == 0) {
                pollQueue(group);
            }
        }

        private Object groupedKey(K key) {
            return key == null ? OperatorGroupBy.NULL_KEY : key;
        }

        private K getKey(Object groupedKey) {
            return groupedKey == OperatorGroupBy.NULL_KEY ? null : groupedKey;
        }

        public void onNext(T t) {
            try {
                Object key = groupedKey(this.keySelector.call(t));
                GroupState<K, T> group = (GroupState) this.groups.get(key);
                if (group == null) {
                    if (!this.child.isUnsubscribed()) {
                        group = createNewGroup(key);
                    } else {
                        return;
                    }
                }
                if (group != null) {
                    emitItem(group, nl.next(t));
                }
            } catch (Throwable e) {
                onError(OnErrorThrowable.addValueAsLastCause(e, t));
            }
        }

        private GroupState<K, T> createNewGroup(Object key) {
            GroupState<K, T> groupState = new GroupState();
            GroupedObservable<K, R> go = GroupedObservable.create(getKey(key), new C12302(groupState, key));
            int wip;
            do {
                wip = this.wipForUnsubscribe;
                if (wip <= 0) {
                    return null;
                }
            } while (!WIP_FOR_UNSUBSCRIBE_UPDATER.compareAndSet(this, wip, wip + TERMINATED_WITH_COMPLETED));
            if (((GroupState) this.groups.putIfAbsent(key, groupState)) != null) {
                throw new IllegalStateException("Group already existed while creating a new one");
            }
            this.child.onNext(go);
            return groupState;
        }

        private void cleanupGroup(Object key) {
            GroupState<K, T> removed = (GroupState) this.groups.remove(key);
            if (removed != null) {
                if (!removed.buffer.isEmpty()) {
                    BUFFERED_COUNT.addAndGet(this.self, (long) (-removed.buffer.size()));
                }
                completeInner();
                requestMoreIfNecessary();
            }
        }

        private void emitItem(GroupState<K, T> groupState, Object item) {
            Queue<Object> q = groupState.buffer;
            AtomicLong keyRequested = groupState.requested;
            REQUESTED.decrementAndGet(this);
            if (keyRequested == null || keyRequested.get() <= 0 || !(q == null || q.isEmpty())) {
                q.add(item);
                BUFFERED_COUNT.incrementAndGet(this);
                if (groupState.count.getAndIncrement() == 0) {
                    pollQueue(groupState);
                }
            } else {
                nl.accept(groupState.getObserver(), item);
                if (keyRequested.get() != Long.MAX_VALUE) {
                    keyRequested.decrementAndGet();
                }
            }
            requestMoreIfNecessary();
        }

        private void pollQueue(GroupState<K, T> groupState) {
            do {
                drainIfPossible(groupState);
                if (groupState.count.decrementAndGet() > 1) {
                    groupState.count.set(1);
                }
            } while (groupState.count.get() > 0);
        }

        private void requestMoreIfNecessary() {
            if (REQUESTED.get(this) == 0 && this.terminated == 0) {
                long toRequest = FileUtils.ONE_KB - BUFFERED_COUNT.get(this);
                if (toRequest > 0 && REQUESTED.compareAndSet(this, 0, toRequest)) {
                    request(toRequest);
                }
            }
        }

        private void drainIfPossible(GroupState<K, T> groupState) {
            while (groupState.requested.get() > 0) {
                Object t = groupState.buffer.poll();
                if (t != null) {
                    nl.accept(groupState.getObserver(), t);
                    if (groupState.requested.get() != Long.MAX_VALUE) {
                        groupState.requested.decrementAndGet();
                    }
                    BUFFERED_COUNT.decrementAndGet(this);
                    requestMoreIfNecessary();
                } else {
                    return;
                }
            }
        }

        private void completeInner() {
            if (WIP_FOR_UNSUBSCRIBE_UPDATER.decrementAndGet(this) == 0) {
                unsubscribe();
            } else if (this.groups.isEmpty() && this.terminated == TERMINATED_WITH_COMPLETED && COMPLETION_EMITTED_UPDATER.compareAndSet(this, 0, TERMINATED_WITH_COMPLETED)) {
                this.child.onCompleted();
            }
        }
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector) {
        this(keySelector, IDENTITY);
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends R> valueSelector) {
        this.keySelector = keySelector;
        this.valueSelector = valueSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super GroupedObservable<K, R>> child) {
        return new GroupBySubscriber(this.keySelector, this.valueSelector, child);
    }

    static {
        IDENTITY = new C12251();
        NULL_KEY = new Object();
    }
}
