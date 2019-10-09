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
public class Ball extends GameObject implements Movable, OnCollisionEnter{
    public static final Point INIT_POSITION = new Point(512, 32);
    public static final double INIT_SPEED = 10.0;
    // Acceleration due to gravity
    public static final double GRAVITY = 0.15;
    private Vector2 velocity;

    public Ball(Point center, Image image, Vector2 velocity) {
        super(center, image);
        this.velocity = velocity;
    }

    public static Ball shoot(Input input){
        Ball ball = new Ball(Ball.INIT_POSITION, new Image("res/ball.png"), Vector2.down.mul(0));
        Vector2 mouseDirection = input.getMousePosition().asVector().sub(Ball.INIT_POSITION.asVector()).normalised();
        ball.setVelocity(mouseDirection.mul(Ball.INIT_SPEED));
        return ball;
    }

    private void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }

    private void applyGravity(){
        setVelocity(this.velocity.add(Vector2.down.mul(GRAVITY)));
    }

    private void bounce(Side col){
        if (col != Side.NONE) {
            if (col == Side.LEFT || col == Side.RIGHT) {
                reverseHorizontal();
            } else {
                reverseVertical();
            }
        }
    }

    private void reverseHorizontal() {
        this.velocity = new Vector2(-this.velocity.x, this.velocity.y);
    }

    /* Reverse vertical velocity */
    private void reverseVertical() {
        this.velocity = new Vector2(this.velocity.x, -this.velocity.y);
    }

    @Override
    public void move(){
        Point newCenter = (this.getCenter().asVector()).add(this.velocity).asPoint();
        this.setCenter(newCenter);
        applyGravity();

        if (this.getCenter().x < 0 || this.getCenter().x > Window.getWidth()) {
            reverseHorizontal();
        }
    }

    public boolean outOfScreen() {
        return this.getBoundingBox().top() > Window.getHeight();
    }

    @ Override
    /* Return a vector2 representing current movement of the object. */
    public Vector2 velocity(){
        return new Vector2(velocity.x, velocity.y);
    }

    @ Override
    public <T extends GameObject> void onCollisionEnter(T col){
        this.bounce(col.collideAtSide(this));
    }
}
