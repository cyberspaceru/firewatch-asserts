package com.wiley.firewatch.asserts;

import com.wiley.firewatch.asserts.context.FirewatchAssertionObserver;
import com.wiley.firewatch.asserts.enums.RelationshipType;
import com.wiley.firewatch.asserts.processing.ProcessingEntries;
import com.wiley.firewatch.asserts.processing.ProcessingEntry;
import com.wiley.firewatch.asserts.processing.ProcessingMetadata;
import com.wiley.firewatch.asserts.strategies.BaseAssertStrategy;
import com.wiley.firewatch.core.FirewatchConnection;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Assert execution.
 *
 * @param <T> Observable object.
 * @param <S> Current class to build fluent API.
 */
public class FirewatchAssertion<T, S extends FirewatchAssertionBlueprint> extends FirewatchAssertionBlueprint<T, S> {
    FirewatchAssertion(FirewatchAssertionBlueprint parent, RelationshipType relationship) {
        super(parent, relationship);
    }

    public FirewatchAssertionResult executeWithTimeout() {
        return executeWithTimeout(Duration.ofSeconds(10));
    }

    public FirewatchAssertionResult executeWithTimeout(Duration duration, String errorMessage) {
        long end = System.currentTimeMillis() + duration.toMillis();
        while (end > System.currentTimeMillis()) {
            try {
                return execute(errorMessage);
            } catch (AssertionError ignored) {
                // AssertionError is ignored
            }
        }
        return execute(errorMessage + " [timeout='" + duration.toString() + "']");
    }

    public FirewatchAssertionResult executeWithTimeout(Duration duration) {
        return executeWithTimeout(duration, "Firewatch assert didn't match.");
    }

    public FirewatchAssertionResult execute() {
        return execute("Firewatch assert didn't match.");
    }

    public FirewatchAssertionResult execute(String errorMessage) {
        List<ProcessingEntries> result = process();
        if (strategy() != null) {
            strategy().execute(result);
        } else {
            new BaseAssertStrategy(errorMessage).execute(result);
        }
        return new FirewatchAssertionResult(result);
    }

    private List<ProcessingEntries> process() {
        List<HarEntry> hars = FirewatchConnection.proxyServer().getHar().getLog().getEntries();
        return buildAssertionsTree().entrySet().stream()
                .map(fPair -> process(fPair, hars))
                .collect(Collectors.toList());
    }

    /**
     * Map the All Firewatches to the HarEntries.
     */
    private static ProcessingEntries process(Entry<FirewatchAssertionRequest, FirewatchAssertionResponse> fPair, List<HarEntry> hars) {
        ProcessingEntries processingEntries = new ProcessingEntries(fPair.getKey(), fPair.getValue());
        hars.stream().map(har -> process(fPair, har)).forEach(processingEntries::add);
        return processingEntries;
    }

    /**
     * Map the Firewatches Pair to the HarEntry.
     */
    private static ProcessingEntry process(Entry<FirewatchAssertionRequest, FirewatchAssertionResponse> fPair, HarEntry har) {
        ProcessingMetadata<HarRequest> request = fPair.getKey() == null ? null : process(fPair.getKey(), har.getRequest());
        ProcessingMetadata<HarResponse> response = fPair.getValue() == null ? null : process(fPair.getValue(), har.getResponse());
        return new ProcessingEntry(har, request, response);
    }

    /**
     * Map the FirewatchAssertion to the Har.
     */
    private static <H> ProcessingMetadata<H> process(FirewatchAssertionBlueprint<H, ?> firewatch, H har) {
        ProcessingMetadata<H> processingMetadata = new ProcessingMetadata<>(har);
        for (FirewatchAssertionObserver<H> firewatchAssertionObserver : firewatch.observers()) {
            try {
                boolean result = firewatchAssertionObserver.observer().observe(har);
                result = firewatchAssertionObserver.invert() != result;
                processingMetadata.processingTable().put(firewatchAssertionObserver, result);
            } catch (Exception ignore) {
                // ignored
            }
        }
        return processingMetadata;
    }

    private Map<FirewatchAssertionRequest, FirewatchAssertionResponse> buildAssertionsTree() {
        Map<FirewatchAssertionRequest, FirewatchAssertionResponse> result = new HashMap<>();
        FirewatchAssertionBlueprint cursor = this;
        while (cursor != null) {
            FirewatchAssertionRequest request = null;
            FirewatchAssertionResponse response = null;
            if (cursor instanceof FirewatchAssertionRequest) {
                request = (FirewatchAssertionRequest) cursor;
                if (cursor.child() != null && (cursor.child() instanceof FirewatchAssertionResponse && cursor.child().relationship() == RelationshipType.THEN)) {
                    response = (FirewatchAssertionResponse) cursor.child();
                }
            } else if (cursor instanceof FirewatchAssertionResponse && cursor.relationship() == RelationshipType.AND) {
                response = (FirewatchAssertionResponse) cursor;
            }
            if (request != null || response != null) {
                result.put(request, response);
            }
            cursor = cursor.parent();
        }
        return result;
    }
}
