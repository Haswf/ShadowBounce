import java.util.Collection;

public interface OnCollisionEnter {
        public <T extends GameObject> void onCollisionEnter(T col);
}
