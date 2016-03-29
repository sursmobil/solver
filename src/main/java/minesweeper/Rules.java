package minesweeper;

import solver.GameState;
import solver.RuleSet;
import solver.utils.Board2d;

import java.util.Collection;

/**
 * Created by CJ on 29/03/2016.
 */
public class Rules implements RuleSet<Rules.State> {
    @Override
    public boolean solve(State state) {
        return false;
    }

    @Override
    public boolean verify(State state) {
        return false;
    }

    @Override
    public boolean isComplete(State state) {
        return state.data.isFilled();
    }

    @Override
    public Collection<State> singleProblemAlternatives(State state) {
        return null;
    }

    public static class State implements GameState {
        public final Board2d<TileType> state;
        public final Board2d<Integer> data;

        private State(Board2d<TileType> state, Board2d<Integer> data) {
            this.state = state;
            this.data = data;
        }
    }
}
