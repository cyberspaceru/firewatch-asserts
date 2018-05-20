package com.wiley.firewatch.asserts;

import com.wiley.firewatch.asserts.enities.ProcessingEntries;
import com.wiley.firewatch.asserts.enities.ProcessingEntry;
import net.lightbody.bmp.core.har.HarEntry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by itatsiy on 5/18/2018.
 */
public class FirewatchPostProcessing {
    private final List<ProcessingEntries> processingEntries;

    public FirewatchPostProcessing(List<ProcessingEntries> processingEntries) {
        this.processingEntries = processingEntries;
    }

    public List<HarEntry> getBestEntries() {
        return processingEntries.stream()
                .map(ProcessingEntries::bestOverlap)
                .map(ProcessingEntry::harEntry)
                .collect(Collectors.toList());
    }

    public Stream<HarEntry> bestEntries() {
        return getAllBestEntries().stream();
    }

    public List<HarEntry> getAllBestEntries() {
        return processingEntries.stream()
                .flatMap(ProcessingEntries::bests)
                .map(ProcessingEntry::harEntry)
                .collect(Collectors.toList());
    }

    public Stream<HarEntry> allBestEntries() {
        return getAllBestEntries().stream();
    }
}
