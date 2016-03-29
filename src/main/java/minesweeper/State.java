package minesweeper;

import solver.GameState;
import solver.utils.Board2d;

import static minesweeper.TileType.miss;

/**
 * Created by CJ on 29/03/2016.
 */
public class State implements GameState {
    public final Board2d<TileType> state;
    public final Board2d<Integer> data;

    public State(Integer[][] data) {
        this.data = new Board2d<>(data);
        state = crateEmptyState(this.data);
    }

    private Board2d<TileType> crateEmptyState(Board2d<Integer> data) {
        TileType[][] board = new TileType[data.nOfRows()][data.nOfColumns()];
        data.tiles().forEach(t -> board[t.position.row][t.position.column] = t.content != null ? miss : null);
        return new Board2d<>(board);
    }

    public State(Board2d<TileType> state, Board2d<Integer> data) {
        this.state = state;
        this.data = data;
    }

    public State copy() {
        return new State(state.copy(), data);
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
