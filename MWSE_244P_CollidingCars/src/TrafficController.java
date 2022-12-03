public class TrafficController {
    public volatile boolean carOnBridge;
    public void enterLeft() throws InterruptedException {
        synchronized (this) {
            while (carOnBridge){
                wait();
            }
            carOnBridge = true;
        }
    }
    public void enterRight() throws InterruptedException {
        synchronized (this){
            while (carOnBridge){
                wait();
            }
            carOnBridge = true;
        }
    }
    public void leaveLeft() throws InterruptedException {
        synchronized (this){
            notify();
            carOnBridge = false;
        }
    }
    public void leaveRight() throws InterruptedException {
        synchronized (this){
            notify();
            carOnBridge = false;
        }
    }

}