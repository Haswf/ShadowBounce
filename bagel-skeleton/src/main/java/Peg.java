import bagel.*;
import bagel.util.Point;

/**
 * A class representing peg in ShadowBounce.
 * Peg is a child class of GameObject.
 * It currently doesn't have any own attribute.
 * Peg is designed to be a class for future expandability.
 * @author Shuyang Fan
 */


public class Peg extends GameObject{

    public Peg(){
        super();
    }
    public Peg(Point centre, Image image, Boolean visible){
        super(centre, image, visible);
    }

    public String toString(){
        return String.format("Peg at %f, %f", this.getPosition().getCentre().x, this.getPosition().getCentre().y);
    }
}
