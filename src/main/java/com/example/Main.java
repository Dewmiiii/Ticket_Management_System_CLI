package com.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static boolean isSimulationRunning = false;

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            Configuration config = null;
            // Welcome message
            System.out.println("*************************************");
            System.out.println(" Welcome to the Ticketing System");
            System.out.println("*************************************");

            // Load existing configuration if available
            config = Configuration.loadConfig("config.json");
            if (config != null) {
                System.out.println("\nConfiguration loaded successfully from file:");
                System.out.println(config);
            } else {
                System.out.println("\nNo configuration file found. Please configure the system.");
            }


            // User inputs new configuration
            System.out.println("\nPlease configure the system with new values.");
            config = configureSystem(scanner);
            config.saveConfig(config); // Save the new configuration to a file
            System.out.println("\nNew configuration saved successfully.");

            // Create a ticket pool
            TicketPool ticketPool = new TicketPool(config);

            boolean exit = false;

            while (!exit) {
                System.out.println("\nMain Menu:");
                System.out.println("1. Start the Ticketing System");
                System.out.println("2. View System Status");
                System.out.println("3. Stop the Ticketing System");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                int choice = getIntInput(scanner, ""); // Generic prompt since menu already displays the options

                switch (choice) {
                    case 1:
                        startTicketingSystem(scanner, config, ticketPool);
                        break;
                    case 2:
                        viewSystemStatus(config, ticketPool);
                        break;
                    case 3:
                        stopTicketingSystem();
                        break;
                    case 4:
                        stopTicketingSystem();
                        System.out.println("Exiting Ticketing System...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static Configuration configureSystem(Scanner scanner) {
        int totalTickets = getIntInput(scanner, "Enter the Total Number of Tickets: ");
        int ticketReleaseRate = getIntInput(scanner, "Enter the Ticket Release Rate (seconds): ");
        int customerRetrievalRate = getIntInput(scanner, "Enter the Customer Retrieval Rate (seconds): ");
        int maxTicketCapacity = getIntInput(scanner, "Enter the Maximum Ticket Capacity: ");

        return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }

    //Method to get valid inputs
    public static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    public static void startTicketingSystem(Scanner scanner, Configuration config, TicketPool ticketPool) {
        if (isSimulationRunning) {
            System.out.println("Ticketing System is already running!");
            return;
        }

        int numVendors = getIntInput(scanner, "Enter the number of vendors: ");
        int numCustomers = getIntInput(scanner, "Enter the number of customers: ");

        isSimulationRunning = true;

        // Start vendor threads
        for (int i = 0; i < numVendors; i++) {
            Vendor vendor = new Vendor(i + 1, config.getTotalTickets(), config.getTicketReleaseRate(), ticketPool);
            new Thread(vendor, "Vendor-" + (i + 1)).start();
        }

        // Start customer threads
        for (int i = 0; i < numCustomers; i++) {
            Customer customer = new Customer(i + 1, ticketPool, config.getCustomerRetrievalRate());
            new Thread(customer, "Customer-" + (i + 1)).start();
        }

        System.out.println("\nSimulation started with " + numVendors + " vendors and " + numCustomers + " customers.\n");
    }

    public static void viewSystemStatus(Configuration config, TicketPool ticketPool) {
        System.out.println("System Status:");
        System.out.println("*************************************");
        System.out.println("  Total Tickets: " + config.getTotalTickets());
        System.out.println("  Ticket Release Rate: " + config.getTicketReleaseRate() + " seconds");
        System.out.println("  Customer Retrieval Rate: " + config.getCustomerRetrievalRate() + " seconds");
        System.out.println("  Maximum Ticket Capacity: " + config.getMaxTicketCapacity());
        System.out.println("  Tickets Added: " + ticketPool.getTicketsAdded());
        System.out.println("  Tickets Sold: " + ticketPool.getTicketsSold());
        System.out.println("  Current Pool Size: " + ticketPool.getTicketCount());
        System.out.println("*************************************");
    }

    public static void stopTicketingSystem() {
        if (!isSimulationRunning) {
            System.out.println("No simulation is running!");
            return;
        }

        System.out.println("Stopping Ticketing System...");
        isSimulationRunning = false;

        System.out.println("Ticketing System stopped.");
    }
}
