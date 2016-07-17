package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;

public final class OperatorBufferWithSize<T> implements Operator<List<T>, T> {
    final int count;
    final int skip;

    /* renamed from: rx.internal.operators.OperatorBufferWithSize.1 */
    class C11951 extends Subscriber<T> {
        List<T> buffer;
        final /* synthetic */ Subscriber val$child;

        /* renamed from: rx.internal.operators.OperatorBufferWithSize.1.1 */
        class C11941 implements Producer {
            private volatile boolean infinite;
            final /* synthetic */ Producer val$producer;

            C11941(Producer producer) {
                this.val$producer = producer;
                this.infinite = false;
            }

            public void request(long n) {
                if (!this.infinite) {
                    if (n >= Long.MAX_VALUE / ((long) OperatorBufferWithSize.this.count)) {
                        this.infinite = true;
                        this.val$producer.request(Long.MAX_VALUE);
                        return;
                    }
                    this.val$producer.request(((long) OperatorBufferWithSize.this.count) * n);
                }
            }
        }

        C11951(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(new C11941(producer));
        }

        public void onNext(T t) {
            if (this.buffer == null) {
                this.buffer = new ArrayList(OperatorBufferWithSize.this.count);
            }
            this.buffer.add(t);
            if (this.buffer.size() == OperatorBufferWithSize.this.count) {
                List<T> oldBuffer = this.buffer;
                this.buffer = null;
                this.val$child.onNext(oldBuffer);
            }
        }

        public void onError(Throwable e) {
            this.buffer = null;
            this.val$child.onError(e);
        }

        public void onCompleted() {
            List<T> oldBuffer = this.buffer;
            this.buffer = null;
            if (oldBuffer != null) {
                try {
                    this.val$child.onNext(oldBuffer);
                } catch (Throwable t) {
                    onError(t);
                    return;
                }
            }
            this.val$child.onCompleted();
        }
    }

    /* renamed from: rx.internal.operators.OperatorBufferWithSize.2 */
    class C11972 extends Subscriber<T> {
        final List<List<T>> chunks;
        int index;
        final /* synthetic */ Subscriber val$child;

        /* renamed from: rx.internal.operators.OperatorBufferWithSize.2.1 */
        class C11961 implements Producer {
            private volatile boolean firstRequest;
            private volatile boolean infinite;
            final /* synthetic */ Producer val$producer;

            C11961(Producer producer) {
                this.val$producer = producer;
                this.firstRequest = true;
                this.infinite = false;
            }

            private void requestInfinite() {
                this.infinite = true;
                this.val$producer.request(Long.MAX_VALUE);
            }

            public void request(long n) {
                if (n != 0) {
                    if (n < 0) {
                        throw new IllegalArgumentException("request a negative number: " + n);
                    } else if (!this.infinite) {
                        if (n == Long.MAX_VALUE) {
                            requestInfinite();
                        } else if (this.firstRequest) {
                            this.firstRequest = false;
                            if (n - 1 >= (Long.MAX_VALUE - ((long) OperatorBufferWithSize.this.count)) / ((long) OperatorBufferWithSize.this.skip)) {
                                requestInfinite();
                            } else {
                                this.val$producer.request(((long) OperatorBufferWithSize.this.count) + (((long) OperatorBufferWithSize.this.skip) * (n - 1)));
                            }
                        } else if (n >= Long.MAX_VALUE / ((long) OperatorBufferWithSize.this.skip)) {
                            requestInfinite();
                        } else {
                            this.val$producer.request(((long) OperatorBufferWithSize.this.skip) * n);
                        }
                    }
                }
            }
        }

        C11972(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
            this.chunks = new LinkedList();
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(new C11961(producer));
        }

        public void onNext(T t) {
            int i = this.index;
            this.index = i + 1;
            if (i % OperatorBufferWithSize.this.skip == 0) {
                this.chunks.add(new ArrayList(OperatorBufferWithSize.this.count));
            }
            Iterator<List<T>> it = this.chunks.iterator();
            while (it.hasNext()) {
                List<T> chunk = (List) it.next();
                chunk.add(t);
                if (chunk.size() == OperatorBufferWithSize.this.count) {
                    it.remove();
                    this.val$child.onNext(chunk);
                }
            }
        }

        public void onError(Throwable e) {
            this.chunks.clear();
            this.val$child.onError(e);
        }

        public void onCompleted() {
            try {
                for (List<T> chunk : this.chunks) {
                    this.val$child.onNext(chunk);
                }
                this.val$child.onCompleted();
            } catch (Throwable t) {
                onError(t);
            } finally {
                this.chunks.clear();
            }
        }
    }

    public OperatorBufferWithSize(int count, int skip) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        } else if (skip <= 0) {
            throw new IllegalArgumentException("skip must be greater than 0");
        } else {
            this.count = count;
            this.skip = skip;
        }
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        return this.count == this.skip ? new C11951(child, child) : new C11972(child, child);
    }
}
