import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.logging.Level;

/*
 * This class implements a bucket in ShadowBounce.
 * @author Shuyang Fan
 * @version 1.0
 */


public class Bucket extends GameObject implements OnCollisionEnter{
    // Initial position of bucket on creation
    private static Point INIT_POSITION = new Point(512, 744);
    // Speed of the bucket
    private final static double INIT_SPEED = 4;

    /**
     *
     */
    public Bucket(){
        super(INIT_POSITION, new Image("res/bucket.png"),  Vector2.left.mul(INIT_SPEED));
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
        super.move();
        /* Reverse horizontal movement of the bucket if it reaches the left or the right side
        */
        if (getBoundingBox().left() < 0 || getBoundingBox().right() > Window.getWidth()) {
            reverseHorizontal();
        }
    }

    @Override
    public <T extends GameObject> void onCollisionEnter(ShadowBounce game, T col) {
        if (col instanceof Ball){
            if (dropIntoBucket((Ball)col)) {
                ShadowBounce.LOGGER.log(Level.INFO, "ballLeft + 1");
                game.removeGameObject(col);
                game.setBallLeft(game.getBallLeft()+1);
            }
        }
    }
}

