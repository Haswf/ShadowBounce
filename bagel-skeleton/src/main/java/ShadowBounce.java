import bagel.*;
import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An simple ball game.
 *
 * @author Shuyang Fan
 */
public class ShadowBounce extends AbstractGame {
    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Random random;
    private ArrayList<Ball> balls;

    private ArrayList<Powerup> powerups;
    private Renderer renderer;

    private Bucket bucket;

    private ArrayList<Board> boards;
    private Board currBoard;
    private Iterator<Board> boardIter;
    private int ballLeft = 20;

    /* ShadowBounce */
    public ShadowBounce() {
        random = new Random();

        renderer = new Renderer();

        boards = new ArrayList<>();
        boards.add(new Board("board/0.csv"));
        boards.add(new Board("board/0.csv"));
        boardIter = boards.iterator();
        currBoard = boardIter.next();

        bucket = new Bucket();
        renderer.add(bucket);

        powerups = new ArrayList<>();
        generatePowerup();

        balls = new ArrayList<>();
        renderer.addAll(currBoard.asList());
    }


    /* The entry point for the program. */
    public static void main(String[] args) {
        ShadowBounce game = new ShadowBounce();
        game.run();
    }

    public void resetBall(Input input){
        Ball ball = new Ball(Ball.INIT_POSITION, new Image("res/ball.png"), new Velocity());
        ball.setPosition(ball.getPosition().setCentre(Ball.INIT_POSITION));;
        Point mousePosition = input.getMousePosition();
        // Calculate normal vector from init point to mouse position.
        Vector2 mouseDirection = mousePosition.asVector().sub(Ball.INIT_POSITION.asVector()).normalised();
        ball.setVelocity(new Velocity(mouseDirection, Ball.INIT_SPEED));
        balls.add(ball);
    }

    /**
     * Performs a state update. This simple example shows an image that can be controlled with the arrow keys, and
     * allows the game to exit when the escape key is pressed.
     */

    @Override
    public void update(Input input) {
        boolean turnEnd = false;
        ArrayList<GameObject> toBeDestroyed = new ArrayList<>();

        if (input.isDown(MouseButtons.LEFT) && balls.size()==0 && ballLeft>0) {
            resetBall(input);
            renderer.addAll((List)balls);
        }

        // Quit game if ESCAPE key was pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        Iterator<GameObject> queueIter = renderer.getQueue().iterator();
        while (queueIter.hasNext()) {
            GameObject obj = queueIter.next();
            if (obj instanceof Peg) {
                Peg peg = (Peg)obj;
                for (Ball ball : balls) {
                    if (ball instanceof FireBall && peg.getPosition().distance(ball) < FireBall.DESTROY_RANGE){
                        if (peg.getColour()!=Peg.COLOUR.GREY){
                            toBeDestroyed.add(peg);
                        }
                    }
                    else if (ball.getCollider().collideWith(peg)) {
                        Side col = peg.getCollider().collideAtSide(ball, ball.getVelocity());
                        ball.bounce(col);
                        if (peg.getColour() != Peg.COLOUR.GREY) {
                            toBeDestroyed.add(peg);
                        }
                    }
                }
            }
            else if (obj instanceof Ball) {
                Ball ball = (Ball) obj;
                ball.move();
                if (ball.getPosition().getCentre().y > Window.getHeight()) {
                    if (ball.getCollider().collideWith(bucket)){
                        ballLeft++;
                        LOGGER.log(Level.INFO, "ballLeft + 1");
                    }
                    toBeDestroyed.add(ball);
                }
            }

            else if (obj instanceof Bucket){
                bucket.move();
            }

            else if (obj instanceof Powerup){
                Powerup up = (Powerup)obj;
                up.move();
                FireBall fb = null;
                for (Ball ball : balls) {
                    if (ball.getCollider().collideWith(up)) {
                        toBeDestroyed.add(up);
                        fb = up.activate(ball);
                        toBeDestroyed.add(ball);
                        renderer.add(fb);
                        LOGGER.log(Level.INFO, "Powerup hit\n");
                    }
                }
                if (fb != null){
                    balls.add(fb);
                }
            }
        }

        for (GameObject go : toBeDestroyed){
            if (go instanceof Peg){
                Peg p = (Peg)go;
                currBoard.remove(p);
                if (p instanceof RedPeg){
                    LOGGER.log(Level.INFO, String.format("Red ball destroyed. %d left\n", currBoard.getRedCount()));
                }
                else if (p instanceof GreenPeg){
                    GreenPeg gr = (GreenPeg)p;
                    balls.addAll(gr.duplicate(balls.get(0)));
                    LOGGER.log(Level.INFO, "Bonus balls released\n");
                    renderer.addAll((List)balls);
                }
            }
            else if (go instanceof Ball){
                if (balls.size() == 1){
                    turnEnd = true;
                }
                balls.remove(go);
            }
        }

        renderer.removeAll(toBeDestroyed);
        toBeDestroyed.clear();

        if (ballLeft==0){
            Image gg = new Image("res/gameover.png");
            gg.draw(Window.getWidth()/2, Window.getWidth()/2);
        }

        if (turnEnd){
            ballLeft--;
            LOGGER.log(Level.INFO, String.format("New turn started. %d balls left\n", ballLeft));
            renderer.removeAll(currBoard.asList());
            generatePowerup();
            currBoard.refreshGreenPeg();
            renderer.addAll(currBoard.asList());
        }

        if (currBoard.getRedCount() == 0){
            loadNextBoard();
        }

        renderer.render();

    }

    public void loadNextBoard() {
        if (boardIter.hasNext()) {
            balls.clear();
            renderer.removeAll(currBoard.asList());
            currBoard = boardIter.next();
            renderer.addAll(currBoard.asList());
            LOGGER.log(Level.INFO, "New board loaded\n");
        }
    }

    public void generatePowerup(){
        if (random.nextDouble() < Powerup.CHANCE){
            Powerup up = new Powerup();
            powerups.add(up);
            renderer.add(up);
        }
    }
}
