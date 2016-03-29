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
}
