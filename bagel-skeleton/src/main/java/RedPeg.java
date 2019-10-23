import bagel.Image;
import bagel.util.Point;

/**
 * A class for Red peg in ShadowBounce.
 */
public class RedPeg extends Peg implements Destroyable, OnCollisionEnter {
    /**
     * Red Peg Constructor
     * @param centre the center of red peg
     * @param image the image of red peg
     * @param shape the shape of peg
     */
    public RedPeg(Point centre, Image image, Shape shape){
        super(centre, image, shape, Colour.RED);
    }

    /**
     * Remove this red peg from the game.
     * @param game an instance of ShadowBounce.
     */
    @Override
    public void destroy(ShadowBounce game) {
        game.removeGameObject(this);
    }

    /**
     * Destroy the red peg if it was hit by a ball.
     * @param game an instance of ShadowBounce
     * @param col the other GameObject that the peg collides with.
     * @param <T> any subclass of GameObject
     */
    @Override
    public <T extends GameObject> void onCollisionEnter(ShadowBounce game, T col) {
        if (col instanceof Ball){
            destroy(game);
        }
    }
}
