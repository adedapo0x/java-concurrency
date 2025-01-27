import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ProducerConsumerExample {
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
                while (true){
                    System.out.println("Producer writes " + i);
                    buffer.write(i++);

                    Thread.sleep((int) Math.random() * 10000);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static class ConsumerTask implements Runnable {
        public void run(){
            try{
                while(true){
                    System.out.println("\t\t\tConsumer reads " + buffer.read());
                    Thread.sleep((int) Math.random() * 10000);
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

        private static Condition notEmpty = lock.newCondition();
        private static Condition notFull = lock.newCondition();

        public void write(int amount){
            lock.lock();
            try{
                while (queue.size() == CAPACITY){
                    System.out.println("Wait for notFull condition");
                    notFull.await();
                }
                queue.offer(amount);
                notEmpty.signalAll();
            } catch (InterruptedException ex){
                ex.printStackTrace();
            } 
            finally{
                lock.unlock();

            }
        }
    }
    
}
