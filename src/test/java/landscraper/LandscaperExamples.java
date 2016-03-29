package landscraper;

import landscaper.Rules;
import landscaper.TileType;
import org.junit.Test;
import solver.Solver;
import solver.utils.Board2d;

import static landscaper.TileType.flower;
import static landscaper.TileType.tree;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by CJ on 29/03/2016.
 */
public class LandscaperExamples {

    @Test
    public void example1() {
        TileType[][] board = new TileType[][]{
                new TileType[]{null,    null,   tree,   null},
                new TileType[]{null,    flower, tree,   flower},
                new TileType[]{flower,  null,   null,   tree},
                new TileType[]{null,    null,   null,   flower}
        };
        TileType[][] expected = new TileType[][]{
                new TileType[]{flower,  flower, tree,   tree},
                new TileType[]{tree,    flower, tree,   flower},
                new TileType[]{flower,  tree,   flower, tree},
                new TileType[]{tree,    tree,   flower, flower}
        };
        solve(board, expected);
    }

    public void example9() {
        TileType[][] board = new TileType[][]{
                new TileType[]{null,    null,   null,   null,   tree,   tree},
                new TileType[]{null,    null,   null,   null,   null,   tree},
                new TileType[]{null,    flower, null,   null,   flower, null},
                new TileType[]{null,    null,   tree,   null,   flower, null},
                new TileType[]{null,    null,   null,   flower, null,   null},
                new TileType[]{null,    null,   null,   null,   null,   null}
        };
//        solve(board);
    }

    @Test
    public void example13() {
        TileType[][] board = new TileType[][]{
                new TileType[]{flower,  null,   null,   flower, tree,   null,   tree,   null},
                new TileType[]{null,    null,   flower, tree,   flower, null,   null,   flower},
                new TileType[]{flower,  null,   null,   flower, null,   null,   null,   null},
                new TileType[]{flower,  tree,   null,   null,   null,   null,   tree,   null},
                new TileType[]{null,    null,   null,   null,   tree,   flower, null,   null},
                new TileType[]{null,    null,   tree,   flower, null,   null,   null,   null},
                new TileType[]{tree,    null,   tree,   null,   tree,   null,   flower, null},
                new TileType[]{null,    tree,   null,   tree,   flower, flower, null,   null}
        };
        TileType[][] expected = new TileType[][]{
                new TileType[]{flower,  flower, tree,   flower, tree,   flower, tree,   tree},
                new TileType[]{tree,    tree,   flower, tree,   flower, tree,   flower, flower},
                new TileType[]{flower,  flower, tree,   flower, tree,   tree,   flower, tree},
                new TileType[]{flower,  tree,   flower, tree,   flower, flower, tree,   tree},
                new TileType[]{tree,    flower, flower, tree,   tree,   flower, tree,   flower},
                new TileType[]{flower,  tree,   tree,   flower, flower, tree,   flower, tree},
                new TileType[]{tree,    flower, tree,   flower, tree,   tree,   flower, flower},
                new TileType[]{tree,    tree,   flower, tree,   flower, flower, tree,   flower}
        };

        solve(board, expected);
    }

    private void solve(TileType[][] board, TileType[][] expected) {
        Board2d<TileType> s = new Board2d<>(board);
        Rules r = new Rules();
        Solver<Board2d<TileType>> solver = new Solver<>(r);
        Board2d<TileType> result = solver.solve(s);
        assertEquals(new Board2d<>(expected), result);
    }
}
