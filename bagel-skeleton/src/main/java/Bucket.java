import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

/*
 * This class implements a bucket in ShadowBounce.
 * @author Shuyang Fan
 * @version 1.0
 */


public class Bucket extends GameObject implements Movable {
    // Initial position of bucket on creation
    private static Point INIT_POSITION = new Point(512, 744);
    // Speed of the bucket
    private static double SPEED = 4;
    private Vector2 velocity;

    public Bucket(){
        super(INIT_POSITION, new Image("res/bucket.png"));
        this.velocity = Vector2.left.mul(SPEED);
    }

    /* Return a Vector2 object representing current velocity of the object. */
    /* @Para
     */
    public Vector2 velocity(){
        return new Vector2(velocity.x, velocity.y);
    }

    /* Reverse horizontal movement of the bucket
     */
    private void reverseHorizontal() {
        this.velocity = new Vector2(-this.velocity.x, this.velocity.y);
    }

    /*
     Reverse vertical velocity
     */
    private void reverseVertical() {
        this.velocity = new Vector2(this.velocity.x, -this.velocity.y);
    }

    /* Return if a specific ball 'drops' into the bucket.
    /* @param ball
    /* @return an boolean representing if the ball enters the bucket or not.
     */
    public boolean dropIntoBucket(Ball ball) {
        return ball.getBoundingBox().intersects(getBoundingBox()) &&
                ball.getBoundingBox().bottom() < getBoundingBox().bottom();
    }

    /*
    * Move the bucket along the bottom of the screen.
    * Reverse direction of movement if it reaches the left or right side of the screen.
     */
    @Override
    public void move(){
        if (getBoundingBox().left() < 0 || getBoundingBox().right() > Window.getWidth()) {
            reverseHorizontal();
        }
        // Move the bucket using current speed
        this.setCenter(getCenter().asVector().add(this.velocity).asPoint());
    }
}

