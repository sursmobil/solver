package minesweeper;

import org.junit.Test;
import solver.Solver;
import solver.utils.Board2d;

import java.util.Arrays;
import java.util.List;

import static minesweeper.TileType.bomb;
import static minesweeper.TileType.miss;
import static org.junit.Assert.assertEquals;

/**
 * Created by CJ on 29/03/2016.
 */
public class MinesweeperExamples {

    @Test
    public void example1() {
        Integer[][] data = parseData(
                " 3111",
                "   2 ",
                " 312 ",
                " 1011",
                "1  0 "
        );
        TileType[][] expected = parseExpected(
                "b    ",
                "bb  b",
                "    b",
                "b    ",
                "     "
        );
        solve(data, expected);
    }

    @Test
    public void example18() {
        Integer[][] data = parseData(
                " 1  1   11",
                "1 02   3  ",
                "      1 3 ",
                "  3    5  ",
                " 5   2   3",
                "2  2 3   2",
                "  3   4 3 ",
                " 3   2 3 1",
                " 23      3",
                "1  2 21   "
        );
        TileType[][] expected = parseExpected(
                "b     b   ",
                "    b   b ",
                "    b  b  ",
                "bb b    b ",
                "b b   bbb ",
                "  b   bbb ",
                "b    b b  ",
                "  bb      ",
                "b  b  b b ",
                "    b   bb"
        );
        solve(data, expected);
    }

    private void solve(Integer[][] board, TileType[][] expected) {
        State s = new State(board);
        Rules r = new Rules();
        Solver<State> solver = new Solver<>(r);
        State result = solver.solve(s);
        assertEquals(new Board2d<>(expected), result.state);
    }

    private Integer[][] parseData(String... lines) {
        Integer[][] result = new Integer[lines.length][lines[0].length()];
        for(int i = 0; i < lines.length; i++) {
            String l = lines[i];
            char[] chars = l.toCharArray();
            for(int y = 0; y < chars.length; y++) {
                result[i][y] = chars[y] == ' ' ? null : Character.digit(chars[y], 10);
            }
        }
        return result;
    }

    private TileType[][] parseExpected(String... lines) {
        TileType[][] result = new TileType[lines.length][lines[0].length()];
        for(int i = 0; i < lines.length; i++) {
            String l = lines[i];
            char[] chars = l.toCharArray();
            for(int y = 0; y < chars.length; y++) {
                result[i][y] = chars[y] == ' ' ? miss : bomb;
            }
        }
        return result;
    }

}
