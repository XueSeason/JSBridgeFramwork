package com.xiaoying.h5core.download;

import com.xiaoying.h5api.util.FileUtil;
import com.xiaoying.h5api.util.H5Log;
import com.xiaoying.h5core.download.Downloader.Status;

class Worker extends Thread {
    public static final String TAG = "Worker";

    public TaskInfo task;
    public boolean running;
    protected Client client;

    public Worker() {
        running = false;
        task = null;
    }

    public boolean disconnect() {
        if (running && client != null) {
            return client.disconnect();
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        running = true;
        H5Log.d(TAG, "worker enter " + getName());
        while (true) {
            task = TaskBoxImpl.getInstance().doTask();
            if (task == null) {
                break;
            }

            String url = task.getUrl();
            H5Log.d(TAG, "worker do " + url);

            client = new ApacheClient();
            client.setListener(task);

            task.setClient(client);
            String path = task.getPath();

            boolean result = client.connect(url, path);

            // in case succeed while disconnect
            long total = task.getTotalSize();
            if (total > 0 && FileUtil.size(path) >= total) {
                result = true;
            }

            if (result) {
                task.setProgress(100);
                task.setStatus(Status.SUCCEED);
            }

            if (!result && task.getStatus() == Status.DOWNLOADING) {
                task.setStatus(Status.FAILED);
            }
            task.setClient(null);
        }
        H5Log.d(TAG, "worker exit " + getName());
        running = false;
        task = null;
        client = null;
    }
}
