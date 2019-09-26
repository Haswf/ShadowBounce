import java.util.Collection;

interface OnCollisionCreate {
    public <T extends GameObject & Movable> Collection<GameObject> onCollisionCreate(T col);
}
