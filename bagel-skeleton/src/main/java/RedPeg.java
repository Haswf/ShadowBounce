import bagel.Image;
import bagel.util.Point;

public class RedPeg extends Peg {
    public RedPeg(Point centre, Image image, SHAPE shape){
        super(centre, image);
        super.setShape(shape);
        super.setColour(COLOUR.RED);
    }
}
