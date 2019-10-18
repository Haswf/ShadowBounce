import bagel.*;
import bagel.util.Point;

/**
 * A class representing peg in ShadowBounce.
 * @author Shuyang Fan
 */

abstract public class Peg extends GameObject{
    /**
     * Enum stands for colour of peg
     */
    public enum Colour {
        BLUE,
        RED,
        GREY,
        GREEN,
    }

    /**
     * Enum stands for shape of peg
     */
    public enum Shape {
        NORMAL,
        HORIZONTAL,
        VERTICAL,
    }

    private Shape shape;
    private Colour colour;

    /**
     * Peg Constructor
     * @param centre centre point of the peg rectangle
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
     * Returns a string representation of the peg.
     * @return a string representation of the peg.
     */
    public String toString() {
        return String.format("%s %s Peg (%f, %f)", this.shape.toString().toLowerCase(),
                this.colour.toString().toLowerCase(),
                this.getCenter().x, this.getCenter().y);
    }

    /**
     * Gets colour of a peg.
     * @return colour of a peg.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Gets shape of a peg.
     * @return shape of a peg.
     */
    public Shape getShape() {
        return shape;
    }

    /** Creates a image path based on colour and shape
     * @param colour the colour of the peg
     * @param shape the shape of the peg
     * @return an string of image path
     */
    public static String imagePath(Peg.Colour colour, Peg.Shape shape) {
        if (shape == Shape.NORMAL) {
            return "res/" + colour.toString().toLowerCase() + "-peg.png";
        } else {
            return "res/" + colour.toString().toLowerCase() + "-" + shape.toString().toLowerCase() + "-peg.png";
        }
    }

    /** Parse colour from a string.
     * @param str input string to be parsed.
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

    /** Parse shape from a string.
     * @param str input string to be parsed.
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
