import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private BlockingQueue queue;
    private int id;

	private volatile boolean stopConsumed;

    public Consumer(BlockingQueue q, int n) {
	queue = q;
	id = n;
	stopConsumed = false;
    }
    
    public void run() {
		Message msg = null;
			AtomicInteger count = new AtomicInteger(0);
			do {
				try {
					msg = (Message) queue.take(); // Get a message from the queue
					count.incrementAndGet();
					RandomUtils.print("Consumed " + msg.get(), id);
					Thread.sleep(RandomUtils.randomInteger());
					if (msg.get() == "stop") {
						queue.put(msg);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					continue;
				}
			} while (msg.get() != "stop");
			// Don't count the "stop" message
			count.decrementAndGet();
			RandomUtils.print("Messages received: " + count, id);
		}

}
