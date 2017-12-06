package custom.osu.util;

import custom.osu.main.CustomTimer;

public class TimeShift
{
    private CustomTimer timer = new CustomTimer();
    private final int start, duration;
    private final boolean smooth;
    private float newBase = 0, newStart;

    public TimeShift(int s, int d, boolean sm)
    {
        start = s; duration = d; smooth = sm;
        newStart = start;

    }
    public float getRatio()
    {
        int time = (int)timer.getDifference();
        if (time <= start) return 0;
        if (time >= start + duration) return 1;
        float timeRatio = ((float)time - newStart) / getDuration();
        if (!smooth)
            return timeRatio;
        else
            {
            newStart = time;
            return (newBase = newBase + (1 - newBase) * timeRatio);
        }
    }
    public boolean hasStarted(){return timer.getDifference() > start;}
    public boolean isOver() {return timer.getDifference() > start + duration;}
    public boolean inShift(){return hasStarted() && !isOver();}
    private int getDuration()
    {
        if (smooth)
            return duration / 6;
        else
            return duration;
    }
}
