import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/12 19:56
 * @Description TODO :
 **/
public class TestExecutorService {


    private ExecutorService executorService = new ThreadPoolExecutor(1,1,0L,
            TimeUnit.SECONDS,new LinkedBlockingDeque<>(1));


    ReentrantLock lock = new ReentrantLock();
    Condition condition  = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        test2();
    }


    public static void test2() throws InterruptedException {

        Executors.newCachedThreadPool();

        Map<String,ThreadPoolExecutor> map = new HashMap<>();
        ThreadPoolExecutor service = new ThreadPoolExecutor(1,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        map.put("default",service);

        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int n = 1;
                    int ttttt = 0;
                    while (true){

                        if(ttttt >1000){
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ttttt = 0;
                        }
                        ttttt++;

                        TestExecutorService t = new TestExecutorService();
                        t.set(n);
                        map.get("default").execute(t::p);
                        n++;

                    }
                }
            }).start();
        }
        System.out.println("waiting");
        Thread.sleep(Integer.MAX_VALUE);

    }





    private int n = 0;
    public void set(int n){
        this.n = n;
    }
    public void p(){

        byte[] abc = new byte[1024*1024];
        System.out.println("this is thread  " + n);
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static  void test1(){
        TestExecutorService t = new TestExecutorService();
        Thread out = new Thread(t::out);
        Thread in = new Thread(t::in);
        out.start();
        in.start();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void  in(){
        long c = 0;
        while (true){
            System.out.println("this is in");
            lock.lock();
            try {

                System.out.println("in getinngg lock");
                condition.signalAll();
                System.out.println("in ending....waiting");

            }  finally {
                lock.unlock();
                System.out.println("in---------------------------------unlock");
            }
            c++;
            System.out.println("in ========> " +c);
        }
    }

    public void out(){
        long c = 0;
        while (true){
            System.out.println("out this is out");
            lock.lock();
            try {
                System.out.println("out : get lock  and send signal");
                condition.await(100,TimeUnit.SECONDS);
                System.out.println("out send signal success");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("out  unlock");
            }

            c++;
            System.out.println("out =====> " + c);
        }



    }



    public void testRun(){
        while (true){
            System.out.println("this is function");
            try {
                throw  new Exception("abc");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
