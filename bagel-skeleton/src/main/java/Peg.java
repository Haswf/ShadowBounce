import bagel.*;
import bagel.util.Point;

/**
 * A class representing peg in ShadowBounce.
 * Peg is a child class of GameObject.
 * It currently doesn't have any own attribute.
 * Peg is designed to be a class for future expandability.
 * @author Shuyang Fan
 */

abstract public class Peg extends GameObject{

    public static enum COLOUR {
        BLUE,
        RED,
        GREY,
        GREEN,
    };

   public static enum SHAPE{
        NORMAL,
        HORIZONTAL,
        VERTICAL,
    }

    private SHAPE shape;
    private COLOUR colour;

    public Peg(Point centre, Image image){
        super(centre, image);
    }

    public Peg(Point centre, Image image, SHAPE shape){
        super(centre, image);
        this.setShape(shape);
    }

    public String toString(){
        return String.format("%s %s Peg (%f, %f)", this.shape.toString().toLowerCase(),
                this.colour.toString().toLowerCase(),
                this.getPosition().getCentre().x, this.getPosition().getCentre().y);
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


    public static String imagePath(Peg.COLOUR colour, Peg.SHAPE shape){
        if (shape == SHAPE.NORMAL){
            return "res/" + colour.toString().toLowerCase() + "-peg.png";
        }
        else{
            return "res/" + colour.toString().toLowerCase() + "-" + shape.toString().toLowerCase() + "-peg.png";
        }
    }

    public static Peg.COLOUR parseColor(String[] data){
        for (Peg.COLOUR colour : Peg.COLOUR.values()){
            if (data[0].contains(colour.toString().toLowerCase())){
                return colour;
            }
        }
        return Peg.COLOUR.BLUE;
    }

    public static Peg.SHAPE parseShape(String[] data){
        String c;
        for (Peg.SHAPE shape : Peg.SHAPE.values()){
            if (data[0].contains(shape.toString().toLowerCase())){
                return shape;
            }
        }
        return Peg.SHAPE.NORMAL;
    }
}
