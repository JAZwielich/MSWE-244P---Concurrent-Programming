import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        LinkedList<HelloThread> threads = new LinkedList<>();
        Reader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);
        while (true) {
            synchronized (System.out) {
                System.out.println("Here are your options:\n\na - create a new thread\nb - " +
                        "stop a given thread (e.g. \"b 2\" kills thread 2)\nc - Stop all threads and exit this program");
                String userInput = in.readLine();
                String firstLetter;
                try {
                    firstLetter = userInput.substring(0, 1);

                }catch (StringIndexOutOfBoundsException e){
                    System.out.println("Could not recognize, please reenter command.");
                    continue;
                }
                if (firstLetter.equals("a")) {
                    threads.add(new HelloThread(String.valueOf(threads.size())));
                } else if (firstLetter.equals("c")) {
                    for (int i = 0; i < threads.size(); i++) {
                        threads.get(i).stopThread();
                    }
                    System.exit(1);
                } else if (firstLetter.equals("b")) {
                    String[] userInputParts = userInput.split(" ");
                    if (!(userInputParts.length == 2)) {
                        System.out.println("Please enter with syntax 'b <threadnumber>'");
                        continue;
                    } else if (Integer.parseInt(userInputParts[1]) > threads.size()) {
                        System.out.println("thread number does not exist");
                        continue;
                    }
                    threads.get(Integer.parseInt(userInputParts[1])).stopThread();
                } else {
                    System.out.println("Please enter 'a' 'b <threadnumber>' or 'c'");
                }
            }
        }
    }
}