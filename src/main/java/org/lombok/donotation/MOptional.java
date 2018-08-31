package org.lombok.donotation;

import lombok.BindTo;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.lombok.donotation.Either.left;

public class MOptional<T> {
    private final Optional<T> x;

    private MOptional(Optional<T> y) {
        x = y;
    }

    public static <T> MOptional<T> of(Optional<T> y) {
        return new MOptional<>(y);
    }

    public static <T> MOptional<T> empty() {
        return of(Optional.empty());
    }


    public static <T> MOptional<T> of(T value) {
        return of(Optional.of(value));
    }


    public static <T> MOptional<T> ofNullable(T value) {
        return of(Optional.ofNullable(value));
    }

    @BindTo(method = "flatMap")
    public T get() {
        return x.get();
    }

    public boolean isPresent() {
        return x.isPresent();
    }

    public void ifPresent(Consumer<? super T> consumer) {
        x.ifPresent(consumer);
    }

    public MOptional<T> filter(Predicate<? super T> predicate) {
        return of(x.filter(predicate));
    }

    public <U> MOptional<U> map(Function<? super T, ? extends U> mapper) {
        return of(x.map(mapper));
    }

    public <U> MOptional<U> flatMap(Function<? super T, MOptional<U>> mapper) {
        return of(x.flatMap(t -> mapper.apply(t).x));
    }

    public T orElse(T other) {
        return x.orElse(other);
    }

    public T orElseGet(Supplier<? extends T> other) {
        return x.orElseGet(other);
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return x.orElseThrow(exceptionSupplier);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MOptional)) {
            return false;
        }

        MOptional<?> other = (MOptional<?>) obj;
        return x.equals(other.x);
    }

    @Override
    public int hashCode() {
        return x.hashCode();
    }

    @Override
    public String toString() {
        return "M" + x.toString();
    }

    public <L> Either<L, T> toEither(Supplier<L> whenfail) {
        return x.<Either<L, T>>map(Either::right).orElseGet(() -> left(whenfail.get()));
    }
    public <L> Either<L, T> toEither(L e) {
        return toEither(() -> e);
    }

}
