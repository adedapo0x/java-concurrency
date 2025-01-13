import java.util.concurrent.*;

public class AccountWithSync {
    public static Account account = new Account();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++){
            executor.execute(new AddMoneyTask());
        }

        executor.shutdown();
        while (!executor.isTerminated()){}
        System.out.println("Balance is " + account.getBalance());
    }

    public static class AddMoneyTask implements Runnable{
        public void run(){
            account.deposit(1);

            // we could either put this here to synchronize or we use the synchronize keyword on the deposit method itself
            // synchronized(account){
            //     account.deposit(1);
            // }
        }
    }

    public static class Account {
        private int balance = 0;

        public int getBalance(){
            return balance;
        }

        // One method is to use the synchronized keyword here
        public synchronized void deposit(int amount){
            balance = balance + amount;
        }
    }
}
