package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchedulableTest {

    private static class MockSchedulable implements Schedulable {
        private final List<Post> posts = new ArrayList<>();

        @Override
        public void schedulePost(Post post) {
            posts.add(post);
        }

        @Override
        public List<Post> getPostsByMonth(int month, int year) {
            List<Post> result = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            for (Post post : posts) {
                if (post.getDate() != null) {
                    calendar.setTime(post.getDate());
                    int postMonth = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
                    int postYear = calendar.get(Calendar.YEAR);
                    if (postMonth == month && postYear == year) {
                        result.add(post);
                    }
                }
            }
            return result;
        }
    }

    private Schedulable schedulable;

    @BeforeEach
    void setUp() {
        schedulable = new MockSchedulable();
    }

    @Test
    void testSchedulePost() {
        Post post = new Post();
        post.setDate(new Date());
        post.setContent("Test Content");

        schedulable.schedulePost(post);
        List<Post> posts = schedulable.getPostsByMonth(
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.YEAR)
        );

        assertEquals(1, posts.size());
        assertEquals("Test Content", posts.get(0).getContent());
    }

    @Test
    void testGetPostsByMonth() {
        Calendar cal = Calendar.getInstance();

        // Add posts for different months
        for (int i = 0; i < 3; i++) {
            Post post = new Post();
            cal.set(2025, Calendar.JANUARY + i, 1); // Posts from January, February, March 2025
            post.setDate(cal.getTime());
            post.setContent("Post for month " + (Calendar.JANUARY + i + 1));
            schedulable.schedulePost(post);
        }

        // Retrieve posts for February (2nd month)
        List<Post> februaryPosts = schedulable.getPostsByMonth(2, 2025);

        assertEquals(1, februaryPosts.size());
        assertEquals("Post for month 2", februaryPosts.get(0).getContent());
    }

    @Test
    void testNoPostsForMonth() {
        // No posts scheduled
        List<Post> posts = schedulable.getPostsByMonth(12, 2025); // December 2025
        assertTrue(posts.isEmpty());
    }
}
