package landscaper;

import solver.GameState;
import solver.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static landscaper.Tile.*;
import static landscaper.Tile.flower;
import static landscaper.Tile.tree;

public class State implements GameState {

    private final Tile[][] board;

    public State(Tile[][] board) {
        this.board = board;
    }

    public List<Sequence> rows() {
        List<Sequence> result = new ArrayList<>();
        for(int i = 0; i < board.length; i++) {
            Tile[] row = board[i];
            result.add(new Sequence(i, Arrays.asList(row)));
        }
        return result;
    }

    public List<Sequence> columns() {
        int columns = board[0].length;
        List<Sequence> result = new ArrayList<>();
        for(int i = 0; i < columns; i++) {
            List<Tile> column = new ArrayList<>();
            for(int y = 0; y < board.length; y++) {
                column.add(board[y][i]);
            }
            result.add(new Sequence(i, column));
        }
        return result;
    }

    public boolean isFilled() {
        for (Tile[] row : board) {
            for (Tile t : row) {
                if (t == empty) return false;
            }
        }
        return true;
    }

    public void set(int row, int column, Tile tile) {
        board[row][column] = tile;
    }

    public State freeze() {
        int columns = board[0].length;
        Tile[][] newBoard = new Tile[board.length][columns];
        for(int i = 0; i < board.length; i++) {
            System.arraycopy(board[i],0,newBoard[i],0,columns);
        }
        return new State(newBoard);
    }

    @Override
    public String toString() {
        List<String> lines = new ArrayList<>();
        for(Tile[] row : board) {
            lines.add(Arrays.toString(row));
        }
        return String.join("\n", lines);
    }

    public static class Sequence {
        public final int orderNumber;
        private final List<Tile> tiles;

        public Sequence(int orderNumber, List<Tile> tiles) {
            this.orderNumber = orderNumber;
            this.tiles = tiles;
        }

        public List<Tile> tiles(){
            return tiles;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Sequence sequence = (Sequence) o;

            return tiles.equals(sequence.tiles);

        }

        @Override
        public int hashCode() {
            return tiles.hashCode();
        }

        @Override
        public String toString() {
            return orderNumber + " : " + tiles;
        }
    }

    public static void main(String... args) {
        // 1
//        Tile[][] board = new Tile[][]{
//                new Tile[]{empty, empty, tree, empty},
//                new Tile[]{empty, flower, tree, flower},
//                new Tile[]{flower, empty, empty, tree},
//                new Tile[]{empty, empty, empty, flower}
//        };
        // 9
        Tile[][] board = new Tile[][]{
                new Tile[]{empty, empty, empty, empty, tree,tree},
                new Tile[]{empty, empty, empty, empty, empty, tree},
                new Tile[]{empty, flower, empty, empty, flower, empty},
                new Tile[]{empty, empty, tree, empty, flower, empty},
                new Tile[]{empty, empty, empty, flower, empty, empty},
                new Tile[]{empty, empty, empty, empty, empty, empty}
        };
        // 13
//        Tile[][] board = new Tile[][]{
//                new Tile[]{flower, empty, empty, flower, tree, empty, tree, empty},
//                new Tile[]{empty, empty, flower, tree, flower, empty, empty, flower},
//                new Tile[]{flower, empty, empty, flower, empty, empty, empty, empty},
//                new Tile[]{flower, tree, empty, empty, empty, empty, tree, empty},
//                new Tile[]{empty, empty, empty, empty, tree, flower, empty, empty},
//                new Tile[]{empty, empty, tree, flower, empty, empty, empty, empty},
//                new Tile[]{tree, empty, tree, empty, tree, empty, flower, empty},
//                new Tile[]{empty, tree, empty, tree, flower, flower, empty, empty}
//        };
        State s = new State(board);
        Rules r = new Rules();
        Solver<State> solver = new Solver<>(r);
        State result = solver.solve(s);
        System.out.println(result);
    }
}
