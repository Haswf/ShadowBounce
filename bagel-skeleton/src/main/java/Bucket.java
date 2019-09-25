import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

public class Bucket extends GameObject implements Movable {
    public static Point INIT_POSITION = new Point(512, 744);
    public static double SPEED = 4;
    private Velocity velocity;
    public Bucket(){
        super(INIT_POSITION, new Image("res/bucket.png"));
        this.velocity = new Velocity(Vector2.left, SPEED);
    }

    /* Return a Velocity object representing current movement of the object. */
    public Velocity getVelocity(){
        return new Velocity(this.velocity);
    }

    /* Set Velocity of the ball with given Velocity. */
    public void setVelocity(Velocity newVelocity){
        this.velocity = newVelocity;
    }

    @Override
    public void move(){
        Point newCentre = (this.getPosition().getCentre().asVector()).add(this.velocity.asVector()).asPoint();
        this.setPosition(getPosition().setCentre(newCentre));
        if (this.getPosition().getCentre().x < getImage().getWidth()/2 || this.getPosition().getCentre().x > Window.getWidth()-getImage().getWidth()/2) {
            this.setVelocity(this.getVelocity().reverseHorizontal());
        }
    }
}

