import bagel.Image;
import bagel.util.Point;

public class GreyPeg extends Peg{
    public GreyPeg(Point centre, Image image, Shape shape){
        super(centre, image, shape, Colour.GREY);
    }
}
