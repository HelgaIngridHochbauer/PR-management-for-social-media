package org.example.demo.model;

import java.util.concurrent.CompletableFuture;

public class GrowthAnalytics implements Analyticable, Comparable<GrowthAnalytics> {

    private int currentLikes;
    private int comments;
    private int currentFollowers;
    private int previousFollowers;

    public GrowthAnalytics(int currentLikes, int comments, int currentFollowers, int previousFollowers) {
        this.currentLikes = currentLikes;
        this.comments = comments;
        this.currentFollowers = currentFollowers;
        this.previousFollowers = previousFollowers;
    }

    @Override
    public void analyze() {
        // Perform the analysis asynchronously
        CompletableFuture.runAsync(() -> {
            System.out.println("Performing growth analysis in a separate thread...");
            // Add your intensive logic here
        }).thenRun(() -> {
            // Callback if needed after the analysis
            System.out.println("Finished growth analysis.");
        });
    }

    @Override
    public double calculateEngagementGrowth(int currentLikes, int previousLikes) {
        if (previousLikes == 0) {
            return 100.0;
        }

        // Execute the calculation asynchronously
        return CompletableFuture.supplyAsync(() -> ((double) (currentLikes - previousLikes) / previousLikes) * 100)
                .join(); // `join` is used to block until the result is ready (optional)
    }

    @Override
    public double analyzeFollowerGrowth(int currentFollowers, int previousFollowers) {
        if (previousFollowers == 0) {
            return 100.0;
        }

        // Execute the calculation asynchronously
        return CompletableFuture.supplyAsync(() -> ((double) (currentFollowers - previousFollowers) / previousFollowers) * 100)
                .join(); // `join` waits for the thread to complete
    }

    @Override
    public int compareTo(GrowthAnalytics other) {
        int thisEngagement = this.currentLikes + this.comments;
        int otherEngagement = other.currentLikes + other.comments;

        // Comparison happens synchronously as it's not resource-intensive
        return Integer.compare(thisEngagement, otherEngagement);
    }

    // Getters and setters remain unchanged
    public int getCurrentLikes() {
        return currentLikes;
    }

    public void setCurrentLikes(int currentLikes) {
        this.currentLikes = currentLikes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getCurrentFollowers() {
        return currentFollowers;
    }

    public void setCurrentFollowers(int currentFollowers) {
        this.currentFollowers = currentFollowers;
    }

    public int getPreviousFollowers() {
        return previousFollowers;
    }

    public void setPreviousFollowers(int previousFollowers) {
        this.previousFollowers = previousFollowers;
    }
}