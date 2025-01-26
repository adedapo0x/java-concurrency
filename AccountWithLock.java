import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class AccountWithLock {
    private static Account account = new Account();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++){
            executor.execute(new AddPennyTask());
        }
        executor.shutdown();
        while (!executor.isTerminated()){}

        System.out.print(account.getBalance());   
        
    }

    public static class AddPennyTask implements Runnable{
        public void run(){
            account.deposit(1);
        }
    }

    public static class Account{
        private int balance = 0;
        private static Lock lock = new ReentrantLock();

        public int getBalance(){
            return balance;
        }

        public void deposit(int amount){ 
            lock.lock();
            // we could also decide not to use the sleep method. This is supposedly just to justify amplify the data coruption problem
            // we could have just acquired lock, do balance += amount, then release balance
            try{
                int newBalance = balance + amount;
                Thread.sleep(5);
                balance = newBalance;

            } catch(InterruptedException ex){
            } finally{
                lock.unlock();
            }
        }
    }
}