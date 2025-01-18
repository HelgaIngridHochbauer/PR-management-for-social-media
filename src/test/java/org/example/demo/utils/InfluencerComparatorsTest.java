package org.example.demo.utils;

import org.example.demo.model.Influencer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfluencerComparatorsTest {

    @Test
    void testSortByName() {
        List<Influencer> influencers = new ArrayList<>();
        influencers.add(new Influencer("3", "Charlie", "Instagram", 5000));
        influencers.add(new Influencer("1", "Alice", "YouTube", 10000));
        influencers.add(new Influencer("2", "Bob", "TikTok", 7000));

        influencers.sort(InfluencerComparators.BY_NAME);

        assertEquals("Alice", influencers.get(0).getName());
        assertEquals("Bob", influencers.get(1).getName());
        assertEquals("Charlie", influencers.get(2).getName());
    }

    @Test
    void testSortByFollowersAscending() {
        List<Influencer> influencers = new ArrayList<>();
        influencers.add(new Influencer("3", "Charlie", "Instagram", 5000));
        influencers.add(new Influencer("1", "Alice", "YouTube", 10000));
        influencers.add(new Influencer("2", "Bob", "TikTok", 7000));

        influencers.sort(InfluencerComparators.BY_FOLLOWERS_ASCENDING);

        assertEquals(5000, influencers.get(0).getFollowers());
        assertEquals(7000, influencers.get(1).getFollowers());
        assertEquals(10000, influencers.get(2).getFollowers());
    }

    @Test
    void testSortByFollowersDescending() {
        List<Influencer> influencers = new ArrayList<>();
        influencers.add(new Influencer("3", "Charlie", "Instagram", 5000));
        influencers.add(new Influencer("1", "Alice", "YouTube", 10000));
        influencers.add(new Influencer("2", "Bob", "TikTok", 7000));

        influencers.sort(InfluencerComparators.BY_FOLLOWERS_DESCENDING);

        assertEquals(10000, influencers.get(0).getFollowers());
        assertEquals(7000, influencers.get(1).getFollowers());
        assertEquals(5000, influencers.get(2).getFollowers());
    }

    @Test
    void testComparatorsAreConsistent() {
        Influencer influencer1 = new Influencer("1", "Alice", "YouTube", 10000);
        Influencer influencer2 = new Influencer("2", "Bob", "TikTok", 7000);

        // Test BY_NAME comparator
        assertTrue(InfluencerComparators.BY_NAME.compare(influencer1, influencer2) < 0);
        assertTrue(InfluencerComparators.BY_NAME.compare(influencer2, influencer1) > 0);

        // Test BY_FOLLOWERS_ASCENDING comparator
        assertTrue(InfluencerComparators.BY_FOLLOWERS_ASCENDING.compare(influencer1, influencer2) > 0);
        assertTrue(InfluencerComparators.BY_FOLLOWERS_ASCENDING.compare(influencer2, influencer1) < 0);

        // Test BY_FOLLOWERS_DESCENDING comparator
        assertTrue(InfluencerComparators.BY_FOLLOWERS_DESCENDING.compare(influencer1, influencer2) < 0);
        assertTrue(InfluencerComparators.BY_FOLLOWERS_DESCENDING.compare(influencer2, influencer1) > 0);
    }
}
