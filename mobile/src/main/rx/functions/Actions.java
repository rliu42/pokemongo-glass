package rx.functions;

public final class Actions {
    private static final EmptyAction EMPTY_ACTION;

    /* renamed from: rx.functions.Actions.10 */
    static class AnonymousClass10 implements Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {
        final /* synthetic */ Action9 val$action;
        final /* synthetic */ Object val$result;

        AnonymousClass10(Action9 action9, Object obj) {
            this.val$action = action9;
            this.val$result = obj;
        }

        public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
            this.val$action.call(t1, t2, t3, t4, t5, t6, t7, t8, t9);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.11 */
    static class AnonymousClass11 implements FuncN<R> {
        final /* synthetic */ ActionN val$action;
        final /* synthetic */ Object val$result;

        AnonymousClass11(ActionN actionN, Object obj) {
            this.val$action = actionN;
            this.val$result = obj;
        }

        public R call(Object... args) {
            this.val$action.call(args);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.1 */
    static class C11331 implements Func0<R> {
        final /* synthetic */ Action0 val$action;
        final /* synthetic */ Object val$result;

        C11331(Action0 action0, Object obj) {
            this.val$action = action0;
            this.val$result = obj;
        }

        public R call() {
            this.val$action.call();
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.2 */
    static class C11342 implements Func1<T1, R> {
        final /* synthetic */ Action1 val$action;
        final /* synthetic */ Object val$result;

        C11342(Action1 action1, Object obj) {
            this.val$action = action1;
            this.val$result = obj;
        }

        public R call(T1 t1) {
            this.val$action.call(t1);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.3 */
    static class C11353 implements Func2<T1, T2, R> {
        final /* synthetic */ Action2 val$action;
        final /* synthetic */ Object val$result;

        C11353(Action2 action2, Object obj) {
            this.val$action = action2;
            this.val$result = obj;
        }

        public R call(T1 t1, T2 t2) {
            this.val$action.call(t1, t2);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.4 */
    static class C11364 implements Func3<T1, T2, T3, R> {
        final /* synthetic */ Action3 val$action;
        final /* synthetic */ Object val$result;

        C11364(Action3 action3, Object obj) {
            this.val$action = action3;
            this.val$result = obj;
        }

        public R call(T1 t1, T2 t2, T3 t3) {
            this.val$action.call(t1, t2, t3);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.5 */
    static class C11375 implements Func4<T1, T2, T3, T4, R> {
        final /* synthetic */ Action4 val$action;
        final /* synthetic */ Object val$result;

        C11375(Action4 action4, Object obj) {
            this.val$action = action4;
            this.val$result = obj;
        }

        public R call(T1 t1, T2 t2, T3 t3, T4 t4) {
            this.val$action.call(t1, t2, t3, t4);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.6 */
    static class C11386 implements Func5<T1, T2, T3, T4, T5, R> {
        final /* synthetic */ Action5 val$action;
        final /* synthetic */ Object val$result;

        C11386(Action5 action5, Object obj) {
            this.val$action = action5;
            this.val$result = obj;
        }

        public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
            this.val$action.call(t1, t2, t3, t4, t5);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.7 */
    static class C11397 implements Func6<T1, T2, T3, T4, T5, T6, R> {
        final /* synthetic */ Action6 val$action;
        final /* synthetic */ Object val$result;

        C11397(Action6 action6, Object obj) {
            this.val$action = action6;
            this.val$result = obj;
        }

        public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
            this.val$action.call(t1, t2, t3, t4, t5, t6);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.8 */
    static class C11408 implements Func7<T1, T2, T3, T4, T5, T6, T7, R> {
        final /* synthetic */ Action7 val$action;
        final /* synthetic */ Object val$result;

        C11408(Action7 action7, Object obj) {
            this.val$action = action7;
            this.val$result = obj;
        }

        public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
            this.val$action.call(t1, t2, t3, t4, t5, t6, t7);
            return this.val$result;
        }
    }

    /* renamed from: rx.functions.Actions.9 */
    static class C11419 implements Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> {
        final /* synthetic */ Action8 val$action;
        final /* synthetic */ Object val$result;

        C11419(Action8 action8, Object obj) {
            this.val$action = action8;
            this.val$result = obj;
        }

        public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
            this.val$action.call(t1, t2, t3, t4, t5, t6, t7, t8);
            return this.val$result;
        }
    }

    private static final class EmptyAction<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Action0, Action1<T0>, Action2<T0, T1>, Action3<T0, T1, T2>, Action4<T0, T1, T2, T3>, Action5<T0, T1, T2, T3, T4>, Action6<T0, T1, T2, T3, T4, T5>, Action7<T0, T1, T2, T3, T4, T5, T6>, Action8<T0, T1, T2, T3, T4, T5, T6, T7>, Action9<T0, T1, T2, T3, T4, T5, T6, T7, T8>, ActionN {
        private EmptyAction() {
        }

        public void call() {
        }

        public void call(T0 t0) {
        }

        public void call(T0 t0, T1 t1) {
        }

        public void call(T0 t0, T1 t1, T2 t2) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        }

        public void call(Object... args) {
        }
    }

    private Actions() {
        throw new IllegalStateException("No instances!");
    }

    public static final <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> EmptyAction<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> empty() {
        return EMPTY_ACTION;
    }

    static {
        EMPTY_ACTION = new EmptyAction();
    }

    public static Func0<Void> toFunc(Action0 action) {
        return toFunc(action, (Void) null);
    }

    public static <T1> Func1<T1, Void> toFunc(Action1<T1> action) {
        return toFunc((Action1) action, (Void) null);
    }

    public static <T1, T2> Func2<T1, T2, Void> toFunc(Action2<T1, T2> action) {
        return toFunc((Action2) action, (Void) null);
    }

    public static <T1, T2, T3> Func3<T1, T2, T3, Void> toFunc(Action3<T1, T2, T3> action) {
        return toFunc((Action3) action, (Void) null);
    }

    public static <T1, T2, T3, T4> Func4<T1, T2, T3, T4, Void> toFunc(Action4<T1, T2, T3, T4> action) {
        return toFunc((Action4) action, (Void) null);
    }

    public static <T1, T2, T3, T4, T5> Func5<T1, T2, T3, T4, T5, Void> toFunc(Action5<T1, T2, T3, T4, T5> action) {
        return toFunc((Action5) action, (Void) null);
    }

    public static <T1, T2, T3, T4, T5, T6> Func6<T1, T2, T3, T4, T5, T6, Void> toFunc(Action6<T1, T2, T3, T4, T5, T6> action) {
        return toFunc((Action6) action, (Void) null);
    }

    public static <T1, T2, T3, T4, T5, T6, T7> Func7<T1, T2, T3, T4, T5, T6, T7, Void> toFunc(Action7<T1, T2, T3, T4, T5, T6, T7> action) {
        return toFunc((Action7) action, (Void) null);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8> Func8<T1, T2, T3, T4, T5, T6, T7, T8, Void> toFunc(Action8<T1, T2, T3, T4, T5, T6, T7, T8> action) {
        return toFunc((Action8) action, (Void) null);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Void> toFunc(Action9<T1, T2, T3, T4, T5, T6, T7, T8, T9> action) {
        return toFunc((Action9) action, (Void) null);
    }

    public static FuncN<Void> toFunc(ActionN action) {
        return toFunc(action, (Void) null);
    }

    public static <R> Func0<R> toFunc(Action0 action, R result) {
        return new C11331(action, result);
    }

    public static <T1, R> Func1<T1, R> toFunc(Action1<T1> action, R result) {
        return new C11342(action, result);
    }

    public static <T1, T2, R> Func2<T1, T2, R> toFunc(Action2<T1, T2> action, R result) {
        return new C11353(action, result);
    }

    public static <T1, T2, T3, R> Func3<T1, T2, T3, R> toFunc(Action3<T1, T2, T3> action, R result) {
        return new C11364(action, result);
    }

    public static <T1, T2, T3, T4, R> Func4<T1, T2, T3, T4, R> toFunc(Action4<T1, T2, T3, T4> action, R result) {
        return new C11375(action, result);
    }

    public static <T1, T2, T3, T4, T5, R> Func5<T1, T2, T3, T4, T5, R> toFunc(Action5<T1, T2, T3, T4, T5> action, R result) {
        return new C11386(action, result);
    }

    public static <T1, T2, T3, T4, T5, T6, R> Func6<T1, T2, T3, T4, T5, T6, R> toFunc(Action6<T1, T2, T3, T4, T5, T6> action, R result) {
        return new C11397(action, result);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> Func7<T1, T2, T3, T4, T5, T6, T7, R> toFunc(Action7<T1, T2, T3, T4, T5, T6, T7> action, R result) {
        return new C11408(action, result);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> toFunc(Action8<T1, T2, T3, T4, T5, T6, T7, T8> action, R result) {
        return new C11419(action, result);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> toFunc(Action9<T1, T2, T3, T4, T5, T6, T7, T8, T9> action, R result) {
        return new AnonymousClass10(action, result);
    }

    public static <R> FuncN<R> toFunc(ActionN action, R result) {
        return new AnonymousClass11(action, result);
    }
}
