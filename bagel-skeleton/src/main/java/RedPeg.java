import bagel.Image;
import bagel.util.Point;

import java.util.logging.Level;

public class RedPeg extends Peg implements OnCollisionRemove {
    public RedPeg(Point centre, Image image, SHAPE shape){
        super(centre, image, shape, COLOUR.RED);
    }

    @ Override
    public GameObject onCollisionRemove(){
        ShadowBounce.LOGGER.log(Level.INFO, "Red ball destroyed.\n");
        return this;
    }
}
