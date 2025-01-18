package org.example.demo.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostSchedulerTest {

    @Test
    void testDefaultConstructor() {
        PostScheduler scheduler = new PostScheduler();
        assertNotNull(scheduler);
        assertTrue(scheduler.getPostsByMonth(1, 2025).isEmpty()); // Should start with no posts
    }

    @Test
    void testSchedulePost() {
        PostScheduler scheduler = new PostScheduler();
        Post post = new Post();
        post.setDate(new Date());
        post.setContent("Scheduled Post");

        scheduler.schedulePost(post);

        // Allow some time for the asynchronous scheduling to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertEquals(1, scheduler.getPostsByMonth(Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.YEAR)).size());
    }

    @Test
    void testGetPostsByMonth() {
        PostScheduler scheduler = new PostScheduler();

        // Add posts with different months
        List<Post> posts = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            Post post = new Post();
            cal.set(2025, Calendar.JANUARY + i, 1); // January to May 2025
            post.setDate(cal.getTime());
            post.setContent("Post " + i);
            scheduler.schedulePost(post);
            posts.add(post);
        }

        // Allow some time for asynchronous scheduling to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<Post> aprilPosts = scheduler.getPostsByMonth(4, 2025); // April is the 4th month
        assertEquals(1, aprilPosts.size());
        assertEquals("Post 3", aprilPosts.get(0).getContent());
    }
}
