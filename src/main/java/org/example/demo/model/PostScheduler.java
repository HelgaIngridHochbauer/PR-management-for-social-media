package org.example.demo.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PostScheduler implements Schedulable {
    private List<Post> scheduledPosts;

    public PostScheduler() {
        this.scheduledPosts = new ArrayList<>();
    }

    @Override
    public void schedulePost(Post post) {
        // Add posts asynchronously to avoid blocking the main thread
        CompletableFuture.runAsync(() -> {
            System.out.println("Scheduling post asynchronously...");
            scheduledPosts.add(post);
            System.out.println("Post scheduled: " + post.getId());
        });
    }

    @Override
    public List<Post> getPostsByMonth(int month, int year) {
        // Filter posts asynchronously and return the result
        System.out.println("Filtering posts asynchronously by month: " + month + ", year: " + year);

        return CompletableFuture.supplyAsync(() -> {
            List<Post> postsInMonth = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();

            for (Post post : scheduledPosts) {
                if (post.getDate() != null) {
                    calendar.setTime(post.getDate());
                    int postMonth = calendar.get(Calendar.MONTH) + 1;  // Calendar.MONTH is zero-based
                    int postYear = calendar.get(Calendar.YEAR);

                    if (postMonth == month && postYear == year) {
                        postsInMonth.add(post);
                    }
                }
            }

            return postsInMonth;
        }).join(); // Wait for the result (can be omitted if non-blocking is desired)
    }
}