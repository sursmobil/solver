package solver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Iteration<GS extends GameState> {
    final RuleSet<GS> rules;
    final GS state;

    public Iteration(RuleSet<GS> rules, GS state) {
        this.rules = rules;
        this.state = state;
    }

    /**
     *
     * @return solved game state, empty otherwise
     */
    public Optional<GS> solve() {
        while(rules.solve(state)){
            if(!rules.verify(state)) {
                return Optional.empty();
            }
        }
        if(!rules.isComplete(state)) {
            return makeAssumption();
        } else {
            return Optional.of(state);
        }
    }

    private Optional<GS> makeAssumption() {
        List<GS> alternatives = rules.singleProblemAlternatives(state).stream()
                .filter(rules::verify)
                .collect(Collectors.toList());
        if(alternatives.isEmpty()) {
            return Optional.empty();
        } else {
            Optional<GS> result = Optional.empty();
            for(GS a : alternatives) {
                if(rules.isComplete(a)) {
                    result = Optional.of(a);
                    break;
                } else {
                    Iteration<GS> next = new Iteration<>(rules, a);
                    result = next.solve();
                    if (result.isPresent()) {
                        break;
                    }
                }
            }
            return result;
        }
    }
}
