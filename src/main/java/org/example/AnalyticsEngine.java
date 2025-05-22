package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.entity.OrderItem;
import org.example.entity.Order;
import org.example.utils.AtomicDouble;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class AnalyticsEngine {
    private final Map<String, AtomicInteger> productSales = new ConcurrentHashMap<>();
    private final Map<String, AtomicDouble> categoryRevenue = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> locationStats = new ConcurrentHashMap<>();
    private final Map<String, AtomicDouble> customerSpending = new ConcurrentHashMap<>();
    private final PriorityQueue<ProductSalesData> topProducts = new PriorityQueue<>(
            (a, b) -> Integer.compare(b.getSalesCount(), a.getSalesCount())
    );

    private AtomicDouble totalRevenue = new AtomicDouble(0.0);
    private AtomicInteger totalOrders = new AtomicInteger(0);

    public synchronized void processOrder(Order order) {
        try {
            // Update total stats
            totalRevenue.addAndGet(order.getTotalAmount());
            totalOrders.incrementAndGet();

            // Update customer spending
            customerSpending.computeIfAbsent(order.getCustomer().getCustomerId(),
                    k -> new AtomicDouble(0.0)).addAndGet(order.getTotalAmount());

            // Update location stats
            locationStats.computeIfAbsent(order.getCustomer().getLocation(),
                    k -> new AtomicInteger(0)).incrementAndGet();

            // Process each item in the order
            for (OrderItem item : order.getItems()) {
                String productId = item.getProduct().getProductId();
                String category = item.getProduct().getCategory();

                // Update product sales
                productSales.computeIfAbsent(productId, k -> new AtomicInteger(0))
                        .addAndGet(item.getQuantity());

                // Update category revenue
                categoryRevenue.computeIfAbsent(category, k -> new AtomicDouble(0.0))
                        .addAndGet(item.getSubTotal());
            }

            logOrderProcessed(order);

        } catch (Exception e) {
            System.err.println("Error processing order: " + order.getOrderId() + " - " + e.getMessage());
        }
    }

    public List<ProductSalesData> getTopProducts(int limit) {
        return productSales.entrySet().stream()
                .map(entry -> new ProductSalesData(entry.getKey(), entry.getValue().get()))
                .sorted((a, b) -> Integer.compare(b.getSalesCount(), a.getSalesCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getCategoryRevenue() {
        return categoryRevenue.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get()
                ));
    }

    public Map<String, Integer> getLocationStats() {
        return locationStats.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get()
                ));
    }

    public void printRealTimeAnalytics() {
        System.out.println("\n=== REAL-TIME ANALYTICS DASHBOARD ===");
        System.out.println("Timestamp: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Total Orders Processed: " + totalOrders.get());
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue.get()));
        System.out.println("Average Order Value: $" + String.format("%.2f",
                totalOrders.get() > 0 ? totalRevenue.get() / totalOrders.get() : 0.0));

        // Top Products
        System.out.println("\nTop 5 Products:");
        getTopProducts(5).forEach(product ->
                System.out.println("  " + product.getProductId() + ": " + product.getSalesCount() + " sales")
        );

        // Category Performance
        System.out.println("\nCategory Revenue:");
        getCategoryRevenue().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .forEach(entry ->
                        System.out.println("  " + entry.getKey() + ": $" + String.format("%.2f", entry.getValue()))
                );

        // Location Stats
        System.out.println("\nTop Locations:");
        getLocationStats().entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(entry ->
                        System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " orders")
                );

        System.out.println("==========================================\n");
    }

    private void logOrderProcessed(Order order) {
        System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " +
                "Processed Order: " + order.getOrderId() +
                " | Customer: " + order.getCustomer().getName() +
                " | Amount: $" + String.format("%.2f", order.getTotalAmount()) +
                " | Items: " + order.getItems().size());
    }
}