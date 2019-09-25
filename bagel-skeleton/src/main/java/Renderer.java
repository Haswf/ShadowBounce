import java.util.*;

public class Renderer{
    private boolean enabled;
    private LinkedHashSet<GameObject> renderQueue;

    public Renderer(){
        enabled = true;
        renderQueue = new LinkedHashSet<GameObject>();
    }

    public LinkedHashSet<GameObject> getQueue(){
        return new LinkedHashSet<>(renderQueue);
    }

    public void render() {
        for (Iterator<GameObject> iter = renderQueue.iterator(); iter.hasNext(); ) {
            GameObject o = iter.next();
            o.render();
        }
    }

    public void clear(){
        renderQueue.clear();
    }

    public void setEnabled(boolean status){
        this.enabled = status;
    }

    public boolean getEnabled(){
        return this.enabled;
    }

    public boolean add(GameObject o){
        return this.renderQueue.add(o);
    }

    public boolean addAll(List<GameObject> gameObjectList){
        return this.renderQueue.addAll(gameObjectList);
    }
    public boolean remove(GameObject o) {
        return this.renderQueue.remove(o);
    }

    public boolean removeAll(List<GameObject> gameObjectList){
        return this.renderQueue.removeAll(gameObjectList);
    }

    public int size(){
        return this.renderQueue.size();
    }
}
