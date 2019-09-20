import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.Random;

public class Powerup extends GameObject implements Movable {
    public static final double CHANCE = 0.1;
    public static final int MIN_DISTANCE = 5;
    public static final int SPEED = 3;
    private Velocity velocity;
    private Position destination;

    public Powerup(){
        super(nextPoint(), new Image("res/powerup.png"));
        destination = new Position(nextPoint());
        this.velocity = new Velocity(destination.getCentre().asVector().sub(getPosition().getCentre().asVector()).normalised(), SPEED);
    }

    /* Return a Velocity object representing current movement of the object. */
    public Velocity getVelocity(){
        return new Velocity(this.velocity);
    }

    /* Set Velocity of the ball with given Velocity. */
    public void setVelocity(Velocity newVelocity){
        this.velocity = newVelocity;
    }

    private static Point nextPoint(){
        Random random = new Random();
        return new Point(random.nextInt(Window.getWidth()), random.nextInt(Window.getHeight()));
    }

    public void move(){
        Point newCentre = (this.getPosition().getCentre().asVector()).add(this.velocity.asVector()).asPoint();
        this.setPosition(getPosition().setCentre(newCentre));

        if (this.destination.distance(this.getPosition()) < MIN_DISTANCE){
            destination = new Position(nextPoint());
            this.velocity = new Velocity(destination.getCentre().asVector().sub(getPosition().getCentre().asVector()).normalised(), SPEED);
        }
    }

    public FireBall activate(Ball incoming){
        return new FireBall(incoming);
    }
}
