package com.wiley.firewatch.asserts.strategies;

import com.wiley.firewatch.asserts.enities.ProcessingEntries;

import java.util.List;

public interface IAssertStrategy {
    void execute(List<ProcessingEntries> processing);
}
