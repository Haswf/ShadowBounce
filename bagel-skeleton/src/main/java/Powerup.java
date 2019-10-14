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
    private static final double CHANCE = 0.1;
    private static final int MIN_DISTANCE = 5;
    private static final int INIT_SPEED = 3;
    private Vector2 velocity;
    private Point destination;

    /**
     *
     */
    public Powerup(){
        super(nextPoint(), new Image("res/powerup.png"));
        destination =nextPoint();
        this.velocity = destination.asVector().sub(getCenter().asVector()).normalised().mul(INIT_SPEED);
    }

    /**
     *
     * @return
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
     *
     * @return
     */
    @ Override
    /* Return a  object representing current movement of the object. */
    public Vector2 velocity(){
        return new Vector2(velocity.x, velocity.y);
    }

    /**
     *
     */
    @ Override
    public void move(){
        setCenter(this.getCenter().asVector().add(velocity).asPoint());

        if (this.distance(this.destination) < MIN_DISTANCE){
            destination = nextPoint();
            this.velocity = destination.asVector().sub(getCenter().asVector()).normalised().mul(INIT_SPEED);
        }
    }

    /**
     *
     * @return
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
     *
     * @param incoming
     * @return
     */
    private FireBall activate(Ball incoming){
        return new FireBall(incoming);
    }

}
