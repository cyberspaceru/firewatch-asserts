package com.wiley.firewatch.asserts.observers.responce;

import com.wiley.firewatch.asserts.observers.IObserver;
import com.wiley.firewatch.asserts.observers.common.TextObserver;
import net.lightbody.bmp.core.har.HarResponse;

import java.util.function.BiPredicate;

/**
 * Created by itatsiy on 4/28/2018.
 */
public class ResponseTextContentObserver extends TextObserver implements IObserver<HarResponse> {
    public ResponseTextContentObserver(String expected, BiPredicate<String, String> predicate) {
        super(expected, predicate);
    }

    @Override
    public boolean observe(HarResponse har) {
        return observeText(har.getContent() == null ? null : har.getContent().getText());
    }
}
