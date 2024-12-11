package com.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TicketPool {
    private final Configuration configuration;
    private final BlockingQueue<Ticket> ticketQueue;
    private int ticketsAdded = 0; // Tracks total tickets added to the pool
    private int ticketsSold = 0;  // Tracks total tickets sold from the pool

    //Constructor
    public TicketPool(Configuration configuration) {
        this.configuration = configuration;
        this.ticketQueue = new LinkedBlockingQueue<>(configuration.getMaxTicketCapacity());
    }

    // Vendor adds tickets to the ticket pool
    public synchronized void addTickets(Ticket ticket) {
        // Ensure the total tickets added doesn't exceed the limit
        if (ticketsAdded >= configuration.getTotalTickets()) {
            System.out.println(Thread.currentThread().getName() + ": All tickets have been added. Vendor stopping...");
            return;
        }

        try {
            // Wait while the pool is full
            while (ticketQueue.size() >= configuration.getMaxTicketCapacity()) {
                System.out.println(Thread.currentThread().getName() + ": Waiting to add ticket (pool full)...");
                wait(); // Wait until there's space in the pool
            }
            if (ticketsAdded < configuration.getTotalTickets()) {
                ticketQueue.add(ticket);
                ticketsAdded++;
                System.out.println(Thread.currentThread().getName() + ": Added ticket " + ticket +
                        " | Total Tickets Added: " + ticketsAdded +
                        " | Current Pool Size: " + ticketQueue.size());
                notifyAll(); // Notify waiting threads
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor interrupted while adding tickets.");
        }
    }

    // Customer buys tickets from the ticket pool
    public synchronized Ticket buyTickets() {
        try {
            while (ticketQueue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + ": Waiting to buy tickets (pool empty)...");
                wait();
            }
            Ticket ticket = ticketQueue.poll(); // Retrieve and remove a ticket from the queue
            ticketsSold++;
            System.out.println(Thread.currentThread().getName() + ": Bought ticket " + ticket +
                    " | Total Tickets Sold: " + ticketsSold +
                    " | Current Pool Size: " + ticketQueue.size());
            notifyAll(); // Notify waiting threads
            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Customer interrupted while buying tickets.");
            return null;
        }
    }

    // Getter for total tickets added
    public int getTicketsAdded() {
        return ticketsAdded;
    }

    // Getter for total tickets sold
    public int getTicketsSold() {
        return ticketsSold;
    }

    // Getter for the current ticket count in the pool
    public int getTicketCount() {
        return ticketQueue.size();
    }

    // Getter for configuration
    public Configuration getConfiguration() {
        return configuration;
    }
}


