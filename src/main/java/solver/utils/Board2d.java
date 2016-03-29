package solver.utils;

import solver.GameState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board2d<T> implements GameState {

    private final T[][] board;

    public Board2d(T[][] board) {
        this.board = board;
    }

    public List<Area<T>> rows() {
        List<Area<T>> result = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            T[] row = board[i];
            result.add(new Area<>(i, Arrays.asList(row)));
        }
        return result;
    }

    public List<Area<T>> columns() {
        int columns = board[0].length;
        List<Area<T>> result = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            List<T> column = new ArrayList<>();
            for (T[] aBoard : board) {
                column.add(aBoard[i]);
            }
            result.add(new Area<>(i, column));
        }
        return result;
    }

    public boolean isFilled() {
        for (T[] row : board) {
            for (T t : row) {
                if (t == null) return false;
            }
        }
        return true;
    }

    public void set(int row, int column, T tile) {
        board[row][column] = tile;
    }

    @SuppressWarnings("unchecked")
    public Board2d<T> freeze() {
        T[][] newBoard = (T[][]) Array.newInstance(board.getClass().getComponentType(), board.length);
        for (int i = 0; i < board.length; i++) {
            newBoard[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return new Board2d<>(newBoard);
    }

    @Override
    public String toString() {
        List<String> lines = new ArrayList<>();
        for (T[] row : board) {
            lines.add(Arrays.toString(row));
        }
        return String.join("\n", lines);
    }

}
