package net.savagedev.funfall.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractManager<I, T> {
    private final Map<I, T> objects = new HashMap<>();

    private final Function<I, T> function;

    public AbstractManager(Function<I, T> function) {
        this.function = function;
    }

    public T unload(I id) {
        return this.objects.remove(this.sanitizeIdentifier(id));
    }

    public T getOrMake(I id) {
        final I sanitizedIdentifier = this.sanitizeIdentifier(id);
        if (this.objects.containsKey(sanitizedIdentifier)) {
            return this.objects.get(sanitizedIdentifier);
        }
        return this.objects.computeIfAbsent(sanitizedIdentifier, this.function);
    }

    public T get(I id) {
        return this.objects.get(this.sanitizeIdentifier(id));
    }

    public Map<I, T> getAll() {
        return this.objects;
    }

    protected I sanitizeIdentifier(I id) {
        return id;
    }
}
