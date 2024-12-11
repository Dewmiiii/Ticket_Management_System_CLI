package com.example;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {
    private int totalTickets; //Total number of tickets
    private int ticketReleaseRate; // Ticket release rate
    private int customerRetrievalRate; // Customer retrieval rate
    private int maxTicketCapacity; // Maximum ticket capacity of the ticket pool

    // constructor
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

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

    //Method to save configuration into json file
    public void saveConfig(Configuration config){
        Gson gson = new Gson();
        String json = gson.toJson(config);
        try {
            FileWriter file = new FileWriter("config.json");
            file.write(json);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to load configuration
    public static Configuration loadConfig(String config){
        Gson gson = new Gson();
        try {
            FileReader file = new FileReader(config);
            return gson.fromJson(file, Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
