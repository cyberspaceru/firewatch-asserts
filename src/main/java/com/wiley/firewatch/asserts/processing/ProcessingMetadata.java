package com.wiley.firewatch.asserts.processing;

import com.wiley.firewatch.asserts.context.FirewatchAssertionObserver;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Accessors(fluent = true)
public class ProcessingMetadata<T> {
    @Getter
    private final T har;
    @Getter
    private Map<FirewatchAssertionObserver<T>, Boolean> processingTable = new HashMap<>();

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
