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

    public enum COLOUR {
        BLUE,
        RED,
        GREY,
        GREEN,
    };

   public enum SHAPE{
        NORMAL,
        HORIZONTAL,
        VERTICAL,
    }

    private SHAPE shape;
    private COLOUR colour;

    public Peg(Point centre, Image image, SHAPE shape, COLOUR colour){
        super(centre, image);
        this.shape = shape;
        this.colour = colour;
    }

    public String toString(){
        return String.format("%s %s Peg (%f, %f)", this.shape.toString().toLowerCase(),
                this.colour.toString().toLowerCase(),
                this.center().x, this.center().y);
    }

    public COLOUR getColour() {
        return colour;
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

    public static Peg.COLOUR parseColor(String str){
        for (Peg.COLOUR colour : Peg.COLOUR.values()){
            if (str.contains(colour.toString().toLowerCase())){
                return colour;
            }
        }
        return Peg.COLOUR.BLUE;
    }

    public static Peg.SHAPE parseShape(String str){
        String c;
        for (Peg.SHAPE shape : Peg.SHAPE.values()){
            if (str.contains(shape.toString().toLowerCase())){
                return shape;
            }
        }
        return Peg.SHAPE.NORMAL;
    }
}
