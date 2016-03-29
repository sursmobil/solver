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

    public List<Line<T>> rows() {
        List<Line<T>> result = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            T[] row = board[i];
            result.add(new Line<>(i, Arrays.asList(row)));
        }
        return result;
    }

    public List<Line<T>> columns() {
        int columns = board[0].length;
        List<Line<T>> result = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            List<T> column = new ArrayList<>();
            for (T[] aBoard : board) {
                column.add(aBoard[i]);
            }
            result.add(new Line<>(i, column));
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

    public static class Line<T> {
        public final int orderNumber;
        private final List<T> tiles;

        public Line(int orderNumber, List<T> tiles) {
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

            Line line = (Line) o;

            return tiles.equals(line.tiles);
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
    }

}
