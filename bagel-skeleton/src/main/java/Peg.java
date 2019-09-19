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

    static enum COLOUR {
        BLUE,
        RED,
        GREY,
        GREEN,
        NONE
    };

    static enum SHAPE{
        NORMAL,
        HORIZONTAL,
        VERTICAL,
        NONE
    }

    private SHAPE shape;
    private COLOUR colour;

    public Peg(Point centre, Image image){
        super(centre, image);
        this.setVisibility(true);
        this.colour = COLOUR.BLUE;
    }

    public Peg(Point centre, Image image, SHAPE shape){
        super(centre, image);
        this.setVisibility(true);
        this.setShape(shape);
        this.setColour(COLOUR.BLUE);
    }

    public String toString(){
        return String.format("Peg at %f, %f", this.getPosition().getCentre().x, this.getPosition().getCentre().y);
    }

    public COLOUR getColour() {
        return colour;
    }

    public void setColour(COLOUR colour) {
        this.colour = colour;
    }

    public void setShape(SHAPE shape){
        this.shape = shape;
    }

    public SHAPE getShape(){
        return this.shape;
    }
}
