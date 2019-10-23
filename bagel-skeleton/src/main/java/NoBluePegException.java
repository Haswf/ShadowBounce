import java.lang.Exception;

/**
 * This class implements NoBluePegException for Board in ShadowBounce,
 */
public class NoBluePegException extends Exception {
    /**
     * This exception was thrown when there is no blue peg remaining on current board.
     * @param msg message to be passed to Exception.
     */
    public NoBluePegException(String msg){
        super(msg);
    }
}
