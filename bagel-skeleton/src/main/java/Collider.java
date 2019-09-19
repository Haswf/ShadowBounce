import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Side;

public class Collider {
    private Rectangle boundingBox;

    public Collider(Position position, Image image){
        this.boundingBox = new Rectangle(position.getTopLeft(), image.getWidth(), image.getHeight());
    }

    // Copy constructor
    public Collider(Collider other){
        this.boundingBox = new Rectangle(other.boundingBox);
    }

    public Rectangle getBoundingBox(){
        return new Rectangle(this.boundingBox);
    }

    public void update(Position position){
        this.boundingBox.moveTo(position.getTopLeft());
    }

    boolean collideWith(GameObject other){
        if (other.getVisibility()){
            return boundingBox.intersects(other.getCollider().getBoundingBox());
        }
        return false;
    }

    Side collideAtSide(GameObject other, Velocity velocity){
        if (other.getVisibility()){
            Rectangle otherBoundingBox = other.getCollider().getBoundingBox();

            Point[] corners = {otherBoundingBox.topLeft(), otherBoundingBox.topRight(),
                    otherBoundingBox.bottomLeft(), otherBoundingBox.bottomRight()};

            Side colSide;
            for (Point p : corners){
                if ((colSide = this.getBoundingBox().intersectedAt(p, velocity.asVector()))!=Side.NONE){
                    return colSide;
                }
            }
        }
        return Side.NONE;
    }
}
