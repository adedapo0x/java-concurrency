import java.util.concurrent.*;

public class NewAccountClassWithSemaphore {
    /*
     * This is to simulate the earlier created account class that can be found in AccountWithLock and some more, but this time
     * we are implementing the mutually exclusive condition by using a semaphore rather than a lock, and allowing permitted threads at a 
     * time to just be 1
     */
    private static Semaphore semaphore = new Semaphore(1);
    
    private static class Account {
        private int balance = 0;

        public int getBalance(){
            return balance;
        }

        public void deposit(int amount){
            try{
                semaphore.acquire();
                int newBalance = balance + amount;

                Thread.sleep(5);
            
                balance = newBalance;
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } finally{
                semaphore.release();
            }
        }
    }
}
