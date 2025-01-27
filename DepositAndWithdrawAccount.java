import java.util.concurrent.*;
import java.util.concurrent.locks.*;

// This is to demonstrate thread cooperation. How conditions work while a lock is acquired

public class DepositAndWithdrawAccount {
    private static Account account = new Account();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new DepositFundsTask());
        executor.execute(new WithdrawFundTask());
        executor.shutdown();

        System.out.println("Thread 1\t\tThread 2\t\tBalance");
    }

    public static class DepositFundsTask implements Runnable {
        public void run(){
            try{
                while (true){
                    account.deposit((int)(Math.random() * 10) + 1);
                    Thread.sleep(1000);
                }

            } catch(InterruptedException ex) {
                ex.printStackTrace();
            }    
        }
    }
    public static class WithdrawFundTask implements Runnable {
        public void run(){
            while (true){
                account.withdraw((int)(Math.random() * 10) + 1);
            }
        }
    }

    public static class Account {
        private static Lock lock = new ReentrantLock();
        private static Condition newDeposit = lock.newCondition();

        private int balance = 0;
        public int getBalance(){
            return balance;
        }
        public void deposit(int amount){
            lock.lock();
            try{
                balance += amount;
                System.out.println("Deposit " + amount + "\t\t\t\t\t" + getBalance());
                newDeposit.signalAll();
            } finally{
                lock.unlock();
            }
        }
        public void withdraw(int amount){
            lock.lock();
            try{
                while(balance < amount){
                    System.out.println("\t\t\tWait for a deposit");
                    newDeposit.await();
                }
                balance -= amount;
                System.out.println("\t\t\tWithdraw " + amount + "\t\t" + getBalance());
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }finally{
                lock.unlock();
            }

        }
    }
}
