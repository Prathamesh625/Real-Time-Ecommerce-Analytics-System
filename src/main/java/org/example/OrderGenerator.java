package org.example;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import org.example.entity.*;
import org.example.factory.*;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderGenerator implements Runnable {
    private final BlockingQueue<Order> orderQueue;
    private final List<Product> availableProducts;
    private final Random random = new Random();
    private volatile boolean running = true;

    public OrderGenerator(BlockingQueue<Order> orderQueue) {
        this.orderQueue = orderQueue;
        this.availableProducts = generateInitialProducts();
    }

    private List<Product> generateInitialProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            products.add(ProductFactory.createRandomProduct());
        }
        return products;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Order order = generateRandomOrder();
                orderQueue.put(order);

                // Generate orders every 1-3 seconds
                Thread.sleep(1000 + random.nextInt(2000));

            } catch (InterruptedException e) {
                System.out.println("Order generator interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private Order generateRandomOrder() {
        Customer customer = CustomerFactory.createRandomCustomer();
        String orderId = "ORD" + System.currentTimeMillis() + random.nextInt(1000);
        Order order = new Order(orderId, customer);

        // Add 1-5 random items to the order
        int itemCount = random.nextInt(5) + 1;
        Set<Product> selectedProducts = new HashSet<>();

        for (int i = 0; i < itemCount; i++) {
            Product product = availableProducts.get(random.nextInt(availableProducts.size()));
            if (!selectedProducts.contains(product)) {
                selectedProducts.add(product);
                int quantity = random.nextInt(3) + 1; // 1-3 quantity
                order.addItem(product, quantity);
            }
        }

        // Set random payment method
        String[] paymentMethods = {"Credit Card", "PayPal", "Bank Transfer", "Digital Wallet"};
        order.setPaymentMethod(paymentMethods[random.nextInt(paymentMethods.length)]);

        return order;
    }

    public void stop() {
        running = false;
    }

    public List<Product> getAvailableProducts() {
        return new ArrayList<>(availableProducts);
    }
}
