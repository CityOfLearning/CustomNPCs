//

//

package noppes.npcs.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomNPCsScheduler {
	private static final ScheduledExecutorService executor;

	static {
		executor = Executors.newScheduledThreadPool(1);
	}

	public static void runTack(final Runnable task) {
		CustomNPCsScheduler.executor.schedule(task, 0L, TimeUnit.MILLISECONDS);
	}

	public static void runTack(final Runnable task, final int delay) {
		CustomNPCsScheduler.executor.schedule(task, delay, TimeUnit.MILLISECONDS);
	}
}
