import bagel.Image;
import bagel.util.Point;

public class RedPeg extends Peg {
    public RedPeg(Point centre, Image image, SHAPE shape){
        super(centre, image, shape);
        super.setColour(COLOUR.RED);
        super.setShape(shape);
    }

    public static RedPeg convertToRed(Peg p){
        String path = imagePath(COLOUR.RED, p.getShape());
        return new RedPeg(p.getPosition().getCentre(), new Image(path), p.getShape());
    }
}
