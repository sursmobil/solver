package landscaper;

import solver.RuleSet;
import solver.Solver;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

import static landscaper.Tile.empty;
import static landscaper.Tile.flower;
import static landscaper.Tile.tree;

public class Rules implements RuleSet<State>{
    @Override
    public boolean solve(State state) {
        return false;
    }

    @Override
    public boolean verify(State state) {
        boolean res = checkSequences(state.rows()) && checkSequences(state.columns());
        return res;
    }

    private boolean checkSequences(List<State.Sequence> sequences) {
        BooleanSupplier singleCheck = () -> {
            for(State.Sequence s : sequences) {
                if(!noMoreThenTwoConsecutive(s.tiles()) || !validNumberOfTiles(s.tiles())) {
                    return false;
                }
            }
            return true;
        };
        return noDuplicateSequence(sequences) && singleCheck.getAsBoolean();
    }

    private boolean noDuplicateSequence(List<State.Sequence> sequences) {
        Set<State.Sequence> unique = new HashSet<>(sequences);
        return unique.size() == sequences.size();
    }

    private boolean validNumberOfTiles(List<Tile> s) {
        int max = s.size()/2;
        Function<Tile, Boolean> validNumberOf = expected ->
            s.stream().filter(t -> t == expected).count() <= max;
        return validNumberOf.apply(Tile.flower) && validNumberOf.apply(tree);
    }

    private boolean noMoreThenTwoConsecutive(List<Tile> s) {
        for(int i = 0; i < s.size()-2; i++) {
            if(s.get(i) != Tile.empty && s.get(i) == s.get(i+1) && s.get(1) == s.get(i+2)) return false;
        }
        return true;
    }

    @Override
    public boolean isComplete(State state) {
        return state.isFilled();
    }

    @Override
    public Collection<State> singleProblemAlternatives(State state) {
        State.Sequence row = state.rows().stream()
                .filter(r -> r.tiles().contains(Tile.empty))
                .sorted((r1, r2) -> Long.compare(count(r1, empty), count(r2, empty)))
                .findFirst()
                .get();
        long trees = count(row, tree);
        long flowers = count(row, flower);
        long max = row.tiles().size()/2;
        List<Tile[]> perms = permutations(max-trees, max-flowers);
        return createAlternatives(state, row, perms);
    }

    private long count(State.Sequence s, Tile expected) {
        return s.tiles().stream().filter(t -> t == expected).count();
    }

    private Collection<State> createAlternatives(State state, State.Sequence row, List<Tile[]> perms) {
        return perms.stream().map(perm -> {
            State copy = state.freeze();
            int p = 0;
            for (int i = 0; i < row.tiles().size(); i++) {
                if (row.tiles().get(i) == Tile.empty) {
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
                new Tile[]{empty, flower, empty, empty},
                new Tile[]{flower, tree, empty, tree},
                new Tile[]{empty, empty, empty, empty},
                new Tile[]{tree, flower, empty, empty}
        };
        State s = new State(board);
        Rules r = new Rules();
        Solver<State> solver = new Solver<>(r);
        Collection<State> states = r.singleProblemAlternatives(s);
        System.out.println(solver.solve(s));
    }
}
