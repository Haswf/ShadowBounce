import bagel.*;
import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;

/**
 * A class representing Ball for ShadowBounce.
 *
 * @author Shuyang Fan
 */
public class Ball extends GameObject{
    private Velocity velocity;
    // Acceleration due to gravity
    private static final double gravityAcceleration = 0.15;

    /* Basic constructor for Ball where velocity is not provided. */
    public Ball(Point centre, Image image){
        super(centre, image);
    }

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

    /* Recalculate the position of the Ball based on its current velocity.
       This method should be called exactly once every frame.
    */
    public void recalculatePosition(){
        Point newCentre = (this.getPosition().getCentre().asVector()).add(this.velocity.asVector()).asPoint();
        this.setPosition(getPosition().setCentre(newCentre));
    }

    private void gravity(){
        // increase vertical speed to simulate gravity if the Ball is visible.
        this.setVelocity(this.getVelocity().add(Vector2.down.mul(gravityAcceleration)));
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

    public void update(){
        if (this.velocity != null) {
            recalculatePosition();
            gravity();
        }
    }
}
