package org.example.demo.model;

import org.example.demo.utils.InputDevice;
import org.example.demo.utils.OutputDevice;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Application1Test {

    @Test
    public void testApplication1Constructor() {
        // Arrange: Mock the InputDevice and OutputDevice
        InputDevice inputDevice = new InputDevice();  // Replace with a mock if needed
        OutputDevice outputDevice = new OutputDevice();  // Replace with a mock if needed

        // Act: Create an instance of Application1
        Application1 application1 = new Application1(inputDevice, outputDevice);

        // Assert: Verify the fields are initialized correctly
        assertNotNull(application1.getInfluencers(), "Influencers list should be initialized.");
        assertNotNull(application1.getCampaigns(), "Campaigns map should be initialized.");
        assertEquals(0, application1.getCurrentFollowers(), "Initial currentFollowers should be 0.");
        assertEquals(0, application1.getPreviousFollowers(), "Initial previousFollowers should be 0.");
    }
}