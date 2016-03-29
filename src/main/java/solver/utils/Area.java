package solver.utils;

import java.util.List;

/**
 * Created by CJ on 29/03/2016.
 */
public class Area<T> {
    public final int orderNumber;
    private final List<T> tiles;

    public Area(int orderNumber, List<T> tiles) {
        this.orderNumber = orderNumber;
        this.tiles = tiles;
    }

    public List<T> tiles() {
        return tiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        return tiles.equals(area.tiles);
    }

    @Override
    public int hashCode() {
        return tiles.hashCode();
    }

    @Override
    public String toString() {
        return orderNumber + " : " + tiles;
    }

    public long count(T expected) {
        return tiles.stream().filter(t -> t == expected).count();
    }

    public int size() {
        return tiles.size();
    }
}
