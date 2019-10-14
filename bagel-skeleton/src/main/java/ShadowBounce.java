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
    public final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private ArrayList<Ball> balls;

    private ArrayList<Powerup> powerups;
    private ArrayList<GameObject> onScreen;

    private Bucket bucket;

    private ArrayList<Board> boards;
    private Board currBoard;
    private Iterator<Board> boardIter;
    private int ballLeft = 20;

    private ArrayList<GameObject> toBeDestroyed;
    private ArrayList<GameObject> toBeAdded;

    /* ShadowBounce */
    public ShadowBounce() {
        toBeDestroyed = new ArrayList<>();
        toBeAdded = new ArrayList<>();

        onScreen = new ArrayList<>();

        boards = new ArrayList<>();
        loadBoards();

        boardIter = boards.iterator();
        currBoard = boardIter.next();

        bucket = new Bucket();
        toBeAdded.add(bucket);

        powerups = new ArrayList<>();
        toBeAdded.addAll(Powerup.createPowerup());

        balls = new ArrayList<>();
        toBeAdded.addAll(currBoard.asList());
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

        if (input.isDown(MouseButtons.LEFT) && balls.size()==0 && ballLeft>0) {
            toBeAdded.add(Ball.shoot(input));
        }

        // Quit game if ESCAPE key was pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        for (GameObject obj : onScreen) {
            if (obj instanceof Movable) {
                ((Movable) obj).move();
            }

            if (obj instanceof Peg) {
                Peg peg = (Peg) obj;

                for (Ball ball : balls) {

                    if (ball.collideWith(peg)) {
                        ball.onCollisionEnter(peg);
                        if (peg instanceof OnCollisionRemove) {
                            OnCollisionRemove removal = (OnCollisionRemove) peg;
                            toBeDestroyed.add(removal.onCollisionRemove());
                        }

                        if (ball instanceof FireBall) {
                            for (Peg p : currBoard.asList()) {
                                if (((FireBall) ball).withinRangeDestroy(p)) {
                                    toBeDestroyed.add(p);
                                }
                            }
                        }
                        if (peg instanceof OnCollisionCreate) {
                            toBeAdded.addAll(((OnCollisionCreate) peg).onCollisionCreate(ball));
                        }
                    }
                }
            } else if (obj instanceof Ball) {
                Ball ball = (Ball) obj;
                if (bucket.dropIntoBucket(ball)) {
                    ShadowBounce.LOGGER.log(Level.INFO, "ballLeft + 1");
                    toBeDestroyed.add(ball);
                    ballLeft++;
                }
                if (ball.outOfScreen()) {
                    toBeDestroyed.add(ball);
                }
            } else if (obj instanceof Powerup) {
                Powerup up = (Powerup) obj;
                for (Ball ball : balls) {
                    if (ball.collideWith(up)) {
                        toBeAdded.addAll(up.onCollisionCreate(ball));
                        toBeDestroyed.add(up);
                        toBeDestroyed.add(ball);
                    }
                }
            }
        }

        addToScreen();

        if (removeFromScreen()){
            nextTurn();
        }


        if (currBoard.getRedCount() == 0){
            loadNextBoard();
        }

        for (GameObject g:onScreen){
            g.render();
        }

    }

    private void loadNextBoard() {
        if (boardIter.hasNext()) {
            onScreen.removeAll(balls);
            balls.clear();
            onScreen.removeAll(currBoard.asList());
            currBoard = boardIter.next();
            onScreen.addAll(currBoard.asList());
            LOGGER.log(Level.INFO, "New board loaded\n");
        }
    }

    private boolean removeFromScreen(){
        boolean turnEnd = false;
        for (GameObject go : toBeDestroyed){
            if (go instanceof Peg){
                currBoard.remove((Peg)go);
            }
            else if (go instanceof Ball){
                if (balls.size() == 1){
                    turnEnd = true;
                }
                balls.remove(go);
            }
        }
        onScreen.removeAll(toBeDestroyed);
        toBeDestroyed.clear();
        return turnEnd;
    }

    private void addToScreen(){
        for (GameObject go : toBeAdded){
            if (go instanceof Ball){
                balls.add((Ball)go);
            }
        }
        onScreen.addAll(toBeAdded);
        toBeAdded.clear();
    }

    private void nextTurn(){
        ballLeft--;
        LOGGER.log(Level.INFO, String.format("New turn started. %d balls left\n", ballLeft));
        onScreen.removeAll(currBoard.asList());
        currBoard.refreshGreenPeg();
        toBeAdded.addAll(Powerup.createPowerup());
        onScreen.addAll(currBoard.asList());
    }

    private void loadBoards(){
        int i;
        int boardNumber = 5;
        for (i=0; i<boardNumber; i++){
            boards.add(new Board(String.format("res/%d.csv", i)));
        }
    }
}