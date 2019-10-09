import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.*;
import java.util.logging.Level;

public class GreenPeg extends Peg implements OnCollisionCreate, OnCollisionRemove {
    public GreenPeg(Point centre, Image image, Peg.Shape shape){
        super(centre, image, shape, Colour.GREEN);
    }

    public ArrayList<Ball> duplicate(Ball incoming){
        ArrayList<Ball> dups = new ArrayList<>();
        double speed = 10.0;

        dups.add(new Ball(getCenter(), incoming.getImage(),
                Vector2.up.add(Vector2.left).mul(speed)));
        dups.add(new Ball(getCenter(), incoming.getImage(),
                Vector2.up.add(Vector2.right).mul(speed)));
        return dups;
    }

    public BluePeg toBlue(){
        String path = imagePath(Colour.BLUE, getShape());
        return new BluePeg(getCenter(), new Image(path), getShape());
    }

    @ Override
    public <T extends GameObject & Movable> Collection<GameObject> onCollisionCreate(T col){
        ArrayList<GameObject> lst = new ArrayList<>();
        if (col instanceof Ball){
            lst.addAll(this.duplicate((Ball)col));
            ShadowBounce.LOGGER.log(Level.INFO, "Bonus balls released\n");
        }
        return lst;
    }

    @ Override
    public GameObject onCollisionRemove(){
        return this;
    }

}
