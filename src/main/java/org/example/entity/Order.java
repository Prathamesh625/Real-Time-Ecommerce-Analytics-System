package org.example.entity;


import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private String orderId;
    private Customer customer;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderTime;
    private String paymentMethod;

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    // Constructor
    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.status = OrderStatus.PENDING;
        this.orderTime = LocalDateTime.now();
    }

    // Method to add items to order
    public void addItem(Product product, int quantity) {
        OrderItem item = new OrderItem(product, quantity);
        items.add(item);
        calculateTotal();
    }

    // Calculate total amount
    private void calculateTotal() {
        totalAmount = items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public void setItems(List<OrderItem> items) {
        this.items = new ArrayList<>(items);
        calculateTotal();
    }

    public double getTotalAmount() { return totalAmount; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customer=" + customer.getName() +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", orderTime=" + orderTime +
                ", itemCount=" + items.size() +
                '}';
    }
}
