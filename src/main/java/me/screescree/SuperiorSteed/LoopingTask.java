package me.screescree.SuperiorSteed;

public interface LoopingTask<T> {
    public void runLoopingTask(T arg);
    public int getIntervalTicks();
}
