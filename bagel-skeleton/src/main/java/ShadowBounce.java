import bagel.*;
import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * An simple ball game.
 *
 * @author Shuyang Fan
 */
public class ShadowBounce extends AbstractGame {

    private static final double INIT_X = 512;
    private static final double INIT_Y = 32;
    private final Point initPosition;
    private ArrayList<Ball> balls;
    // Downward acceleration due to gravity

    // initial speed of the ball
    private double initSpeed;
    private Renderer renderer;
    private ArrayList<Board> boards;
    private Board currBoard;
    private Iterator<Board> boardIter;
    private int ballLeft = 20;

    /* ShadowBounce */
    public ShadowBounce() {
        renderer = new Renderer();
        boards = new ArrayList<>();
        boards.add(new Board("board/0.csv"));
        boards.add(new Board("board/0.csv"));

        initPosition = new Point(INIT_X, INIT_Y); // initial position where Ball will be generated
        // initial speed of the ball
        initSpeed = 10;
        boardIter = boards.iterator();
        currBoard = boardIter.next();
        renderer.addAll(currBoard.asList());
        balls = new ArrayList<>();
    }


    /* The entry point for the program. */
    public static void main(String[] args) {
        ShadowBounce game = new ShadowBounce();
        game.run();
    }

    public void resetBall(Input input){
        Ball ball = new Ball(initPosition, new Image("res/ball.png"));
        ball.setPosition(ball.getPosition().setCentre(initPosition));;
        Point mousePosition = input.getMousePosition();
        // Calculate normal vector from init point to mouse position.
        Vector2 mouseDirection = mousePosition.asVector().sub(initPosition.asVector()).normalised();
        ball.setVelocity(new Velocity(mouseDirection, initSpeed));
        balls.add(ball);
    }

    /**
     * Performs a state update. This simple example shows an image that can be controlled with the arrow keys, and
     * allows the game to exit when the escape key is pressed.
     */

    @Override
    public void update(Input input) {
        ArrayList<GameObject> toBeDestroyed = new ArrayList<>();


        if (input.isDown(MouseButtons.LEFT) && balls.size()==0 && ballLeft>0) {
            ballLeft--;
            resetBall(input);
            renderer.addAll((List)balls);
        }

        // Quit game if ESCAPE key was pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // Update the ball if it is visible.
        if (balls.size()>0){
            for (Ball ball : balls){
                ball.update();
                // Reverse horizontal movement when the ball reaches the left or right sides.
                if (ball.getPosition().getCentre().x < 0 || ball.getPosition().getCentre().x > Window.getWidth()){
                    ball.setVelocity(ball.getVelocity().reverseHorizontal());
                }
                if (ball.getPosition().getCentre().y > Window.getHeight()){
                    toBeDestroyed.add(ball);
                }
            }
        }

        if (balls.size()>0){
            for (Ball ball : balls){
                Iterator<LinkedList<Peg>> it = currBoard.iterPegs();
                while (it.hasNext()) {
                    LinkedList<Peg> list = it.next();
                    for (Iterator<Peg> iterator = list.iterator(); iterator.hasNext(); ) {
                        Peg p = iterator.next();
                        if (ball.getCollider().collideWith(p)) {
                            Side col = p.getCollider().collideAtSide(ball, ball.getVelocity());
                            ball.bounce(col);
                            if (p.getColour() != Peg.COLOUR.GREY){
                                toBeDestroyed.add(p);
                            }
                        }
                    }
                }
            }
        }


        for (GameObject p : toBeDestroyed){
            if (p instanceof BluePeg){
                currBoard.destroy(p);
            }
            if (p instanceof GreenPeg){
                currBoard.destroy(p);
                balls.addAll(((GreenPeg) p).duplicate(balls.get(0)));
                renderer.addAll((List)balls);
            }
            if (p instanceof Ball){
                balls.remove(p);
            }
        }

        renderer.removeAll(toBeDestroyed);
        toBeDestroyed.clear();

        if (balls.size()==0) {
            turnEnd();
        }

        if (currBoard.getRedCount() == 0){
            loadNextBoard();
        }

        if (ballLeft==0){
            Image gg = new Image("res/gameover.png");
            gg.draw(Window.getWidth()/2, Window.getWidth()/2);
        }


        renderer.render();

    }

    public void loadNextBoard() {
        if (boardIter.hasNext()) {
            currBoard = boardIter.next();
            renderer.clear();
            renderer.addAll(currBoard.asList());
        }
    }

    public void turnEnd(){
        renderer.clear();
        currBoard.refreshGreenPeg();
        renderer.addAll(currBoard.asList());
    }
}
