package solver.utils;

import landscaper.TileType;

import java.lang.reflect.Array;
import java.util.*;

import static landscaper.TileType.tree;

/**
 * Created by CJ on 29/03/2016.
 */
public class Permutations {
    public static <T> NoRepetition<T> noRepetition(Class<T> type) {
        return new NoRepetition<>(type);
    }

    public static class NoRepetition<T> {
        private final Map<T, Integer> times = new HashMap<>();
        private final Class<T> type;

        private NoRepetition(Class<T> type) {
            this.type = type;
        }

        public NoRepetition<T> withInstance(T instance) {
            return withInstance(instance, 1);
        }

        public NoRepetition<T> withInstance(T instance, int i) {
            if(i > 0) {
                int t = times.getOrDefault(instance, 0);
                times.put(instance, t + i);
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public Collection<T[]> get() {
            return permutations((T[]) Array.newInstance(type, 0), times);
        }

        private List<T[]> permutations(T[] base, Map<T, Integer> timesMap) {
            List<T[]> result = new ArrayList<>();
            if(timesMap.isEmpty()) {
                result.add(base);
            } else {
                timesMap.forEach((instance, times) -> {
                    if (times > 0) {
                        T[] subbase = Arrays.copyOf(base, base.length + 1);
                        subbase[base.length] = instance;
                        Map<T, Integer> newTimesMap = new HashMap<>(timesMap);
                        if (times == 1) {
                            newTimesMap.remove(instance);
                        } else {
                            newTimesMap.put(instance, times - 1);
                        }
                        result.addAll(permutations(subbase, newTimesMap));
                    }
                });
            }
            return result;
        }
    }

    public static void main(String... args) {
        Collection<String[]> perms = Permutations.noRepetition(String.class)
                .withInstance("mama", 1)
                .withInstance("tata", 2)
                .get();
        perms.stream().map(Arrays::toString).forEach(System.out::println);
    }

}
