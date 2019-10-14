import bagel.Image;
import bagel.util.Point;

public class FireBall extends Ball {
    public final static int DESTROY_RANGE = 70;
    public FireBall(Ball other){
        super(other.getCenter(), new Image("res/fireball.png"), other.velocity());
    }

}
