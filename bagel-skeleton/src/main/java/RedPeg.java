import bagel.Image;
import bagel.util.Point;

import java.util.logging.Level;

public class RedPeg extends Peg implements Destroyable {
    /**
     *
     * @param centre
     * @param image
     * @param shape
     */
    public RedPeg(Point centre, Image image, Shape shape){
        super(centre, image, shape, Colour.RED);
    }

    @Override
    public void destroy(ShadowBounce game) {
        game.removeGameObject(this);
    }

    @Override
    public <T extends GameObject> void onCollisionEnter(ShadowBounce game, T col) {
        destroy(game);
    }
}
