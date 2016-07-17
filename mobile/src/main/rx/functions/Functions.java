package rx.functions;

public final class Functions {

    /* renamed from: rx.functions.Functions.10 */
    static class AnonymousClass10 implements FuncN<R> {
        final /* synthetic */ Func9 val$f;

        AnonymousClass10(Func9 func9) {
            this.val$f = func9;
        }

        public R call(Object... args) {
            if (args.length == 9) {
                return this.val$f.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
            }
            throw new RuntimeException("Func9 expecting 9 arguments.");
        }
    }

    /* renamed from: rx.functions.Functions.11 */
    static class AnonymousClass11 implements FuncN<Void> {
        final /* synthetic */ Action0 val$f;

        AnonymousClass11(Action0 action0) {
            this.val$f = action0;
        }

        public Void call(Object... args) {
            if (args.length != 0) {
                throw new RuntimeException("Action0 expecting 0 arguments.");
            }
            this.val$f.call();
            return null;
        }
    }

    /* renamed from: rx.functions.Functions.12 */
    static class AnonymousClass12 implements FuncN<Void> {
        final /* synthetic */ Action1 val$f;

        AnonymousClass12(Action1 action1) {
            this.val$f = action1;
        }

        public Void call(Object... args) {
            if (args.length != 1) {
                throw new RuntimeException("Action1 expecting 1 argument.");
            }
            this.val$f.call(args[0]);
            return null;
        }
    }

    /* renamed from: rx.functions.Functions.13 */
    static class AnonymousClass13 implements FuncN<Void> {
        final /* synthetic */ Action2 val$f;

        AnonymousClass13(Action2 action2) {
            this.val$f = action2;
        }

        public Void call(Object... args) {
            if (args.length != 2) {
                throw new RuntimeException("Action3 expecting 2 arguments.");
            }
            this.val$f.call(args[0], args[1]);
            return null;
        }
    }

    /* renamed from: rx.functions.Functions.14 */
    static class AnonymousClass14 implements FuncN<Void> {
        final /* synthetic */ Action3 val$f;

        AnonymousClass14(Action3 action3) {
            this.val$f = action3;
        }

        public Void call(Object... args) {
            if (args.length != 3) {
                throw new RuntimeException("Action3 expecting 3 arguments.");
            }
            this.val$f.call(args[0], args[1], args[2]);
            return null;
        }
    }

    /* renamed from: rx.functions.Functions.1 */
    static class C11421 implements FuncN<R> {
        final /* synthetic */ Func0 val$f;

        C11421(Func0 func0) {
            this.val$f = func0;
        }

        public R call(Object... args) {
            if (args.length == 0) {
                return this.val$f.call();
            }
            throw new RuntimeException("Func0 expecting 0 arguments.");
        }
    }

    /* renamed from: rx.functions.Functions.2 */
    static class C11432 implements FuncN<R> {
        final /* synthetic */ Func1 val$f;

        C11432(Func1 func1) {
            this.val$f = func1;
        }

        public R call(Object... args) {
            if (args.length == 1) {
                return this.val$f.call(args[0]);
            }
            throw new RuntimeException("Func1 expecting 1 argument.");
        }
    }

    /* renamed from: rx.functions.Functions.3 */
    static class C11443 implements FuncN<R> {
        final /* synthetic */ Func2 val$f;

        C11443(Func2 func2) {
            this.val$f = func2;
        }

        public R call(Object... args) {
            if (args.length == 2) {
                return this.val$f.call(args[0], args[1]);
            }
            throw new RuntimeException("Func2 expecting 2 arguments.");
        }
    }

    /* renamed from: rx.functions.Functions.4 */
    static class C11454 implements FuncN<R> {
        final /* synthetic */ Func3 val$f;

        C11454(Func3 func3) {
            this.val$f = func3;
        }

        public R call(Object... args) {
            if (args.length == 3) {
                return this.val$f.call(args[0], args[1], args[2]);
            }
            throw new RuntimeException("Func3 expecting 3 arguments.");
        }
    }

    /* renamed from: rx.functions.Functions.5 */
    static class C11465 implements FuncN<R> {
        final /* synthetic */ Func4 val$f;

        C11465(Func4 func4) {
            this.val$f = func4;
        }

        public R call(Object... args) {
            if (args.length == 4) {
                return this.val$f.call(args[0], args[1], args[2], args[3]);
            }
            throw new RuntimeException("Func4 expecting 4 arguments.");
        }
    }

    /* renamed from: rx.functions.Functions.6 */
    static class C11476 implements FuncN<R> {
        final /* synthetic */ Func5 val$f;

        C11476(Func5 func5) {
            this.val$f = func5;
        }

        public R call(Object... args) {
            if (args.length == 5) {
                return this.val$f.call(args[0], args[1], args[2], args[3], args[4]);
            }
            throw new RuntimeException("Func5 expecting 5 arguments.");
        }
    }

    /* renamed from: rx.functions.Functions.7 */
    static class C11487 implements FuncN<R> {
        final /* synthetic */ Func6 val$f;

        C11487(Func6 func6) {
            this.val$f = func6;
        }

        public R call(Object... args) {
            if (args.length == 6) {
                return this.val$f.call(args[0], args[1], args[2], args[3], args[4], args[5]);
            }
            throw new RuntimeException("Func6 expecting 6 arguments.");
        }
    }

    /* renamed from: rx.functions.Functions.8 */
    static class C11498 implements FuncN<R> {
        final /* synthetic */ Func7 val$f;

        C11498(Func7 func7) {
            this.val$f = func7;
        }

        public R call(Object... args) {
            if (args.length == 7) {
                return this.val$f.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            }
            throw new RuntimeException("Func7 expecting 7 arguments.");
        }
    }

    /* renamed from: rx.functions.Functions.9 */
    static class C11509 implements FuncN<R> {
        final /* synthetic */ Func8 val$f;

        C11509(Func8 func8) {
            this.val$f = func8;
        }

        public R call(Object... args) {
            if (args.length == 8) {
                return this.val$f.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            }
            throw new RuntimeException("Func8 expecting 8 arguments.");
        }
    }

    private Functions() {
        throw new IllegalStateException("No instances!");
    }

    public static <R> FuncN<R> fromFunc(Func0<? extends R> f) {
        return new C11421(f);
    }

    public static <T0, R> FuncN<R> fromFunc(Func1<? super T0, ? extends R> f) {
        return new C11432(f);
    }

    public static <T0, T1, R> FuncN<R> fromFunc(Func2<? super T0, ? super T1, ? extends R> f) {
        return new C11443(f);
    }

    public static <T0, T1, T2, R> FuncN<R> fromFunc(Func3<? super T0, ? super T1, ? super T2, ? extends R> f) {
        return new C11454(f);
    }

    public static <T0, T1, T2, T3, R> FuncN<R> fromFunc(Func4<? super T0, ? super T1, ? super T2, ? super T3, ? extends R> f) {
        return new C11465(f);
    }

    public static <T0, T1, T2, T3, T4, R> FuncN<R> fromFunc(Func5<? super T0, ? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        return new C11476(f);
    }

    public static <T0, T1, T2, T3, T4, T5, R> FuncN<R> fromFunc(Func6<? super T0, ? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
        return new C11487(f);
    }

    public static <T0, T1, T2, T3, T4, T5, T6, R> FuncN<R> fromFunc(Func7<? super T0, ? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
        return new C11498(f);
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, R> FuncN<R> fromFunc(Func8<? super T0, ? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
        return new C11509(f);
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, R> FuncN<R> fromFunc(Func9<? super T0, ? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
        return new AnonymousClass10(f);
    }

    public static FuncN<Void> fromAction(Action0 f) {
        return new AnonymousClass11(f);
    }

    public static <T0> FuncN<Void> fromAction(Action1<? super T0> f) {
        return new AnonymousClass12(f);
    }

    public static <T0, T1> FuncN<Void> fromAction(Action2<? super T0, ? super T1> f) {
        return new AnonymousClass13(f);
    }

    public static <T0, T1, T2> FuncN<Void> fromAction(Action3<? super T0, ? super T1, ? super T2> f) {
        return new AnonymousClass14(f);
    }
}
