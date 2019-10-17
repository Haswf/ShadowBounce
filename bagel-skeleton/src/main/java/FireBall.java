import bagel.Image;

/**
 * Fireball class which defines creation of fireball,
 */
public class FireBall extends Ball implements Destroyable {
    // The range of explosion in pixels within which all pegs of the struck peg’s centre are destroyed.
    public final static int DESTROY_RANGE = 70;

    /**
     * Constructor to convert any ball into a fireball
     * @param other
     */
    public FireBall(Ball other){
        super(other.getCenter(), new Image("res/fireball.png"), other.getVelocity());
    }

    public void onCollisionEnter(ShadowBounce game, GameObject col){
        super.onCollisionEnter(game, col);
        for (GameObject go: withinRange(game.getCurrBoard().asList(), FireBall.DESTROY_RANGE)){
            if (go instanceof Destroyable){
                ((Destroyable)go).destroy(game);
            }
        }
    }

    /**
     * Requires the game to destroy itself.
     * @param game an instance of ShadowBounce
     */
    @Override
    public void destroy(ShadowBounce game) {
        game.removeGameObject(this);
    }
}
