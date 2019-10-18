import bagel.Image;
import bagel.util.Point;

/**
 *  A class represents GreenPeg.
 */
public class GreenPeg extends Peg implements Destroyable, OnCollisionEnter {

    /**
     * GreenPeg Constructor.
     * @param centre the center of green peg.
     * @param image the image of green peg to be created.
     * @param shape the shape of green peg to be created.
     */
    public GreenPeg(Point centre, Image image, Peg.Shape shape){
        super(centre, image, shape, Colour.GREEN);
    }

    /**
     * Convert this peg to a BluePeg at the same position.
     * @return a blue peg at the same position.
     */
    public BluePeg toBlue(){
        String path = imagePath(Colour.BLUE, getShape());
        return new BluePeg(getCenter(), new Image(path), getShape());
    }

    /**
     * Remove this GameObject from the game.
     * @param shadowBounce an instance of ShadowBounce.
     */
    @Override
    public void destroy(ShadowBounce shadowBounce) {
        shadowBounce.removeGameObject(this);
    }

    /**
     * The GreenPeg should be removed when it was hit by a ball.
     * @param game an instance of ShadowBounce.
     * @param col another object which interacts with this green peg.
     * @param <T> another object must be a subclass of GameObject.
     */
    @Override
    public <T extends GameObject> void onCollisionEnter(ShadowBounce game, T col) {
        if (col instanceof Ball){
            destroy(game);
        }
    }
}
