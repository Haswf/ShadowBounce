import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.logging.Level;

public class Bucket extends GameObject implements Movable {
    public static Point INIT_POSITION = new Point(512, 744);
    public static double SPEED = 4;
    private Vector2 velocity;
    public Bucket(){
        super(INIT_POSITION, new Image("res/bucket.png"));
        this.velocity = Vector2.left.mul(SPEED);
    }

    /* Return a Vector2 object representing current movement of the object. */
    public Vector2 velocity(){
        return new Vector2(velocity.x, velocity.y);
    }

    private void reverseHorizontal() {
        this.velocity = new Vector2(-this.velocity.x, this.velocity.y);
    }

    /*
     Reverse vertical velocity
     */
    private void reverseVertical() {
        this.velocity = new Vector2(this.velocity.x, -this.velocity.y);
    }

    public boolean dropIntoBucket(Ball ball) {
        return (ball.outOfScreen() && ball.collideWith(this));
    }


    @Override
    public void move(){
        Point newCentre = (getCenter().asVector()).add(this.velocity).asPoint();
        this.setCenter(newCentre);
        if (getCenter().x < getImage().getWidth()/2 || this.getCenter().x > Window.getWidth()-getImage().getWidth()/2) {
            reverseHorizontal();
        }
    }
}

