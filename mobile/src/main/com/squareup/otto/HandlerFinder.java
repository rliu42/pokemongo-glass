package com.squareup.otto;

import java.util.Map;
import java.util.Set;

interface HandlerFinder {
    public static final HandlerFinder ANNOTATED;

    /* renamed from: com.squareup.otto.HandlerFinder.1 */
    static class C08011 implements HandlerFinder {
        C08011() {
        }

        public Map<Class<?>, EventProducer> findAllProducers(Object listener) {
            return AnnotatedHandlerFinder.findAllProducers(listener);
        }

        public Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object listener) {
            return AnnotatedHandlerFinder.findAllSubscribers(listener);
        }
    }

    Map<Class<?>, EventProducer> findAllProducers(Object obj);

    Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object obj);

    static {
        ANNOTATED = new C08011();
    }
}
