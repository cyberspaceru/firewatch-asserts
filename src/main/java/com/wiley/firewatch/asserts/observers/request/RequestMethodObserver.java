package com.wiley.firewatch.asserts.observers.request;

import com.wiley.firewatch.asserts.observers.IObserver;
import io.netty.handler.codec.http.HttpMethod;
import net.lightbody.bmp.core.har.HarRequest;

/**
 * Created by itatsiy on 4/23/2018.
 */
public class RequestMethodObserver implements IObserver<HarRequest> {
    private HttpMethod method;

    public RequestMethodObserver(HttpMethod method) {
        this.method = method;
    }

    @Override
    public boolean observe(HarRequest har) {
        return har.getMethod().equalsIgnoreCase(method.name());
    }

    @Override
    public String toString() {
        return "Method(" + method.name() + ")";
    }
}
