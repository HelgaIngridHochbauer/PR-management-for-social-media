package org.example.demo.model;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public class Influencer implements Comparable<Influencer>, Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String platform;
    private int followers;

    // Default constructor
    public Influencer() {
    }

    // Parameterized constructor
    public Influencer(String id, String name, String platform, int followers) {
        this.id = id;
        this.name = name;
        this.platform = platform;
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    @Override
    public int compareTo(Influencer other) {
        // Async option for comparison, if the name comparison is part of a larger intensive task
        return CompletableFuture.supplyAsync(() -> this.name.compareTo(other.name))
                .join(); // Join is optional if you want to get a result immediately
    }

    // Perform a simulated async task, e.g., fetching additional influencer details
    public CompletableFuture<Void> fetchDetailsAsync() {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Fetching details for influencer: " + this.name);
            // Simulate fetching data with a delay
            try {
                Thread.sleep(2000); // Emulate IO latency (e.g., API or DB call)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Details fetched successfully for: " + this.name);
        });
    }

    @Override
    public String toString() {
        return "Influencer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", platform='" + platform + '\'' +
                ", followers=" + followers +
                '}';
    }
}