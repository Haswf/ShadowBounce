import bagel.*;
import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * An simple ball game.
 *
 * @author Shuyang Fan
 */
public class Test extends AbstractGame {

    private static final double INIT_X = 512;
    private static final double INIT_Y = 32;
    private final Point initPosition;
    private Ball ball;
    private ArrayList<GameObject> toBeDestroyed;
    // Downward acceleration due to gravity
    private double gravityAcceleartion;
    // initial speed of the ball
    private double initSpeed;
    private Renderer renderer;
    private Board zero;
    /* ShadowBounce */
    public Test() {
        renderer = new Renderer();
        toBeDestroyed = new ArrayList<GameObject>();
        zero = new Board("res/0.csv");
        initPosition = new Point(INIT_X, INIT_Y); // initial position where Ball will be generated
        // Acceleration due to gravity
        gravityAcceleartion = 0.15;
        // initial speed of the ball
        initSpeed = 10;
    }

    public void OnBecameInvisible(GameObject go){
        if (go.getPosition().getCentre().y > Window.getHeight()){
            go.setVisibility(false);
            toBeDestroyed.add(go);
        }

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
        if (input.isDown(MouseButtons.LEFT) && ball==null) {
            ball = new Ball(initPosition, new Image("res/ball.png"));
            ball.setPosition(ball.getPosition().setCentre(initPosition));;
            Point mousePosition = input.getMousePosition();
            // Calculate normal vector from init point to mouse position.
            Vector2 mouseDirection = mousePosition.asVector().sub(initPosition.asVector()).normalised();
            ball.setVelocity(new Velocity(mouseDirection, initSpeed));
            // Make ball visible so it will be rendered in next frame.
            ball.setVisibility(true);
            renderer.add(ball);
        }

        // Update the ball if it is visible.
        if (ball!=null){
            ball.recalculatePosition(); // recalculate position based on velocity
            // increase vertical speed to simulate gravity if the Ball is visible.
            ball.setVelocity(ball.getVelocity().add(Vector2.down.mul(gravityAcceleartion)));
            // Reverse horizontal movement when the ball reaches the left or right sides.
            if (ball.getPosition().getCentre().x < 0 || ball.getPosition().getCentre().x > Window.getWidth()){
                ball.setVelocity(ball.getVelocity().reverseHorizontal());
            }
        }

        // Quit game if ESCAPE key was pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        if (ball!=null){
            Iterator<LinkedList<Peg>> it = zero.iterPegs();
            while (it.hasNext()) {
                LinkedList<Peg> list = it.next();
                for (Iterator<Peg> iterator = list.iterator(); iterator.hasNext(); ) {
                    Peg p = iterator.next();
                    if (ball.getCollider().collideWith(p)) {
                        Side col = p.getCollider().collideAtSide(ball, ball.getVelocity());
                        ball.bounce(col);
                        toBeDestroyed.add(p);
                    }
                }
            }
        }

        for (GameObject p : toBeDestroyed){
            if (p instanceof Peg){
                zero.destroy(p);
            }
        }
        renderer.addAll(zero.asList());
        renderer.removeAll(toBeDestroyed);
        renderer.render();
    }
}
