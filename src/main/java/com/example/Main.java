package com.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static boolean isSimulationRunning = false;  // Tracks if the ticketing simulation is running

    public static void main(String[] args) {

        // Use a try-with-resources statement to ensure the scanner is closed after use
        try (Scanner scanner = new Scanner(System.in)) {
            Configuration config = null;

            // Welcome message for user
            System.out.println("*************************************");
            System.out.println(" Welcome to the Ticketing System");
            System.out.println("*************************************");

            // Attempt to load existing configuration from a file
            config = Configuration.loadConfig("config.json");
            if (config != null) {
                System.out.println("\nConfiguration loaded successfully from file:");
                System.out.println(config);

                // Prompt user to continue with loaded configuration
                System.out.println("\nDo you want to continue with the loaded configuration? (yes/no)");
                String userResponse = scanner.nextLine().trim().toLowerCase();

                if (!userResponse.equals("yes")) {
                    // If the user opts not to use the existing configuration, prompt for new values
                    System.out.println("\nPlease configure the system with new values.");
                    config = configureSystem(scanner);// Get new configuration
                    config.saveConfig(config); // Save the new configuration to a file
                    System.out.println("\nNew configuration saved successfully.");
                }
            } else {
                // If no configuration file found, prompt for new configuration
                System.out.println("\nNo configuration file found. Please configure the system.");
                config = configureSystem(scanner); // Get new configuration
                config.saveConfig(config); // Save the new configuration to a file
                System.out.println("\nNew configuration saved successfully.");
            }

            // Initialize the ticket pool using the provided configuration
            TicketPool ticketPool = new TicketPool(config);

            boolean exit = false; // Flag to manage the main menu loop

            // Main menu loop
            while (!exit) {
                System.out.println("\nMain Menu:");
                System.out.println("1. Start the Ticketing System");
                System.out.println("2. View System Status");
                System.out.println("3. Stop the Ticketing System");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                // Get the user's menu choice
                int choice = getIntInput(scanner, ""); // Generic prompt since menu already displays the options

                switch (choice) {
                    case 1:
                        startTicketingSystem(scanner, config, ticketPool); // Start the simulation
                        break;
                    case 2:
                        viewSystemStatus(config, ticketPool); // Display system status
                        break;
                    case 3:
                        stopTicketingSystem(); // Stop the simulation
                        break;
                    case 4:
                        stopTicketingSystem(); // Ensure simulation is stopped before exiting
                        System.out.println("Exiting Ticketing System...");
                        exit = true;
                        break;
                    default:
                        // Handle invalid menu options
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            // Handle unexpected exceptions gracefully
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Method to configure the system by prompting the user for necessary values
    private static Configuration configureSystem(Scanner scanner) {
        int totalTickets = getIntInput(scanner, "Enter the Total Number of Tickets: ");
        int ticketReleaseRate = getIntInput(scanner, "Enter the Ticket Release Rate (seconds): ");
        int customerRetrievalRate = getIntInput(scanner, "Enter the Customer Retrieval Rate (seconds): ");
        int maxTicketCapacity = getIntInput(scanner, "Enter the Maximum Ticket Capacity: ");

        // Create and return a new Configuration object with user input
        return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }

    // Method to get valid integer inputs
    public static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number."); // Handle invalid input and prompt again
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    // Starts the ticketing simulation with user-defined vendor and customer counts
    public static void startTicketingSystem(Scanner scanner, Configuration config, TicketPool ticketPool) {
        if (isSimulationRunning) {
            System.out.println("Ticketing System is already running!"); // Prevent starting a simulation if one is already running
            return;
        }

        int numVendors = getIntInput(scanner, "Enter the number of vendors: ");
        int numCustomers = getIntInput(scanner, "Enter the number of customers: ");

        isSimulationRunning = true;

        // Start vendor threads to release tickets
        for (int i = 0; i < numVendors; i++) {
            Vendor vendor = new Vendor(i + 1, config.getTotalTickets(), config.getTicketReleaseRate(), ticketPool);
            new Thread(vendor, "Vendor-" + (i + 1)).start();
        }

        // Start customer threads to retrieve tickets
        for (int i = 0; i < numCustomers; i++) {
            Customer customer = new Customer(i + 1, ticketPool, config.getCustomerRetrievalRate());
            new Thread(customer, "Customer-" + (i + 1)).start();
        }

        System.out.println("\nSimulation started with " + numVendors + " vendors and " + numCustomers + " customers.\n");

        // Pause before returning to the menu
        System.out.println("Press Enter to return to the main menu...");
        scanner.nextLine(); // Consume the newline left by previous input
        scanner.nextLine(); // Wait for the user to press Enter
    }

    // Displays the current status of the system
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

    // Stops the ticketing simulation if it is running
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
