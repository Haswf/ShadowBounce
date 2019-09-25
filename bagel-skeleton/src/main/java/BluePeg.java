import bagel.Image;
import bagel.util.Point;

public class BluePeg extends Peg{
    public BluePeg(Point centre, Image image, Peg.SHAPE shape){
        super(centre, image, shape, COLOUR.BLUE);
    }

    public static BluePeg toBluePeg(Peg p){
        String path = imagePath(COLOUR.BLUE, p.getShape());
        return new BluePeg(p.center(), new Image(path), p.getShape());
    }
}
