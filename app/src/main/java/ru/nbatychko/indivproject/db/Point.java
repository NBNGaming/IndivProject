package ru.nbatychko.indivproject.db;

public class Point {
    private long timestamp;
    private double price;

    public Point(long timestamp, double price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public Point(String[] array) {
        if (array.length != 2) {
            throw new IllegalArgumentException("Array must have exactly two elements");
        }
        timestamp = Long.parseLong(array[0]);
        price = Double.parseDouble(array[1]);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
