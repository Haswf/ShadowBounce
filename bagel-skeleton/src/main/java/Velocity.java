import bagel.util.Vector2;

/**
 * A class to manipulate velocity for game ShadowBounce
 *
 * @author Shuyang Fan
 */

public class Velocity {
    // velocity is internally represented as unit vector direction * speed
    private Vector2 direction;
    private double speed;

    public Velocity(){
        this.direction = Vector2.left;
        this.speed = 0;
    }
    /*
     Create a velocity object  with a given direction and speed
     */
    public Velocity(Vector2 direction, double speed) {
        // normalise direction in case it's not normalised
        this.direction = direction.normalised();
        this.speed = speed;
    }

    /*
     Create a velocity object with a given Vector2
     */
    public Velocity(Vector2 newVelocity) {
        setDirection(newVelocity.normalised());
        setSpeed(newVelocity.length());
    }

    /*
     Copy constructor for Velocity
     */
    public Velocity(Velocity other) {
        this.direction = new Vector2(other.direction.x, other.direction.y);
        this.speed = other.speed;
    }

    /*
     Return the direction as a Vector2
     */
    public Vector2 getDirection() {
        // manually create new Vector2 since copy constructor of Vector2 is not provided.
        return new Vector2(this.direction.x, this.direction.y);
    }

    /*
       Set the direction of movement with a given normal vector
     */
    public void setDirection(Vector2 newDirection) {
        this.direction = newDirection.normalised();
    }

    /*
     Return the speed of movement as a double.
     */
    public double getSpeed() {
        return this.speed;
    }

    /*
     Set the speed of movement with a given double.
     */
    public void setSpeed(Double newSpeed) {
        this.speed = newSpeed;
    }

    /*
     Offset the velocity with a given Vector2.
     */
    public Velocity add(Vector2 offset) {
        Vector2 newVelocity = this.direction.mul(this.speed).add(offset);
        setDirection(newVelocity.normalised());
        setSpeed(newVelocity.length());
        return this;
    }

    /*
     Return a double representing horizontal speed.
     */
    public double getXSpeed() {
        return this.getDirection().x * this.getSpeed();
    }

    /*
     Return a double representing vertical speed.
     */
    public double getYSpeed() {
        return this.getDirection().y * this.getSpeed();
    }

    /*
     Return current velocity as a Vector2
     */
    public Vector2 asVector() {
        return this.direction.mul(this.speed);
    }

    /*
     Reverse horizontal velocity
     */
    public Velocity reverseHorizontal() {
        setDirection(new Vector2(this.getDirection().x * -1, this.getDirection().y));
        return this;
    }

    /*
     Reverse vertical velocity
     */
    public Velocity reverseVertical() {
        setDirection(new Vector2(this.getDirection().x, this.getDirection().y * -1));
        return this;
    }
}
