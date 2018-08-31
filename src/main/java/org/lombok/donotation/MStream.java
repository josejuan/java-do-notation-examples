package org.lombok.donotation;

import lombok.BindTo;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class MStream<T> implements Stream<T> {

    private final Stream<T> s;

    public MStream(final Stream<T> s) {
        this.s = s;
    }

    public static <T> MStream<T> from(final Stream<T> s) {
        return new MStream<>(s);
    }

    public static <T> MStream<T> of(T v) {
        return from(Stream.of(v));
    }

    public static <T> MStream<T> of(T[] v) {
        return from(Arrays.stream(v));
    }

    @BindTo(method = "flatMap")
    public T get() {
        throw new IllegalStateException("'get' method should be removed at compilation time!");
    }

    @Override
    public MStream<T> filter(Predicate<? super T> predicate) {
        return from(s.filter(predicate));
    }

    @Override
    public <R> MStream<R> map(Function<? super T, ? extends R> mapper) {
        return from(s.map(mapper));
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return s.mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return s.mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return s.mapToDouble(mapper);
    }

    @Override
    public <R> MStream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return from(s.flatMap(mapper));
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return s.flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return s.flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return s.flatMapToDouble(mapper);
    }

    @Override
    public MStream<T> distinct() {
        return from(s.distinct());
    }

    @Override
    public MStream<T> sorted() {
        return from(s.sorted());
    }

    @Override
    public MStream<T> sorted(Comparator<? super T> comparator) {
        return from(s.sorted(comparator));
    }

    @Override
    public MStream<T> peek(Consumer<? super T> action) {
        return from(s.peek(action));
    }

    @Override
    public MStream<T> limit(long maxSize) {
        return from(s.limit(maxSize));
    }

    @Override
    public MStream<T> skip(long n) {
        return from(s.skip(n));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        s.forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        s.forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return s.toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return s.toArray(generator);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return s.reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return s.reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return s.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return s.collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return s.collect(collector);
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return s.min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return s.max(comparator);
    }

    @Override
    public long count() {
        return s.count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return s.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return s.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return s.noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return s.findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return s.findAny();
    }

    @Override
    public Iterator<T> iterator() {
        return s.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return s.spliterator();
    }

    @Override
    public MStream<T> sequential() {
        return from(s.sequential());
    }

    @Override
    public MStream<T> parallel() {
        return from(s.parallel());
    }

    @Override
    public MStream<T> unordered() {
        return from(s.unordered());
    }

    @Override
    public MStream<T> onClose(Runnable closeHandler) {
        return from(s.onClose(closeHandler));
    }

    @Override
    public void close() {
        s.close();
    }

    @Override
    public boolean isParallel() {
        return s.isParallel();
    }
}
