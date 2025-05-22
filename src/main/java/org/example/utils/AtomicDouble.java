package org.example.utils;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDouble {
    private final AtomicLong bits;

    public AtomicDouble() {
        this(0.0);
    }

    public AtomicDouble(double initialValue) {
        bits = new AtomicLong(Double.doubleToLongBits(initialValue));
    }

    public final double get() {
        return Double.longBitsToDouble(bits.get());
    }

    public final void set(double newValue) {
        bits.set(Double.doubleToLongBits(newValue));
    }

    public final double addAndGet(double delta) {
        while (true) {
            long current = bits.get();
            double currentValue = Double.longBitsToDouble(current);
            double newValue = currentValue + delta;
            long newBits = Double.doubleToLongBits(newValue);
            if (bits.compareAndSet(current, newBits)) {
                return newValue;
            }
        }
    }

    public final double getAndAdd(double delta) {
        while (true) {
            long current = bits.get();
            double currentValue = Double.longBitsToDouble(current);
            double newValue = currentValue + delta;
            long newBits = Double.doubleToLongBits(newValue);
            if (bits.compareAndSet(current, newBits)) {
                return currentValue;
            }
        }
    }

    @Override
    public String toString() {
        return Double.toString(get());
    }
}
