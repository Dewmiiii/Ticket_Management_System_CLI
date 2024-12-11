package com.example;

public class Ticket {
    private static int currentId = 0; // Shared counter for unique ticket IDs
    private final int ticketId;      // Unique ID for the ticket

    public Ticket() {
        this.ticketId = generateUniqueId();
    }

    // Synchronized method to ensure unique ID generation for tickets
    private static synchronized int generateUniqueId() {
        return currentId++;
    }

    public int getTicketId() {
        return ticketId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                '}';
    }
}

