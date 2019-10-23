import bagel.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An arcade game developed in Bagel for SWEN20003 Project 2.
 * @author Shuyang Fan, shuyangf@student.unimelb.edu.au
 */
public class ShadowBounce extends AbstractGame {
    private final static int TOTAL_BOARDS = 5;
    private final static int TOTAL_BALLS = 20;
    // Get global logger for logging
    public final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // The current board on the screen.
    private Board currBoard;
    // board iterator used to switch to next board
    private Iterator<Board> boardIter;

    // An ArrayList storing balls
    private int ballLeft = TOTAL_BALLS;
    // An ArrayList of balls representing balls on the screen.
    private ArrayList<Ball> balls;
    private Bucket bucket;

    private ArrayList<Powerup> powerups;

    // All GameObjects on the screen;
    private ArrayList<GameObject> onScreen;
    // GameObjects to be added to the screen in the next frame
    private ArrayList<GameObject> toAdd;
    // GameObjects to be removed in the next frame
    private ArrayList<GameObject> toRemove;
    // A flap indicating this turn has ended
    private boolean turnEnd;

    /**
     * ShadowBounce Constructor
     * Initialise the game.
     */
    public ShadowBounce() {
        // Instantiate toAdd and toRemove .
        toAdd = new ArrayList<>();
        toRemove = new ArrayList<>();

        // Instantiate onScreen.
        onScreen = new ArrayList<>();
        // Load boards from csv files.
        ArrayList<Board> boards = loadBoards();
        // Create an iterator for boards
        boardIter = boards.iterator();
        // Load the first board into screen.
        currBoard = boardIter.next();
        balls = new ArrayList<>();
        addGameObject(currBoard.asList());
        // Create a bucket
        bucket = new Bucket();
        addGameObject(bucket);

        powerups = new ArrayList<>();
        addGameObject(Powerup.createPowerup());
    }

    /**
     * Returns the number of balls(turns) left.
     * @return the number of balls(turns) left
     */
    public int getBallLeft(){
        return ballLeft;
    }

    /**
     * Sets the number of balls left.
     * @param left the number of balls(turns) left
     */
    public void setBallLeft(int left){
        this.ballLeft = left;
    }

    /**
     * Gets the board currently on the screen.
     * @return a board on the screen.
     */
    public Board getCurrBoard() {
        return currBoard;
    }

    /* The entry point for the program. */
    public static void main(String[] args) {
        ShadowBounce game = new ShadowBounce();
        game.run();
    }

    /**
     * update is called once per frame.
     * @param input Bagel input
     */
    @Override
    public void update(Input input) {
        /* Shoot a ball at the direction of mouse position
         - if there is no ball on the screen
         - the player has ball remaining.
        */
        if (input.isDown(MouseButtons.LEFT) && balls.size() == 0 && ballLeft > 0) {
            addGameObject(Ball.shoot(input.getMousePosition()));
            ballLeft--;
        }

        // Quit game if ESCAPE key was pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }


        for (Ball ball : balls) {
            /* Conduct collision detection for each ball and each peg
               trigger their onCollisionEnter if a collision is detected
            */
            for (Peg peg : currBoard.asList()) {
                if (ball.collideWith(peg)) {
                    ball.onCollisionEnter(this, peg);
                    if (peg instanceof OnCollisionEnter){
                        ((OnCollisionEnter)peg).onCollisionEnter(this, ball);
                    }
                }
            }
            /* Conduct collision detection for each ball and each powerup
               trigger powerup's onCollisionEnter if a collision is detected
            */
            for (Powerup powerup : powerups) {
                if (ball.collideWith(powerup)) {
                    powerup.onCollisionEnter(this, ball);
                    ball.onCollisionEnter(this, powerup);
                }
            }
            /* Conduct collision detection for each ball and the bucket
               trigger the bucket's onCollisionEnter if a collision is detected
            */
            if (ball.collideWith(bucket)){
                bucket.onCollisionEnter(this, ball);
                ball.onCollisionEnter(this, bucket);
            }
        }

        // Add GameObject in toAdd to onScreen.
        addToScreen();
        // Remove GameObject in toRemove from onScreen.
        removeFromScreen();

        // Start next turn
        if (turnEnd) {
            nextTurn();
            turnEnd = false;
        }

        // Load next board if there's no red ball left on the board.
        if (currBoard.getRedCount() == 0) {
            loadNextBoard();
        }

        // Call update method for each GameObject on the screen.
        for (GameObject g : onScreen) {
            g.update(this);
        }
    }

    /**
     * Loads next board into screen.
     */
    private void loadNextBoard () {
        if (boardIter.hasNext()) {
            // Remove remaining pegs from the screen.
            removeGameObject(onScreen);
            removeFromScreen();
            // Add the bucket again
            addGameObject(bucket);
            // Switch to the next boards
            currBoard = boardIter.next();
            // Add pegs on the next boards to the screen
            addGameObject(currBoard.asList());
            LOGGER.log(Level.INFO, "New board loaded\n");
        }
    }

    /**
     * Remove all GameObjects in toRemove from the screen.
     */
    private void removeFromScreen () {
        for (GameObject go : toRemove) {
            if (go instanceof Peg) {
                currBoard.remove((Peg) go);
            } else if (go instanceof Ball) {
                // if the last ball has fallen out of the screen
                // this turn should end.
                if (balls.size() == 1) {
                    turnEnd = true;
                }
                balls.remove(go);
            } else if (go instanceof Powerup) {
                powerups.remove(go);
            }
        }
        // Remove everything from the screen.
        onScreen.removeAll(toRemove);
        toRemove.clear();
    }

    /**
     * Add GameObject in ToAdd to onScreen
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
     * Change the position of green peg and attempts to create a powerup.
     */
    private void nextTurn() {
        LOGGER.log(Level.INFO, String.format("New turn started. %d balls left\n", ballLeft));
        onScreen.clear();
        // Change the position of green peg it hasn't been destroyed.
        currBoard.refreshGreenPeg();
        // Reload remaining pegs on the current board
        onScreen.addAll(currBoard.asList());
        // Add bucket to the screen.
        addGameObject(bucket);
        // Attempt to add a Powerup to the screen.
        addGameObject(Powerup.createPowerup());
    }

    /**
     * Load boards from csv files
     * @return An ArrayList of boards read from csv
     */
    private ArrayList<Board> loadBoards(){
        ArrayList<Board> boards = new ArrayList<>();
        for (int i = 0; i< ShadowBounce.TOTAL_BOARDS; i++){
            boards.add(new Board(String.format("res/%d.csv", i)));
        }
        return boards;
    }

    /**
     * Removes a GameObject from screen in next frame.
     * @param removal the GameObject to be removed.
     * @param <T> removal must be a subclass of GameObject.
     */
    public <T extends GameObject> void removeGameObject(T removal){
        this.toRemove.add(removal);
    }

    /**
     * Removes an ArrayList of GameObjects from the screen in the next frame.
     * @param removals an ArrayList of GameObjects to be removed.
     * @param <T> removals must be a ArrayList of subclass of GameObject.
     */
    public  <T extends GameObject> void removeGameObject(ArrayList<T> removals){
        this.toRemove.addAll(removals);
    }

    /**
     * Adds a GameObject to the screen in the next frame.
     * @param creation the GameObject to be added.
     * @param <T> creation must be a subclass of GameObject.
     */
    public  <T extends GameObject> void addGameObject(T creation){
        this.toAdd.add(creation);
    }

    /**
     * Adds an ArrayList of GameObjects to the screen in the next frame.
     * @param creations an ArrayList of GameObjects to be created.
     * @param <T> creations must be a ArrayList of subclass of GameObject.
     */
    public  <T extends GameObject> void addGameObject(ArrayList<T> creations){
        this.toAdd.addAll(creations);
    }
}