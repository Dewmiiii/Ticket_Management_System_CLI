package com.example;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {
    private int totalTickets; //Total number of tickets
    private int ticketReleaseRate; // Interval (in seconds) at which tickets are released by vendors
    private int customerRetrievalRate; // Interval (in seconds) at which customers retrieve tickets
    private int maxTicketCapacity; // Maximum number of tickets the pool can hold

    // Constructor to initialize the configuration object
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Getter and setter methods for each field
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Method to save the current configuration to a JSON file
    public void saveConfig(Configuration config){
        Gson gson = new Gson(); // Initialize Gson for JSON processing
        String json = gson.toJson(config);// Serialize the configuration object into a JSON string

        try {
            FileWriter file = new FileWriter("config.json"); // Open file for writing
            file.write(json);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Static method to load configuration from a JSON file
    public static Configuration loadConfig(String config){
        Gson gson = new Gson();
        try {
            FileReader file = new FileReader(config);
            return gson.fromJson(file, Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Return null if loading fails
        }
    }
}
