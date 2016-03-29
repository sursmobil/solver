package solver.utils;

import java.util.List;

/**
 * Created by CJ on 29/03/2016.
 */
public class Area<T, P> {
    private final List<Tile<T, P>> tiles;

    public Area(List<Tile<T, P>> tiles) {
        this.tiles = tiles;
    }

    public List<Tile<T, P>> tiles() {
        return tiles;
    }

    public boolean contains(T content) {
        return tiles().stream()
                .anyMatch(t -> t.content == content);
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
        return tiles.toString();
    }

    public long count(T expected) {
        return tiles.stream().filter(t -> t.content == expected).count();
    }

    public int size() {
        return tiles.size();
    }
}
