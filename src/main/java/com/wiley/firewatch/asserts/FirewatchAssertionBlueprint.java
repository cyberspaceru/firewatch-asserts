package com.wiley.firewatch.asserts;

import com.wiley.firewatch.asserts.context.FirewatchAssertionObserver;
import com.wiley.firewatch.asserts.enums.RelationshipType;
import com.wiley.firewatch.asserts.strategies.IAssertStrategy;
import com.wiley.firewatch.core.observers.IObserver;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Common API in order to create Firewatch Assert.
 * @param <T> Observable object.
 * @param <S> Current class to build fluent API.
 */
@Slf4j
@Accessors(fluent = true)
public abstract class FirewatchAssertionBlueprint<T, S extends FirewatchAssertionBlueprint> {
    @Getter
    private List<FirewatchAssertionObserver<T>> observers = new ArrayList<>();
    @Getter
    private RelationshipType relationship;
    @Getter
    private FirewatchAssertionBlueprint parent;
    @Getter
    private FirewatchAssertionBlueprint child;
    @Getter(AccessLevel.PRIVATE)
    private boolean inverted;
    @Getter(AccessLevel.PROTECTED)
    private IAssertStrategy strategy;

    FirewatchAssertionBlueprint(FirewatchAssertionBlueprint parent, RelationshipType relationship) {
        this.relationship = relationship;
        if (parent != null) {
            this.parent = parent;
            this.parent.child = this;
        }
    }

    public static FirewatchAssertionResponse response() {
        return new FirewatchAssertionResponse(null, RelationshipType.AND);
    }

    public static FirewatchAssertionRequest request() {
        return new FirewatchAssertionRequest(null, RelationshipType.AND);
    }

    protected static FirewatchAssertionRequest request(FirewatchAssertionBlueprint parent, RelationshipType relationship) {
        return new FirewatchAssertionRequest(parent, relationship);
    }

    protected static FirewatchAssertionResponse response(FirewatchAssertionBlueprint parent, RelationshipType relationship) {
        return new FirewatchAssertionResponse(parent, relationship);
    }

    public FirewatchAssertionRequest andRequest() {
        return request(this, RelationshipType.AND);
    }

    public FirewatchAssertionResponse andResponse() {
        return response(this, RelationshipType.AND);
    }

    public S strategy(IAssertStrategy strategy) {
        this.strategy = strategy;
        return self();
    }

    public S not() {
        this.inverted = true;
        return self();
    }

    S observe(IObserver<T> observer) {
        observers.add(new FirewatchAssertionObserver<>(observer, inverted));
        if (inverted) {
            inverted = false;
        }
        return self();
    }

    public S custom(IObserver<T> observer) {
        return observe(observer);
    }

    @SuppressWarnings("unchecked")
    S self() {
        return (S) this;
    }

    @Override
    public String toString() {
        return "[" + observers.stream().map(FirewatchAssertionObserver::toString).collect(Collectors.joining(" -> ")) + "]";
    }
}
