package com.wiley.firewatch.asserts.enities;

import com.google.common.collect.ComparisonChain;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;

/**
 * Created by itatsiy on 4/24/2018.
 */
@Accessors(fluent = true)
public class ProcessingEntry implements Comparable<ProcessingEntry> {
    @Getter
    private final HarEntry harEntry;
    @Getter
    private final ProcessingMetadata<HarRequest> request;
    @Getter
    private final ProcessingMetadata<HarResponse> response;

    public ProcessingEntry(HarEntry harEntry, ProcessingMetadata<HarRequest> request, ProcessingMetadata<HarResponse> response) {
        this.harEntry = harEntry;
        this.request = request;
        this.response = response;
    }

    public boolean finished() {
        return (request == null || request.finished()) && (response == null || response.finished());
    }

    public double overlap() {
        return (request != null ? request.overlap() : 0) + (response != null ? response.overlap() : 0);
    }

    @Override
    public int compareTo(ProcessingEntry o) {
        return ComparisonChain.start()
                .compare(request == null ? 0d : request.overlap(), o.request == null ? 0d : o.request.overlap())
                .compare(response == null ? 0d : response.overlap(), o.response == null ? 0d : o.response.overlap())
                .result();
    }
}
