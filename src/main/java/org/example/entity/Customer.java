package org.example.entity;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String location;
    private CustomerType type;

    public enum CustomerType {
        REGULAR, PREMIUM, VIP
    }

    // Constructor
    public Customer(String customerId, String name, String email, String location, CustomerType type) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.location = location;
        this.type = type;
    }

    // Getters and Setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public CustomerType getType() { return type; }
    public void setType(CustomerType type) { this.type = type; }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        return customerId.hashCode();
    }
}
