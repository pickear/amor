package com.amor.common.helper;

import java.util.concurrent.CountDownLatch;

/**用于保持jvm不退出
 * @author dylan
 * @time 2017/6/15
 */
public final class KeepAliveHelper {

    /**

     * 保持jvm不退出，该方法需要用在main方法中

     */
    public static void keepAlive(){

        final CountDownLatch keepAliveLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                keepAliveLatch.countDown();
            }
        });
        Thread keepAliveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    keepAliveLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        keepAliveThread.setDaemon(false);
        keepAliveThread.start();
    }

    private KeepAliveHelper(){}
}