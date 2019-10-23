import bagel.Image;
import bagel.util.Point;

public class GreyPeg extends Peg{
    /**
     * Grey Peg Constructor
     * @param centre the centre of image.
     * @param image the image of the grey peg to be created.
     * @param shape the shape of the grey peg to be created.
     */
    public GreyPeg(Point centre, Image image, Shape shape){
        super(centre, image, shape, Colour.GREY);
    }
}

