public interface HighLevelDisplay {

    public void clear() throws InterruptedException;
    public void addRow(String str) throws InterruptedException;
    public void deleteRow(int row) throws InterruptedException;

}