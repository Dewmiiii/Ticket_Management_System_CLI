package com.example;

public class Customer implements Runnable {
    private final int customerId;  // Unique ID for the customer
    private final int customerRetrievalRate;  // How fast tickets are retrieved (in seconds)
    private final TicketPool ticketPool;  // Shared ticket pool

    public Customer(int customerId, TicketPool ticketPool, int customerRetrievalRate) {
        this.customerId = customerId;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (true) {
            // Stop buying if all tickets are sold
            if (ticketPool.getTicketsSold() >= ticketPool.getTicketsAdded()) {
                System.out.println("Customer-" + customerId + ": All tickets are sold....");
                break;
            }

            Ticket ticket = ticketPool.buyTickets(); // Attempt to buy a ticket
            if (ticket != null) {
                System.out.println("Customer-" + customerId + ": Successfully bought ticket: " + ticket);
            } else {
                System.out.println("Customer-" + customerId + ": is waiting for tickets...");
            }

            try {
                Thread.sleep(customerRetrievalRate * 1000); // Simulate delay between ticket purchases
            } catch (InterruptedException e) {
                System.err.println("Customer-" + customerId + " was interrupted....");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

