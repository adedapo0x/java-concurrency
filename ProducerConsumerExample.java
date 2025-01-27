import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ProducerConsumerExample {
    /*
     *  Suppose you use a buffer to store integers and that the buffer size is limited. The buffer pro
vides the method write(int) to add an int value to the buffer and the method read()
 to read and delete an int value from the buffer. To synchronize the operations, use a lock 
with two conditions: notEmpty (i.e., the buffer is not empty) and notFull (i.e., the buffer 
is not full). When a task adds an int to the buffer, if the buffer is full, the task will wait for 
the notFull condition. When a task reads an int from the buffer, if the buffer is empty, the 
task will wait for the notEmpty condition. 
The buffer is actually a first-in, first-out queue 
     */
    private static Buffer buffer = new Buffer();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new ProducerTask());
        executor.execute(new ConsumerTask());
        executor.shutdown();
    }

    public static class ProducerTask implements Runnable {
        public void run(){
            try {
                int i = 1;
                // a loop is needed here, else we only write to the buffer once, same for the consumer run method
                while (true){
                    System.out.println("Producer writes " + i);
                    buffer.write(i++);
                    // intentionally sleep thread to observe the concept in action properly
                    Thread.sleep((int) (Math.random() * 10000));
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class ConsumerTask implements Runnable {
        public void run(){
            try{
                while(true){
                    System.out.println("\t\t\tConsumer reads " + buffer.read());
                    Thread.sleep((int) (Math.random() * 10000));
                }
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    private static class Buffer {
        private static final int CAPACITY = 1;
        private LinkedList<Integer> queue = new LinkedList<>();

        private static Lock lock = new ReentrantLock();

        // two conditions to check if buffer is empty or full
        private static Condition notEmpty = lock.newCondition();
        private static Condition notFull = lock.newCondition();

        public void write(int amount){
            lock.lock();
            try{
                while (queue.size() == CAPACITY){
                    System.out.println("Wait for notFull condition");
                    notFull.await(); // while queue is full, we keep the thread waiting and release the lock on queue
                }
                queue.offer(amount);
                notEmpty.signal(); // allows the read operation to continue as it is no longer empty after a write operation
            } catch (InterruptedException ex){
                ex.printStackTrace();
            } 
            finally{
                lock.unlock();
            }
        }

        public int read(){
            int value = 0;
            lock.lock();
            try {
                while (queue.isEmpty()){
                    System.out.println("\t\t\tWait for notEmpty condition");
                    notEmpty.await(); // waits for queue to not be empty
                }
                value = queue.remove();
                notFull.signal(); // alerts that queue is no longer empty
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally{
                lock.unlock();
                return value;
            }
        }
    }
    
}
