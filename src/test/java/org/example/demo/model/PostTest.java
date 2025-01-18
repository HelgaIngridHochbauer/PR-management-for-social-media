package org.example.demo.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void testDefaultConstructor() {
        Post post = new Post();
        assertNotNull(post.getId()); // ID should be auto-generated
        assertNull(post.getContent());
        assertNull(post.getDate());
        assertEquals(0, post.getExpectedLikes());
        assertEquals(0, post.getCurrentLikes());
        assertEquals(0, post.getPreviousLikes());
        assertEquals(0, post.getComments());
        assertEquals(0, post.getCurrentFollowers());
        assertEquals(0, post.getPreviousFollowers());
        assertNull(post.getCampaignName());
        assertNull(post.getInfluencerId());
        assertEquals(0, post.getExpectedComments());
    }

    @Test
    void testParameterizedConstructorValidInput() throws CustomException.PostCreationException, CustomException.InvalidNumberException {
        Date date = new Date();
        Post post = new Post("Sample content", date, 100, 50, 20, 10, 1000, 800, "Campaign1");

        assertNotNull(post.getId()); // ID should be auto-generated
        assertEquals("Sample content", post.getContent());
        assertEquals(date, post.getDate());
        assertEquals(100, post.getExpectedLikes());
        assertEquals(50, post.getCurrentLikes());
        assertEquals(20, post.getPreviousLikes());
        assertEquals(10, post.getComments());
        assertEquals(1000, post.getCurrentFollowers());
        assertEquals(800, post.getPreviousFollowers());
        assertEquals("Campaign1", post.getCampaignName());
        assertNull(post.getInfluencerId());
        assertEquals(0, post.getExpectedComments());
    }

    @Test
    void testParameterizedConstructorInvalidInput() {
        assertThrows(CustomException.PostCreationException.class, () -> {
            new Post("", new Date(), 100, 50, 20, 10, 1000, 800, "Campaign1");
        });

        assertThrows(CustomException.InvalidNumberException.class, () -> {
            new Post("Valid content", new Date(), -1, 50, 20, 10, 1000, 800, "Campaign1");
        });
    }

    @Test
    void testSettersAndValidation() throws CustomException.InvalidNumberException {
        Post post = new Post();

        post.setContent("Updated content");
        assertEquals("Updated content", post.getContent());

        Date date = new Date();
        post.setDate(date);
        assertEquals(date, post.getDate());

        post.setExpectedLikes(200);
        assertEquals(200, post.getExpectedLikes());

        assertThrows(CustomException.InvalidNumberException.class, () -> post.setExpectedLikes(-10));

        post.setCurrentLikes(150);
        assertEquals(150, post.getCurrentLikes());

        assertThrows(CustomException.InvalidNumberException.class, () -> post.setCurrentLikes(-1));

        post.setPreviousLikes(100);
        assertEquals(100, post.getPreviousLikes());

        post.setComments(25);
        assertEquals(25, post.getComments());

        post.setCurrentFollowers(2000);
        assertEquals(2000, post.getCurrentFollowers());

        post.setPreviousFollowers(1500);
        assertEquals(1500, post.getPreviousFollowers());

        post.setCampaignName("New Campaign");
        assertEquals("New Campaign", post.getCampaignName());

        Influencer influencer = new Influencer("1", "Alice", "Instagram", 5000);
        post.setInfluencer(influencer);
        assertEquals("1", post.getInfluencerId());
        assertEquals(influencer, post.getInfluencer());
    }

    @Test
    void testToString() throws CustomException.InvalidNumberException {
        Post post = new Post();
        post.setContent("Test Content");
        post.setCampaignName("Test Campaign");
        post.setExpectedLikes(100);

        String expected = "Post{" +
                "id='" + post.getId() + '\'' +
                ", content='Test Content'" +
                ", date=" + post.getDate() +
                ", expectedLikes=100" +
                ", currentLikes=0" +
                ", previousLikes=0" +
                ", comments=0" +
                ", currentFollowers=0" +
                ", previousFollowers=0" +
                ", campaignName='Test Campaign'" +
                ", influencerId='null'" +
                ", expectedComments=0" +
                '}';

        assertEquals(expected, post.toString());
    }
}
