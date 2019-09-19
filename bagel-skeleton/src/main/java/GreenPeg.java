import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.*;

public class GreenPeg extends Peg {
    public GreenPeg(Point centre, Image image, Peg.SHAPE shape){
        super(centre, image, shape);
        super.setColour(COLOUR.GREEN);
        super.setShape(shape);
    }

    public static GreenPeg convertToGreen(Peg p){
        String path = imagePath(COLOUR.GREEN, p.getShape());
        return new GreenPeg(p.getPosition().getCentre(), new Image(path), p.getShape());
    }

    public ArrayList<Ball> duplicate(Ball incoming){
        ArrayList<Ball> dups = new ArrayList<>();
        double speed = 10.0/60;

        dups.add(new Ball(this.getPosition().getCentre(), incoming.getImage(),
                new Velocity(Vector2.up.add(Vector2.left), speed)));
        dups.add(new Ball(this.getPosition().getCentre(), incoming.getImage(),
                new Velocity(Vector2.up.add(Vector2.right), speed)));
        return dups;
    }

}
