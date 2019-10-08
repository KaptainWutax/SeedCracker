package kaptainwutax.seedcracker.render;

import java.util.*;

public class RenderQueue {

    private final static RenderQueue INSTANCE = new RenderQueue();

    private Map<String, List<Runnable>> typeRunnableMap = new HashMap<>();
    private boolean trackRender = false;

    public static RenderQueue get() {
        return INSTANCE;
    }

    public void add(String type, Runnable runnable) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(runnable);

        if(!this.typeRunnableMap.containsKey(type)) {
            this.typeRunnableMap.put(type, new ArrayList<>());
        }

        List<Runnable> runnableList = this.typeRunnableMap.get(type);
        runnableList.add(runnable);
    }

    public void remove(String type, Runnable runnable) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(runnable);

        if(!this.typeRunnableMap.containsKey(type)) {
            return;
        }

        List<Runnable> runnableList = this.typeRunnableMap.get(type);
        runnableList.remove(runnable);
    }

    public void setTrackRender(boolean flag) {
        this.trackRender = flag;
    }

    public void onRender(String type) {
        if(!this.trackRender || !this.typeRunnableMap.containsKey(type))return;
        this.typeRunnableMap.get(type).forEach(Runnable::run);
    }

}
