package com.wiley.firewatch.asserts.observers.responce;

import com.wiley.firewatch.asserts.observers.IObserver;
import com.wiley.firewatch.core.utils.MatchingType;
import com.wiley.firewatch.core.utils.StringMatcher;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.core.har.HarResponse;

/**
 * Created by itatsiy on 4/24/2018.
 */
public class ResponseHeaderObserver implements IObserver<HarResponse> {
    private final MatchingType nameMatchingType;
    private final String name;
    private final MatchingType valueMatchingType;
    private final String value;

    public ResponseHeaderObserver(MatchingType nameMatchingType, String name, MatchingType valueMatchingType, String value) {
        this.nameMatchingType = nameMatchingType;
        this.name = name;
        this.valueMatchingType = valueMatchingType;
        this.value = value;
    }

    @Override
    public boolean observe(HarResponse har) {
        String actual = null;
        for (HarNameValuePair pair : har.getHeaders()) {
            if (StringMatcher.match(pair.getName(), nameMatchingType, name)) {
                actual = pair.getValue();
                break;
            }
        }
        return StringMatcher.match(actual, valueMatchingType, value);
    }

    @Override
    public String toString() {
        return "Header(nMatching='" + nameMatchingType + ", name='" + name + "', vMatching='" + valueMatchingType + "', value='" + value + "')";
    }
}
