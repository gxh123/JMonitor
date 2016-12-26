import com.jmonitor.core.message.type.*;
import com.jmonitor.core.JMonitor;

import javax.naming.NamingException;
import javax.xml.xpath.XPathException;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by gxh on 2016/10/1.
 */
public class JMonitorTest {

    public static void generateException(int i) throws ClassNotFoundException {
        if(i == 0){
            System.out.println("generateException: " + "NullPointerException");
            throw new NullPointerException();
        }
        if(i == 1){
            System.out.println("generateException: " + "IndexOutOfBoundsException");
            throw new IndexOutOfBoundsException();
        }
        if (i == 2) {
            System.out.println("generateException: " + "IllegalArgumentException");
            throw new IllegalArgumentException();
        }
        if (i == 3) {
            System.out.println("generateException: " + "ClassNotFoundException");
            throw new ClassNotFoundException();
        }
        if (i == 4) {
            System.out.println("generateException: " + "ArithmeticException");
            throw new ArithmeticException();
        }
    }

    public static void generateEvent(int i)  {
        if(i == 0){
            JMonitor.logEvent("Event", "Call", Message.SUCCESS, "data"+i);
            System.out.println("generateEvent: " + "Call");
        }
        if(i == 1){
            JMonitor.logEvent("Event", "Login", Message.SUCCESS, "user:gxh");
            System.out.println("generateEvent: " + "Login");
        }
        if (i == 2) {
            JMonitor.logEvent("Event", "Logout", Message.SUCCESS, "user:gxh");
            System.out.println("generateEvent: " + "Logout");
        }
        if (i == 3) {
            JMonitor.logEvent("Event", "redirect", Message.SUCCESS, "homePage"+i);
            System.out.println("generateEvent: " + "redirect");
        }
    }

    public static void generateTransaction(int i)  {
        if(i == 0){
            Transaction t = JMonitor.newTransaction("Transaction", "query database");
            Random r = new Random();
            try {
                Thread.sleep(r.nextInt(100));    //模拟耗时操作
            } catch (InterruptedException e) {
            }
            t.complete();
            System.out.println("generateTransaction: " + "query database");
        }
        if(i == 1){
            Transaction t = JMonitor.newTransaction("Transaction", "query cache");
            Random r = new Random();
            try {
                Thread.sleep(r.nextInt(100));    //模拟耗时操作
            } catch (InterruptedException e) {
            }
            t.complete();
            System.out.println("generateTransaction: " + "query cache");
        }
        if (i == 2) {
            Transaction t = JMonitor.newTransaction("Transaction", "IO");
            Random r = new Random();
            try {
                Thread.sleep(r.nextInt(100));    //模拟耗时操作
            } catch (InterruptedException e) {
            }
            t.complete();
            System.out.println("generateTransaction: " + "IO");
        }
    }

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                while(true) {
                    try {
                        generateException(r.nextInt(6));
                    }catch (Exception e){
                        JMonitor.logError(e);
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                while(true) {
                    generateEvent(r.nextInt(4));
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                while(true) {
                    generateTransaction(r.nextInt(3));
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
