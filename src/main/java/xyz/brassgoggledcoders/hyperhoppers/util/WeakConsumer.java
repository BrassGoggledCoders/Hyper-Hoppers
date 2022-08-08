package xyz.brassgoggledcoders.hyperhoppers.util;

import java.lang.ref.WeakReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WeakConsumer<T, U> implements Consumer<T> {
    private final BiConsumer<T, U> consumer;
    private final WeakReference<U> reference;

    public WeakConsumer(BiConsumer<T, U> consumer, U reference) {
        this.consumer = consumer;
        this.reference = new WeakReference<>(reference);
    }

    @Override
    public void accept(T t) {
        U value = this.reference.get();
        if (value != null) {
            consumer.accept(t, value);
        }
    }
}
