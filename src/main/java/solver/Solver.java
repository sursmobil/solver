package solver;

public class Solver<GS extends GameState> {
    final RuleSet<GS> rules;

    public Solver(RuleSet<GS> rules) {
        this.rules = rules;
    }

    public GS solve(GS state) {
        Iteration<GS> first = new Iteration<>(rules, state);
        return first.solve().get();
    }
}
