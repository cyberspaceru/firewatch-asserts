package com.wiley.firewatch.asserts.context;

import com.wiley.firewatch.core.observers.IObserver;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class FirewatchAssertionObserver<T> {
    private IObserver<T> observer;
    private boolean invert;

    public FirewatchAssertionObserver(IObserver<T> observer) {
        this.observer = observer;
    }

    public FirewatchAssertionObserver(IObserver<T> observer, boolean invert) {
        this.observer = observer;
        this.invert = invert;
    }

    @Override
    public String toString() {
        return (invert ? "!" : "") + observer;
    }
}
