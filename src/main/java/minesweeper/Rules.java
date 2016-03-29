package minesweeper;

import solver.RuleSet;
import solver.utils.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static minesweeper.TileType.bomb;
import static minesweeper.TileType.miss;

/**
 * Created by CJ on 29/03/2016.
 */
public class Rules implements RuleSet<State> {
    @Override
    public boolean solve(State state) {
        return fillDoneWithMisses(state) || fillWhatCanBeDone(state);
    }

    private boolean fillWhatCanBeDone(State state) {
        List<Tile<Integer, Point2d>> canBeDone = state.data.tiles().stream()
                .filter(t -> !noConstraints(t))
                .filter(t -> !allFilled(t, state))
                .filter(t -> missing(t, state) == free(t, state))
                .collect(Collectors.toList());

        canBeDone.forEach(t -> {
            Area<TileType, Point2d> around = state.state.around(t.position);
            around.tiles().stream()
                    .filter(a -> a.content == null)
                    .forEach(a ->
                            state.state.set(a.position.row, a.position.column, bomb)
                    );
        });

        return !canBeDone.isEmpty();
    }

    private boolean fillDoneWithMisses(State state) {
        List<Tile<Integer, Point2d>> done = state.data.tiles().stream()
                .filter(t -> !noConstraints(t))
                .filter(t -> !allFilled(t, state))
                .filter(t -> missing(t, state) == 0)
                .collect(Collectors.toList());

        done.forEach(t -> {
            Area<TileType, Point2d> around = state.state.around(t.position);
            around.tiles().stream()
                    .filter(a -> a.content == null)
                    .forEach(a ->
                            state.state.set(a.position.row, a.position.column, miss)
                    );
        });

        return !done.isEmpty();
    }

    private boolean allFilled(Tile<Integer, Point2d> t, State state) {
        Area<TileType, Point2d> around = state.state.around(t.position);
        return around.tiles().stream().allMatch(a -> a.content != null);
    }


    @Override
    public boolean verify(State state) {
        return state.data.tiles().stream()
                .allMatch(t -> noConstraints(t) || validMineCount(t, state));
    }

    private boolean validMineCount(Tile<Integer, Point2d> t, State state) {
        Area<TileType, Point2d> around = state.state.around(t.position);
        return notTooManyMines(around, t) && enoughFreeSpace(around, t);
    }

    private boolean enoughFreeSpace(Area<TileType, Point2d> around, Tile<Integer, Point2d> t) {
        return around.count(null) + around.count(bomb) >= t.content;
    }

    private boolean notTooManyMines(Area<TileType, Point2d> around, Tile<Integer, Point2d> t) {
        return around.count(bomb) <= t.content;
    }

    private boolean noConstraints(Tile<Integer, Point2d> t) {
        return t.content == null;
    }

    @Override
    public boolean isComplete(State state) {
        return state.state.isFilled();
    }

    @Override
    public Collection<State> singleProblemAlternatives(State state) {
        Tile<Integer, Point2d> t = tileWithLeastMissing(state);
        int missing = missing(t, state);
        int free = free(t, state);
        Collection<TileType[]> perms = Permutations.noRepetition(TileType.class)
                .withInstance(bomb, missing)
                .withInstance(miss, free - missing)
                .get();
        return Utils.createAlternatives(perms, state::copy,
                (gs, val, p) -> gs.state.set(p.row, p.column, val),
                state.state.around(t.position));
    }

    private Tile<Integer, Point2d> tileWithLeastMissing(State state) {
//        System.out.println("State: \n" + state.state + "\n");
        return state.data.tiles().stream()
                .filter(t -> !noConstraints(t))
                .filter(t -> missing(t, state) > 0)
                .sorted((t1, t2) -> Integer.compare(missing(t1, state), missing(t2, state)))
                .findFirst()
                .get();

    }

    private int missing(Tile<Integer, Point2d> t, State state) {
        Area<TileType, Point2d> around = state.state.around(t.position);
        return (int) (t.content - around.count(bomb));
    }

    private int free(Tile<Integer, Point2d> t, State state) {
        return (int) state.state.around(t.position).count(null);
    }

}
