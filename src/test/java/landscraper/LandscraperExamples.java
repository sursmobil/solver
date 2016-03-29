package landscraper;

import landscaper.Rules;
import landscaper.TileType;
import solver.Solver;
import solver.utils.Board2d;

import static landscaper.TileType.flower;
import static landscaper.TileType.tree;

/**
 * Created by CJ on 29/03/2016.
 */
public class LandscraperExamples {

    public void example1() {
        TileType[][] board = new TileType[][]{
                new TileType[]{null, null, tree, null},
                new TileType[]{null, flower, tree, flower},
                new TileType[]{flower, null, null, tree},
                new TileType[]{null, null, null, flower}
        };
        solve(board);
    }

    public void example9() {
        TileType[][] board = new TileType[][]{
                new TileType[]{null, null, null, null, tree,tree},
                new TileType[]{null, null, null, null, null, tree},
                new TileType[]{null, flower, null, null, flower, null},
                new TileType[]{null, null, tree, null, flower, null},
                new TileType[]{null, null, null, flower, null, null},
                new TileType[]{null, null, null, null, null, null}
        };
        solve(board);
    }

    public void example13() {
        TileType[][] board = new TileType[][]{
                new TileType[]{flower, null, null, flower, tree, null, tree, null},
                new TileType[]{null, null, flower, tree, flower, null, null, flower},
                new TileType[]{flower, null, null, flower, null, null, null, null},
                new TileType[]{flower, tree, null, null, null, null, tree, null},
                new TileType[]{null, null, null, null, tree, flower, null, null},
                new TileType[]{null, null, tree, flower, null, null, null, null},
                new TileType[]{tree, null, tree, null, tree, null, flower, null},
                new TileType[]{null, tree, null, tree, flower, flower, null, null}
        };
        solve(board);
    }

    private void solve(TileType[][] board) {
        Board2d<TileType> s = new Board2d<>(board);
        Rules r = new Rules();
        Solver<Board2d<TileType>> solver = new Solver<>(r);
        Board2d result = solver.solve(s);
        System.out.println(result);
    }
}
