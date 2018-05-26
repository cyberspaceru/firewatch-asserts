package com.wiley.firewatch.asserts;

import com.wiley.firewatch.asserts.enums.RelationshipType;
import com.wiley.firewatch.core.observers.har.responce.ResponseHeaderObserver;
import com.wiley.firewatch.core.observers.har.responce.ResponseJsonContentObserver;
import com.wiley.firewatch.core.observers.har.responce.ResponseStatusObserver;
import com.wiley.firewatch.core.observers.har.responce.ResponseTextContentObserver;
import com.wiley.firewatch.core.utils.ContentType;
import com.wiley.firewatch.core.utils.MatchingType;
import com.wiley.firewatch.core.utils.StringMatcher;
import net.lightbody.bmp.core.har.HarResponse;

import java.util.function.BiPredicate;

import static com.wiley.firewatch.core.utils.MatchingType.*;

public class FirewatchAssertionResponse extends FirewatchAssertion<HarResponse, FirewatchAssertionResponse> {
    FirewatchAssertionResponse(FirewatchAssertionBlueprint parent, RelationshipType relationship) {
        super(parent, relationship);
    }

    public FirewatchAssertionResponse status(int status) {
        return observe(new ResponseStatusObserver(status));
    }

    public FirewatchAssertionResponse headerEquals(String name, String value) {
        return header(name, EQUALS, value);
    }

    public FirewatchAssertionResponse header(String name, MatchingType type, String value) {
        return observe(new ResponseHeaderObserver(EQUALS, name, type, value));
    }

    public <K> FirewatchAssertionResponse jsonContentEquals(Class<K> objectClass, K instance) {
        return observe(new ResponseJsonContentObserver<>(objectClass, instance, Object::equals));
    }

    public <K> FirewatchAssertionResponse jsonContent(Class<K> objectClass, K instance, BiPredicate<K, K> predicate) {
        return observe(new ResponseJsonContentObserver<>(objectClass, instance, predicate));
    }

    public FirewatchAssertionResponse textContent(MatchingType matchingType, String expected) {
        return observe(new ResponseTextContentObserver(expected, (a, e) -> StringMatcher.match(a, matchingType, e)));
    }

    public FirewatchAssertionResponse textContent(String expected, BiPredicate<String, String> predicate) {
        return observe(new ResponseTextContentObserver(expected, predicate));
    }

    public FirewatchAssertionResponse contentType(ContentType contentType) {
        return observe(new ResponseHeaderObserver(EQUALS_IGNORE_CASE, "Content-Type", REGEXP_CASE_INSENSITIVE, contentType.toPattern()));
    }

    @Override
    public String toString() {
        return "Response" + super.toString();
    }
}
