import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

// TODO - ADD FOR CASE 4 PRINT OUT WHAT N AND M ARE BEFORE THINGY
public class MessageQueue {
    private static int n_ids;

	//FirstTask
    public static void main(String[] args) throws IOException {
		System.out.println("which task are you Demoing?");
		Reader reader = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(reader);
		String answer = in.readLine();
		BlockingQueue<Message> queue = new LinkedBlockingQueue(10);
		Producer p1;
		Producer p2;
		Consumer c1;
		Consumer c2;
		switch (answer) {
			case ("1"):
				p1 = new Producer(queue, n_ids++);
				c1 = new Consumer(queue, n_ids++);

				new Thread(p1).start();
				new Thread(c1).start();

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				p1.stop();
				break;
			case ("2"):
				p1 = new Producer(queue, n_ids++);
				c1 = new Consumer(queue, n_ids++);
				c2 = new Consumer(queue, n_ids++);

				new Thread(p1).start();
				new Thread(c1).start();
				new Thread(c2).start();

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				p1.stop();
				break;
			case ("3"):
				p1 = new Producer(queue, n_ids++);
				p2 = new Producer(queue, n_ids++);
				c1 = new Consumer(queue, n_ids++);
				c2 = new Consumer(queue, n_ids++);

				new Thread(p1).start();
				new Thread(p2).start();
				new Thread(c1).start();
				new Thread(c2).start();

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				p1.stop();
				p2.stop();
				break;
			case "4":
				ArrayList<Consumer> consumers = new ArrayList<>();
				ArrayList<Producer> producers = new ArrayList<>();
				Integer n = 0;
				Integer m = 0;
				//Check if we have enough arguments
				if(args.length != 2){
					System.out.println("Please enter 2 arguments. 'N M' (N for consumers M for producers)");
					System.exit(0);
				}
				//Try to parse the arguments throw catch exception if its not a number
				try {
					n = Integer.parseInt(args[0]);
					m = Integer.parseInt(args[1]);
				} catch (NumberFormatException e){
					System.out.println("One of the arguments entered was NOT a number :(");
					System.exit(0);
				}
				System.out.println("Your number of consumers is " + n + "| Your number of producers is " + m);
				//Start up and store all the consumers
				for (int i = 0; i < n; i++){
					consumers.add(new Consumer(queue, n_ids++));
					new Thread(consumers.get(i)).start();
				}
				//Start up and store all the producers
				for (int i = 0; i < m; i++){
					producers.add(new Producer(queue, n_ids++));
					new Thread(producers.get(i)).start();
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < m; i++){
					producers.get(i).stop();
				}
				break;
			default:
				System.out.println("Try entering '1', '2', '3', or '4' (these correspond to the tasks in the description of the assignment");
		}
	}
}
