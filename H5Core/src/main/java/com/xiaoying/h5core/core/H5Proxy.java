/**
 *
 */

package com.xiaoying.h5core.core;

public class H5Proxy {
    H5ProxyStateListener listener;

    /**
     * set listener to H5Proxy, listener cleared when null is set
     */
    public void setStateListenr(H5ProxyStateListener listener) {
        this.listener = listener;
    }

    public boolean start() {
        return true;
    }

    public boolean stop() {
        return true;
    }
}
