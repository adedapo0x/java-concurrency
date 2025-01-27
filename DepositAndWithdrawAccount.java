import java.util.concurrent.*;
import java.util.concurrent.locks.*;

// This is to demonstrate thread cooperation. How conditions work while a lock is acquired

public class DepositAndWithdrawAccount {
    /*
     *  The example creates a new inner class named Account to model the account with two meth
ods,deposit(int) and withdraw(int), a class named DepositTask to add an amount 
to the balance, a class named WithdrawTask to withdraw an amount from the balance, and a 
main class that creates and launches two threads.
 The program creates and submits the deposit task and the withdraw task. 
The deposit task is purposely put to sleep to let the withdraw task run. When there are 
not enough funds to withdraw, the withdraw task waits for notification of the balance 
change from the deposit task.
 A lock is created. A condition named newDeposit on the lock is created in. A condition is bound to a lock. Before waiting or signaling the condition, a thread 
must first acquire the lock for the condition. The withdraw task acquires the lock in line, waits for the newDeposit condition when there is not a sufficient amount 
to withdraw, and releases the lock. The deposit task acquires the lock
and signals all waiting threads for the newDeposit condition after a new deposit 
is made.
     */
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
                    Thread.sleep(1000); // Purposefully delay here to let withdraw thread proceed
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
