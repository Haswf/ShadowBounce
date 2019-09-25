import bagel.Image;
import bagel.util.Point;

public class RedPeg extends Peg {
    public RedPeg(Point centre, Image image, SHAPE shape){
        super(centre, image, shape, COLOUR.RED);
    }

    public static RedPeg toRedPeg(Peg p){
        String path = imagePath(COLOUR.RED, p.getShape());
        return new RedPeg(p.center(), new Image(path), p.getShape());
    }
}
