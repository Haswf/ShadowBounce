import bagel.Image;
import bagel.util.Point;

/**
 * A class representing blue peg in ShadowBounce.
 */
public class BluePeg extends Peg implements Destroyable, OnCollisionEnter{
    /**
     * BluePeg Constructor
     * @param centre the center of the blue peg.
     * @param image the image of the blue peg.
     * @param shape the shape of blue peg to be created.
     */
    public BluePeg(Point centre, Image image, Peg.Shape shape){
        super(centre, image, shape, Colour.BLUE);
    }

    /**
     * Creates a red peg at the same position.
     * @return a red peg at the same position.
     */
    public RedPeg toRed(){
        // Construct image path based on colour and original shape
        String path = imagePath(Colour.RED, this.getShape());
        // Create a red peg of the original shape at the original position
        return new RedPeg(this.getCenter(), new Image(path), this.getShape());
    }

    /**
     * Creates a green pep at the same position.
     * @return a green pep at the same position
     */
    public GreenPeg toGreen(){
        // Construct image path based on colour and original shape
        String path = imagePath(Colour.GREEN, getShape());
        // Create a green peg of the original shape at the original position
        return new GreenPeg(getCenter(), new Image(path), getShape());
    }

    /**
     * Remove this blue peg from the game.
     * @param game an instance of ShadowBounce.
     */
    @Override
    public void destroy(ShadowBounce game) {
        game.removeGameObject(this);
    }

    /**
     * Destroy the blue peg if it was hit by a ball.
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
