import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.Random;

/**
 * An simple ball game.
 *
 * @author Shuyang Fan
 */
public class Test extends AbstractGame {
    private Board zero;
    /* ShadowBounce */
    public Test() {
        zero = new Board("res/0.csv");
    }

    /* The entry point for the program. */
    public static void main(String[] args) {
        Test game = new Test();
        game.run();
    }

    /**
     * Performs a state update. This simple example shows an image that can be controlled with the arrow keys, and
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        for (Peg el : zero.pegs) {
            System.out.println(el.toString());
            el.render();
        }
    }
}
