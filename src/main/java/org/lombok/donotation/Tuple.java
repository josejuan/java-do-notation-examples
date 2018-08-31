package org.lombok.donotation;

import java.util.Optional;
import java.util.function.Function;

public final class Tuple<A, B> {
    public final A fst;
    public final B snd;

    private Tuple(A fst, B snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public static <U, V> Tuple<U, V> tuple(U u, V v) {
        return new Tuple(u, v);
    }

    public static <U> Tuple<U, U> tuple(U u) {
        return new Tuple(u, u);
    }

    public A getFirst() {
        return this.fst;
    }

    public B getSecond() {
        return this.snd;
    }

    public <U> Tuple<U, B> first(Function<A, U> f) {
        return tuple(f.apply(this.fst), this.snd);
    }

    public <V> Tuple<A, V> second(Function<B, V> f) {
        return tuple(this.fst, f.apply(this.snd));
    }

    public int hashCode() {
        return 4011 * (Integer)Optional.ofNullable(this.fst).map(Object::hashCode).orElse(0) + (Integer)Optional.ofNullable(this.snd).map(Object::hashCode).orElse(0);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple)) {
            return false;
        } else {
            boolean var10000;
            label38: {
                label27: {
                    Tuple<A, B> t = (Tuple)obj;
                    if (this.fst == null) {
                        if (t.fst != null) {
                            break label27;
                        }
                    } else if (!this.fst.equals(t.fst)) {
                        break label27;
                    }

                    if (this.snd == null) {
                        if (t.snd == null) {
                            break label38;
                        }
                    } else if (this.snd.equals(t.snd)) {
                        break label38;
                    }
                }

                var10000 = false;
                return var10000;
            }

            var10000 = true;
            return var10000;
        }
    }
}
