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

abstract public class Peg extends GameObject implements OnCollisionEnter{

    public enum Colour {
        BLUE,
        RED,
        GREY,
        GREEN,
    }

    public enum Shape {
        NORMAL,
        HORIZONTAL,
        VERTICAL,
    }

    private Shape shape;
    private Colour colour;

    /**
     * @param centre
     * @param image
     * @param shape
     * @param colour
     */
    public Peg(Point centre, Image image, Shape shape, Colour colour) {
        super(centre, image);
        this.shape = shape;
        this.colour = colour;
    }

    /**
     * @return
     */
    public String toString() {
        return String.format("%s %s Peg (%f, %f)", this.shape.toString().toLowerCase(),
                this.colour.toString().toLowerCase(),
                this.getCenter().x, this.getCenter().y);
    }

    public Colour getColour() {
        return colour;
    }

    public Shape getShape() {
        return this.shape;
    }

    /**
     * @param colour
     * @param shape
     * @return
     */
    public static String imagePath(Peg.Colour colour, Peg.Shape shape) {
        if (shape == Shape.NORMAL) {
            return "res/" + colour.toString().toLowerCase() + "-peg.png";
        } else {
            return "res/" + colour.toString().toLowerCase() + "-" + shape.toString().toLowerCase() + "-peg.png";
        }
    }

    /**
     * @param str
     * @return
     */
    public static Peg.Colour parseColor(String str) {
        for (Peg.Colour Colour : Peg.Colour.values()) {
            if (str.contains(Colour.toString().toLowerCase())) {
                return Colour;
            }
        }
        return Peg.Colour.BLUE;
    }

    /**
     * @param str
     * @return
     */
    public static Peg.Shape parseShape(String str) {
        for (Peg.Shape shape : Peg.Shape.values()) {
            if (str.contains(shape.toString().toLowerCase())) {
                return shape;
            }
        }
        return Peg.Shape.NORMAL;
    }
}
