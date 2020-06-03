package com.idgindigo.pms.channel.wubook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vomel
 * @since 31.01.14 12:23
 */
public abstract class WubookCaller<T> {
    private static final Logger logger = LoggerFactory.getLogger(WubookCaller.class);
    private final WubookAccount account;

    public WubookCaller(WubookAccount account) {
        this.account = account;
    }

    public T call() {
        Wubook wubook = new WubookImpl(account);
        String token = wubook.getToken();
        WubookException e1;
        try {
            return run(wubook, token);
        } catch (WubookException e) {
            e1 = e;
            onFailure(e1);
        } finally {
            wubook.release(token);
        }
        throw e1;
    }

    protected void onFailure(WubookException e) {
        logger.error("Transmission error", e);
        throw e;
    }

    protected abstract T run(Wubook wubook, String token);

}
