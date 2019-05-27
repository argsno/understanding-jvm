package com.argsno.understaingjvm.completablefuture

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * CompletableFuture 不能被中断
 * http://ifeve.com/completablefuture/
 */
class InterruptibleTask implements Runnable {
    private final CountDownLatch started = new CountDownLatch(1)
    private final CountDownLatch interrupted = new CountDownLatch(1)

    @Override
    void run() {
        println "test"
        started.countDown()
        try {
            Thread.sleep(10_000)
        } catch (InterruptedException ignored) {
            interrupted.countDown()
        }
    }

    void blockUntilStarted() {
        started.await()
    }

    void blockUntilInterrupted() {
        assert interrupted.await(1, TimeUnit.SECONDS)
    }
}
