package solver.utils;

import solver.GameState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Math.min;

public class Board2d<T> implements GameState {

    private final T[][] board;

    public Board2d(T[][] board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board2d<?> board2d = (Board2d<?>) o;

        return Arrays.deepEquals(board, board2d.board);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public List<Line<T>> rows() {
        List<Line<T>> result = new ArrayList<>();
        for (int r = 0; r < nOfRows(); r++) {
            T[] row = board[r];
            List<Tile<T, Point2d>> rowResult = new ArrayList<>();
            for (int c = 0; c < nOfColumns(); c++) {
                rowResult.add(tile(row[c], r, c));
            }
            result.add(new Line<>(rowResult, r));
        }
        return result;
    }

    public List<Line<T>> columns() {
        List<Line<T>> result = new ArrayList<>();
        for (int c = 0; c < nOfColumns(); c++) {
            List<Tile<T, Point2d>> column = new ArrayList<>();
            for (int r = 0; r < nOfRows(); r++) {
                column.add(tile(board[r][c], r, c));
            }
            result.add(new Line<>(column, c));
        }
        return result;
    }

    public List<Tile<T, Point2d>> tiles() {
        List<Tile<T, Point2d>> result = new ArrayList<>();
        for (int r = 0; r < nOfRows(); r++) {
            for (int c = 0; c < nOfColumns(); c++) {
                result.add(tile(board[r][c], r, c));
            }
        }
        return result;
    }

    public int nOfRows() {
        return board.length;
    }

    public int nOfColumns() {
        return board[0].length;
    }

    public Area<T, Point2d> around(Point2d point) {
        List<Tile<T, Point2d>> result = new ArrayList<>();
        for (int r = max(point.row-1, 0); r <= min(point.row+1, nOfRows()-1); r++) {
            for (int c = max(point.column-1, 0); c <= min(point.column+1, nOfColumns()-1); c++) {
                result.add(tile(board[r][c], r, c));
            }
        }
        return new Area<>(result);
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
    public Board2d<T> copy() {
        T[][] newBoard = (T[][]) Array.newInstance(board.getClass().getComponentType(), board.length);
        for (int i = 0; i < nOfRows(); i++) {
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

    public static class Line<T> extends Area<T, Point2d> {
        public final int order;

        public Line(List<Tile<T, Point2d>> tiles, int order) {
            super(tiles);
            this.order = order;
        }

        public Tile<T, Point2d> get(int i) {
            return tiles().get(i);
        }
    }

    private Tile<T, Point2d> tile(T content, int row, int column) {
        return new Tile<>(content, new Point2d(row, column));
    }

}
