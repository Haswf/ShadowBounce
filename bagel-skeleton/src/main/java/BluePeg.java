import bagel.Image;
import bagel.util.Point;

public class BluePeg extends Peg implements OnCollisionRemove{
    /**
     *
     * @param centre
     * @param image
     * @param shape
     */
    public BluePeg(Point centre, Image image, Peg.Shape shape){
        super(centre, image, shape, Colour.BLUE);
    }

    /**
     *
     * @return
     */
    @ Override
    public GameObject onCollisionRemove(){
        return this;
    }

    /**
     *
     * @return
     */
    public RedPeg toRed(){
        String path = imagePath(Colour.RED, this.getShape());
        return new RedPeg(this.getCenter(), new Image(path), this.getShape());
    }

    /**
     *
     * @return
     */
    public GreenPeg toGreen(){
        String path = imagePath(Colour.GREEN, getShape());
        return new GreenPeg(getCenter(), new Image(path), getShape());
    }

}
