package org.lombok.donotation;

import lombok.BindTo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class Either<L, R> {
    public Either() {
    }

    @BindTo(method = "bind")
    public R get() { throw new IllegalStateException("get implementation should be replaced at compile time"); }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract <X> X either(Function<L, X> var1, Function<R, X> var2);

    public final void then(Consumer<L> whenLeft, Consumer<R> whenRight) {
        this.either((l) -> {
            whenLeft.accept(l);
            return null;
        }, (r) -> {
            whenRight.accept(r);
            return null;
        });
    }

    public final Either<L, R> with(Consumer<L> whenLeft, Consumer<R> whenRight) {
        this.then(whenLeft, whenRight);
        return this;
    }

    public final Either<L, R> withLeft(Consumer<L> whenLeft) {
        return this.with(whenLeft, (ignore) -> {
        });
    }

    public final Either<L, R> withRight(Consumer<R> whenRight) {
        return this.with((ignore) -> {
        }, whenRight);
    }

    public final Either<L, R> guard(Function<R, Boolean> assertion, L whenFail) {
        return this.guard(assertion, (ignore) -> whenFail);
    }

    public final Either<L, R> guard(Function<R, Boolean> assertion, Function<R, L> whenFail) {
        return this.bind((x) -> (Boolean)assertion.apply(x) ? right(x) : left(whenFail.apply(x)));
    }

    public final Either<L, R> guard(Supplier<Boolean> assertion, L whenFail) {
        return this.guard((ignore) -> (Boolean)assertion.get(), whenFail);
    }

    public final Either<L, R> guard(Supplier<Boolean> assertion, Function<R, L> whenFail) {
        return this.guard((ignore) -> assertion.get(), whenFail);
    }

    public abstract L getLeft();

    public abstract R getRight();

    public static <A, B> Either<A, B> maybe(Optional<B> x, A whenFail) {
        return (Either)x.map(Either::right).orElse(left(whenFail));
    }

    public static <A, B> Either<A, B> left(A leftValue) {
        return new Either.Left(leftValue);
    }

    public static <B> Either<String, B> leftEx(Throwable ex) {
        return new Either.Left(ex.getLocalizedMessage());
    }

    public static <A, B, E extends Throwable> Either<A, B> left(A leftValue, E ignoredException) {
        return left(leftValue);
    }

    public static <A, B> Either<A, B> right(B rightValue) {
        return new Either.Right(rightValue);
    }

    public static <A, B> Either<A, B> left(A leftValue, Class<B> ignore) {
        return left(leftValue);
    }

    public static <A, B> Either<A, B> right(B rightValue, Class<A> ignore) {
        return right(rightValue);
    }

    public static <A, B> Either<A, B> ofNullable(B nullableValue, A leftValue) {
        return nullableValue == null ? left(leftValue) : right(nullableValue);
    }

    public static <A, B> Stream<A> lefts(Stream<Either<A, B>> s) {
        return s.filter(Either::isLeft).map(Either::getLeft);
    }

    public static <A, B> Stream<B> rights(Stream<Either<A, B>> s) {
        return s.filter(Either::isRight).map(Either::getRight);
    }

    public static <B> Either<String, B> safe(Supplier<B> f) {
        try {
            return right(f.get());
        } catch (RuntimeException var2) {
            return left(var2.getMessage(), (Throwable)var2);
        }
    }

    public static <A, B> Either<A, List<B>> sequence(List<Either<A, B>> xs) {
        List<B> rs = new ArrayList();
        Iterator var2 = xs.iterator();

        while(var2.hasNext()) {
            Either<A, B> x = (Either)var2.next();
            if (x.isLeft()) {
                return left(x.getLeft());
            }

            rs.add(x.getRight());
        }

        return right(rs);
    }

    public final <G> Either<L, G> map(Function<R, G> f) {
        return this.isRight() ? right(f.apply(this.getRight())) : left(this.getLeft());
    }

    public final <F> Either<F, R> mapLeft(Function<L, F> f) {
        return this.isRight() ? right(this.getRight()) : left(f.apply(this.getLeft()));
    }

    public final <G> Either<L, G> bind(Function<R, Either<L, G>> k) {
        return this.isLeft() ? left(this.getLeft()) : (Either)k.apply(this.getRight());
    }

    public final Either<L, R> join(Either<L, Either<L, R>> w) {
        return this.isLeft() ? this : (Either)w.getRight();
    }

    public final R orElse(R defaultValue) {
        return this.either((ignore) -> {
            return defaultValue;
        }, Function.identity());
    }

    public final <X extends Throwable> R orThrown(X e) throws X {
        if (this.isLeft()) {
            throw e;
        } else {
            return this.getRight();
        }
    }

    public final Either<L, R> orElse(Function<L, Either<L, R>> f) {
        return (Either)this.either(f, Either::right);
    }

    public final R left2right(Function<L, R> f) {
        return this.either(f, (x) -> x);
    }

    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null) {
            return false;
        } else if (!this.getClass().isInstance(other)) {
            return false;
        } else {
            Either<L, R> o = (Either)other;
            if (this.isLeft()) {
                return this.getLeft() == null ? o.getLeft() == null : this.getLeft().equals(o.getLeft());
            } else {
                return this.getRight() == null ? o.getRight() == null : this.getRight().equals(o.getRight());
            }
        }
    }

    public final int hashCode() {
        return (Integer)this.either(Object::hashCode, Object::hashCode);
    }

    public String toString() {
        return this.isRight() ? String.format("%s { %s }", "Right", this.getRight().toString()) : String.format("%s { %s }", "Left", this.getLeft().toString());
    }

    private static final class Right<A, B> extends Either<A, B> {
        private final B b;

        Right(B b) {
            this.b = b;
        }

        public boolean isLeft() {
            return false;
        }

        public boolean isRight() {
            return true;
        }

        public <X> X either(Function<A, X> left, Function<B, X> right) {
            return right.apply(this.b);
        }

        public A getLeft() {
            throw new IllegalAccessError("cannot get Left value from Right instance");
        }

        public B getRight() {
            return this.b;
        }
    }

    private static final class Left<A, B> extends Either<A, B> {
        private final A a;

        Left(A a) {
            this.a = a;
        }

        public boolean isLeft() {
            return true;
        }

        public boolean isRight() {
            return false;
        }

        public <X> X either(Function<A, X> left, Function<B, X> right) {
            return left.apply(this.a);
        }

        public A getLeft() {
            return this.a;
        }

        public B getRight() {
            throw new IllegalAccessError("cannot get Right value from Left instance");
        }
    }
}
