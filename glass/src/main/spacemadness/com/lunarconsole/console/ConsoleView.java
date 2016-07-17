package spacemadness.com.lunarconsole.console;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import spacemadness.com.lunarconsole.C1391R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.debug.Tags;
import spacemadness.com.lunarconsole.ui.LogTypeButton;
import spacemadness.com.lunarconsole.ui.ToggleButton;
import spacemadness.com.lunarconsole.ui.ToggleButton.OnStateChangeListener;
import spacemadness.com.lunarconsole.ui.ToggleImageButton;
import spacemadness.com.lunarconsole.utils.StackTrace;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsole.utils.ThreadUtils;
import spacemadness.com.lunarconsole.utils.UIUtils;

public class ConsoleView extends LinearLayout implements Destroyable, LunarConsoleListener, OnStateChangeListener {
    private final Console console;
    private final LogTypeButton errorButton;
    private final ListView listView;
    private Listener listener;
    private final LogTypeButton logButton;
    private final TextView overflowText;
    private final ConsoleAdapter recyclerViewAdapter;
    private final View rootView;
    private ToggleImageButton scrollLockButton;
    private boolean scrollLocked;
    private boolean softKeyboardVisible;
    private final LogTypeButton warningButton;

    public interface Listener {
        void onClose(ConsoleView consoleView);
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.1 */
    class C14031 implements OnTouchListener {
        C14031() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.2 */
    class C14062 implements OnItemClickListener {
        final /* synthetic */ Console val$console;

        /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.2.1 */
        class C14041 implements Runnable {
            final /* synthetic */ Context val$ctx;
            final /* synthetic */ ConsoleEntry val$entry;
            final /* synthetic */ View val$view;

            C14041(View view, ConsoleEntry consoleEntry, Context context) {
                this.val$view = view;
                this.val$entry = consoleEntry;
                this.val$ctx = context;
            }

            public void run() {
                try {
                    this.val$view.setBackgroundColor(this.val$entry.getBackgroundColor(this.val$ctx));
                } catch (Throwable e) {
                    Log.m862e(e, "Error while settings entry background color", new Object[0]);
                }
            }
        }

        /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.2.2 */
        class C14052 implements OnClickListener {
            final /* synthetic */ ConsoleEntry val$entry;
            final /* synthetic */ String val$message;
            final /* synthetic */ String val$stackTrace;

            C14052(String str, ConsoleEntry consoleEntry, String str2) {
                this.val$message = str;
                this.val$entry = consoleEntry;
                this.val$stackTrace = str2;
            }

            public void onClick(DialogInterface dialog, int which) {
                String text = this.val$message;
                if (this.val$entry.hasStackTrace()) {
                    text = text + "\n\n" + this.val$stackTrace;
                }
                ConsoleView.this.copyToClipboard(text);
            }
        }

        C14062(Console console) {
            this.val$console = console;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Context ctx = ConsoleView.this.getContext();
            ConsoleEntry entry = this.val$console.getEntry(position);
            view.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            ThreadUtils.runOnUIThread(new C14041(view, entry, ctx), 200);
            Builder builder = new Builder(ctx);
            View contentView = LayoutInflater.from(ctx).inflate(C1391R.layout.lunar_layout_log_details_dialog, null);
            ImageView imageView = (ImageView) contentView.findViewById(C1391R.id.lunar_console_log_details_icon);
            TextView messageView = (TextView) contentView.findViewById(C1391R.id.lunar_console_log_details_message);
            TextView stacktraceView = (TextView) contentView.findViewById(C1391R.id.lunar_console_log_details_stacktrace);
            String message = entry.message;
            String stackTrace = entry.hasStackTrace() ? StackTrace.optimize(entry.stackTrace) : ConsoleView.this.getResources().getString(C1391R.string.lunar_console_log_details_dialog_no_stacktrace_warning);
            messageView.setText(message);
            stacktraceView.setText(stackTrace);
            imageView.setImageDrawable(entry.getIconDrawable(ctx));
            builder.setView(contentView);
            builder.setPositiveButton(C1391R.string.lunar_console_log_details_dialog_button_copy_to_clipboard, new C14052(message, entry, stackTrace));
            builder.create().show();
        }
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.3 */
    class C14073 implements OnTouchListener {
        C14073() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (ConsoleView.this.scrollLocked && event.getAction() == 0) {
                ConsoleView.this.scrollLockButton.setOn(false);
            }
            return false;
        }
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.4 */
    class C14084 implements TextWatcher {
        C14084() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            ConsoleView.this.filterByText(s.toString());
        }
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.5 */
    class C14095 implements View.OnClickListener {
        C14095() {
        }

        public void onClick(View v) {
            ConsoleView.this.softKeyboardVisible = true;
        }
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.6 */
    class C14106 implements OnEditorActionListener {
        C14106() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId != 3) {
                return false;
            }
            ConsoleView.this.hideSoftKeyboard();
            return true;
        }
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.7 */
    class C14117 implements View.OnClickListener {
        C14117() {
        }

        public void onClick(View v) {
            ConsoleView.this.clearConsole();
        }
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.8 */
    class C14128 implements ToggleImageButton.OnStateChangeListener {
        C14128() {
        }

        public void onStateChanged(ToggleImageButton button) {
            ConsoleView.this.toggleScrollLock();
        }
    }

    /* renamed from: spacemadness.com.lunarconsole.console.ConsoleView.9 */
    class C14139 implements View.OnClickListener {
        C14139() {
        }

        public void onClick(View v) {
            ConsoleView.this.copyConsoleOutputToClipboard();
        }
    }

    public ConsoleView(Context context, Console console) {
        super(context);
        if (console == null) {
            throw new NullPointerException("Console is null");
        }
        this.console = console;
        this.console.setConsoleListener(this);
        this.scrollLocked = ConsoleViewState.scrollLocked;
        setOnTouchListener(new C14031());
        this.rootView = LayoutInflater.from(context).inflate(C1391R.layout.lunar_layout_console, this, false);
        addView(this.rootView, new LayoutParams(-1, -1));
        this.recyclerViewAdapter = new ConsoleAdapter(console);
        LinearLayout recyclerViewContainer = (LinearLayout) findExistingViewById(C1391R.id.lunar_console_recycler_view_container);
        this.listView = new ListView(context);
        this.listView.setDivider(null);
        this.listView.setDividerHeight(0);
        this.listView.setAdapter(this.recyclerViewAdapter);
        this.listView.setOverScrollMode(2);
        this.listView.setScrollingCacheEnabled(false);
        this.listView.setOnItemClickListener(new C14062(console));
        this.listView.setOnTouchListener(new C14073());
        recyclerViewContainer.addView(this.listView, new LayoutParams(-1, -1));
        setupFilterTextEdit();
        this.logButton = (LogTypeButton) findExistingViewById(C1391R.id.lunar_console_log_button);
        this.warningButton = (LogTypeButton) findExistingViewById(C1391R.id.lunar_console_warning_button);
        this.errorButton = (LogTypeButton) findExistingViewById(C1391R.id.lunar_console_error_button);
        setupLogTypeButtons();
        setupOperationsButtons();
        setupFakeStatusBar();
        this.overflowText = (TextView) findExistingViewById(C1391R.id.lunar_console_text_overflow);
        reloadData();
    }

    public void destroy() {
        Log.m860d(Tags.CONSOLE, "Destroy console", new Object[0]);
        this.console.setConsoleListener(null);
        setListener(null);
    }

    private void filterByText(String text) {
        if (this.console.entries().setFilterByText(text)) {
            reloadData();
        }
    }

    private void setFilterByLogTypeMask(int logTypeMask, boolean disabled) {
        if (this.console.entries().setFilterByLogTypeMask(logTypeMask, disabled)) {
            reloadData();
        }
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() != 4) {
            return super.dispatchKeyEventPreIme(event);
        }
        if (event.getAction() != 1) {
            return true;
        }
        if (this.softKeyboardVisible) {
            hideSoftKeyboard();
            return true;
        }
        notifyClose();
        return true;
    }

    private void hideSoftKeyboard() {
        this.softKeyboardVisible = false;
        ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 0);
    }

    private void notifyClose() {
        this.softKeyboardVisible = false;
        if (this.listener != null) {
            this.listener.onClose(this);
        }
    }

    private void reloadData() {
        this.recyclerViewAdapter.notifyDataSetChanged();
        updateOverflowText();
    }

    private void clearConsole() {
        this.console.clear();
    }

    private boolean copyConsoleOutputToClipboard() {
        return copyToClipboard(this.console.getText());
    }

    private boolean sendConsoleOutputByEmail() {
        try {
            String packageName = getContext().getPackageName();
            String subject = StringUtils.TryFormat("'%s' console log", packageName);
            String outputText = this.console.getText();
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra("android.intent.extra.SUBJECT", subject);
            intent.putExtra("android.intent.extra.TEXT", outputText);
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(intent);
                return true;
            }
            UIUtils.showToast(getContext(), "Can't send email");
            return false;
        } catch (Throwable e) {
            Log.m862e(e, "Error while trying to send console output by email", new Object[0]);
            return false;
        }
    }

    private void toggleScrollLock() {
        this.scrollLocked = !this.scrollLocked;
        ConsoleViewState.scrollLocked = this.scrollLocked;
        scrollToBottom(this.console);
    }

    private void scrollToBottom(Console console) {
        if (this.scrollLocked) {
            int entryCount = console.getEntryCount();
            if (entryCount > 0) {
                this.listView.setSelection(entryCount - 1);
            }
        }
    }

    private void scrollToTop(Console console) {
        if (console.getEntryCount() > 0) {
            this.listView.setSelection(0);
        }
    }

    private EditText setupFilterTextEdit() {
        EditText editText = (EditText) findExistingViewById(C1391R.id.lunar_console_text_edit_filter);
        String filterText = this.console.entries().getFilterText();
        if (!StringUtils.IsNullOrEmpty(filterText)) {
            editText.setText(filterText);
            editText.setSelection(filterText.length());
        }
        editText.addTextChangedListener(new C14084());
        editText.setOnClickListener(new C14095());
        editText.setOnEditorActionListener(new C14106());
        return editText;
    }

    private void setupLogTypeButtons() {
        setupLogTypeButton(this.logButton, 3);
        setupLogTypeButton(this.warningButton, 2);
        setupLogTypeButton(this.errorButton, 0);
        updateLogButtons();
    }

    private void setupLogTypeButton(LogTypeButton button, int logType) {
        button.setOn(this.console.entries().isFilterLogTypeEnabled(logType));
        button.setOnStateChangeListener(this);
    }

    private void setupOperationsButtons() {
        setOnClickListener(C1391R.id.lunar_console_button_clear, new C14117());
        this.scrollLockButton = (ToggleImageButton) this.rootView.findViewById(C1391R.id.lunar_console_button_lock);
        Resources resources = getContext().getResources();
        this.scrollLockButton.setOnDrawable(resources.getDrawable(C1391R.drawable.lunar_console_icon_button_lock));
        this.scrollLockButton.setOffDrawable(resources.getDrawable(C1391R.drawable.lunar_console_icon_button_unlock));
        this.scrollLockButton.setOn(this.scrollLocked);
        this.scrollLockButton.setOnStateChangeListener(new C14128());
        setOnClickListener(C1391R.id.lunar_console_button_copy, new C14139());
        setOnClickListener(C1391R.id.lunar_console_button_email, new View.OnClickListener() {
            public void onClick(View v) {
                ConsoleView.this.sendConsoleOutputByEmail();
            }
        });
        setOnClickListener(C1391R.id.lunar_console_button_close, new View.OnClickListener() {
            public void onClick(View v) {
                ConsoleView.this.notifyClose();
            }
        });
    }

    private void setupFakeStatusBar() {
        TextView statusBar = (TextView) findExistingViewById(C1391R.id.lunar_console_fake_status_bar);
        statusBar.setText(String.format(getResources().getString(C1391R.string.lunar_console_title_fake_status_bar), new Object[]{ConsolePlugin.getVersion()}));
        statusBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConsoleView.this.scrollLockButton.setOn(false);
                ConsoleView.this.scrollToTop(ConsoleView.this.console);
            }
        });
    }

    private void updateLogButtons() {
        ConsoleEntryList entries = this.console.entries();
        this.logButton.setCount(entries.getLogCount());
        this.warningButton.setCount(entries.getWarningCount());
        this.errorButton.setCount(entries.getErrorCount());
    }

    public void onAddEntry(Console console, ConsoleEntry entry, boolean filtered) {
        if (filtered) {
            this.recyclerViewAdapter.notifyDataSetChanged();
            scrollToBottom(console);
        }
        updateLogButtons();
    }

    public void onRemoveEntries(Console console, int start, int length) {
        this.recyclerViewAdapter.notifyDataSetChanged();
        scrollToBottom(console);
        updateLogButtons();
        updateOverflowText();
    }

    public void onClearEntries(Console console) {
        reloadData();
        updateLogButtons();
    }

    private void updateOverflowText() {
        if (this.console.trimmedCount() > 0) {
            this.overflowText.setVisibility(0);
            this.overflowText.setText(getResources().getString(C1391R.string.lunar_console_overflow_warning_text, new Object[]{Integer.valueOf(trimmedCount)}));
            return;
        }
        this.overflowText.setVisibility(8);
    }

    public void onStateChanged(ToggleButton button) {
        boolean z = true;
        int mask = 0;
        if (button == this.logButton) {
            mask = 0 | ConsoleLogType.getMask(3);
        } else if (button == this.warningButton) {
            mask = 0 | ConsoleLogType.getMask(2);
        } else if (button == this.errorButton) {
            mask = 0 | ((ConsoleLogType.getMask(4) | ConsoleLogType.getMask(0)) | ConsoleLogType.getMask(1));
        }
        if (button.isOn()) {
            z = false;
        }
        setFilterByLogTypeMask(mask, z);
    }

    private boolean copyToClipboard(String outputText) {
        try {
            ((ClipboardManager) getContext().getSystemService("clipboard")).setText(outputText);
            UIUtils.showToast(getContext(), "Copied to clipboard");
            return true;
        } catch (Throwable e) {
            Log.m862e(e, "Error to copy text to clipboard", new Object[0]);
            return false;
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return this.listener;
    }

    private <T extends View> T findExistingViewById(int id) throws ClassCastException {
        return findExistingViewById(this.rootView, id);
    }

    private <T extends View> T findExistingViewById(View parent, int id) throws ClassCastException {
        View view = parent.findViewById(id);
        if (view != null) {
            return view;
        }
        throw new IllegalArgumentException("View with id " + id + " not found");
    }

    private void setOnClickListener(int viewId, View.OnClickListener listener) {
        findExistingViewById(viewId).setOnClickListener(listener);
    }
}
