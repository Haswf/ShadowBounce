import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Side;

/**
 * An abstract GameObject class representing
 * something on the screen.
 * A typical GameObject has a position,
 * image, visibility and bounding box.
 * @author Shuyang Fan
 */

abstract public class GameObject implements Renderable{
    private Position position; // position of object on the screen
    private Image image; // image of the object
    private boolean visibility;
    private Collider collider; // bounding box for collision detection

    public GameObject(){
        this.position = new Position();
    }

    // constructor for GameObject where both position and image are provided.
    public GameObject(Point centre, Image image) {
        this.position = new Position(centre, image.getWidth(),image.getHeight());
        this.image = image;
        this.visibility = false; // by default, the object is set to invisible
        // Move the bounding box to the correct position.
        this.collider = new Collider(position, image);
    }

    // constructor for GameObject where position, image and visibility are provided.
    public GameObject(Point centre, Image image, boolean visible) {
        this.position = new Position(centre, image.getWidth(),image.getHeight());
        this.image = image;
        this.visibility = visible;
        // Move the bounding box to the correct position.
        this.collider = new Collider(position, image);
    }

    // copy constructor
    public GameObject(GameObject other) {
        this.position = new Position(other.position.getCentre(), other.position.getTopLeft());
        if (other.getImage() != null) {
            this.image = other.image;
            this.collider = new Collider(position, image);
        }
        this.visibility = other.visibility;
    }

    /* Return the position of the GameObject as a Point.
     */
    public Position getPosition() {
        return new Position(this.position);
    }

    /* Moves the GameObject so that its centre is at the specified point. */
    public void setPosition(Position position) {
        this.position = position;
        updateCollider();
    }

    /* Return the image of the GameObject */
    public Image getImage() {
        return this.image;
    }

    /*
    Set the image of the GameObject
     */
    public void setImage(Image newImage) {
        this.image = newImage;
    }

    /* Return an boolean representing visibility of the GameObject. */
    public boolean getVisibility() {
        return this.visibility;
    }

    /* Set visibility of the GameObject. The GameObject will be rendered if visibility is True. */
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    /* Return the Bounding Box of the gameObject as a Rectangle*/
    public Collider getCollider() {
        return new Collider(this.collider);
    }

    /* Set the Bounding Box of the gameObject with a given Rectangle*/
    public void setBoundingBox(Collider collider) {
        this.collider = collider;
    }

    /* Move Bounding box to right position after position has been changed */
    private void updateCollider() {
        this.collider.update(this.position);
    }

    /* Render the GameObject if its visibility is True */
    @ Override
    public void render() {
        if (this.image == null){
            return;
        }

        if (this.getVisibility()) {
            this.image.draw(this.getPosition().getCentre().x, this.getPosition().getCentre().y);
        }
    }
}
