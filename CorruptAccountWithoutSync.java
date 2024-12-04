import java.util.concurrent.*;

public class CorruptAccountWithoutSync {
    /**
     * Program to represent how a shared resource can be corrupted if accessed by multiple threads simultaneously
     * The example used here is in the case of a bank account. 100 people(threads) trying to donate a penny to a specific account
     */
    public static Account account = new Account(); // create account object
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool(); // creates executor

        for (int i = 0; i < 100; i++){
            executor.execute(new AddMoneyTask()); // creates 100 threads for the tasks
        }
        executor.shutdown();
        while (!executor.isTerminated()){} // waits for all threads to finish running before balance if fetched

        System.out.println("The balance is " + account.getBalance());
    }

    // Task for adding a penny to the account
    private static class AddMoneyTask implements Runnable {
        public void run(){
            account.deposit(1);
        }
    }
    
    // inner class that represents the account itself
    private static class Account {
        private int balance = 0;
    
        public int getBalance() {
            return balance;
        }
    
        public void deposit(int fund){
            int newBalance = balance + fund;
    
            // deliberate delay to make the data corruption problem easier to notice
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {}
    
            balance = newBalance;
        }
    }
}



