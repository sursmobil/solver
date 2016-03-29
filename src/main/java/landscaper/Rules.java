package landscaper;

import solver.RuleSet;
import solver.utils.*;
import solver.utils.Board2d.Line;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

import static landscaper.TileType.flower;
import static landscaper.TileType.tree;

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
        Collection<TileType[]> perms = permutations(max-trees, max-flowers);
        return createAlternatives(state, row, perms);
    }

    private Collection<Board2d<TileType>> createAlternatives(Board2d<TileType> state, Line<TileType> row, Collection<TileType[]> perms) {
        return Utils.createAlternatives(perms, state::copy, (gs, t, p) -> gs.set(p.row, p.column, t), row);
    }

    private Collection<TileType[]> permutations(long trees, long flowers) {
        return Permutations.noRepetition(TileType.class)
                .withInstance(tree, (int) trees)
                .withInstance(flower, (int) flowers)
                .get();
    }

}
