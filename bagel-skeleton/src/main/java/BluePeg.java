import bagel.Image;
import bagel.util.Point;

public class BluePeg extends Peg implements OnCollisionRemove{
    public BluePeg(Point centre, Image image, Peg.SHAPE shape){
        super(centre, image, shape, COLOUR.BLUE);
    }

    @ Override
    public GameObject onCollisionRemove(){
        return this;
    }

    public RedPeg toRed(){
        String path = imagePath(COLOUR.RED, this.getShape());
        return new RedPeg(this.center(), new Image(path), this.getShape());
    }

    public GreenPeg toGreen(){
        String path = imagePath(COLOUR.GREEN, getShape());
        return new GreenPeg(center(), new Image(path), getShape());
    }

}
