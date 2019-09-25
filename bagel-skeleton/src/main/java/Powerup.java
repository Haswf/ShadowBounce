import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.Random;

public class Powerup extends GameObject implements Movable {
    public static final double CHANCE = 0.1;
    public static final int MIN_DISTANCE = 5;
    public static final int SPEED = 3;
    private Vector2 velocity;
    private Point destination;

    public Powerup(){
        super(nextPoint(), new Image("res/powerup.png"));
        destination =nextPoint();
        this.velocity = destination.asVector().sub(center().asVector()).normalised().mul(SPEED);
    }

    /* Return a  object representing current movement of the object. */
    public Vector2 velocity(){
        return new Vector2(velocity.x, velocity.y);
    }


    private static Point nextPoint(){
        Random random = new Random();
        return new Point(random.nextInt(Window.getWidth()), random.nextInt(Window.getHeight()));
    }

    public void move(){
        moveTo(this.center().asVector().add(velocity).asPoint());

        if (this.distance(this.destination) < MIN_DISTANCE){
            destination = nextPoint();
            this.velocity = destination.asVector().sub(center().asVector()).normalised().mul(SPEED);
        }
    }

    public FireBall activate(Ball incoming){
        return new FireBall(incoming);
    }
}
