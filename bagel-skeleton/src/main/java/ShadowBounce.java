import bagel.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An simple ball game game
 *
 * @author Shuyang Fan
 */
public class ShadowBounce extends AbstractGame {
    // Get global logger for debug
    private final static int TotalBoard = 5;
    //
    private final static int TotalBall = 20;
    public final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Board currBoard;
    // board iterator used to switch to next board
    private Iterator<Board> boardIter;

    // An ArrayList storing balls
    private int ballLeft = TotalBall;
    // An ArrayList of balls representing balls on the screen.
    private ArrayList<Ball> balls;
    private Bucket bucket;

    // All GameObjects on the screen;
    private ArrayList<GameObject> onScreen;
    // GameObjects to be removed in the next frame
    private ArrayList<GameObject> toRemove;

    // GameObjects to be added to the screen in the next frame
    private ArrayList<GameObject> toAdd;

    /* ShadowBounce */
    public ShadowBounce() {
        toRemove = new ArrayList<>();
        toAdd = new ArrayList<>();

        onScreen = new ArrayList<>();
        // Load boards from csv
        ArrayList<Board> boards = loadBoards();

        boardIter = boards.iterator();
        currBoard = boardIter.next();

        bucket = new Bucket();
        toAdd.add(bucket);

        toAdd.addAll(Powerup.createPowerup());

        balls = new ArrayList<>();
        toAdd.addAll(currBoard.asList());
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
        // Only shoot a new ball if there is more than 0 ball left
        if (input.isDown(MouseButtons.LEFT) && balls.size()==0 && ballLeft>0) {
            toAdd.add(Ball.shoot(input));
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
                            toRemove.add(removal.onCollisionRemove());
                        }

                        if (ball instanceof FireBall) {
                            toRemove.addAll(peg.withinRange(currBoard.asList(), FireBall.DESTROY_RANGE));
                        }
                        if (peg instanceof OnCollisionCreate) {
                            toAdd.addAll(((OnCollisionCreate) peg).onCollisionCreate(ball));
                        }
                    }
                }
            }

            else if (obj instanceof Ball) {
                Ball ball = (Ball) obj;
                if (bucket.dropIntoBucket(ball)) {
                    ShadowBounce.LOGGER.log(Level.INFO, "ballLeft + 1");
                    toRemove.add(ball);
                    ballLeft++;
                }
                if (ball.outOfScreen()) {
                    toRemove.add(ball);
                }

            } else if (obj instanceof Powerup) {
                Powerup powerup = (Powerup) obj;
                for (Ball ball : balls) {
                    if (ball.collideWith(powerup)) {
                        toAdd.addAll(powerup.onCollisionCreate(ball));
                        toRemove.add(powerup);
                        toRemove.add(ball);
                    }
                }
            }
        }

        addToScreen();
        removeFromScreen();

        if (turnEnd()){
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

    /**
     *
     */
    private void removeFromScreen(){
        for (GameObject go : toRemove){
            if (go instanceof Peg){
                currBoard.remove((Peg)go);
            }
            else if (go instanceof Ball){
                balls.remove(go);
            }
        }
        onScreen.removeAll(toRemove);
        toRemove.clear();
    }

    /**
     * Add each object in ToAdd to Screen
     */
    private void addToScreen(){
        for (GameObject go : toAdd){
            if (go instanceof Ball){
                balls.add((Ball)go);
            }
        }
        onScreen.addAll(toAdd);
        toAdd.clear();
    }

    /**
     * Return
     * @return if this turn ended (i.e. there's no ball on the screen)
     */
    private boolean turnEnd(){
        /* ballLeft is compared with TotalBall to handle the case
         * that at the start of the game, there is no ball on the screen.
         */

        return balls.size() == 0 && ballLeft != TotalBall;
    }

    /*
     *
     */
    private void nextTurn(){
        ballLeft--;
        LOGGER.log(Level.INFO, String.format("New turn started. %d balls left\n", ballLeft));
        onScreen.removeAll(currBoard.asList());
        currBoard.refreshGreenPeg();
        onScreen.addAll(currBoard.asList());
        onScreen.addAll(Powerup.createPowerup());
    }

    /**
     * Load boards from csv
     * @return An ArrayList of boards read from csv
     */
    private ArrayList<Board> loadBoards(){
        ArrayList<Board> boards = new ArrayList<>();
        for (int i = 0; i< ShadowBounce.TotalBoard; i++){
            boards.add(new Board(String.format("res/%d.csv", i)));
        }
        return boards;
    }
}