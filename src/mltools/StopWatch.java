package mltools;

import java.util.concurrent.TimeUnit;

/** Dead simple class measuring elapsed time */
public class StopWatch {
	
	public long time;
	public long totalTime;
	
	public StopWatch() {
		time = 0;
		totalTime = 0;
	}
	
	public void start() {
		time = System.nanoTime();
	}
	
	public void stop() {
		time = System.nanoTime() - time;
		totalTime = totalTime + time;
	}
	
	public String toString() {
		return StopWatch.toString(totalTime);
	}
	
	// --- --- --- Static
	
    public static long convert(long t, TimeUnit tu) {
        return tu.convert(t, TimeUnit.NANOSECONDS);
    }
	
	public static String toString(long t) {
		long mins = convert(t, TimeUnit.MINUTES);
		long sec = convert(t, TimeUnit.SECONDS) - mins*60;
		long msec = convert(t, TimeUnit.MILLISECONDS) - (sec+mins*60);
        return String.format("%d min, %d sec %d msec",mins, sec, msec);
	}
	
}

