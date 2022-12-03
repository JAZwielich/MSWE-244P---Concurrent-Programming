import java.util.concurrent.*;

public class Main3 {
   private static void nap(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void addProc(HighLevelDisplay d, Semaphore sem) throws InterruptedException {
       for (int i = 0; i < 10; i++) {
           try {
               sem.acquire();
               d.addRow("Flight " + i + " Departing");
           } catch (InterruptedException e){
               System.out.println(e);
           }
           sem.release();
           nap((int)( Math.random() * 1000) + (1000));
       }
   }

    private static void deleteProc(HighLevelDisplay d,Semaphore sem) throws InterruptedException {
           for (int i = 0; i < 10; i++){
               nap((int) ((Math.random() * 2000) + 4000));
               try {
                   sem.acquire();
                   d.deleteRow(0);
               } catch (InterruptedException e){
                   System.out.println(e);
               }
               sem.release();
           }
    }

    public static void main(String [] args) throws InterruptedException {
       Semaphore sem = new Semaphore(1);
	final HighLevelDisplay d = new JDisplay2();

	new Thread () {
	    public void run() {
            try {
                addProc(d, sem);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
	}.start();


	new Thread () {
	    public void run() {
            try {
                deleteProc(d, sem);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
	}.start();

    }
}