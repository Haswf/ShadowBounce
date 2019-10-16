import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.*;
import java.util.logging.Level;

public class GreenPeg extends Peg implements Destroyable, OnCollisionEnter {
    public static final float BALL_SPEED = 10;

    /**
     *
     * @param centre
     * @param image
     * @param shape
     */
    public GreenPeg(Point centre, Image image, Peg.Shape shape){
        super(centre, image, shape, Colour.GREEN);
    }

    private ArrayList<Ball> duplicate(Ball incoming){
        ArrayList<Ball> dups = new ArrayList<>();
        dups.add(new Ball(getCenter(), incoming.getImage(),
                Vector2.up.add(Vector2.left).mul(BALL_SPEED)));
        dups.add(new Ball(getCenter(), incoming.getImage(),
                Vector2.up.add(Vector2.right).mul(BALL_SPEED)));
        return dups;
    }

    /**
     *
     * @return
     */
    public BluePeg toBlue(){
        String path = imagePath(Colour.BLUE, getShape());
        return new BluePeg(getCenter(), new Image(path), getShape());
    }

    @Override
    public void destroy(ShadowBounce shadowBounce) {
        shadowBounce.removeGameObject(this);
    }

    @Override
    public <T extends GameObject> void onCollisionEnter(ShadowBounce game, T col) {
        if (col instanceof Ball){
            ArrayList<GameObject> duplicates = new ArrayList<>(this.duplicate((Ball) col));
            ShadowBounce.LOGGER.log(Level.INFO, "Bonus balls released\n");
            game.addGameObject(duplicates);
        }
        destroy(game);

    }
}
