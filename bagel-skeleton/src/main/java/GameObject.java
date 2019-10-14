import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Side;
import bagel.util.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.BinaryOperator;

/**
 * An abstract GameObject class representing
 * something on the screen.
 * A typical GameObject has a position,
 * image, visibility and bounding box.
 * @author Shuyang Fan
 */

abstract public class GameObject{
    private Image image;
    private Rectangle boundingBox;

    // constructor for GameObject where position, image are provided.
    public GameObject(Point centre, Image image) {
        this.image = image;
        this.boundingBox = image.getBoundingBox();
        // Move the bounding box to the correct position.
        this.boundingBox.moveTo(computeTopLeft(centre));
    }

    // copy constructor
    public GameObject(GameObject other) {
        this.image = other.image;
        this.boundingBox = new Rectangle(other.boundingBox);
    }

    /**
     * Return the centre of the GameObject on the screen.
     * @return a Point representing the centre of the GameObject.
     */
    public Point getCenter() {
        return boundingBox.centre();
    }

    /**
     * Compute the TopLeft point of a GameObject given its center
     * @param center
     * @return The TopLeft coordinate of the GameObject
     */
    private Point computeTopLeft(Point center){
        return new Point(center.x - image.getWidth()/2, center.y - image.getHeight()/2);
    }

    /**
     * Move a GameObject's center to a given point
     * @param center the point where the GameObject's center will be removed to.
     */
    public void setCenter(Point center) {
        this.boundingBox.moveTo(computeTopLeft(center));
    }

    /**
     * Get bounding box of a GameObject
     * @return a rectangle representing the bounding box of the GameObject
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(boundingBox);
    }

    /**
     * Return image of a GameObject
     * @return the image of a GameObject
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
     *
     * @param movable
     * @param <T> any class that extends from GameObject and implements Movable interface
     * @return The Side indicating where the collision occurs. Returns None if no collision
     * will occur.
     */
    public <T extends GameObject & Movable> Side collideAtSide(T movable){
        //return this.getBoundingBox().intersectedAt(getCenter(), other.velocity());
        Rectangle otherBoundingBox = movable.getBoundingBox();
        Point[] corners = {otherBoundingBox.topLeft(), otherBoundingBox.topRight(),
                otherBoundingBox.bottomLeft(), otherBoundingBox.bottomRight()};

        Side colSide;
        for (Point p : corners){
            if ((colSide = this.getBoundingBox().intersectedAt(p, movable.velocity()))!=Side.NONE){
                return colSide;
            }
        }
        return Side.NONE;
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
     *
     */
    public void render() {
        this.image.draw(getCenter().x, getCenter().y);
    }
}
