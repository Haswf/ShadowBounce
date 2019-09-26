import bagel.Image;
import bagel.util.Point;

public class GreyPeg extends Peg{
    public GreyPeg(Point centre, Image image, SHAPE shape){
        super(centre, image, shape, COLOUR.GREY);
    }
}
