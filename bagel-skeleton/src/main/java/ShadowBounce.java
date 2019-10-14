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
    public final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    // An ArrayList storing balls
    private ArrayList<Ball> balls;

    private ArrayList<Powerup> powerups;
    private ArrayList<GameObject> onScreen;

    private Bucket bucket;

    private ArrayList<Board> boards;
    private Board currBoard;
    private Iterator<Board> boardIter;
    private int ballLeft = 20;
    private HashMap<Class, ArrayList<GameObject>> GameObjectManager;
    private ArrayList<GameObject> toRemove;
    private ArrayList<GameObject> toAdd;

    /* ShadowBounce */
    public ShadowBounce() {
        toRemove = new ArrayList<>();
        toAdd = new ArrayList<>();
        GameObjectManager = new HashMap<>();


        onScreen = new ArrayList<>();

        boards = new ArrayList<>();
        loadBoards();

        boardIter = boards.iterator();
        currBoard = boardIter.next();

        bucket = new Bucket();
        toAdd.add(bucket);

        powerups = new ArrayList<>();
        toAdd.addAll(Powerup.createPowerup());

        balls = new ArrayList<>();
        //toAdd.addAll(currBoard.asList());
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
                            for (Peg p : currBoard.asList()) {
                                if (((FireBall) ball).withinRangeDestroy(p)) {
                                    toRemove.add(p);
                                }
                            }
                        }
                        if (peg instanceof OnCollisionCreate) {
                            toAdd.addAll(((OnCollisionCreate) peg).onCollisionCreate(ball));
                        }
                    }
                }
            } else if (obj instanceof Ball) {
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
                    }
                }
            }
        }


        addToScreen();
        boolean isEnd = removeFromScreen();

        if (isEnd){
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
        for (GameObject go : toRemove){
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
        onScreen.removeAll(toRemove);
        toRemove.clear();
        return turnEnd;
    }

    private void addToScreen(){
        for (GameObject go : toAdd){
            if (go instanceof Ball){
                balls.add((Ball)go);
            }
        }
        onScreen.addAll(toAdd);
        toAdd.clear();
    }

    private void nextTurn(){
        ballLeft--;
        LOGGER.log(Level.INFO, String.format("New turn started. %d balls left\n", ballLeft));
        onScreen.removeAll(currBoard.asList());
        currBoard.refreshGreenPeg();
        onScreen.addAll(currBoard.asList());
        onScreen.addAll(Powerup.createPowerup());
    }

    private void loadBoards(){
        int i;
        int boardNumber = 5;
        for (i=0; i<boardNumber; i++){
            boards.add(new Board(String.format("res/%d.csv", i)));
        }
    }
}