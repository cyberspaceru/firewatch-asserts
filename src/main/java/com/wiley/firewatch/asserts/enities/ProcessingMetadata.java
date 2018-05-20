package com.wiley.firewatch.asserts.enities;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by itatsiy on 4/23/2018.
 */
@Accessors(fluent = true)
public class ProcessingMetadata<T> {
    @Getter
    private final T har;
    @Getter
    private Map<ObserverMetadata<T>, Boolean> processingTable = new HashMap<>();

    public ProcessingMetadata(T har) {
        this.har = har;
    }

    public double overlap() {
        if (processingTable.size() == 0) {
            return 1;
        }
        return (double) processingTable().values().stream().filter(x -> x).count() / processingTable.size();
    }

    public boolean finished() {
        return overlap() == 1.0d;
    }
}
