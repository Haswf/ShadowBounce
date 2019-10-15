import bagel.Image;

/**
 *
 */
public class FireBall extends Ball {
    // The range of explosion in pixels within which all pegs of the struck peg’s centre are destroyed.
    public final static int DESTROY_RANGE = 70;

    /**
     * Constructor to convert any ball into a fireball
     * @param other
     */
    public FireBall(Ball other){
        super(other.getCenter(), new Image("res/fireball.png"), other.velocity());
    }

}
