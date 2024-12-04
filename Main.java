public class Main {
    public static void main(String[] args){
        Runnable task1 = new PrintChar('a', 100);
        Runnable task2 = new PrintChar('b', 100);
        Runnable task3 = new PrintNums(100);

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class PrintChar implements Runnable{
    private char charToPrint;
    private int times;

    public PrintChar(char c, int t){
        charToPrint = c;
        times = t;
    }

    @Override
    public void run(){
        for (int i = 0; i < times; i++){
            System.out.print(charToPrint);
        }
    }
}

class PrintNums implements Runnable{
    private int num;

    public PrintNums(int n){
        num = n;
    }

    @Override
    public void run(){
        Thread thread4 = new Thread(new PrintChar('c', 40));
        thread4.start();
        try{
            for (int i = 1; i <= num; i++){
                System.out.print(" " + i);
                if (i == 50) thread4.join();
            }
        } catch (InterruptedException ex){}
    }
}