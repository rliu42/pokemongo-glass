package com.voxelbusters.nativeplugins.features.reachability;

import com.voxelbusters.nativeplugins.defines.CommonDefines;
import com.voxelbusters.nativeplugins.utilities.Debug;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HostConnectionPoller {
    private long connectionTimeOutPeriod;
    private int currentRetryCount;
    private String ip;
    private int maxRetryCount;
    private int port;
    private Future socketFutureTask;
    private float timeGapBetweenPolls;

    /* renamed from: com.voxelbusters.nativeplugins.features.reachability.HostConnectionPoller.1 */
    class C09831 implements Runnable {
        C09831() {
        }

        public void run() {
            InetSocketAddress address = new InetSocketAddress(HostConnectionPoller.this.getIp(), HostConnectionPoller.this.getPort());
            while (true) {
                Socket socket = new Socket();
                try {
                    socket.connect(address, (int) (HostConnectionPoller.this.getConnectionTimeOutPeriod() * 1000));
                    HostConnectionPoller.this.ReportConnectionSuccess();
                    socket.close();
                } catch (IOException e) {
                    HostConnectionPoller.this.ReportConnectionFailure();
                    e.printStackTrace();
                }
                try {
                    Thread.sleep((long) (HostConnectionPoller.this.timeGapBetweenPolls * 1000.0f));
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    HostConnectionPoller() {
        this.socketFutureTask = null;
        this.ip = "8.8.8.8";
        this.port = 56;
        this.connectionTimeOutPeriod = 60;
        this.maxRetryCount = 3;
        this.timeGapBetweenPolls = 2.0f;
    }

    void Start() {
        if (this.socketFutureTask != null) {
            this.socketFutureTask.cancel(true);
        }
        this.socketFutureTask = Executors.newSingleThreadExecutor().submit(new C09831());
    }

    private void ReportConnectionFailure() {
        this.currentRetryCount++;
        if (this.currentRetryCount > getMaxRetryCount()) {
            NetworkReachabilityHandler.sendSocketConnectionStatus(false);
            this.currentRetryCount = 0;
        }
    }

    private void ReportConnectionSuccess() {
        NetworkReachabilityHandler.sendSocketConnectionStatus(true);
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public float getTimeGapBetweenPolls() {
        return this.timeGapBetweenPolls;
    }

    public void setTimeGapBetweenPolls(float timeGapBetweenPolls) {
        this.timeGapBetweenPolls = timeGapBetweenPolls;
    }

    public long getConnectionTimeOutPeriod() {
        return this.connectionTimeOutPeriod;
    }

    public void setConnectionTimeOutPeriod(int connectionTimeOutPeriod) {
        if (connectionTimeOutPeriod != 0) {
            this.connectionTimeOutPeriod = (long) connectionTimeOutPeriod;
        } else {
            Debug.warning(CommonDefines.NETWORK_CONNECTIVITY_TAG, "time out value should not be zero. Considering default 60 secs for timeout");
        }
    }

    public int getMaxRetryCount() {
        return this.maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }
}
