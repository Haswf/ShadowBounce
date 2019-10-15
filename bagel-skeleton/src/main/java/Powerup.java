import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Powerup extends GameObject implements Movable, OnCollisionCreate, OnCollisionRemove {
    // The chance that a powerup will be created at the start of each turn.
    private static final double CHANCE = 0.1;
    // The minimum distance from destination
    private static final int MIN_DISTANCE = 5;
    // Initial speed of the powerup
    private static final int INIT_SPEED = 3;
    // velocity of the powerup
    private Vector2 velocity;
    // Current destination
    private Point destination;

    /**
     * Poweru[ Constructor
     */
    public Powerup(){
        // Choose a random point on the screen as position
        super(nextPoint(), new Image("res/powerup.png"));
        // Choose another point as destination
        destination =nextPoint();
        this.velocity = destination.asVector().sub(getCenter().asVector()).normalised().mul(INIT_SPEED);
    }

    /**
     * Gets a random point on the screen
     * @return Point a random point on the screen
     */
    private static Point nextPoint(){
        Random random = new Random();
        return new Point(random.nextInt(Window.getWidth()), random.nextInt(Window.getHeight()));
    }


    /**
     *
     * @return
     */
    public static ArrayList<GameObject> createPowerup(){
        Random random = new Random();
        ArrayList<GameObject> lst = new ArrayList<>();
        if (random.nextDouble() < Powerup.CHANCE){
            Powerup pw = new Powerup();
            lst.add(pw);
        }
        return lst;
    }

    /**
     * Gets the velocity of the powerup
     * @return a Vector2 representing current velocity
     */
    @ Override
    /* Return a  object representing current movement of the object. */
    public Vector2 velocity(){
        return new Vector2(velocity.x, velocity.y);
    }

    /**
     * Move this powerup to its current destination.
     * When the powerup is within 5 pixels of its destination,
     * it will choose another random position as new destination.
     *
     */
    @ Override
    public void move(){
        // Move the object based on its velocity
        setCenter(this.getCenter().asVector().add(velocity).asPoint());

        // choose next destination if the the powerup is within 5 pixels of its destination
        if (this.distance(this.destination) < MIN_DISTANCE){
            destination = nextPoint();
            this.velocity = destination.asVector().sub(getCenter().asVector()).normalised().mul(INIT_SPEED);
        }
    }

    /**
     * Return the object to be removed when this powerup was hit. In this case, itself should be removed.
     * @returm the GameObject to be removed as the result of a collision.
     */
    @ Override
    public GameObject onCollisionRemove(){
        return this;
    }

    /**
     *
     * @param col
     * @param <T>
     * @return
     */
    @ Override
    public <T extends GameObject & Movable> Collection<GameObject> onCollisionCreate(T col){
        ArrayList<GameObject> lst = new ArrayList<>();
        ShadowBounce.LOGGER.log(Level.INFO, "Powerup hit\n");
        if (col instanceof Ball){
            lst.add(this.activate((Ball)col));
        }
        return lst;
    }

    /**
     * Activate the powerup and replaces the incoming ball with a fire ball.
     * @param incoming incoming ball
     * @return Fireball a fire ball with the same velocity as the incoming one.
     */
    private FireBall activate(Ball incoming){
        return new FireBall(incoming);
    }

}
