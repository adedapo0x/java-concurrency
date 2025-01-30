public class SimpleLockOrderingDeadlock {
    // Simple lock ordering deadlock occurs when multiple threads try to acquire the same locks in different orders
    // This is deadlock prone!!

    // very basic example implemented here
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight(){
        synchronized(left){
            synchronized(right){
                doSomething();
            }
        }
    }

    public void rightLeft(){
        synchronized(right){
            synchronized(left){
                doSomethingElse();
            }
        }
    }

    public static void doSomething(){}
    public static void doSomethingElse(){}

    // Solution would be to implement the rightLeft to have the same order of acquiring the locks as left right or vice versa
    // public void rightLeft(){
    //     synchronized(left){
    //         synchronized(right){
    //             doSomethingElse();
    //         }
    //     }
    // }
}
