package com.nianticlabs.nia.network;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;

public class NiaNet {
    private static final int CHUNK_SIZE = 32768;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_OK = 200;
    private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    private static final int METHOD_DELETE = 4;
    private static final int METHOD_GET = 0;
    private static final int METHOD_HEAD = 1;
    private static final int METHOD_OPTIONS = 5;
    private static final int METHOD_POST = 2;
    private static final int METHOD_PUT = 3;
    private static final int METHOD_TRACE = 6;
    private static final int NETWORK_TIMEOUT_MS = 15000;
    private static final int POOL_THREAD_NUM = 6;
    private static final String TAG = "NiaNet";
    private static final ThreadPoolExecutor executor;
    private static Set<Integer> pendingRequestIds;
    static ThreadLocal<ByteBuffer> readBuffer;
    private static final ThreadLocal<byte[]> threadChunk;

    /* renamed from: com.nianticlabs.nia.network.NiaNet.1 */
    static class C07721 extends ThreadLocal<byte[]> {
        C07721() {
        }

        protected byte[] initialValue() {
            return new byte[NiaNet.CHUNK_SIZE];
        }
    }

    /* renamed from: com.nianticlabs.nia.network.NiaNet.2 */
    static class C07732 extends ThreadLocal<ByteBuffer> {
        C07732() {
        }

        protected ByteBuffer initialValue() {
            return ByteBuffer.allocateDirect(NiaNet.CHUNK_SIZE);
        }
    }

    /* renamed from: com.nianticlabs.nia.network.NiaNet.3 */
    static class C07743 implements Runnable {
        final /* synthetic */ ByteBuffer val$body;
        final /* synthetic */ int val$bodyOffset;
        final /* synthetic */ int val$bodySize;
        final /* synthetic */ String val$headers;
        final /* synthetic */ int val$method;
        final /* synthetic */ long val$object;
        final /* synthetic */ int val$request_id;
        final /* synthetic */ String val$url;

        C07743(long j, int i, String str, int i2, String str2, ByteBuffer byteBuffer, int i3, int i4) {
            this.val$object = j;
            this.val$request_id = i;
            this.val$url = str;
            this.val$method = i2;
            this.val$headers = str2;
            this.val$body = byteBuffer;
            this.val$bodyOffset = i3;
            this.val$bodySize = i4;
        }

        public void run() {
            NiaNet.doSyncRequest(this.val$object, this.val$request_id, this.val$url, this.val$method, this.val$headers, this.val$body, this.val$bodyOffset, this.val$bodySize);
        }
    }

    private static native void nativeCallback(long j, int i, String str, ByteBuffer byteBuffer, int i2, int i3);

    static {
        executor = new ThreadPoolExecutor(POOL_THREAD_NUM, 12, 5, TimeUnit.SECONDS, new LinkedBlockingQueue());
        pendingRequestIds = new HashSet();
        threadChunk = new C07721();
        readBuffer = new C07732();
    }

    public static void request(long object, int request_id, String url, int method, String headers, ByteBuffer body, int bodyOffset, int bodySize) {
        synchronized (pendingRequestIds) {
            pendingRequestIds.add(Integer.valueOf(request_id));
        }
        executor.execute(new C07743(object, request_id, url, method, headers, body, bodyOffset, bodySize));
    }

    public static void cancel(int request_id) {
        synchronized (pendingRequestIds) {
            pendingRequestIds.remove(Integer.valueOf(request_id));
        }
    }

    private NiaNet() {
    }

    private static int readDataSteam(HttpURLConnection conn) throws IOException {
        InputStream is;
        if (conn.getResponseCode() == HTTP_OK) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }
        if (is == null) {
            return METHOD_GET;
        }
        ByteBuffer buffer = (ByteBuffer) readBuffer.get();
        try {
            int length;
            byte[] chunk = buffer.array();
            int bufferOffset = buffer.arrayOffset();
            int offset = bufferOffset;
            boolean keepReading = true;
            while (true) {
                int available = is.available();
                length = chunk.length;
                if (r0 <= available + offset) {
                    ByteBuffer newBuffer = ByteBuffer.allocateDirect(((available + offset) - bufferOffset) * METHOD_POST);
                    int bytesToCopy = offset - bufferOffset;
                    int newBufferOffset = newBuffer.arrayOffset();
                    if (bytesToCopy > 0) {
                        System.arraycopy(chunk, bufferOffset, newBuffer.array(), newBufferOffset, bytesToCopy);
                    }
                    bufferOffset = newBufferOffset;
                    offset = bytesToCopy + newBufferOffset;
                    chunk = newBuffer.array();
                    buffer = newBuffer;
                    readBuffer.set(buffer);
                }
                int i = offset;
                int bytesRead = is.read(chunk, i, chunk.length - offset);
                if (bytesRead >= 0) {
                    offset += bytesRead;
                    continue;
                } else {
                    keepReading = false;
                    continue;
                }
                if (!keepReading) {
                    break;
                }
            }
            length = offset - bufferOffset;
            return length;
        } finally {
            is.close();
        }
    }

    private static void doSyncRequest(long object, int request_id, String url, int method, String headers, ByteBuffer body, int bodyOffset, int bodyCount) {
        synchronized (pendingRequestIds) {
            if (!pendingRequestIds.contains(Integer.valueOf(request_id))) {
                return;
            }
            int responseSize;
            pendingRequestIds.remove(Integer.valueOf(request_id));
            HttpURLConnection conn = null;
            int responseCode = HTTP_BAD_REQUEST;
            String responseHeaders = null;
            OutputStream os;
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                setHeaders(conn, headers);
                conn.setConnectTimeout(NETWORK_TIMEOUT_MS);
                conn.setRequestProperty("Connection", "Keep-Alive");
                HttpURLConnection.setFollowRedirects(false);
                conn.setRequestMethod(getMethodString(method));
                if (body != null && bodyCount > 0) {
                    conn.setDoOutput(true);
                    os = conn.getOutputStream();
                    if (body.hasArray()) {
                        os.write(body.array(), body.arrayOffset() + bodyOffset, bodyCount);
                    } else {
                        byte[] chunk = (byte[]) threadChunk.get();
                        while (body.hasRemaining()) {
                            int bytesToRead = Math.min(body.remaining(), chunk.length);
                            body.get(chunk, METHOD_GET, bytesToRead);
                            os.write(chunk, METHOD_GET, bytesToRead);
                        }
                    }
                    os.close();
                }
                responseCode = conn.getResponseCode();
                responseHeaders = joinHeaders(conn);
                responseSize = readDataSteam(conn);
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                try {
                    Log.e(TAG, "Network op failed: " + e.getMessage());
                    responseSize = METHOD_GET;
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Throwable th) {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            } catch (Throwable th2) {
                os.close();
            }
            if (responseSize > 0) {
                nativeCallback(object, responseCode, responseHeaders, (ByteBuffer) readBuffer.get(), METHOD_GET, responseSize);
            } else {
                nativeCallback(object, responseCode, responseHeaders, null, METHOD_GET, METHOD_GET);
            }
        }
    }

    private static String getMethodString(int method) {
        switch (method) {
            case METHOD_GET /*0*/:
                return "GET";
            case METHOD_HEAD /*1*/:
                return "HEAD";
            case METHOD_POST /*2*/:
                return "POST";
            case METHOD_PUT /*3*/:
                return "PUT";
            case METHOD_DELETE /*4*/:
                return "DELETE";
            default:
                Log.e(TAG, "Unsupported HTTP method " + method + ", using GET.");
                return "GET";
        }
    }

    private static void setHeaders(HttpURLConnection conn, String headers) {
        if (headers != null && !headers.isEmpty()) {
            int start = METHOD_GET;
            do {
                int newLine = headers.indexOf(10, start);
                if (newLine < 0) {
                    newLine = headers.length();
                }
                int colon = headers.indexOf(58, start);
                if (colon < 0) {
                    colon = headers.length();
                }
                String key = headers.substring(start, colon);
                String value = headers.substring(colon + METHOD_HEAD, newLine);
                if (IF_MODIFIED_SINCE.equals(key)) {
                    try {
                        conn.setIfModifiedSince(parseHttpDateTime(value));
                    } catch (ParseException e) {
                        Log.e(TAG, "If-Modified-Since Date/Time parse failed. " + e.getMessage());
                    }
                } else {
                    conn.setRequestProperty(key, value);
                }
                start = newLine + METHOD_HEAD;
            } while (start < headers.length());
        }
    }

    private static String joinHeaders(HttpURLConnection conn) {
        StringBuilder headers = new StringBuilder();
        int i = METHOD_GET;
        while (true) {
            String fieldKey = conn.getHeaderFieldKey(i);
            if (fieldKey == null) {
                break;
            }
            String field = conn.getHeaderField(i);
            if (field == null) {
                break;
            }
            headers.append(fieldKey);
            headers.append(": ");
            headers.append(field);
            headers.append(IOUtils.LINE_SEPARATOR_UNIX);
            i += METHOD_HEAD;
        }
        if (headers.length() == 0) {
            return null;
        }
        return headers.toString();
    }

    private static long parseHttpDateTime(String s) throws ParseException {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(s).getTime();
    }
}
