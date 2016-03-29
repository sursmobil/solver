package solver.utils;

/**
 * Created by CJ on 29/03/2016.
 */
public class Tile<T, P> {
    public final T content;
    public final P position;

    public Tile(T content, P position) {
        this.content = content;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile<?, ?> tile = (Tile<?, ?>) o;

        return !(content != null ? !content.equals(tile.content) : tile.content != null);

    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
