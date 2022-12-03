import java.util.concurrent.*;

public class Main3 {

   private static void nap(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void addProc(HighLevelDisplay d) {
       for (int i = 0; i < 10; i++) {
           d.addRow("Flight " + i + " Departing");
           nap((int)( Math.random() * 1000) + (1000));
       }
   }

    private static void deleteProc(HighLevelDisplay d) {
           for (int i = 0; i < 10; i++){
               nap((int) ((Math.random() * 2000) + 4000));
               d.deleteRow(0);
           }
    }

    public static void main(String [] args) {
	final HighLevelDisplay d = new JDisplay2();

	new Thread () {
	    public void run() {
		addProc(d);
	    }
	}.start();


	new Thread () {
	    public void run() {
		deleteProc(d);
	    }
	}.start();

    }
}