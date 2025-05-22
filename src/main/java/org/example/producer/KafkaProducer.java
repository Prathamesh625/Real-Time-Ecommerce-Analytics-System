package org.example.producer;

import org.example.entity.Order;

import java.util.concurrent.BlockingQueue;

public class KafkaProducer implements Runnable {
    private final BlockingQueue<Order> orderQueue;
    private final BlockingQueue<String> kafkaMessages;
    private volatile boolean running = true;

    public KafkaProducer(BlockingQueue<Order> orderQueue, BlockingQueue<String> kafkaMessages) {
        this.orderQueue = orderQueue;
        this.kafkaMessages = kafkaMessages;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Order order = orderQueue.take();
                String message = convertOrderToMessage(order);
                kafkaMessages.put(message);

                System.out.println("[KAFKA PRODUCER] Sent message for order: " + order.getOrderId());

            } catch (InterruptedException e) {
                System.out.println("Kafka producer interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private String convertOrderToMessage(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER_ID:").append(order.getOrderId()).append("|");
        sb.append("CUSTOMER_ID:").append(order.getCustomer().getCustomerId()).append("|");
        sb.append("CUSTOMER_NAME:").append(order.getCustomer().getName()).append("|");
        sb.append("TOTAL_AMOUNT:").append(order.getTotalAmount()).append("|");
        sb.append("ORDER_TIME:").append(order.getOrderTime()).append("|");
        sb.append("LOCATION:").append(order.getCustomer().getLocation()).append("|");
        sb.append("ITEM_COUNT:").append(order.getItems().size());

        return sb.toString();
    }

    public void stop() {
        running = false;
    }
}
