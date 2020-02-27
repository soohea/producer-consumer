package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Semaphore
 */
public class ProducerConsumer4 {
    static Semaphore fullSemaphore = new Semaphore(0);
    static Semaphore emptySemaphore = new Semaphore(1);
    static AtomicReference<Integer> integerReference = new AtomicReference<>();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                try {
                    emptySemaphore.acquire();

                    integerReference.set(new Random().nextInt());
                    System.out.println("Producing " + integerReference.get());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    fullSemaphore.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                try {
                    fullSemaphore.acquire();

                    System.out.println("Consuming " + integerReference.get());
                    integerReference.set(null);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    emptySemaphore.release();
                }
            }
        }
    }
}
