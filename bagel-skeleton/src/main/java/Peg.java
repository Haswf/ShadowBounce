import bagel.*;
import bagel.util.Point;

import java.util.AbstractMap;

/**
 * A class representing peg in ShadowBounce.
 * Peg is a child class of GameObject.
 * It currently doesn't have any own attribute.
 * Peg is designed to be a class for future expandability.
 * @author Shuyang Fan
 */

abstract public class Peg extends GameObject{

    public enum Colour {
        BLUE,
        RED,
        GREY,
        GREEN,
    };

   public enum Shape{
        NORMAL,
        HORIZONTAL,
        VERTICAL,
    }

    private Shape shape;
    private Colour colour;

    public Peg(Point centre, Image image, Shape shape, Colour colour){
        super(centre, image);
        this.shape = shape;
        this.colour = colour;
    }

    public String toString(){
        return String.format("%s %s Peg (%f, %f)", this.shape.toString().toLowerCase(),
                this.colour.toString().toLowerCase(),
                this.center().x, this.center().y);
    }

    public Colour getColour() {
        return colour;
    }

    public Shape getShape(){
        return this.shape;
    }

    public static String imagePath(Peg.Colour Colour, Peg.Shape shape){
        if (shape == Shape.NORMAL){
            return "res/" + Colour.toString().toLowerCase() + "-peg.png";
        }
        else{
            return "res/" + Colour.toString().toLowerCase() + "-" + shape.toString().toLowerCase() + "-peg.png";
        }
    }

    public static Peg.Colour parseColor(String str){
        for (Peg.Colour Colour : Peg.Colour.values()){
            if (str.contains(Colour.toString().toLowerCase())){
                return Colour;
            }
        }
        return Peg.Colour.BLUE;
    }

    public static Peg.Shape parseShape(String str){
        String c;
        for (Peg.Shape shape : Peg.Shape.values()){
            if (str.contains(shape.toString().toLowerCase())){
                return shape;
            }
        }
        return Peg.Shape.NORMAL;
    }
}
