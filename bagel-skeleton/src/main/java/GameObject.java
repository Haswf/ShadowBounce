import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Side;
import bagel.util.Vector2;

import java.util.function.BinaryOperator;

/**
 * An abstract GameObject class representing
 * something on the screen.
 * A typical GameObject has a position,
 * image, visibility and bounding box.
 * @author Shuyang Fan
 */

abstract public class GameObject{
    private Image image; // image of the object
    private Rectangle boundingBox;

    // constructor for GameObject where position, image and visibility are provided.
    public GameObject(Point centre, Image image) {
        this.image = image;
        // Move the bounding box to the correct position.
        this.boundingBox = image.getBoundingBox();
        this.boundingBox.moveTo(computeTopLeft(centre));
    }

    // copy constructor
    public GameObject(GameObject other) {
        this.image = other.image;
        this.boundingBox = new Rectangle(boundingBox);
    }

    /* Return the position of the GameObject as a Point.
     */
    public Point getCenter() {
        return boundingBox.centre();
    }

    public Point topLeft(){
        return boundingBox.topLeft();
    }

    private Point computeTopLeft(Point center){
        return new Point(center.x - image.getWidth()/2, center.y - image.getHeight()/2);
    }

    /* Moves the GameObject so that its centre is at the specified point. */
    public void setCenter(Point center) {
        this.boundingBox.moveTo(computeTopLeft(center));
    }


    public Rectangle getBoundingBox(){
        return new Rectangle(boundingBox);
    }

    /* Return the image of the GameObject */
    public Image getImage() {
        return this.image;
    }

    public boolean collideWith(GameObject other){
        return boundingBox.intersects(other.getBoundingBox());
    }

    public <T extends GameObject & Movable> Side collideAtSide(T other){
        return this.getBoundingBox().intersectedAt(this.getCenter(), other.velocity());
    }

    /* Return distance from this GameObject to another. */
    public double distance(GameObject other){
        return other.getCenter().asVector().sub(this.getCenter().asVector()).length();
    }

    /* Return distance from this GameObject to a point. */
    public double distance(Point other){
        return other.asVector().sub(this.getCenter().asVector()).length();
    }

    public void render() {
        this.image.draw(getCenter().x, getCenter().y);
    }
}
