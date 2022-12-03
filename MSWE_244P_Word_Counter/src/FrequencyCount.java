/*
 * @author Crista Lopes
 * Simple word frequency program
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class FrequencyCount {
	private static int corePoolSize = 10;
	private static int maxPoolSize = 20;
	private static long keepAliveTime = 3000;
	static ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
			corePoolSize,
			maxPoolSize,
			keepAliveTime,
			TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<>(128)
	);
	private static final List<String> stop_words = new ArrayList<String>();
    static final class Counter {
	private volatile HashMap<String, Integer> frequencies = new HashMap<String, Integer>();

	private void process(Path filepath) {
			try {
					try (Stream<String> lines = Files.lines(filepath /*Paths.get(filename)*/)) {
						lines.forEach(line -> {
							process(line);
						});
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	// Keep only the non stop words with 3 or more characters
	//~~~~~SYNCHRONIZED THE PLACEMENT OF THE WORDS IN FREQUENCIES TO MAKE THREADSAFE~~~~~
	private void process(String line) {
			String[] words = line.split("\\W+");
			for (String word : words) {
				String w = word.toLowerCase();
				if (!stop_words.contains(w) && w.length() > 2) {
					if (frequencies.containsKey(w))
						synchronized (frequencies) {
							frequencies.put(w, frequencies.get(w) + 1);
						}
					else
						synchronized (frequencies) {
							frequencies.put(w, 1);
						}
				}
		}
	}

	private List<Map.Entry<String, Integer>> sort() {
			Set<Map.Entry<String, Integer>> set = frequencies.entrySet();
			List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
					set);
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o1,
								   Map.Entry<String, Integer> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
			return list;
	}

	private HashMap<String, Integer> getFrequencies() {
			return frequencies;
	}

	public void merge(Counter other) {
				other.getFrequencies().forEach((k, v) -> frequencies.merge(k, v, Integer::sum));
		}

	// Only the top 40 words that are 3 or more characters
	public String toString() {
	    List<Map.Entry<String, Integer>> sortedMap = sort();
	    StringBuilder sb = new StringBuilder("---------- Word counts (top 40) -----------\n");
	    int i = 0;
	    for (Map.Entry<String, Integer> e : sortedMap) {
		String k = e.getKey();
		sb.append(k + ":" + e.getValue() +"\n"); 
		if (i++ > 40)
		    break;
	    }
	    return sb.toString();
	}

    }

    private static void loadStopWords() {
	String str = "";
	try {
	    byte[] encoded = Files.readAllBytes(Paths.get("stop_words"));
	    str = new String(encoded);    
	} catch (IOException e) {
	    System.out.println("Error reading stop_words");
	}
	String[] words = str.split(",");
	stop_words.addAll(Arrays.asList(words));
    }

    private static void countWords(Path p, Counter c) {
	System.out.println("Started " + p);
	c.process(p);
	System.out.println("Ended " + p);
    }

    public static void main(String[] args) {

	loadStopWords();
	Counter c = new Counter();

	long start = System.nanoTime();
	try {
		//ADDED IN THIS TRY BLOCK INSTANCES OF THREADPOOLEXECUTER IT ALSO
		//WAIT FOR THE THREADS TO SHUTDOWN BEFORE ENDING PROGRAM
	    try (Stream<Path> paths = Files.walk(Paths.get("."))) {
		    paths.filter(p -> p.toString().endsWith(".txt"))
					.forEach(p -> threadPoolExecutor.execute(newRunnable(p,c)));
			threadPoolExecutor.shutdown();
			threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		}
	} catch (IOException e) {
	    e.printStackTrace();
	}catch (InterruptedException e) {
		throw new RuntimeException(e);
	}
	long end = System.nanoTime();

	long elapsed = end - start;

	System.out.println(c);
	System.out.println("Elapsed time: " + elapsed/1000000 + "ms");

    }

	//PLACED Countwords IN A RUNNABLE
	private static Runnable newRunnable(Path p,Counter c){
		return new Runnable() {
			@Override
			public void run() {
				countWords(p, c);
			}
		};
	}
}
/**
 * ANSWER FROM SINGLE THREADED PROGRAM:
 * Started .\gems.txt
 * Ended .\gems.txt
 * Started .\out\production\MSWE_244P_Word_Counter\anonymit.txt
 * Ended .\out\production\MSWE_244P_Word_Counter\anonymit.txt
 * Started .\out\production\MSWE_244P_Word_Counter\cDc-0200.txt
 * Ended .\out\production\MSWE_244P_Word_Counter\cDc-0200.txt
 * Started .\out\production\MSWE_244P_Word_Counter\crossbow.txt
 * Ended .\out\production\MSWE_244P_Word_Counter\crossbow.txt
 * Started .\out\production\MSWE_244P_Word_Counter\gems.txt
 * Ended .\out\production\MSWE_244P_Word_Counter\gems.txt
 * Started .\src\anonymit.txt
 * Ended .\src\anonymit.txt
 * Started .\src\cDc-0200.txt
 * Ended .\src\cDc-0200.txt
 * Started .\src\crossbow.txt
 * Ended .\src\crossbow.txt
 * Started .\src\gems.txt
 * Ended .\src\gems.txt
 * ---------- Word counts (top 40) -----------
 * stone:549
 * one:498
 * diamond:306
 * stones:300
 * more:294
 * out:267
 * ___:234
 * cut:213
 * such:213
 * diamonds:210
 * new:207
 * now:207
 * even:207
 * time:189
 * over:183
 * people:180
 * color:177
 * very:177
 * cdc:168
 * make:150
 * xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx:147
 * computer:147
 * take:147
 * don:144
 * carat:144
 * government:135
 * emeralds:129
 * well:126
 * know:120
 * kkkkk:120
 * number:120
 * those:120
 * file:120
 * name:120
 * see:120
 * much:117
 * first:117
 * ratte:114
 * down:114
 * light:114
 * information:111
 * real:111
 *
 * Elapsed time: 257ms
 *
 * Process finished with exit code 0
 */