package com.wiley.firewatch.asserts.observers.request;

import com.wiley.firewatch.asserts.observers.IObserver;
import com.wiley.firewatch.asserts.observers.common.JsonObserver;
import net.lightbody.bmp.core.har.HarRequest;

import java.util.function.BiPredicate;

/**
 * Created by itatsiy on 4/28/2018.
 */
public class RequestJsonPostDataObserver<K> extends JsonObserver<K> implements IObserver<HarRequest> {
    public RequestJsonPostDataObserver(Class<K> objectClass, K instance, BiPredicate<K, K> predicate) {
        super(objectClass, instance, predicate);
    }

    @Override
    public boolean observe(HarRequest har) {
        return observeJson(har.getPostData() == null ? null : har.getPostData().getText());
    }
}
