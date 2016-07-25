package com.ticket;

import com.ticket.bean.UserVo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {


        int number = 10 & 1;
        System.out.println(Integer.toBinaryString(number));
        number = number >> 1;
        System.out.println(Integer.toBinaryString(number));
        System.out.println(number);
        System.out.println(Integer.toBinaryString(128));
        System.out.println(Integer.toBinaryString(129));
        System.out.println(128 | 129);
        Thread thread = new Thread(new SleepRunner(), "sleep");
        thread.setDaemon(true);

        Thread buys = new Thread(new BuysRunner(), "buys");
        buys.setDaemon(true);

        thread.start();
        buys.start();

        TimeUnit.SECONDS.sleep(5);
        thread.interrupt();

        buys.interrupt();

        System.out.println("sleep " + thread.isInterrupted());
        System.out.println("buys " + buys.isInterrupted());
        TimeUnit.SECONDS.sleep(2);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<UserVo>> listFuture = executorService.submit(new CallBack());
        List<UserVo> userVos = listFuture.get();
        for (UserVo userVo : userVos) {
            System.out.println(userVo.getNickName());
        }
    }

    static class SleepRunner implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(11);
            }
        }
    }


    static class BuysRunner implements Runnable {

        @Override
        public void run() {
            while (true) {
            }
        }
    }

    class CallBack implements Callable<List<UserVo>> {


        @Override
        public List<UserVo> call() throws Exception {
            List<UserVo> userVos = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                UserVo userVo = new UserVo();
                userVo.setNickName("userVo" + i);
                userVos.add(userVo);
            }
            return userVos;
        }
    }
}