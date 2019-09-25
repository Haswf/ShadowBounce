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
public class Ball extends GameObject implements Movable{
    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static final Point INIT_POSITION = new Point(512, 32);
    public static final double INIT_SPEED = 10.0;
    // Acceleration due to gravity
    public static final double GRAVITY = 0.15;
    private Vector2 velocity;

    public Ball(Point center, Image image, Vector2 velocity) {
        super(center, image);
        this.velocity = velocity;
    }


    private void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }

    private void applyGravity(){
        setVelocity(this.velocity.add(Vector2.down.mul(GRAVITY)));
    }

    public void bounce(Side col){
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
        Point newCenter = (this.center().asVector()).add(this.velocity).asPoint();
        this.moveTo(newCenter);
        applyGravity();

        if (this.center().x < 0 || this.center().x > Window.getWidth()) {
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

}
