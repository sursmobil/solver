package solver.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by CJ on 29/03/2016.
 */
public interface Utils {
    interface Setter<GS, T, P> {
         void set(GS gs, T t , P p);
    }

    static <GS, T, P> Collection<GS> createAlternatives(Collection<T[]> perms, Supplier<GS> gsSupplier, Setter<GS, T, P> setter, Area<T, P> area) {
        return perms.stream()
                .map(perm -> createAlternative(perm, gsSupplier, setter, area))
                .collect(Collectors.toList());
    }

    static <GS, T, P> GS createAlternative(T[] perm, Supplier<GS> gsSupplier, Setter<GS, T, P> setter, Area<T, P> area) {
        GS gs = gsSupplier.get();
        Iterator<T> i = Arrays.asList(perm).iterator();
        area.tiles().stream()
                .filter(t -> t.content == null)
                .forEach(t -> {
                    T val = i.next();
                    setter.set(gs, val, t.position);
                });
        return gs;
    }
}
