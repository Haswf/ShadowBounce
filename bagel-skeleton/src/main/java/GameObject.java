import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Side;
import bagel.util.Vector2;
import java.util.ArrayList;

/**
 * An abstract class representing GameObject on the screen.
 * @author Shuyang Fan, shuyangf@student.unimelb.edu.au
 */

abstract public class GameObject{
    private Image image;
    private Rectangle boundingBox;
    // a static GameObject will have Vector2.Zero as velocity.
    private Vector2 velocity;

    /**
     * GameObject constructor with provided position, image.
     * By default, velocity is set to zero.
     * @param centre the center of the GameObject
     * @param image the image of the GameObject
     */
    public GameObject(Point centre, Image image) {
        this.image = image;
        // get bounding box from image
        this.boundingBox = image.getBoundingBox();
        this.velocity = Vector2.down.mul(0);
        // Move the bounding box to the correct position.
        this.boundingBox.moveTo(computeTopLeft(centre));
    }

    /**
     * GameObject constructor with provided position, image and initial velocity.
     * @param centre centre of the GameObject
     * @param image image of the GameObject
     * @param velocity initial velocity
     */
    public GameObject(Point centre, Image image, Vector2 velocity) {
        this.image = image;
        // get bounding box from image
        this.boundingBox = image.getBoundingBox();
        this.velocity = velocity;
        // Move the bounding box to the correct position.
        this.boundingBox.moveTo(computeTopLeft(centre));
    }


    /**
     * Copy constructor for GameObject
     * @param other GameObject to be duplicated.
     */
    public GameObject(GameObject other) {
        this.image = other.image;
        this.boundingBox = new Rectangle(other.boundingBox);
    }

    /**
     * Return the position of GameObject's center
     * @return a Vector2 representing the centre of the GameObject.
     */
    public Point getCenter() {
        return boundingBox.centre();
    }

    /**
     * Compute the TopLeft point of a GameObject given its center
     * @param center The center of the GameObject
     * @return The TopLeft coordinate of the GameObject
     */
    private Point computeTopLeft(Point center){
        return new Point(center.x - image.getWidth()/2, center.y - image.getHeight()/2);
    }

    /**
     * Move a GameObject's center to a given position defined by a point.
     * @param center the point where the GameObject's center will be removed to.
     */
    public void setCenter(Point center) {
        this.boundingBox.moveTo(computeTopLeft(center));
    }

    /**
     * Gets the velocity of a GameObject.
     * @return a Vector2 representing a GameObject's velocity.
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Sets velocity of a GameObject.
     * @param velocity velocity to be assigned.
     */
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets bounding box of a GameObject.
     * @return a rectangle representing the bounding box of the GameObject.
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(boundingBox);
    }

    /**
     * Return the image of a GameObject
     * @return the image of a GameObject
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Return if two GameObjects interacts.
     * @param other the other GameObject you want to test
     * @return if two GameObjects interacts.
     */
    public boolean collideWith(GameObject other){
        return boundingBox.intersects(other.getBoundingBox());
    }

    /**
     * Returns on which side the two GameObject interacts.
     * @param other the other GameObject.
     * @param <T> any class that extends from GameObject.
     * @return The Side indicating where the collision occurs. Returns None if no collision
     * will occur.
     */
    public <T extends GameObject> Side collideAtSide(T other){
        return getBoundingBox().intersectedAt(getCenter(), other.getVelocity());
    }

    /**
     * Returns the distance between this GameObject and the other.
     * @param other the other GameObject.
     * @return the distance between this GameObject and the other.
     */
    public double distance(GameObject other){
        return other.getCenter().asVector().sub(this.getCenter().asVector()).length();
    }

    /**
     * Returns the distance between this GameObject and a point.
     * @param other a point
     * @return the distance between this GameObject and a point.
     */
    public double distance(Point other){
        return other.asVector().sub(this.getCenter().asVector()).length();
    }

    /**
     * Filter out GameObjects that has distance smaller than a certain value.
     * @param objects GameObject to be examined.
     * @param distance the distance threshold.
     * @param <T> objects must be a array of subclass of GameObject.
     * @return an ArrayList of GameObjects
     */
    public <T extends GameObject> ArrayList<GameObject> withinRange(ArrayList<T> objects, float distance){
        ArrayList<GameObject> inRange = new ArrayList<>();
        for (GameObject go : objects) {
            if (go.distance(this) < distance) {
                inRange.add(go);
            }
        }
        return inRange;
    }

    /**
     * Reverse horizontal direction of the GameObject.
     */
    public void reverseHorizontal() {
        this.velocity = new Vector2(-this.velocity.x, this.velocity.y);
    }

    /**
     * Reverse vertical direction of the GameObject.
     */
    public void reverseVertical() {
        this.velocity = new Vector2(this.velocity.x, -this.velocity.y);
    }

    /**
     * Move the GameObject based on its velocity.
     */
    public void move(){
        setCenter(getCenter().asVector().add(velocity).asPoint());
    }

    /**
     * Draw the GameObject on the screen.
     */
    private void render() {
        this.image.draw(getCenter().x, getCenter().y);
    }

    /**
     * Update the GameObject. This method should be called once per frame.
     * @param shadowBounce an instance of ShadowBounce.
     */
    public void update(ShadowBounce shadowBounce){
        move();
        render();
    }
}
