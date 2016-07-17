package android.support.v4.speech.tts;

import android.os.Build.VERSION;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.speech.tts.UtteranceProgressListener;
import java.util.Locale;
import java.util.Set;

class TextToSpeechICSMR1 {
    public static final String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";
    public static final String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";

    /* renamed from: android.support.v4.speech.tts.TextToSpeechICSMR1.1 */
    static class C00631 extends UtteranceProgressListener {
        final /* synthetic */ UtteranceProgressListenerICSMR1 val$listener;

        C00631(UtteranceProgressListenerICSMR1 utteranceProgressListenerICSMR1) {
            this.val$listener = utteranceProgressListenerICSMR1;
        }

        public void onStart(String utteranceId) {
            this.val$listener.onStart(utteranceId);
        }

        public void onError(String utteranceId) {
            this.val$listener.onError(utteranceId);
        }

        public void onDone(String utteranceId) {
            this.val$listener.onDone(utteranceId);
        }
    }

    /* renamed from: android.support.v4.speech.tts.TextToSpeechICSMR1.2 */
    static class C00642 implements OnUtteranceCompletedListener {
        final /* synthetic */ UtteranceProgressListenerICSMR1 val$listener;

        C00642(UtteranceProgressListenerICSMR1 utteranceProgressListenerICSMR1) {
            this.val$listener = utteranceProgressListenerICSMR1;
        }

        public void onUtteranceCompleted(String utteranceId) {
            this.val$listener.onStart(utteranceId);
            this.val$listener.onDone(utteranceId);
        }
    }

    interface UtteranceProgressListenerICSMR1 {
        void onDone(String str);

        void onError(String str);

        void onStart(String str);
    }

    TextToSpeechICSMR1() {
    }

    static Set<String> getFeatures(TextToSpeech tts, Locale locale) {
        if (VERSION.SDK_INT >= 15) {
            return tts.getFeatures(locale);
        }
        return null;
    }

    static void setUtteranceProgressListener(TextToSpeech tts, UtteranceProgressListenerICSMR1 listener) {
        if (VERSION.SDK_INT >= 15) {
            tts.setOnUtteranceProgressListener(new C00631(listener));
        } else {
            tts.setOnUtteranceCompletedListener(new C00642(listener));
        }
    }
}
