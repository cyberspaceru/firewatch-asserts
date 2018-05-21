package com.wiley.firewatch.asserts.processing;

import com.wiley.firewatch.asserts.FirewatchAssertionRequest;
import com.wiley.firewatch.asserts.FirewatchAssertionResponse;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

@Accessors(fluent = true)
public class ProcessingEntries extends ArrayList<ProcessingEntry> {
    @Getter
    private final transient FirewatchAssertionRequest firewatchRequest;
    @Getter
    private final transient FirewatchAssertionResponse firewatchResponse;

    public ProcessingEntries(int initialCapacity, FirewatchAssertionRequest firewatchRequest, FirewatchAssertionResponse firewatchResponse) {
        super(initialCapacity);
        this.firewatchRequest = firewatchRequest;
        this.firewatchResponse = firewatchResponse;
    }

    public ProcessingEntries(FirewatchAssertionRequest firewatchRequest, FirewatchAssertionResponse firewatchResponse) {
        this.firewatchRequest = firewatchRequest;
        this.firewatchResponse = firewatchResponse;
    }

    public ProcessingEntries(Collection<? extends ProcessingEntry> c, FirewatchAssertionRequest firewatchRequest, FirewatchAssertionResponse firewatchResponse) {
        super(c);
        this.firewatchRequest = firewatchRequest;
        this.firewatchResponse = firewatchResponse;
    }

    public boolean finished() {
        ProcessingEntry best = best();
        return best != null && (best.request() == null || best.request().finished()) && (best.response() == null || best.response().finished());
    }

    public ProcessingEntry bestOverlap() {
        ProcessingEntry best = stream().max(ProcessingEntry::compareTo).orElse(null);
        return (best != null && best.overlap() > 0) ? best : null;
    }

    private ProcessingEntry best() {
        Stream<ProcessingEntry> bests = bests();
        return bests == null ? null : bests.findFirst().orElse(null);
    }

    public Stream<ProcessingEntry> bests() {
        ProcessingEntry best = bestOverlap();
        if (best == null) {
            return null;
        }
        return stream().filter(x -> (x.request() == best.request() || x.request().overlap() == best.request().overlap())
                && (x.response() == best.response() || x.response().overlap() == best.response().overlap()));
    }

}
