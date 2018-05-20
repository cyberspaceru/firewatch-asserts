package com.wiley.firewatch.asserts;

import com.wiley.firewatch.asserts.enums.RelationshipType;
import com.wiley.firewatch.core.utils.MatchingType;
import com.wiley.firewatch.asserts.observers.request.*;
import com.wiley.firewatch.core.utils.ContentType;
import com.wiley.firewatch.core.utils.StringMatcher;
import io.netty.handler.codec.http.HttpMethod;
import net.lightbody.bmp.core.har.HarRequest;

import java.util.function.BiPredicate;

import static com.wiley.firewatch.core.utils.MatchingType.EQUALS_IGNORE_CASE;
import static com.wiley.firewatch.core.utils.MatchingType.REGEXP_CASE_INSENSITIVE;

/**
 * Created by itatsiy on 4/23/2018.
 */
public class FirewatchRequest extends Firewatch<HarRequest, FirewatchRequest> {
    FirewatchRequest(FirewatchBlueprint parent, RelationshipType relationship) {
        super(parent, relationship);
    }

    public FirewatchResponse thenResponse() {
        return response(this, RelationshipType.THEN);
    }

    public FirewatchRequest url(MatchingType type, String url) {
        return observe(new RequestUrlObserver(type, url));
    }

    public FirewatchRequest method(HttpMethod method) {
        return observe(new RequestMethodObserver(method));
    }

    public FirewatchRequest headerEquals(String name, String value) {
        return header(name, MatchingType.EQUALS, value);
    }

    public FirewatchRequest header(String name, MatchingType type, String value) {
        return observe(new RequestHeaderObserver(MatchingType.EQUALS, name, type, value));
    }

    public <K> FirewatchRequest jsonPostDataEquals(Class<K> objectClass, K instance) {
        return observe(new RequestJsonPostDataObserver<>(objectClass, instance, Object::equals));
    }

    public <K> FirewatchRequest jsonPostData(Class<K> objectClass, K instance, BiPredicate<K, K> predicate) {
        return observe(new RequestJsonPostDataObserver<>(objectClass, instance, predicate));
    }

    public FirewatchRequest textPostData(MatchingType matchingType, String expected) {
        return observe(new RequestTextPostDataObserver(expected, (a, e) -> StringMatcher.match(a, matchingType, e)));
    }

    public FirewatchRequest textPostData(String expected, BiPredicate<String, String> predicate) {
        return observe(new RequestTextPostDataObserver(expected, predicate));
    }

    public FirewatchRequest contentType(ContentType contentType) {
        return observe(new RequestHeaderObserver(EQUALS_IGNORE_CASE, "Content-Type", REGEXP_CASE_INSENSITIVE, contentType.toPattern()));
    }

    public FirewatchRequest parameterEquals(String name, String value) {
        return parameter(name, MatchingType.EQUALS, value);
    }

    public FirewatchRequest parameter(String name, MatchingType type, String value) {
        return observe(new RequestParameterObserver(MatchingType.EQUALS, name, type, value));
    }

    @Override
    public String toString() {
        return "Request" + super.toString();
    }
}
