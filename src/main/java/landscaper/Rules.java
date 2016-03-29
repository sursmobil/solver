package landscaper;

import solver.RuleSet;
import solver.Solver;
import solver.utils.Board2d;
import solver.utils.Area;
import solver.utils.Board2d.Line;
import solver.utils.Tile;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

import static landscaper.TileType.*;

public class Rules implements RuleSet<Board2d<TileType>>{
    @Override
    public boolean solve(Board2d<TileType> state) {
        return false;
    }

    @Override
    public boolean verify(Board2d<TileType> state) {
        return checkSequences(state.rows()) && checkSequences(state.columns());
    }

    private boolean checkSequences(List<Line<TileType>> areas) {
        BooleanSupplier singleCheckF = () -> {
            for(Line<TileType> s : areas) {
                boolean consecutive = noMoreThenTwoConsecutive(s);
                boolean nOfTiles = validNumberOfTiles(s);
                if(!consecutive || !nOfTiles) {
                    return false;
                }
            }
            return true;
        };
        boolean duplicates = noDuplicateSequence(areas);
        boolean singleCheck =  singleCheckF.getAsBoolean();
        return duplicates && singleCheck;
    }

    private boolean noDuplicateSequence(List<Line<TileType>> areas) {
        List<Area> filled = areas.stream()
                .filter(s -> s.count(null) == 0)
                .collect(Collectors.toList());
        Set<Area> unique = new HashSet<>(filled);
        return unique.size() == filled.size();
    }

    private boolean validNumberOfTiles(Line<TileType> s) {
        int max = s.size()/2;
        Function<TileType, Boolean> validNumberOf = expected -> s.count(expected) <= max;
        return validNumberOf.apply(TileType.flower) && validNumberOf.apply(tree);
    }

    private boolean noMoreThenTwoConsecutive(Line<TileType> s) {
        for(int i = 0; i < s.size()-2; i++) {
            Tile<TileType, ?> current = s.get(i);
            Tile<TileType, ?> next = s.get(i+1);
            Tile<TileType, ?> next2 = s.get(i+2);
            if(current.content != null && current.content.equals(next.content) && current.content.equals(next2.content)) return false;
        }
        return true;
    }

    @Override
    public boolean isComplete(Board2d state) {
        return state.isFilled();
    }

    @Override
    public Collection<Board2d<TileType>> singleProblemAlternatives(Board2d<TileType> state) {
        Line<TileType> row = state.rows().stream()
                .filter(r -> r.contains(null))
                .sorted((r1, r2) -> Long.compare(r1.count(null), r1.count(null)))
                .findFirst()
                .get();
        long trees = row.count(tree);
        long flowers = row.count(flower);
        long max = row.size()/2;
        List<TileType[]> perms = permutations(max-trees, max-flowers);
        return createAlternatives(state, row, perms);
    }

    private Collection<Board2d<TileType>> createAlternatives(Board2d<TileType> state, Line<TileType> row, List<TileType[]> perms) {
        return perms.stream().map(perm -> {
            Board2d<TileType> copy = state.copy();
            int p = 0;
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i).content == null) {
                    copy.set(row.order, i, perm[p]);
                    p++;
                }
            }
            return copy;

        }).collect(Collectors.toList());
    }

    private List<TileType[]> permutations(long trees, long flowers) {
        return permutations(new TileType[(int) (trees + flowers)], 0, trees, flowers);
    }

    private List<TileType[]> permutations(TileType[] base, int i, long trees, long flowers) {
        if(trees == 0) {
            for(int y = 0; y < flowers; y++) {
                base[i+y] = TileType.flower;
            }
            return Arrays.<TileType[]>asList(base);
        } else if(flowers == 0) {
            for(int y = 0; y < trees; y++) {
                base[i+y] = tree;
            }
            return Arrays.<TileType[]>asList(base);
        } else {
            TileType[] treeBase = new TileType[base.length];
            System.arraycopy(base, 0, treeBase, 0, base.length);
            treeBase[i] = tree;
            TileType[] flowerBase = new TileType[base.length];
            System.arraycopy(base, 0, flowerBase, 0, base.length);
            flowerBase[i] = TileType.flower;
            List<TileType[]> result = new ArrayList<>(permutations(treeBase, i+1, trees-1, flowers));
            result.addAll(permutations(flowerBase, i+1, trees, flowers-1));
            return result;
        }
    }

    public static void main(String... args) {
        TileType[][] board = new TileType[][]{
                new TileType[]{null, flower, null, null},
                new TileType[]{flower, tree, null, tree},
                new TileType[]{null, null, null, null},
                new TileType[]{tree, flower, null, null}
        };
        Board2d<TileType> s = new Board2d<>(board);
        Rules r = new Rules();
        Solver<Board2d<TileType>> solver = new Solver<>(r);
        System.out.println(solver.solve(s));
    }
}
