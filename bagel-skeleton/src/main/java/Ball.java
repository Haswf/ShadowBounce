import bagel.*;
import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;

/**
 * A class representing Ball for ShadowBounce.
 *
 * @author Shuyang Fan
 */
public class Ball extends GameObject implements Movable{
    public static final Point INIT_POSITION = new Point(512, 32);
    public static final double INIT_SPEED = 10.0;
    // Acceleration due to gravity
    public static final double GRAVITY = 0.15;
    private Velocity velocity;


    /* Constructor for Ball with a given velocity */
    public Ball(Point centre, Image image, Velocity velocity){
        super(centre, image);
        this.velocity = velocity;
    }

    /* Return a Velocity object representing current movement of the object. */
    public Velocity getVelocity(){
        return new Velocity(this.velocity);
    }

    /* Set Velocity of the ball with given Velocity. */
    public void setVelocity(Velocity newVelocity){
        this.velocity = newVelocity;
    }

    private void applyGravity(){
        // increase vertical speed to simulate gravity if the Ball is visible.
        this.setVelocity(this.getVelocity().add(Vector2.down.mul(GRAVITY)));
    }

    public void bounce(Side col){
        if (col != Side.NONE) {
            if (col == Side.LEFT || col == Side.RIGHT) {
                setVelocity(getVelocity().reverseHorizontal());
            } else {
                setVelocity(getVelocity().reverseVertical());
            }
        }
    }

    @Override
    public void move(){
        if (this.velocity != null) {
            Point newCentre = (this.getPosition().getCentre().asVector()).add(this.velocity.asVector()).asPoint();
            this.setPosition(getPosition().setCentre(newCentre));
            applyGravity();
        }
        if (this.getPosition().getCentre().x < 0 || this.getPosition().getCentre().x > Window.getWidth()) {
            this.setVelocity(this.getVelocity().reverseHorizontal());
        }
    }
}
