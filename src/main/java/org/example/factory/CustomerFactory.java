package org.example.factory;


import org.example.entity.Customer;
import java.util.Random;

public class CustomerFactory {
    private static final String[] FIRST_NAMES = {
            "John", "Jane", "Mike", "Sarah", "David", "Emma", "Chris", "Lisa", "Tom", "Anna"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"
    };

    private static final String[] LOCATIONS = {
            "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"
    };

    private static final Random random = new Random();

    public static Customer createRandomCustomer() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String customerId = "CUST" + System.currentTimeMillis() + random.nextInt(1000);
        String name = firstName + " " + lastName;
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@email.com";
        String location = LOCATIONS[random.nextInt(LOCATIONS.length)];

        Customer.CustomerType[] types = Customer.CustomerType.values();
        Customer.CustomerType type = types[random.nextInt(types.length)];

        return new Customer(customerId, name, email, location, type);
    }
}