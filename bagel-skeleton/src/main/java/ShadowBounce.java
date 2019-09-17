import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.Random;

/**
 * An simple ball game.
 *
 * @author Shuyang Fan
 */
public class ShadowBounce extends AbstractGame {
    private final Point initPosition;
    private Ball ball;
    private int numOfPegs = 50;
    private Peg[] pegs;

    // the range where peg will be randomly generated.
    private static final double MIN_X = 0;
    private static final double MAX_X = 1024;
    private static final double MIN_Y = 100;
    private static final double MAX_Y = 768;

    private static final double INIT_X = 512;
    private static final double INIT_Y = 32;

    // Downward acceleration due to gravity
    private double gravityAcceleartion;
    // initial speed of the ball
    private double initSpeed;

    /* ShadowBounce */
    public ShadowBounce() {
        Random random = new Random(); // random generator to randomly place Peg
        initPosition = new Point(INIT_X, INIT_Y); // initial position where Ball will be generated
        ball = new Ball(initPosition, new Image("res/ball.png"));
        pegs = new Peg[numOfPegs]; // An array of pegs


        // Acceleration due to gravity
        gravityAcceleartion = 0.15;
        // initial speed of the ball
        initSpeed = 10;

        // Randomly generate 50 pegs
        for (int i=0;i<numOfPegs;i++){
            pegs[i] = new Peg(new Point(0, 0), new Image("res/blue-peg.png"), true);

            // Choose position so the pegs don't overlap
            outer: while (true){
                Point position = new Point(random.nextDouble()*MAX_X+MIN_X,
                                        random.nextDouble()*(MAX_Y-MIN_Y)+MIN_Y);
                pegs[i].setPosition(new Position(position, pegs[i].getImage().getWidth(), pegs[i].getImage().getHeight()));
                for (int j=0; j<i; j++){
                    double distance = pegs[j].distance(pegs[i]);
                    double right = pegs[j].getBoundingBox().right();
                    double left = pegs[j].getBoundingBox().left();
                    // if the distance between 2 pegs is smaller than 2*r where r is radius of the peg
                    if (distance < right - left){
                        // generate a new position and try again.
                        continue outer;
                    }
                }
                // if the peg won't overlap with any existing pegs, break
                break;
            }
        }
    }

    /* The entry point for the program. */
    public static void main(String[] args) {
        ShadowBounce game = new ShadowBounce();
        game.run();
    }

    /**
     * Performs a state update. This simple example shows an image that can be controlled with the arrow keys, and
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        /* Make the ball start off moving towards the mouse if it's invisible */
        if (input.isDown(MouseButtons.LEFT) && !ball.getVisibility()) {
            ball.setPosition(ball.getPosition().setCentre(initPosition));;
            Point mousePosition = input.getMousePosition();
            // Calculate normal vector from init point to mouse position.
            Vector2 mouseDirection = mousePosition.asVector().sub(initPosition.asVector()).normalised();
            ball.setVelocity(new Velocity(mouseDirection, initSpeed));
            // Make ball visible so it will be rendered in next frame.
            ball.setVisibility(true);
        }

        // Quit game if ESCAPE key was pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // Update the ball if it is visible.
        if (ball.getVisibility()){
            ball.recalculatePosition(); // recalculate position based on velocity
            // increase vertical speed to simulate gravity if the Ball is visible.
            ball.setVelocity(ball.getVelocity().add(Vector2.down.mul(gravityAcceleartion)));
        }

        // Reverse horizontal movement when the ball reaches the left or right sides.
        if (ball.getPosition().getCentre().x < 0 || ball.getPosition().getCentre().x > Window.getWidth()){
            ball.setVelocity(ball.getVelocity().reverseHorizontal());
        }

        // Make the ball invisible when it drops out of the window
        if (ball.getPosition().getCentre().y > Window.getHeight()){
            ball.setVisibility(false);
        }

        // Render the ball
        ball.render();

        for (int i=0;i<numOfPegs;i++){
            // Make the peg disappear if it was hit by the ball
            if (ball.getBoundingBox().intersects(pegs[i].getBoundingBox())){
                pegs[i].setVisibility(false);
            }
            // render each peg
            pegs[i].render();
        }
    }
}
