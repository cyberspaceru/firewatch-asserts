package com.wiley.firewatch.asserts;

import com.wiley.firewatch.asserts.processing.ProcessingEntries;
import com.wiley.firewatch.asserts.processing.ProcessingEntry;
import net.lightbody.bmp.core.har.HarEntry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FirewatchAssertionResult {
    private final List<ProcessingEntries> processingEntries;

    FirewatchAssertionResult(List<ProcessingEntries> processingEntries) {
        this.processingEntries = processingEntries;
    }

    public List<HarEntry> getBestEntries() {
        return processingEntries.stream()
                .map(ProcessingEntries::bestOverlap)
                .map(ProcessingEntry::harEntry)
                .collect(Collectors.toList());
    }

    public Stream<HarEntry> bestEntries() {
        return getBestEntries().stream();
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
