package solver;

import java.util.Collection;

public interface RuleSet<GS extends GameState> {
    /**
     * Used to apply special patterns which can make solving faster. For example in
     * sudoku if only one number is missing in row insert this number instead of
     * checking every possible number
     * @return true if state was changed false otherwise
     * @param state Current state of game
     */
    boolean solve(GS state);

    /**
     * Used to verify if current state applies all the rules
     * @return true if state is valid of false if there is an error
     * @param state Current state of game
     */
    boolean verify(GS state);

    /**
     *
     * @param state Current state of game
     * @return boolean determining if game is complete
     */
    boolean isComplete(GS state);

    /**
     * Return collection of all alternatives for single problem. For example for
     * sudoku where two numbers are missing in row this method will return two possible states.
     * It is not specified which problem should be used, but it is encouraged to use problem with
     * minimal number of alternatives.
     *
     * It is expected for this method to return at least two alternatives. Zero alternatives is not possible in
     * not completed game and one alternative is certain and should be handled in solve method.
     *
     * @param state Current game state
     * @return collection of possible states
     */
    Collection<GS> singleProblemAlternatives(GS state);
}
