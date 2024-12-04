import java.util.concurrent.*;

public class Executor {
    public static void main(String[] args) {
        // creates 3 fixed maximum threads for tasks
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // submits tasks to executor
        executor.execute(new PrintChar('a', 100));
        executor.execute(new PrintChar('b', 100));
        executor.execute(new PrintNums(100));

        // shuts down the executor; no new tasks accepted but completes unfinished tasks already in it
        executor.shutdown();
    }    
    
}
