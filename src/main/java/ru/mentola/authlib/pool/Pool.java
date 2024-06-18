package ru.mentola.authlib.pool;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public abstract class Pool<T> {
    private final List<T> cache = new ArrayList<>();

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
        if (this.cache.contains(t))
            return;
        this.cache.add(t);
    }

    public void clear() {
        this.cache.clear();
    }

    public boolean contains(final T t) {
        return this.cache.contains(t);
    }
}
