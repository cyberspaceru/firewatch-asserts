package com.wiley.firewatch.asserts.observers.responce;

import com.wiley.firewatch.asserts.observers.IObserver;
import com.wiley.firewatch.asserts.observers.common.JsonObserver;
import net.lightbody.bmp.core.har.HarResponse;

import java.util.function.BiPredicate;

/**
 * Created by itatsiy on 4/28/2018.
 */
public class ResponseJsonContentObserver<K> extends JsonObserver<K> implements IObserver<HarResponse> {
    public ResponseJsonContentObserver(Class<K> objectClass, K instance, BiPredicate<K, K> predicate) {
        super(objectClass, instance, predicate);
    }

    @Override
    public boolean observe(HarResponse har) {
        return observeJson(har.getContent() == null ? null : har.getContent().getText());
    }
}
