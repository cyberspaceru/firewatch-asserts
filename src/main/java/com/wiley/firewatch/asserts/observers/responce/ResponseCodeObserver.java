package com.wiley.firewatch.asserts.observers.responce;

import com.wiley.firewatch.asserts.observers.IObserver;
import net.lightbody.bmp.core.har.HarResponse;

/**
 * Created by itatsiy on 4/23/2018.
 */
public class ResponseCodeObserver implements IObserver<HarResponse> {
    private int code;

    public ResponseCodeObserver(int code) {
        this.code = code;
    }

    @Override
    public boolean observe(HarResponse har) {
        return har.getStatus() == code;
    }

    @Override
    public String toString() {
        return "Code(" + code + ")";
    }
}
