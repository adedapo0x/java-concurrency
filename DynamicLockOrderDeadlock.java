public class DynamicLockOrderDeadlock {
    public void transferMoney(Account fromAccount, Account toAccount, double amount){
        /*
         * This is also deadlock prone though it might initially appear as if all the threads acquire their lock in the same order
         * but in fact, the lock order here depends on the order of the arguments passed to transferMoney.
         * Deadlock can actually occur if two threads call transferMoney at the same time, one transferring from X to Y, the other transferring
         * from Y to X.
         * 
         * So the first thread acquires a lock on X, the second thread acquires lock on Y first, but the first thread needs Y which is already held by the second thread
         * and the second thread now needs X which is already held by the first thread, so hence a deadlock occurs.
         */
        
        synchronized(fromAccount){
            synchronized(toAccount){
                fromAccount.debit(amount);
                toAccount.credit(amount);
            }
        }

        // Solution to solve the deadlock issue
        // Here, we rely on their unique IDs, we are always making sure that the ones with lesser ID number get synchronized first before 
        // the one with larger ID no matter how the arguments are passed.

        Account firstLock, secondLock;
        // Determine lock order based on accout IDs
        if (fromAccount.getID() < toAccount.getID()){
            firstLock = fromAccount;
            secondLock = toAccount;
        } else {
            firstLock = toAccount;
            secondLock = fromAccount;
        }

        // Acquire lock in determined order
        synchronized(firstLock){
            synchronized(secondLock){
                if (firstLock == fromAccount){
                    firstLock.debit(amount);
                    secondLock.credit(amount);
                } else {
                    secondLock.debit(amount);
                    firstLock.credit(amount);
                }
            }
        }

    }

    // class definition
    public static class Account {
        private int id;
        public int getID(){
            return id;
        }
        public void debit(double amount){}
        public void credit(double amount){}
    }
}
