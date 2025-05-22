package org.example.consumer;

import org.example.AnalyticsEngine;
import org.example.entity.*;
import java.util.HashMap;
import java.util.Map;



import java.util.concurrent.BlockingQueue;

public class KafkaConsumer implements Runnable {
    private final BlockingQueue<String> kafkaMessages;
    private final AnalyticsEngine analyticsEngine;
    private volatile boolean running = true;

    public KafkaConsumer(BlockingQueue<String> kafkaMessages, AnalyticsEngine analyticsEngine) {
        this.kafkaMessages = kafkaMessages;
        this.analyticsEngine = analyticsEngine;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String message = kafkaMessages.take();
                Order order = parseMessageToOrder(message);

                if (order != null) {
                    analyticsEngine.processOrder(order);
                    System.out.println("[KAFKA CONSUMER] Processed message for order: " + order.getOrderId());
                }

            } catch (InterruptedException e) {
                System.out.println("Kafka consumer interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private Order parseMessageToOrder(String message) {
        try {
            // This is a simplified parser - in real Kafka, you'd use proper serialization
            String[] parts = message.split("\\|");
            Map<String, String> data = new HashMap<>();

            for (String part : parts) {
                String[] keyValue = part.split(":", 2);
                if (keyValue.length == 2) {
                    data.put(keyValue[0], keyValue[1]);
                }
            }

            // Create simplified order for analytics (you'd reconstruct full order in real scenario)
            Customer customer = new Customer(
                    data.get("CUSTOMER_ID"),
                    data.get("CUSTOMER_NAME"),
                    "",
                    data.get("LOCATION"),
                    Customer.CustomerType.REGULAR
            );

            Order order = new Order(data.get("ORDER_ID"), customer);

            // Add dummy product for demonstration (in real scenario, you'd include product details in message)
            Product dummyProduct = new Product("DUMMY", "Sample Product", "General",
                    Double.parseDouble(data.get("TOTAL_AMOUNT")), 1);
            order.addItem(dummyProduct, 1);

            return order;

        } catch (Exception e) {
            System.err.println("Error parsing Kafka message: " + e.getMessage());
            return null;
        }
    }

    public void stop() {
        running = false;
    }
}