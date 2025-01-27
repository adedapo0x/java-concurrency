import java.util.concurrent.*;

public class ProducerConsumerUsingBlockingQueue {
    /*
     * Implementation of the producer consumer example, but this time with the inbuilt BlockingQueue
     * We use an ArrayBlockingQueue specifically here.
     * With this there is no need for explicitly creating a buffer with locks and putting conditions.
     * Since the put and take methods have already been synchronized
     */
    private static ArrayBlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(2);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new ProducerTask());
        executor.execute(new ConsumerTask());

        executor.shutdown();
    }

    private static class ProducerTask implements Runnable {
        public void run(){
            try{
                int i = 1;
                while (true){
                    System.out.println("Producer writes " + i);
                    buffer.put(i++);
                    Thread.sleep((int) (Math.random() * 10000));
                }
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    private static class ConsumerTask implements Runnable {
        public void run(){
            try{
                while (true){
                System.out.println("\t\t\tConsumer reads " + buffer.take());
                Thread.sleep((int) (Math.random() * 10000));
                }
            } catch(InterruptedException ex){

            }
        }
    }
}
