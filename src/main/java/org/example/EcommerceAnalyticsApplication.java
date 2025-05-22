package org.example;

import org.example.consumer.KafkaConsumer;
import org.example.producer.KafkaProducer;
import org.example.entity.Order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EcommerceAnalyticsApplication {
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> kafkaMessages = new LinkedBlockingQueue<>();
    private final AnalyticsEngine analyticsEngine = new AnalyticsEngine();

    private OrderGenerator orderGenerator;
    private KafkaProducer kafkaProducer;
    private KafkaConsumer kafkaConsumer;

    private Thread generatorThread;
    private Thread producerThread;
    private Thread consumerThread;

    private ScheduledExecutorService scheduler;

    public void start() {
        System.out.println("🚀 Starting E-commerce Real-Time Analytics System...\n");

        // Initialize components
        orderGenerator = new OrderGenerator(orderQueue);
        kafkaProducer = new KafkaProducer(orderQueue, kafkaMessages);
        kafkaConsumer = new KafkaConsumer(kafkaMessages, analyticsEngine);

        // Start threads
        generatorThread = new Thread(orderGenerator, "OrderGenerator");
        producerThread = new Thread(kafkaProducer, "KafkaProducer");
        consumerThread = new Thread(kafkaConsumer, "KafkaConsumer");

        generatorThread.start();
        producerThread.start();
        consumerThread.start();

        // Schedule analytics dashboard updates every 10 seconds
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                analyticsEngine::printRealTimeAnalytics,
                10, 10, TimeUnit.SECONDS
        );

        System.out.println("✅ System started successfully!");
        System.out.println("📊 Analytics dashboard will update every 10 seconds");
        System.out.println("⚡ Processing real-time orders...\n");
    }

    public void stop() {
        System.out.println("\n🛑 Shutting down system...");

        // Stop components gracefully
        if (orderGenerator != null) orderGenerator.stop();
        if (kafkaProducer != null) kafkaProducer.stop();
        if (kafkaConsumer != null) kafkaConsumer.stop();

        // Interrupt threads
        if (generatorThread != null) generatorThread.interrupt();
        if (producerThread != null) producerThread.interrupt();
        if (consumerThread != null) consumerThread.interrupt();

        // Shutdown scheduler
        if (scheduler != null) scheduler.shutdown();

        // Print final analytics
        System.out.println("\n📈 Final Analytics Report:");
        analyticsEngine.printRealTimeAnalytics();

        System.out.println("✅ System shutdown complete!");
    }

    public static void main(String[] args) {
        EcommerceAnalyticsApplication app = new EcommerceAnalyticsApplication();

        // Add shutdown hook for graceful termination
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));

        try {
            app.start();

            // Keep the application running
            // In a real application, you might want to add a web interface or API
            Thread.sleep(Long.MAX_VALUE);

        } catch (InterruptedException e) {
            System.out.println("Application interrupted");
        } finally {
            app.stop();
        }
    }
}
