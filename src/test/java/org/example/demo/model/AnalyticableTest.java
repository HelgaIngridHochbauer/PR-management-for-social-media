package org.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticableTest {

    private static class MockAnalyticable implements Analyticable {
        private boolean analyzed = false;

        @Override
        public void analyze() {
            analyzed = true;
        }

        @Override
        public double calculateEngagementGrowth(int currentLikes, int previousLikes) {
            if (previousLikes < 0 || currentLikes < 0) {
                throw new IllegalArgumentException("Likes cannot be negative.");
            }
            if (previousLikes == 0) {
                return currentLikes > 0 ? 100.0 : 0.0;
            }
            return ((double) (currentLikes - previousLikes) / previousLikes) * 100;
        }

        @Override
        public double analyzeFollowerGrowth(int currentFollowers, int previousFollowers) {
            if (previousFollowers < 0 || currentFollowers < 0) {
                throw new IllegalArgumentException("Followers cannot be negative.");
            }
            if (previousFollowers == 0) {
                return currentFollowers > 0 ? 100.0 : 0.0;
            }
            return ((double) (currentFollowers - previousFollowers) / previousFollowers) * 100;
        }

        public boolean isAnalyzed() {
            return analyzed;
        }
    }

    @Test
    void testAnalyze() {
        MockAnalyticable analyticable = new MockAnalyticable();
        assertFalse(analyticable.isAnalyzed()); // Initially not analyzed
        analyticable.analyze();
        assertTrue(analyticable.isAnalyzed()); // Verify that analyze is executed
    }

    @Test
    void testCalculateEngagementGrowth() {
        MockAnalyticable analyticable = new MockAnalyticable();

        assertEquals(100.0, analyticable.calculateEngagementGrowth(200, 100), 0.01);
        assertEquals(50.0, analyticable.calculateEngagementGrowth(150, 100), 0.01);
        assertEquals(0.0, analyticable.calculateEngagementGrowth(100, 100), 0.01);
        assertEquals(-50.0, analyticable.calculateEngagementGrowth(50, 100), 0.01);

        assertThrows(IllegalArgumentException.class, () -> analyticable.calculateEngagementGrowth(-10, 100));
        assertThrows(IllegalArgumentException.class, () -> analyticable.calculateEngagementGrowth(100, -10));
    }

    @Test
    void testAnalyzeFollowerGrowth() {
        MockAnalyticable analyticable = new MockAnalyticable();

        assertEquals(100.0, analyticable.analyzeFollowerGrowth(2000, 1000), 0.01);
        assertEquals(50.0, analyticable.analyzeFollowerGrowth(1500, 1000), 0.01);
        assertEquals(0.0, analyticable.analyzeFollowerGrowth(1000, 1000), 0.01);
        assertEquals(-50.0, analyticable.analyzeFollowerGrowth(500, 1000), 0.01);

        assertThrows(IllegalArgumentException.class, () -> analyticable.analyzeFollowerGrowth(-10, 1000));
        assertThrows(IllegalArgumentException.class, () -> analyticable.analyzeFollowerGrowth(1000, -10));
    }
}
