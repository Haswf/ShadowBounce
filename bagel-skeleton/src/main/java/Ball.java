import bagel.*;
import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class representing Ball for ShadowBounce.
 *
 * @author Shuyang Fan
 */
public class Ball extends GameObject implements OnCollisionEnter{
    private static final Point INIT_POSITION = new Point(512, 32);
    private static final double INIT_SPEED = 10.0;
    // Acceleration due to gravity
    private static final double GRAVITY = 0.15;

    /**
     * Ball Constructor
     * @param center the centre of the GameObject
     * @param image
     * @param velocity
     */
    public Ball(Point center, Image image, Vector2 velocity) {
        super(center, image, velocity);
    }

    /**
     * Creates a ball located at INIT_POSITION　with a velocity towards the given position.
     * @param position where the ball will move towards to.
     * @return a ball with a velocity towards the given position.
     */
    public static Ball shoot(Point position){
        Ball ball = new Ball(Ball.INIT_POSITION, new Image("res/ball.png"), Vector2.down.mul(0));
        // Calculate the direction of movement
        Vector2 mouseDirection = position.asVector().sub(Ball.INIT_POSITION.asVector()).normalised();
        // Set the velocity to direction * INIT_SPEED
        ball.setVelocity(mouseDirection.mul(Ball.INIT_SPEED));
        return ball;
    }

    /**
     * Applies gravitational acceleration specified as GRAVITY.
     */
    private void applyGravity(){
        setVelocity(getVelocity().add(Vector2.down.mul(GRAVITY)));
    }

    /**
     * Changes the velocity of the ball according to on which the
     * collision occurs.
     * @param collisionSide which side of the rectangle the ball intersected at
     */
    private void bounce(Side collisionSide){
        if (collisionSide != Side.NONE) {
            // Reverse horizontal direction of the ball if LEFT OR RIGHT side was hit.
            if (collisionSide == Side.LEFT || collisionSide == Side.RIGHT) {
                reverseHorizontal();
            }
            // Reverse vertical directional of the ball if TOP or BOTTOM was hit.
            else {
                reverseVertical();
            }
        }
    }


    /**
     * Move the ball according to its current velocity.
     * Reverse horizontal direction if either left or right side of
     * the screen is reached.
     */
    @Override
    public void move() {
        super.move();
        if (getCenter().x < 0 || getCenter().x > Window.getWidth()) {
            reverseHorizontal();
        }
    }

    /**
     * Check whether the ball is out of the screen.
     * @return whether the ball is below the bottom of the screen.
     */
    public boolean outOfScreen() {
        return this.getBoundingBox().top() > Window.getHeight();
    }

    /**
     * Defines the behaviour to trigger when the ball was hit by
     * other GameObject.
     * @param col The other GameObject that hit the ball.
     * @param <T> The other GameObject must be able to move.
     */
    @ Override
    public <T extends GameObject> void onCollisionEnter(ShadowBounce game, T col){
        if (col instanceof Peg){
            bounce(col.collideAtSide(this));
        }
    }

    @ Override
    public void update(ShadowBounce game){
        super.update(game);
        applyGravity();
        if (outOfScreen()) {
            game.removeGameObject(this);
        }
    }
}
