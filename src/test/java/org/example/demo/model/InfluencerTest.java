package org.example.demo.model;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class InfluencerTest {

    @Test
    void testDefaultConstructor() {
        Influencer influencer = new Influencer();
        assertNull(influencer.getId());
        assertNull(influencer.getName());
        assertNull(influencer.getPlatform());
        assertEquals(0, influencer.getFollowers());
    }

    @Test
    void testParameterizedConstructor() {
        Influencer influencer = new Influencer("1", "Alice", "Instagram", 5000);
        assertEquals("1", influencer.getId());
        assertEquals("Alice", influencer.getName());
        assertEquals("Instagram", influencer.getPlatform());
        assertEquals(5000, influencer.getFollowers());
    }

    @Test
    void testGettersAndSetters() {
        Influencer influencer = new Influencer();
        influencer.setId("2");
        assertEquals("2", influencer.getId());

        influencer.setName("Bob");
        assertEquals("Bob", influencer.getName());

        influencer.setPlatform("YouTube");
        assertEquals("YouTube", influencer.getPlatform());

        influencer.setFollowers(10000);
        assertEquals(10000, influencer.getFollowers());
    }

    @Test
    void testCompareTo() {
        Influencer influencer1 = new Influencer("1", "Alice", "Instagram", 5000);
        Influencer influencer2 = new Influencer("2", "Bob", "YouTube", 10000);

        assertTrue(influencer1.compareTo(influencer2) < 0); // "Alice" comes before "Bob"
        assertTrue(influencer2.compareTo(influencer1) > 0); // "Bob" comes after "Alice"

        Influencer influencer3 = new Influencer("3", "Alice", "TikTok", 8000);
        assertEquals(0, influencer1.compareTo(influencer3)); // Same name
    }

    @Test
    void testFetchDetailsAsync() {
        Influencer influencer = new Influencer("1", "Alice", "Instagram", 5000);

        CompletableFuture<Void> future = influencer.fetchDetailsAsync();

        // Verify that the CompletableFuture completes without throwing exceptions
        assertDoesNotThrow(future::join);
    }

    @Test
    void testToString() {
        Influencer influencer = new Influencer("1", "Alice", "Instagram", 5000);
        String expected = "Influencer{id='1', name='Alice', platform='Instagram', followers=5000}";
        assertEquals(expected, influencer.toString());
    }
}
