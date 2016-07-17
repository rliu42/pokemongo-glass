package spacemadness.com.lunarconsole.console;

import java.util.ArrayList;
import java.util.List;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.utils.ThreadUtils;

class ConsoleEntryDispatcher {
    private final Runnable dispatchRunnable;
    private final List<ConsoleEntry> entries;
    private final OnDispatchListener listener;

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleEntryDispatcher.1 */
    class C13931 implements Runnable {
        C13931() {
        }

        public void run() {
            ConsoleEntryDispatcher.this.dispatchEntries();
        }
    }

    public interface OnDispatchListener {
        void onDispatchEntries(List<ConsoleEntry> list);
    }

    public ConsoleEntryDispatcher(OnDispatchListener listener) {
        if (listener == null) {
            throw new NullPointerException("Listener is null");
        }
        this.listener = listener;
        this.entries = new ArrayList();
        this.dispatchRunnable = createDispatchRunnable();
    }

    public void add(ConsoleEntry entry) {
        synchronized (this.entries) {
            this.entries.add(entry);
            if (this.entries.size() == 1) {
                postEntriesDispatch();
            }
        }
    }

    protected void postEntriesDispatch() {
        ThreadUtils.runOnUIThread(this.dispatchRunnable);
    }

    protected void cancelEntriesDispatch() {
        ThreadUtils.cancel(this.dispatchRunnable);
    }

    protected void dispatchEntries() {
        synchronized (this.entries) {
            try {
                this.listener.onDispatchEntries(this.entries);
            } catch (Throwable e) {
                Log.m862e(e, "Can't dispatch entries", new Object[0]);
            }
            this.entries.clear();
        }
    }

    private Runnable createDispatchRunnable() {
        return new C13931();
    }

    public void cancelAll() {
        cancelEntriesDispatch();
        synchronized (this.entries) {
            this.entries.clear();
        }
    }
}
