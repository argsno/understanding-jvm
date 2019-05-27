package com.argsno.understaingjvm

import com.argsno.understaingjvm.completablefuture.InterruptibleTask
import spock.lang.Ignore
import spock.lang.Specification

import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Supplier

class CompletableFutureTest extends Specification {
    ExecutorService myThreadPool = Executors.newFixedThreadPool(4)

    def "Future is cancelled without exception"() {
        given:
        def task = new InterruptibleTask()
        def future = myThreadPool.submit(task)
        task.blockUntilStarted()
        and:
        future.cancel(true)
        when:
        future.get()
        then:
        thrown(CancellationException)
    }

    def "CompletableFuture is cancelled via CancellationException"() {
        given:
        def task = new InterruptibleTask()
        def future = CompletableFuture.supplyAsync({ task.run() } as Supplier, myThreadPool)
        task.blockUntilStarted()
        and:
        future.cancel(true)
        when:
        future.get()
        then:
        thrown(CancellationException)
    }

    def "should cancel Future"() {
        given:
        def task = new InterruptibleTask()
        def future = myThreadPool.submit(task)
        task.blockUntilStarted()
        when:
        future.cancel(true)
        then:
        task.blockUntilInterrupted()
    }

    @Ignore("Fails with CompletableFuture")
    def "should cancel CompletableFuture"() {
        given:
        def task = new InterruptibleTask()
        def future = CompletableFuture.supplyAsync({task.run()} as Supplier, myThreadPool)
        task.blockUntilStarted()
        when:
        future.cancel(true)
        then:
        task.blockUntilInterrupted()
    }
}
