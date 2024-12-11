package com.example;

public class Vendor implements Runnable {
    private final int vendorId;   // Unique ID for the vendor
    private final int totalTickets;  // Total tickets this vendor is responsible for adding
    private final int ticketReleaseRate;   // Delay between adding tickets (in seconds)
    private final TicketPool ticketPool;   // Shared ticket pool

    //Constructor
    public Vendor(int vendorId, int totalTickets, int ticketReleaseRate, TicketPool ticketPool) {
        this.vendorId = vendorId;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        int ticketsAddedByVendor = 0; // Tracks tickets added by this vendor

        while (true) {
            // Stop adding tickets if the vendor has added all its assigned tickets or the total pool limit is reached
            if (ticketsAddedByVendor >= totalTickets || ticketPool.getTicketsAdded() >= ticketPool.getConfiguration().getTotalTickets()) {
                System.out.println("Vendor-" + vendorId + ": All tickets added. Stopping...");
                break;
            }

            Ticket ticket = new Ticket();
            ticketPool.addTickets(ticket);
            ticketsAddedByVendor++;

            try {
                Thread.sleep(ticketReleaseRate * 1000); // Delay between adding tickets
            } catch (InterruptedException e) {
                System.err.println("Vendor-" + vendorId + " interrupted. Exiting gracefully...");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}


