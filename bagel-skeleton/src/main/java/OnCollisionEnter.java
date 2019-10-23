/**
 * OnCollisionEnter defines general behaviour when a GameObject was hit by another.
 * if a class implements this interface, it should response to a collision.
 */
public interface OnCollisionEnter {
        /**
         *  Defines the behaviour when a GameObject was hit by another.
         *  if a class implements this interface, it should response to a collision.
         * @param game an instance of ShadowBounce.
         * @param col the other GameObject which collides with this one.
         * @param <T> any subclass of GameObject.
         */
        public <T extends GameObject> void onCollisionEnter(ShadowBounce game, T col);
}
