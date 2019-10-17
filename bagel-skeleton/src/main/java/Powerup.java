import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

/**
 *
 */
public class Powerup extends GameObject implements OnCollisionEnter {
    // The chance that a powerup will be created at the start of each turn.
    private static final double CHANCE = 0.1;
    // The minimum distance from destination
    private static final int MIN_DISTANCE = 5;
    // Initial speed of the powerup
    private static final int INIT_SPEED = 3;
    // Current destination
    private Point destination;

    /**
     * Powerup Constructor.
     */
    public Powerup(){
        // Choose a random point on the screen as position
        super(nextPoint(), new Image("res/powerup.png"), Vector2.down.mul(0));
        // Choose another point as destination
        destination =nextPoint();
        setVelocity(destination.asVector().sub(getCenter().asVector()).normalised().mul(INIT_SPEED));
    }

    /**
     * Gets a random point on the screen
     * @return a random point on the screen
     */
    private static Point nextPoint(){
        Random random = new Random();
        return new Point(random.nextInt(Window.getWidth()), random.nextInt(Window.getHeight()));
    }

    /**
     * Creates a powerup by chance
     * @return a ArrayList of powerup, which may or may not contain a powerup.
     */
    public static ArrayList<Powerup> createPowerup(){
        ArrayList<Powerup> powerups = new ArrayList<>();
        Random random = new Random();
        if (random.nextDouble() < Powerup.CHANCE){
            powerups.add(new Powerup());
        }
        return powerups;
    }

    /**
     * Move this powerup to its current destination.
     * When the powerup is within 5 pixels of its destination,
     * it will choose another random position as new destination.
     */

    public void move(){
        // Call parent's move to set position of the object based on its velocity
        super.move();

        // choose next destination if the the powerup is within 5 pixels of its destination
        if (this.distance(this.destination) < MIN_DISTANCE){
            destination = nextPoint();
            setVelocity(destination.asVector().sub(getCenter().asVector()).normalised().mul(INIT_SPEED));
        }
    }

    /**
     * Specifics powerup's behaviour when it collided with other GameObject.
     * @param game an instance of ShadowBounce.
     * @param col another GameObject which hit this powerup.
     * @param <T> The other party of the collision might be a subclass of GameObject.
     */
    @Override
    public <T extends GameObject> void onCollisionEnter(ShadowBounce game, T col) {
        ShadowBounce.LOGGER.log(Level.INFO, "Powerup hit\n");
        if (col instanceof Ball){
            game.addGameObject(new FireBall((Ball)col));
            game.removeGameObject(col);
            game.removeGameObject(this);
        }
    }
}
