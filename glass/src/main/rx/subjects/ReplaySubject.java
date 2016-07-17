package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Scheduler;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.NotificationLite;
import rx.internal.util.UtilityFunctions;
import rx.schedulers.Timestamped;

public final class ReplaySubject<T> extends Subject<T, T> {
    final SubjectSubscriptionManager<T> ssm;
    final ReplayState<T, ?> state;

    /* renamed from: rx.subjects.ReplaySubject.1 */
    static class C13791 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ UnboundedReplayState val$state;

        C13791(UnboundedReplayState unboundedReplayState) {
            this.val$state = unboundedReplayState;
        }

        public void call(SubjectObserver<T> o) {
            o.index(Integer.valueOf(this.val$state.replayObserverFromIndex(Integer.valueOf(0), (SubjectObserver) o).intValue()));
        }
    }

    /* renamed from: rx.subjects.ReplaySubject.2 */
    static class C13802 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ UnboundedReplayState val$state;

        C13802(UnboundedReplayState unboundedReplayState) {
            this.val$state = unboundedReplayState;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void call(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r7) {
            /*
            r6 = this;
            monitor-enter(r7);
            r4 = r7.first;	 Catch:{ all -> 0x0047 }
            if (r4 == 0) goto L_0x0009;
        L_0x0005:
            r4 = r7.emitting;	 Catch:{ all -> 0x0047 }
            if (r4 == 0) goto L_0x000b;
        L_0x0009:
            monitor-exit(r7);	 Catch:{ all -> 0x0047 }
        L_0x000a:
            return;
        L_0x000b:
            r4 = 0;
            r7.first = r4;	 Catch:{ all -> 0x0047 }
            r4 = 1;
            r7.emitting = r4;	 Catch:{ all -> 0x0047 }
            monitor-exit(r7);	 Catch:{ all -> 0x0047 }
            r3 = 0;
        L_0x0013:
            r4 = r7.index();	 Catch:{ all -> 0x004f }
            r4 = (java.lang.Integer) r4;	 Catch:{ all -> 0x004f }
            r0 = r4.intValue();	 Catch:{ all -> 0x004f }
            r4 = r6.val$state;	 Catch:{ all -> 0x004f }
            r2 = r4.index;	 Catch:{ all -> 0x004f }
            if (r0 == r2) goto L_0x0030;
        L_0x0023:
            r4 = r6.val$state;	 Catch:{ all -> 0x004f }
            r5 = java.lang.Integer.valueOf(r0);	 Catch:{ all -> 0x004f }
            r1 = r4.replayObserverFromIndex(r5, r7);	 Catch:{ all -> 0x004f }
            r7.index(r1);	 Catch:{ all -> 0x004f }
        L_0x0030:
            monitor-enter(r7);	 Catch:{ all -> 0x004f }
            r4 = r6.val$state;	 Catch:{ all -> 0x004c }
            r4 = r4.index;	 Catch:{ all -> 0x004c }
            if (r2 != r4) goto L_0x004a;
        L_0x0037:
            r4 = 0;
            r7.emitting = r4;	 Catch:{ all -> 0x004c }
            r3 = 1;
            monitor-exit(r7);	 Catch:{ all -> 0x004c }
            if (r3 != 0) goto L_0x000a;
        L_0x003e:
            monitor-enter(r7);
            r4 = 0;
            r7.emitting = r4;	 Catch:{ all -> 0x0044 }
            monitor-exit(r7);	 Catch:{ all -> 0x0044 }
            goto L_0x000a;
        L_0x0044:
            r4 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0044 }
            throw r4;
        L_0x0047:
            r4 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0047 }
            throw r4;
        L_0x004a:
            monitor-exit(r7);	 Catch:{ all -> 0x004c }
            goto L_0x0013;
        L_0x004c:
            r4 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x004c }
            throw r4;	 Catch:{ all -> 0x004f }
        L_0x004f:
            r4 = move-exception;
            if (r3 != 0) goto L_0x0057;
        L_0x0052:
            monitor-enter(r7);
            r5 = 0;
            r7.emitting = r5;	 Catch:{ all -> 0x0058 }
            monitor-exit(r7);	 Catch:{ all -> 0x0058 }
        L_0x0057:
            throw r4;
        L_0x0058:
            r4 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0058 }
            throw r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.2.call(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
        }
    }

    /* renamed from: rx.subjects.ReplaySubject.3 */
    static class C13813 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ UnboundedReplayState val$state;

        C13813(UnboundedReplayState unboundedReplayState) {
            this.val$state = unboundedReplayState;
        }

        public void call(SubjectObserver<T> o) {
            Integer idx = (Integer) o.index();
            if (idx == null) {
                idx = Integer.valueOf(0);
            }
            this.val$state.replayObserverFromIndex(idx, (SubjectObserver) o);
        }
    }

    /* renamed from: rx.subjects.ReplaySubject.4 */
    static class C13824 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ BoundedState val$state;

        C13824(BoundedState boundedState) {
            this.val$state = boundedState;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void call(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r7) {
            /*
            r6 = this;
            monitor-enter(r7);
            r4 = r7.first;	 Catch:{ all -> 0x0043 }
            if (r4 == 0) goto L_0x0009;
        L_0x0005:
            r4 = r7.emitting;	 Catch:{ all -> 0x0043 }
            if (r4 == 0) goto L_0x000b;
        L_0x0009:
            monitor-exit(r7);	 Catch:{ all -> 0x0043 }
        L_0x000a:
            return;
        L_0x000b:
            r4 = 0;
            r7.first = r4;	 Catch:{ all -> 0x0043 }
            r4 = 1;
            r7.emitting = r4;	 Catch:{ all -> 0x0043 }
            monitor-exit(r7);	 Catch:{ all -> 0x0043 }
            r3 = 0;
        L_0x0013:
            r0 = r7.index();	 Catch:{ all -> 0x004b }
            r0 = (rx.subjects.ReplaySubject.NodeList.Node) r0;	 Catch:{ all -> 0x004b }
            r4 = r6.val$state;	 Catch:{ all -> 0x004b }
            r2 = r4.tail();	 Catch:{ all -> 0x004b }
            if (r0 == r2) goto L_0x002a;
        L_0x0021:
            r4 = r6.val$state;	 Catch:{ all -> 0x004b }
            r1 = r4.replayObserverFromIndex(r0, r7);	 Catch:{ all -> 0x004b }
            r7.index(r1);	 Catch:{ all -> 0x004b }
        L_0x002a:
            monitor-enter(r7);	 Catch:{ all -> 0x004b }
            r4 = r6.val$state;	 Catch:{ all -> 0x0048 }
            r4 = r4.tail();	 Catch:{ all -> 0x0048 }
            if (r2 != r4) goto L_0x0046;
        L_0x0033:
            r4 = 0;
            r7.emitting = r4;	 Catch:{ all -> 0x0048 }
            r3 = 1;
            monitor-exit(r7);	 Catch:{ all -> 0x0048 }
            if (r3 != 0) goto L_0x000a;
        L_0x003a:
            monitor-enter(r7);
            r4 = 0;
            r7.emitting = r4;	 Catch:{ all -> 0x0040 }
            monitor-exit(r7);	 Catch:{ all -> 0x0040 }
            goto L_0x000a;
        L_0x0040:
            r4 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0040 }
            throw r4;
        L_0x0043:
            r4 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0043 }
            throw r4;
        L_0x0046:
            monitor-exit(r7);	 Catch:{ all -> 0x0048 }
            goto L_0x0013;
        L_0x0048:
            r4 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0048 }
            throw r4;	 Catch:{ all -> 0x004b }
        L_0x004b:
            r4 = move-exception;
            if (r3 != 0) goto L_0x0053;
        L_0x004e:
            monitor-enter(r7);
            r5 = 0;
            r7.emitting = r5;	 Catch:{ all -> 0x0054 }
            monitor-exit(r7);	 Catch:{ all -> 0x0054 }
        L_0x0053:
            throw r4;
        L_0x0054:
            r4 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0054 }
            throw r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.4.call(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
        }
    }

    /* renamed from: rx.subjects.ReplaySubject.5 */
    static class C13835 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ BoundedState val$state;

        C13835(BoundedState boundedState) {
            this.val$state = boundedState;
        }

        public void call(SubjectObserver<T> t1) {
            Node l = (Node) t1.index();
            if (l == null) {
                l = this.val$state.head();
            }
            this.val$state.replayObserverFromIndex(l, (SubjectObserver) t1);
        }
    }

    static final class AddTimestamped implements Func1<Object, Object> {
        final Scheduler scheduler;

        public AddTimestamped(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        public Object call(Object t1) {
            return new Timestamped(this.scheduler.now(), t1);
        }
    }

    interface ReplayState<T, I> {
        void complete();

        void error(Throwable th);

        boolean isEmpty();

        T latest();

        void next(T t);

        boolean replayObserver(SubjectObserver<? super T> subjectObserver);

        I replayObserverFromIndex(I i, SubjectObserver<? super T> subjectObserver);

        I replayObserverFromIndexTest(I i, SubjectObserver<? super T> subjectObserver, long j);

        int size();

        boolean terminated();

        T[] toArray(T[] tArr);
    }

    static final class BoundedState<T> implements ReplayState<T, Node<Object>> {
        final Func1<Object, Object> enterTransform;
        final EvictionPolicy evictionPolicy;
        final Func1<Object, Object> leaveTransform;
        final NodeList<Object> list;
        final NotificationLite<T> nl;
        volatile Node<Object> tail;
        volatile boolean terminated;

        public BoundedState(EvictionPolicy evictionPolicy, Func1<Object, Object> enterTransform, Func1<Object, Object> leaveTransform) {
            this.nl = NotificationLite.instance();
            this.list = new NodeList();
            this.tail = this.list.tail;
            this.evictionPolicy = evictionPolicy;
            this.enterTransform = enterTransform;
            this.leaveTransform = leaveTransform;
        }

        public void next(T value) {
            if (!this.terminated) {
                this.list.addLast(this.enterTransform.call(this.nl.next(value)));
                this.evictionPolicy.evict(this.list);
                this.tail = this.list.tail;
            }
        }

        public void complete() {
            if (!this.terminated) {
                this.terminated = true;
                this.list.addLast(this.enterTransform.call(this.nl.completed()));
                this.evictionPolicy.evictFinal(this.list);
                this.tail = this.list.tail;
            }
        }

        public void error(Throwable e) {
            if (!this.terminated) {
                this.terminated = true;
                this.list.addLast(this.enterTransform.call(this.nl.error(e)));
                this.evictionPolicy.evictFinal(this.list);
                this.tail = this.list.tail;
            }
        }

        public void accept(Observer<? super T> o, Node<Object> node) {
            this.nl.accept(o, this.leaveTransform.call(node.value));
        }

        public void acceptTest(Observer<? super T> o, Node<Object> node, long now) {
            Object v = node.value;
            if (!this.evictionPolicy.test(v, now)) {
                this.nl.accept(o, this.leaveTransform.call(v));
            }
        }

        public Node<Object> head() {
            return this.list.head;
        }

        public Node<Object> tail() {
            return this.tail;
        }

        public boolean replayObserver(SubjectObserver<? super T> observer) {
            synchronized (observer) {
                observer.first = false;
                if (observer.emitting) {
                    return false;
                }
                observer.index(replayObserverFromIndex((Node) observer.index(), (SubjectObserver) observer));
                return true;
            }
        }

        public Node<Object> replayObserverFromIndex(Node<Object> l, SubjectObserver<? super T> observer) {
            while (l != tail()) {
                accept(observer, l.next);
                l = l.next;
            }
            return l;
        }

        public Node<Object> replayObserverFromIndexTest(Node<Object> l, SubjectObserver<? super T> observer, long now) {
            while (l != tail()) {
                acceptTest(observer, l.next, now);
                l = l.next;
            }
            return l;
        }

        public boolean terminated() {
            return this.terminated;
        }

        public int size() {
            int size = 0;
            Node<Object> l = head();
            for (Node<Object> next = l.next; next != null; next = next.next) {
                size++;
                l = next;
            }
            if (l.value == null) {
                return size;
            }
            Object value = this.leaveTransform.call(l.value);
            if (value == null) {
                return size;
            }
            if (this.nl.isError(value) || this.nl.isCompleted(value)) {
                return size - 1;
            }
            return size;
        }

        public boolean isEmpty() {
            Node<Object> next = head().next;
            if (next == null) {
                return true;
            }
            Object value = this.leaveTransform.call(next.value);
            if (this.nl.isError(value) || this.nl.isCompleted(value)) {
                return true;
            }
            return false;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public T[] toArray(T[] r7) {
            /*
            r6 = this;
            r1 = new java.util.ArrayList;
            r1.<init>();
            r0 = r6.head();
            r2 = r0.next;
        L_0x000b:
            if (r2 == 0) goto L_0x0029;
        L_0x000d:
            r4 = r6.leaveTransform;
            r5 = r2.value;
            r3 = r4.call(r5);
            r4 = r2.next;
            if (r4 != 0) goto L_0x002e;
        L_0x0019:
            r4 = r6.nl;
            r4 = r4.isError(r3);
            if (r4 != 0) goto L_0x0029;
        L_0x0021:
            r4 = r6.nl;
            r4 = r4.isCompleted(r3);
            if (r4 == 0) goto L_0x002e;
        L_0x0029:
            r4 = r1.toArray(r7);
            return r4;
        L_0x002e:
            r1.add(r3);
            r0 = r2;
            r2 = r2.next;
            goto L_0x000b;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.BoundedState.toArray(java.lang.Object[]):T[]");
        }

        public T latest() {
            Node<Object> h = head().next;
            if (h == null) {
                return null;
            }
            Node<Object> p = null;
            while (h != tail()) {
                p = h;
                h = h.next;
            }
            Object value = this.leaveTransform.call(h.value);
            if (!this.nl.isError(value) && !this.nl.isCompleted(value)) {
                return this.nl.getValue(value);
            }
            if (p == null) {
                return null;
            }
            return this.nl.getValue(this.leaveTransform.call(p.value));
        }
    }

    static final class DefaultOnAdd<T> implements Action1<SubjectObserver<T>> {
        final BoundedState<T> state;

        public DefaultOnAdd(BoundedState<T> state) {
            this.state = state;
        }

        public void call(SubjectObserver<T> t1) {
            t1.index(this.state.replayObserverFromIndex(this.state.head(), (SubjectObserver) t1));
        }
    }

    interface EvictionPolicy {
        void evict(NodeList<Object> nodeList);

        void evictFinal(NodeList<Object> nodeList);

        boolean test(Object obj, long j);
    }

    static final class EmptyEvictionPolicy implements EvictionPolicy {
        EmptyEvictionPolicy() {
        }

        public boolean test(Object value, long now) {
            return true;
        }

        public void evict(NodeList<Object> nodeList) {
        }

        public void evictFinal(NodeList<Object> nodeList) {
        }
    }

    static final class NodeList<T> {
        final Node<T> head;
        int size;
        Node<T> tail;

        static final class Node<T> {
            volatile Node<T> next;
            final T value;

            Node(T value) {
                this.value = value;
            }
        }

        NodeList() {
            this.head = new Node(null);
            this.tail = this.head;
        }

        public void addLast(T value) {
            Node<T> t = this.tail;
            Node<T> t2 = new Node(value);
            t.next = t2;
            this.tail = t2;
            this.size++;
        }

        public T removeFirst() {
            if (this.head.next == null) {
                throw new IllegalStateException("Empty!");
            }
            Node<T> t = this.head.next;
            this.head.next = t.next;
            if (this.head.next == null) {
                this.tail = this.head;
            }
            this.size--;
            return t.value;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public int size() {
            return this.size;
        }

        public void clear() {
            this.tail = this.head;
            this.size = 0;
        }
    }

    static final class PairEvictionPolicy implements EvictionPolicy {
        final EvictionPolicy first;
        final EvictionPolicy second;

        public PairEvictionPolicy(EvictionPolicy first, EvictionPolicy second) {
            this.first = first;
            this.second = second;
        }

        public void evict(NodeList<Object> t1) {
            this.first.evict(t1);
            this.second.evict(t1);
        }

        public void evictFinal(NodeList<Object> t1) {
            this.first.evictFinal(t1);
            this.second.evictFinal(t1);
        }

        public boolean test(Object value, long now) {
            return this.first.test(value, now) || this.second.test(value, now);
        }
    }

    static final class RemoveTimestamped implements Func1<Object, Object> {
        RemoveTimestamped() {
        }

        public Object call(Object t1) {
            return ((Timestamped) t1).getValue();
        }
    }

    static final class SizeEvictionPolicy implements EvictionPolicy {
        final int maxSize;

        public SizeEvictionPolicy(int maxSize) {
            this.maxSize = maxSize;
        }

        public void evict(NodeList<Object> t1) {
            while (t1.size() > this.maxSize) {
                t1.removeFirst();
            }
        }

        public boolean test(Object value, long now) {
            return false;
        }

        public void evictFinal(NodeList<Object> t1) {
            while (t1.size() > this.maxSize + 1) {
                t1.removeFirst();
            }
        }
    }

    static final class TimeEvictionPolicy implements EvictionPolicy {
        final long maxAgeMillis;
        final Scheduler scheduler;

        public TimeEvictionPolicy(long maxAgeMillis, Scheduler scheduler) {
            this.maxAgeMillis = maxAgeMillis;
            this.scheduler = scheduler;
        }

        public void evict(NodeList<Object> t1) {
            long now = this.scheduler.now();
            while (!t1.isEmpty() && test(t1.head.next.value, now)) {
                t1.removeFirst();
            }
        }

        public void evictFinal(NodeList<Object> t1) {
            long now = this.scheduler.now();
            while (t1.size > 1 && test(t1.head.next.value, now)) {
                t1.removeFirst();
            }
        }

        public boolean test(Object value, long now) {
            return ((Timestamped) value).getTimestampMillis() <= now - this.maxAgeMillis;
        }
    }

    static final class TimedOnAdd<T> implements Action1<SubjectObserver<T>> {
        final Scheduler scheduler;
        final BoundedState<T> state;

        public TimedOnAdd(BoundedState<T> state, Scheduler scheduler) {
            this.state = state;
            this.scheduler = scheduler;
        }

        public void call(SubjectObserver<T> t1) {
            Node<Object> l;
            if (this.state.terminated) {
                l = this.state.replayObserverFromIndex(this.state.head(), (SubjectObserver) t1);
            } else {
                l = this.state.replayObserverFromIndexTest(this.state.head(), (SubjectObserver) t1, this.scheduler.now());
            }
            t1.index(l);
        }
    }

    static final class UnboundedReplayState<T> implements ReplayState<T, Integer> {
        static final AtomicIntegerFieldUpdater<UnboundedReplayState> INDEX_UPDATER;
        volatile int index;
        private final ArrayList<Object> list;
        private final NotificationLite<T> nl;
        private volatile boolean terminated;

        static {
            INDEX_UPDATER = AtomicIntegerFieldUpdater.newUpdater(UnboundedReplayState.class, "index");
        }

        public UnboundedReplayState(int initialCapacity) {
            this.nl = NotificationLite.instance();
            this.list = new ArrayList(initialCapacity);
        }

        public void next(T n) {
            if (!this.terminated) {
                this.list.add(this.nl.next(n));
                INDEX_UPDATER.getAndIncrement(this);
            }
        }

        public void accept(Observer<? super T> o, int idx) {
            this.nl.accept(o, this.list.get(idx));
        }

        public void complete() {
            if (!this.terminated) {
                this.terminated = true;
                this.list.add(this.nl.completed());
                INDEX_UPDATER.getAndIncrement(this);
            }
        }

        public void error(Throwable e) {
            if (!this.terminated) {
                this.terminated = true;
                this.list.add(this.nl.error(e));
                INDEX_UPDATER.getAndIncrement(this);
            }
        }

        public boolean terminated() {
            return this.terminated;
        }

        public boolean replayObserver(SubjectObserver<? super T> observer) {
            synchronized (observer) {
                observer.first = false;
                if (observer.emitting) {
                    return false;
                }
                Integer lastEmittedLink = (Integer) observer.index();
                if (lastEmittedLink != null) {
                    observer.index(Integer.valueOf(replayObserverFromIndex(lastEmittedLink, (SubjectObserver) observer).intValue()));
                    return true;
                }
                throw new IllegalStateException("failed to find lastEmittedLink for: " + observer);
            }
        }

        public Integer replayObserverFromIndex(Integer idx, SubjectObserver<? super T> observer) {
            int i = idx.intValue();
            while (i < this.index) {
                accept(observer, i);
                i++;
            }
            return Integer.valueOf(i);
        }

        public Integer replayObserverFromIndexTest(Integer idx, SubjectObserver<? super T> observer, long now) {
            return replayObserverFromIndex(idx, (SubjectObserver) observer);
        }

        public int size() {
            int idx = this.index;
            if (idx <= 0) {
                return idx;
            }
            Object o = this.list.get(idx - 1);
            if (this.nl.isCompleted(o) || this.nl.isError(o)) {
                return idx - 1;
            }
            return idx;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public T[] toArray(T[] a) {
            int s = size();
            if (s > 0) {
                if (s > a.length) {
                    a = (Object[]) ((Object[]) Array.newInstance(a.getClass().getComponentType(), s));
                }
                for (int i = 0; i < s; i++) {
                    a[i] = this.list.get(i);
                }
                if (a.length > s) {
                    a[s] = null;
                }
            } else if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }

        public T latest() {
            int idx = this.index;
            if (idx <= 0) {
                return null;
            }
            Object o = this.list.get(idx - 1);
            if (!this.nl.isCompleted(o) && !this.nl.isError(o)) {
                return this.nl.getValue(o);
            }
            if (idx > 1) {
                return this.nl.getValue(this.list.get(idx - 2));
            }
            return null;
        }
    }

    public static <T> ReplaySubject<T> create() {
        return create(16);
    }

    public static <T> ReplaySubject<T> create(int capacity) {
        UnboundedReplayState<T> state = new UnboundedReplayState(capacity);
        SubjectSubscriptionManager<T> ssm = new SubjectSubscriptionManager();
        ssm.onStart = new C13791(state);
        ssm.onAdded = new C13802(state);
        ssm.onTerminated = new C13813(state);
        return new ReplaySubject(ssm, ssm, state);
    }

    static <T> ReplaySubject<T> createUnbounded() {
        BoundedState<T> state = new BoundedState(new EmptyEvictionPolicy(), UtilityFunctions.identity(), UtilityFunctions.identity());
        return createWithState(state, new DefaultOnAdd(state));
    }

    public static <T> ReplaySubject<T> createWithSize(int size) {
        BoundedState<T> state = new BoundedState(new SizeEvictionPolicy(size), UtilityFunctions.identity(), UtilityFunctions.identity());
        return createWithState(state, new DefaultOnAdd(state));
    }

    public static <T> ReplaySubject<T> createWithTime(long time, TimeUnit unit, Scheduler scheduler) {
        BoundedState<T> state = new BoundedState(new TimeEvictionPolicy(unit.toMillis(time), scheduler), new AddTimestamped(scheduler), new RemoveTimestamped());
        return createWithState(state, new TimedOnAdd(state, scheduler));
    }

    public static <T> ReplaySubject<T> createWithTimeAndSize(long time, TimeUnit unit, int size, Scheduler scheduler) {
        BoundedState<T> state = new BoundedState(new PairEvictionPolicy(new SizeEvictionPolicy(size), new TimeEvictionPolicy(unit.toMillis(time), scheduler)), new AddTimestamped(scheduler), new RemoveTimestamped());
        return createWithState(state, new TimedOnAdd(state, scheduler));
    }

    static final <T> ReplaySubject<T> createWithState(BoundedState<T> state, Action1<SubjectObserver<T>> onStart) {
        SubjectSubscriptionManager<T> ssm = new SubjectSubscriptionManager();
        ssm.onStart = onStart;
        ssm.onAdded = new C13824(state);
        ssm.onTerminated = new C13835(state);
        return new ReplaySubject(ssm, ssm, state);
    }

    ReplaySubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> ssm, ReplayState<T, ?> state) {
        super(onSubscribe);
        this.ssm = ssm;
        this.state = state;
    }

    public void onNext(T t) {
        if (this.ssm.active) {
            this.state.next(t);
            for (SubjectObserver<? super T> o : this.ssm.observers()) {
                if (caughtUp(o)) {
                    o.onNext(t);
                }
            }
        }
    }

    public void onError(Throwable e) {
        if (this.ssm.active) {
            this.state.error(e);
            List<Throwable> errors = null;
            for (SubjectObserver<? super T> o : this.ssm.terminate(NotificationLite.instance().error(e))) {
                try {
                    if (caughtUp(o)) {
                        o.onError(e);
                    }
                } catch (Throwable e2) {
                    if (errors == null) {
                        errors = new ArrayList();
                    }
                    errors.add(e2);
                }
            }
            Exceptions.throwIfAny(errors);
        }
    }

    public void onCompleted() {
        if (this.ssm.active) {
            this.state.complete();
            for (SubjectObserver<? super T> o : this.ssm.terminate(NotificationLite.instance().completed())) {
                if (caughtUp(o)) {
                    o.onCompleted();
                }
            }
        }
    }

    int subscriberCount() {
        return this.ssm.state.observers.length;
    }

    public boolean hasObservers() {
        return this.ssm.observers().length > 0;
    }

    private boolean caughtUp(SubjectObserver<? super T> o) {
        if (o.caughtUp) {
            return true;
        }
        if (this.state.replayObserver(o)) {
            o.caughtUp = true;
            o.index(null);
        }
        return false;
    }

    @Experimental
    public boolean hasThrowable() {
        return this.ssm.nl.isError(this.ssm.get());
    }

    @Experimental
    public boolean hasCompleted() {
        NotificationLite<T> nl = this.ssm.nl;
        Object o = this.ssm.get();
        return (o == null || nl.isError(o)) ? false : true;
    }

    @Experimental
    public Throwable getThrowable() {
        NotificationLite<T> nl = this.ssm.nl;
        Object o = this.ssm.get();
        if (nl.isError(o)) {
            return nl.getError(o);
        }
        return null;
    }

    @Experimental
    public int size() {
        return this.state.size();
    }

    @Experimental
    public boolean hasAnyValue() {
        return !this.state.isEmpty();
    }

    @Experimental
    public boolean hasValue() {
        return hasAnyValue();
    }

    @Experimental
    public T[] getValues(T[] a) {
        return this.state.toArray(a);
    }

    public T getValue() {
        return this.state.latest();
    }
}
