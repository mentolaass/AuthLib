package ru.mentola.authlib.pool;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public abstract class Pool<T> {
    private final List<T> cache;

    public Pool() {
        this.cache = new ArrayList<>();
    }

    public T get(final int index) {
        return this.cache.get(index);
    }

    @Nullable
    public T get(final Predicate<T> filter) {
        return this.cache.stream()
                .filter(filter)
                .findFirst()
                .orElse(null);
    }

    public void remove(final Predicate<T> filter) {
        this.cache.removeIf(filter);
    }

    public List<T> getUnModifiableCachePool() {
        return Collections.unmodifiableList(this.cache);
    }

    public void add(final T t) {
        this.cache.add(t);
    }
}
