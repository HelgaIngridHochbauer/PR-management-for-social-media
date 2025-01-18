package org.example.demo.model;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class GrowthAnalyticsTest {

    @Test
    void testAnalyze() {
        GrowthAnalytics analytics = new GrowthAnalytics(100, 50, 1000, 800);

        // The analyze method runs asynchronously and only prints output.
        // We verify that it runs without throwing exceptions.
        assertDoesNotThrow(analytics::analyze);
    }

    @Test
    void testCalculateEngagementGrowth() {
        GrowthAnalytics analytics = new GrowthAnalytics(0, 0, 0, 0);

        // Case: Previous likes are 0, should return 100.0
        double growth = analytics.calculateEngagementGrowth(50, 0);
        assertEquals(100.0, growth, 0.01);

        // Case: Normal growth calculation
        growth = analytics.calculateEngagementGrowth(150, 100);
        assertEquals(50.0, growth, 0.01);

        // Case: Negative growth
        growth = analytics.calculateEngagementGrowth(50, 100);
        assertEquals(-50.0, growth, 0.01);
    }

    @Test
    void testAnalyzeFollowerGrowth() {
        GrowthAnalytics analytics = new GrowthAnalytics(0, 0, 0, 0);

        // Case: Previous followers are 0, should return 100.0
        double growth = analytics.analyzeFollowerGrowth(1000, 0);
        assertEquals(100.0, growth, 0.01);

        // Case: Normal growth calculation
        growth = analytics.analyzeFollowerGrowth(1200, 1000);
        assertEquals(20.0, growth, 0.01);

        // Case: Negative growth
        growth = analytics.analyzeFollowerGrowth(800, 1000);
        assertEquals(-20.0, growth, 0.01);
    }

    @Test
    void testCompareTo() {
        GrowthAnalytics analytics1 = new GrowthAnalytics(100, 50, 1000, 800);
        GrowthAnalytics analytics2 = new GrowthAnalytics(200, 30, 1000, 800);

        // Compare based on engagement (likes + comments)
        assertTrue(analytics1.compareTo(analytics2) < 0); // analytics1 has less engagement
        assertTrue(analytics2.compareTo(analytics1) > 0); // analytics2 has more engagement

        GrowthAnalytics analytics3 = new GrowthAnalytics(100, 50, 1000, 800);
        assertEquals(0, analytics1.compareTo(analytics3)); // Same engagement
    }

    @Test
    void testGettersAndSetters() {
        GrowthAnalytics analytics = new GrowthAnalytics(100, 50, 1000, 800);

        analytics.setCurrentLikes(200);
        assertEquals(200, analytics.getCurrentLikes());

        analytics.setComments(100);
        assertEquals(100, analytics.getComments());

        analytics.setCurrentFollowers(1500);
        assertEquals(1500, analytics.getCurrentFollowers());

        analytics.setPreviousFollowers(1200);
        assertEquals(1200, analytics.getPreviousFollowers());
    }
}

