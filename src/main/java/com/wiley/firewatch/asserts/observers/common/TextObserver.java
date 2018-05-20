package com.wiley.firewatch.asserts.observers.common;

import java.util.function.BiPredicate;

/**
 * Created by itatsiy on 4/28/2018.
 */
public abstract class TextObserver {
    private final String expected;
    private final BiPredicate<String, String> predicate;

    public TextObserver(String expected, BiPredicate<String, String>predicate) {
        this.expected = expected;
        this.predicate = predicate;
    }

    protected boolean observeText(String actual) {
        try {
            return predicate.test(actual, expected);
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Text(" + expected + ")";
    }
}
