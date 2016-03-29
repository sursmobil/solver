package landscaper;

import solver.RuleSet;
import solver.Solver;
import solver.utils.Board2d;
import solver.utils.Board2d.Line;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

import static landscaper.Tile.*;

public class Rules implements RuleSet<Board2d<Tile>>{
    @Override
    public boolean solve(Board2d<Tile> state) {
        return false;
    }

    @Override
    public boolean verify(Board2d<Tile> state) {
        return checkSequences(state.rows()) && checkSequences(state.columns());
    }

    private boolean checkSequences(List<Line<Tile>> lines) {
        BooleanSupplier singleCheckF = () -> {
            for(Line<Tile> s : lines) {
                boolean consecutive = noMoreThenTwoConsecutive(s);
                boolean nOfTiles = validNumberOfTiles(s);
                if(!consecutive || !nOfTiles) {
                    return false;
                }
            }
            return true;
        };
        boolean duplicates = noDuplicateSequence(lines);
        boolean singleCheck =  singleCheckF.getAsBoolean();
        return duplicates && singleCheck;
    }

    private boolean noDuplicateSequence(List<Line<Tile>> lines) {
        List<Line> filled = lines.stream()
                .filter(s -> s.count(null) == 0)
                .collect(Collectors.toList());
        Set<Line> unique = new HashSet<>(filled);
        return unique.size() == filled.size();
    }

    private boolean validNumberOfTiles(Line<Tile> s) {
        int max = s.tiles().size()/2;
        Function<Tile, Boolean> validNumberOf = expected -> s.count(expected) <= max;
        return validNumberOf.apply(Tile.flower) && validNumberOf.apply(tree);
    }

    private boolean noMoreThenTwoConsecutive(Line<Tile> s) {
        for(int i = 0; i < s.tiles().size()-2; i++) {
            if(s.tiles().get(i) != null && s.tiles().get(i) == s.tiles().get(i+1) && s.tiles().get(i) == s.tiles().get(i+2)) return false;
        }
        return true;
    }

    @Override
    public boolean isComplete(Board2d state) {
        return state.isFilled();
    }

    @Override
    public Collection<Board2d<Tile>> singleProblemAlternatives(Board2d<Tile> state) {
        Line<Tile> row = state.rows().stream()
                .filter(r -> r.tiles().contains(null))
                .sorted((r1, r2) -> Long.compare(r1.count(null), r1.count(null)))
                .findFirst()
                .get();
        long trees = row.count(tree);
        long flowers = row.count(flower);
        long max = row.tiles().size()/2;
        List<Tile[]> perms = permutations(max-trees, max-flowers);
        return createAlternatives(state, row, perms);
    }

    private Collection<Board2d<Tile>> createAlternatives(Board2d<Tile> state, Line<Tile> row, List<Tile[]> perms) {
        return perms.stream().map(perm -> {
            Board2d<Tile> copy = state.freeze();
            int p = 0;
            for (int i = 0; i < row.tiles().size(); i++) {
                if (row.tiles().get(i) == null) {
                    copy.set(row.orderNumber, i, perm[p]);
                    p++;
                }
            }
            return copy;
        }).collect(Collectors.toList());
    }

    private List<Tile[]> permutations(long trees, long flowers) {
        return permutations(new Tile[(int) (trees + flowers)], 0, trees, flowers);
    }

    private List<Tile[]> permutations(Tile[] base, int i, long trees, long flowers) {
        if(trees == 0) {
            for(int y = 0; y < flowers; y++) {
                base[i+y] = Tile.flower;
            }
            return Arrays.<Tile[]>asList(base);
        } else if(flowers == 0) {
            for(int y = 0; y < trees; y++) {
                base[i+y] = tree;
            }
            return Arrays.<Tile[]>asList(base);
        } else {
            Tile[] treeBase = new Tile[base.length];
            System.arraycopy(base, 0, treeBase, 0, base.length);
            treeBase[i] = tree;
            Tile[] flowerBase = new Tile[base.length];
            System.arraycopy(base, 0, flowerBase, 0, base.length);
            flowerBase[i] = Tile.flower;
            List<Tile[]> result = new ArrayList<>(permutations(treeBase, i+1, trees-1, flowers));
            result.addAll(permutations(flowerBase, i+1, trees, flowers-1));
            return result;
        }
    }

    public static void main(String... args) {
        Tile[][] board = new Tile[][]{
                new Tile[]{null, flower, null, null},
                new Tile[]{flower, tree, null, tree},
                new Tile[]{null, null, null, null},
                new Tile[]{tree, flower, null, null}
        };
        Board2d<Tile> s = new Board2d<>(board);
        Rules r = new Rules();
        Solver<Board2d<Tile>> solver = new Solver<>(r);
        System.out.println(solver.solve(s));
    }
}
