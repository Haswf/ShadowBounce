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
    //private Powerup powerup;
    private ArrayList<Powerup> powerups;
    // All GameObjects on the screen;
    private ArrayList<GameObject> onScreen;
    // GameObjects to be removed in the next frame
    private ArrayList<GameObject> toRemove;

    // GameObjects to be added to the screen in the next frame
    private ArrayList<GameObject> toAdd;
    private boolean turnEnd;
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
        addGameObject(bucket);

        powerups = new ArrayList<>();
        addGameObject(Powerup.createPowerup());

        balls = new ArrayList<>();
        addGameObject(currBoard.asList());
    }

    public int getBallLeft(){
        return ballLeft;
    }

    public void setBallLeft(int left){
        this.ballLeft = left;
    }

    public Board getCurrBoard() {
        return currBoard;
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
        if (input.isDown(MouseButtons.LEFT) && balls.size() == 0 && ballLeft > 0) {
            addGameObject(Ball.shoot(input.getMousePosition()));
            ballLeft--;
        }

        // Quit game if ESCAPE key was pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        for (Ball ball : balls) {
            for (Peg peg : currBoard.asList()) {
                if (ball.collideWith(peg)) {
                    ball.onCollisionEnter(this, peg);
                    peg.onCollisionEnter(this, ball);
                }
            }

            for (Powerup powerup : powerups) {
                if (ball.collideWith(powerup)) {
                    powerup.onCollisionEnter(this, ball);
                }
            }
            if (ball.collideWith(bucket)){
                bucket.onCollisionEnter(this, ball);
            }
        }

        addToScreen();
        removeFromScreen();

        if (turnEnd) {
            nextTurn();
            turnEnd = false;
        }

        if (currBoard.getRedCount() == 0) {
            loadNextBoard();
        }

        for (GameObject g : onScreen) {
            g.update(this);
            }
        }

    private void loadNextBoard () {
        if (boardIter.hasNext()) {
            removeGameObject(onScreen);
            removeFromScreen();
            addGameObject(bucket);
            currBoard = boardIter.next();
            addGameObject(currBoard.asList());
            LOGGER.log(Level.INFO, "New board loaded\n");
        }
    }

    /**
     *
     */
    private void removeFromScreen () {
        for (GameObject go : toRemove) {
            if (go instanceof Peg) {
                currBoard.remove((Peg) go);
            } else if (go instanceof Ball) {
                if (balls.size() == 1) {
                    turnEnd = true;
                }
                balls.remove(go);
            } else if (go instanceof Powerup) {
                powerups.remove(go);
            }
        }
        onScreen.removeAll(toRemove);
        toRemove.clear();
    }

    /**
     * Add each object in ToAdd to Screen
     */
    private void addToScreen () {
        for (GameObject go : toAdd) {
            // if a new ball is created, add it to balls.
            if (go instanceof Ball) {
                balls.add((Ball) go);
            }
            // if a new powerup is created, add it to powerups.
            else if (go instanceof Powerup) {
                powerups.add((Powerup) go);
            }
        }
        // Add all GameObject to the screen.
        onScreen.addAll(toAdd);
        // Clear toAdd for next frame.
        toAdd.clear();
    }

    /*
     *
     */
    private void nextTurn() {
        LOGGER.log(Level.INFO, String.format("New turn started. %d balls left\n", ballLeft));
        onScreen.clear();
        // Change the position of green peg it hasn't been destroyed.
        currBoard.refreshGreenPeg();
        onScreen.addAll(currBoard.asList());
        addGameObject(bucket);
        addGameObject(Powerup.createPowerup());
    }

    /**
     * Load boards from csv files
     * @return An ArrayList of boards read from csv
     */
    private ArrayList<Board> loadBoards(){
        ArrayList<Board> boards = new ArrayList<>();
        for (int i = 0; i< ShadowBounce.TotalBoard; i++){
            boards.add(new Board(String.format("res/%d.csv", i)));
        }
        return boards;
    }

    public <T extends GameObject> void removeGameObject(T go){
        this.toRemove.add(go);
    }
    public  <T extends GameObject> void removeGameObject(ArrayList<T> gameObjects){
        this.toRemove.addAll(gameObjects);
    }

    public  <T extends GameObject> void addGameObject(T go){
        this.toAdd.add(go);
    }
    public  <T extends GameObject> void addGameObject(ArrayList<T> gameObjects){
        this.toAdd.addAll(gameObjects);
    }
}