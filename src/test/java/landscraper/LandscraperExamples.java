package landscraper;

import landscaper.Rules;
import landscaper.Tile;
import solver.Solver;
import solver.utils.Board2d;

import static landscaper.Tile.flower;
import static landscaper.Tile.tree;

/**
 * Created by CJ on 29/03/2016.
 */
public class LandscraperExamples {

    public void example1() {
        Tile[][] board = new Tile[][]{
                new Tile[]{null, null, tree, null},
                new Tile[]{null, flower, tree, flower},
                new Tile[]{flower, null, null, tree},
                new Tile[]{null, null, null, flower}
        };
        solve(board);
    }

    public void example9() {
        Tile[][] board = new Tile[][]{
                new Tile[]{null, null, null, null, tree,tree},
                new Tile[]{null, null, null, null, null, tree},
                new Tile[]{null, flower, null, null, flower, null},
                new Tile[]{null, null, tree, null, flower, null},
                new Tile[]{null, null, null, flower, null, null},
                new Tile[]{null, null, null, null, null, null}
        };
        solve(board);
    }

    public void example13() {
        Tile[][] board = new Tile[][]{
                new Tile[]{flower, null, null, flower, tree, null, tree, null},
                new Tile[]{null, null, flower, tree, flower, null, null, flower},
                new Tile[]{flower, null, null, flower, null, null, null, null},
                new Tile[]{flower, tree, null, null, null, null, tree, null},
                new Tile[]{null, null, null, null, tree, flower, null, null},
                new Tile[]{null, null, tree, flower, null, null, null, null},
                new Tile[]{tree, null, tree, null, tree, null, flower, null},
                new Tile[]{null, tree, null, tree, flower, flower, null, null}
        };
        solve(board);
    }

    private void solve(Tile[][] board) {
        Board2d<Tile> s = new Board2d<>(board);
        Rules r = new Rules();
        Solver<Board2d<Tile>> solver = new Solver<>(r);
        Board2d result = solver.solve(s);
        System.out.println(result);
    }
}
