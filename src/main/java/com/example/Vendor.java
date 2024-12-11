package com.example;

public class Vendor implements Runnable {
    private final int vendorId;   // Unique ID for the vendor
    private final int totalTickets;  // Total tickets this vendor is responsible for adding
    private final int ticketReleaseRate;   // Delay between adding tickets (in seconds)
    private final TicketPool ticketPool;   // Shared ticket pool

    // Constructor to initialize vendor properties
    public Vendor(int vendorId, int totalTickets, int ticketReleaseRate, TicketPool ticketPool) {
        this.vendorId = vendorId;  // Assign a unique ID to the vendor
        this.totalTickets = totalTickets;  // Set the number of tickets this vendor should add
        this.ticketReleaseRate = ticketReleaseRate;  // Define the rate at which tickets are released
        this.ticketPool = ticketPool;  // Reference to the shared ticket pool
    }

    @Override
    public void run() {
        int ticketsAddedByVendor = 0; // Tracks tickets added by this vendor

        while (true) {
            // Stop adding tickets if the vendor has added all its assigned tickets or the total pool limit is reached
            if (ticketsAddedByVendor >= totalTickets || ticketPool.getTicketsAdded() >= ticketPool.getConfiguration().getTotalTickets()) {
                System.out.println("Vendor-" + vendorId + ": All tickets added. Stopping...");
                break;  // Exit the loop once all tickets are added
            }

            // Create a new ticket and add it to the ticket pool
            Ticket ticket = new Ticket();  // Ticket object represents a single ticket
            ticketPool.addTickets(ticket);  // Add the ticket to the shared pool
            ticketsAddedByVendor++;  // Increment the count of tickets added by this vendor

            try {
                // Pause for the defined ticket release rate to simulate delayed addition of tickets
                Thread.sleep(ticketReleaseRate * 1000);
            } catch (InterruptedException e) {
                System.err.println("Vendor-" + vendorId + " interrupted. Exiting gracefully...");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}


