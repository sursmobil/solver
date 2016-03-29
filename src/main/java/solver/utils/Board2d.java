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
        for (int r = 0; r < board.length; r++) {
            T[] row = board[r];
            List<Tile<T, Point2d>> rowResult = new ArrayList<>();
            for (int c = 0; c < row.length; c++) {
                rowResult.add(tile(row[c], r, c));
            }
            result.add(new Line<T>(rowResult, r));
        }
        return result;
    }

    public List<Line<T>> columns() {
        int columns = board[0].length;
        List<Line<T>> result = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            List<Tile<T, Point2d>> column = new ArrayList<>();
            for (int y = 0; y < board.length; y++) {
                column.add(tile(board[i][y], i, y));
            }
            result.add(new Line<>(column, i));
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
    public Board2d<T> copy() {
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

    public static class Line<T> extends Area<T, Point2d> {
        public final int order;

        public Line(List<Tile<T, Point2d>> tiles, int order) {
            super(tiles);
            this.order = order;
        }
    }

    private Tile<T, Point2d> tile(T content, int row, int column) {
        return new Tile<>(content, new Point2d(row, column));
    }

}
