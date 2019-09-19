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

    public void enable(){
        this.enabled = true;
    }
    public void disable(){
        this.enabled = false;
    }

    public void add(GameObject o){
        if (!renderQueue.contains(o)){
            this.renderQueue.add(o);
        }
    }

    public void addAll(List<GameObject> gameObjectList){
        gameObjectList.forEach(this::add);
    }
    public void remove(GameObject o) {
        if (renderQueue.contains(o)) {
            this.renderQueue.remove(o);
        }
    }

    public void removeAll(List<GameObject> gameObjectList){
        gameObjectList.forEach(this::remove);
    }

    public int size(){
        return this.renderQueue.size();
    }
}
