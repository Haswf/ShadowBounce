import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.ArrayList;

/**
 * A class represents FireBall in ShadowBounce.
 */
public class FireBall extends Ball implements Destroyable {
    // The range of explosion in pixels within which all pegs of the struck peg’s centre are destroyed.
    public final static int DESTROY_RANGE = 70;
    /**
     * Powerup Constructor
     * @param center the center of the GameObject
     * @param image the image of fireball
     * @param velocity the velocity of fireball
     */
    public FireBall(Point center, Image image, Vector2 velocity) {
        super(center, image, velocity);
    }
    /**
     * Constructor to convert a ball into a fireball.
     * @param other the ball to be converted.
     */
    public FireBall(Ball other){
        super(other.getCenter(), new Image("res/fireball.png"), other.getVelocity());
    }

    /**
     * Destroys surrounding pegs when fireball strikes a peg
     * @param game an instance of ShadowBounce.
     * @param col The other GameObject that hit the ball.
     */
    public void onCollisionEnter(ShadowBounce game, GameObject col){
        if (col instanceof Peg){
            // Call parent onCollisionEnter to bounce off
            super.onCollisionEnter(game, col);
            // Remove any surrounding peg that is destroyable.
            for (GameObject go: withinRange(game.getCurrBoard().asList(), FireBall.DESTROY_RANGE)){
                if (go instanceof Destroyable){
                    ((Destroyable)go).destroy(game);
                }
            }
        }
    }

    /**
     * Requires the game instance to destroy itself.
     * @param game an instance of ShadowBounce
     */
    @Override
    public void destroy(ShadowBounce game) {
        game.removeGameObject(this);
    }

    /**
     * Return two balls of the same type at its position,
     * with an initial velocity of 10 pixels per second diagonally
     * upwards and to the left and right
     * @return An array of balls created
     */
    @Override
    public ArrayList<Ball> duplicate(){
        ArrayList<Ball> dups = new ArrayList<>();
        dups.add(new FireBall(getCenter(), getImage(), Vector2.up.add(Vector2.left).mul(DUPLICATE_SPEED)));
        dups.add(new FireBall(getCenter(), getImage(), Vector2.up.add(Vector2.right).mul(DUPLICATE_SPEED)));
        return dups;
    }
}
