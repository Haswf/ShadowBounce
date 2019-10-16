import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Side;
import bagel.util.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

/**
 * An abstract class representing
 * GameObject on the screen.
 * @author Shuyang Fan
 */

abstract public class GameObject{
    private Image image;
    private Rectangle boundingBox;
    private Vector2 velocity;

    /**
     * GameObject constructor with provided position, image.
     * @param centre
     * @param image
     */
    public GameObject(Point centre, Image image) {
        this.image = image;
        // get bounding box from image
        this.boundingBox = image.getBoundingBox();
        this.velocity = Vector2.down.mul(0);
        // Move the bounding box to the correct position.
        this.boundingBox.moveTo(computeTopLeft(centre));
    }

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

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * Get bounding box of a GameObject.
     * @return a rectangle representing the bounding box of the GameObject.
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(boundingBox);
    }

    /**
     * Return image of the GameObject
     * @return the image of the GameObject
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Return if
     * @param other
     * @return
     */
    public boolean collideWith(GameObject other){
        return boundingBox.intersects(other.getBoundingBox());
    }

    /**
     * Concude
     * @param other a GameObject with which
     * @param <T> any class that extends from GameObject and implements Movable interface
     * @return The Side indicating where the collision occurs. Returns None if no collision
     * will occur.
     */
    public <T extends GameObject> Side collideAtSide(T other){
        return getBoundingBox().intersectedAt(getCenter(), other.getVelocity());
    }

    /* Return distance from this GameObject to another. */
    public double distance(GameObject other){
        return other.getCenter().asVector().sub(this.getCenter().asVector()).length();
    }

    /* Return distance from this GameObject to a point. */
    public double distance(Point other){
        return other.asVector().sub(this.getCenter().asVector()).length();
    }

    /**
     *
     * @param objects
     * @param distance
     * @param <T>
     * @return
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
     * Reverse horizontal direction
     */
    public void reverseHorizontal() {
        this.velocity = new Vector2(-this.velocity.x, this.velocity.y);
    }

    /**
     * Reverse vertical direction
     */
    public void reverseVertical() {
        this.velocity = new Vector2(this.velocity.x, -this.velocity.y);
    }

    public void move(){
        setCenter(getCenter().asVector().add(velocity).asPoint());
    }

    private void render() {
        this.image.draw(getCenter().x, getCenter().y);
    }

    public void update(ShadowBounce shadowBounce){
        move();
        render();
    }
}
