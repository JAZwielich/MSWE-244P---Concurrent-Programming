import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class HelloThread implements Runnable{
    Thread t;
    public HelloThread(String name){
        this.t = new Thread(this, name);
        t.start();
    }
    public void run(){
        while (true){
            LocalDateTime now = LocalDateTime.now();
            System.out.print("Hello World! I'm thread " + t.getName() + ". The time is " + now + "\n");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.print(t + " thread has stopped.\n");
                break;
            }
        }
    }
    public void stopThread(){
        this.t.interrupt();
    }
}
