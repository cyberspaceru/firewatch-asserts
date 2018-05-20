package com.wiley.firewatch.asserts.enities;

import com.wiley.firewatch.asserts.observers.IObserver;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by itatsiy on 4/23/2018.
 */
@Data
@Accessors(fluent = true)
public class ObserverMetadata<T> {
    private IObserver<T> observer;
    private boolean invert;

    public ObserverMetadata(IObserver<T> observer) {
        this.observer = observer;
    }

    public ObserverMetadata(IObserver<T> observer, boolean invert) {
        this.observer = observer;
        this.invert = invert;
    }

    @Override
    public String toString() {
        return (invert ? "!" : "") + observer;
    }
}
