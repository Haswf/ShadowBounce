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

abstract public class GameObject implements Renderable{
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
    public Point center() {
        return boundingBox.centre();
    }

    public void topLeft(){
        this.boundingBox.topLeft();
    }

    public Point computeTopLeft(Point center){
        return new Point(center.x - image.getWidth()/2, center.y - image.getHeight()/2);
    }

    /* Moves the GameObject so that its centre is at the specified point. */
    public void moveTo(Point center) {
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
        Rectangle otherBoundingBox = other.getBoundingBox();
        Point[] corners = {otherBoundingBox.topLeft(), otherBoundingBox.topRight(),
                otherBoundingBox.bottomLeft(), otherBoundingBox.bottomRight()};

        Side colSide;
        for (Point p : corners){
            if ((colSide = this.getBoundingBox().intersectedAt(p, other.velocity()))!=Side.NONE){
                return colSide;
            }
        }
        return Side.NONE;
    }

    /* Return distance from this GameObject to another. */
    public double distance(GameObject other){
        return other.center().asVector().sub(this.center().asVector()).length();
    }

    /* Return distance from this GameObject to a point. */
    public double distance(Point other){
        return other.asVector().sub(this.center().asVector()).length();
    }

    /* Render the GameObject if its visibility is True */
    @ Override
    public void render() {
        if (this.image!=null){
            this.image.draw(center().x, center().y);
        }
    }
}
