package LIN2.schedule;

public abstract class Entry {
    private float frameTime;

    public Entry(float frameTime) {
        this.frameTime = frameTime;
    }

    public float getFrameTime() {
        return frameTime;
    }
}
