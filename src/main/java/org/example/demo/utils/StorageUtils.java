package org.example.demo.utils;

import org.example.demo.model.CustomException;
import org.example.demo.model.Influencer;
import org.example.demo.model.Post;
import org.example.demo.DatabaseConnection;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class StorageUtils {

    // Save all data to the database
    public static void saveData(List<Influencer> influencers, Map<String, List<Post>> campaigns) {
        try {
            // Step 1: Save influencers first
            saveInfluencers(influencers);

            // Step 2: Save all campaigns
            saveCampaign(campaigns.keySet());

            // Step 3: Flatten posts from campaigns into a list and save them
            List<Post> posts = campaigns.values()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            savePosts(posts);

        } catch (Exception e) {
            System.err.println("Error in saveData method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load all data from the database into provided collections
    public static void loadData(List<Influencer> influencers, List<Post> posts, Map<String, List<Post>> campaigns) {
        // Clear the provided collections
        influencers.clear();
        posts.clear();
        campaigns.clear();

        // Load influencers
        loadInfluencers(influencers);

        // Load posts
        loadPosts(posts);

        // Rebuild campaigns map from the posts list
        for (Post post : posts) {
            campaigns.computeIfAbsent(post.getCampaignName(), k -> new ArrayList<>()).add(post);
        }

        // Load all campaign names and ensure campaigns with no posts are included
        Set<String> campaignNames = loadCampaign();
        for (String campaignName : campaignNames) {
            campaigns.putIfAbsent(campaignName, new ArrayList<>());
        }
    }

    // Utility to save influencers into the database
    private static void saveInfluencers(List<Influencer> influencers) {
        String query = "INSERT INTO Influencers (id, name, platform, followers) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name = VALUES(name), platform = VALUES(platform), followers = VALUES(followers)";

        try (Connection connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(query)) {
            for (Influencer influencer : influencers) {
                try {
                    System.out.println("Saving influencer: " + influencer); // Debug influencer
                    preparedStatement.setString(1, influencer.getId());
                    preparedStatement.setString(2, influencer.getName());
                    preparedStatement.setString(3, influencer.getPlatform());
                    preparedStatement.setInt(4, influencer.getFollowers());
                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    System.err.println("Error while saving influencer: " + influencer);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error while saving influencers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Utility to save posts into the database
    private static void savePosts(List<Post> posts) {
        ExecutorService executorService = Executors.newFixedThreadPool(4); // Use 4 threads, adjust as needed

        try {
            List<Future<?>> futures = new ArrayList<>(); // Keep track of submitted tasks

            for (Post post : posts) {
                futures.add(executorService.submit(() -> { // Submit each post save as a task
                    try (Connection connection = DatabaseConnection.getConnection();
                         var preparedStatement = connection.prepareStatement(
                                 "INSERT INTO Posts (id, content, date, expectedLikes, currentLikes, previousLikes, comments, " +
                                         "currentFollowers, previousFollowers, campaignName, influencerId, expectedComments) " +
                                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                         "ON DUPLICATE KEY UPDATE content = VALUES(content), date = VALUES(date), expectedLikes = VALUES(expectedLikes), " +
                                         "currentLikes = VALUES(currentLikes), previousLikes = VALUES(previousLikes), comments = VALUES(comments), " +
                                         "currentFollowers = VALUES(currentFollowers), previousFollowers = VALUES(previousFollowers), " +
                                         "campaignName = VALUES(campaignName), influencerId = VALUES(influencerId), expectedComments = VALUES(expectedComments)"
                         )) {

                        // Save the main post data
                        System.out.println("Saving post with ID: " + post.getId());
                        preparedStatement.setString(1, post.getId());
                        preparedStatement.setString(2, post.getContent());

                        if (post.getDate() != null) {
                            preparedStatement.setTimestamp(3, new java.sql.Timestamp(post.getDate().getTime()));
                        } else {
                            preparedStatement.setTimestamp(3, null);
                        }

                        preparedStatement.setInt(4, post.getExpectedLikes());
                        preparedStatement.setInt(5, post.getCurrentLikes());
                        preparedStatement.setInt(6, post.getPreviousLikes());
                        preparedStatement.setInt(7, post.getComments());
                        preparedStatement.setInt(8, post.getCurrentFollowers());
                        preparedStatement.setInt(9, post.getPreviousFollowers());
                        preparedStatement.setString(10, post.getCampaignName());
                        preparedStatement.setString(11, post.getInfluencerId());
                        preparedStatement.setInt(12, post.getExpectedComments());

                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        System.err.println("Error while saving post: " + post.getId());
                        e.printStackTrace();
                    }
                }));
            }

            // Wait for all tasks to complete
            for (Future<?> future : futures) {
                future.get(); // Ensures task completion and propagates exceptions
            }
        } catch (Exception e) {
            System.err.println("Error while saving posts: " + e.getMessage());
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // Shutdown the thread pool
        }
    }

    // Utility to save campaigns into the database
    private static void saveCampaign(Set<String> campaignNames) {
        String query = "INSERT INTO Campaigns (campaignName) VALUES (?) " +
                "ON DUPLICATE KEY UPDATE campaignName = VALUES(campaignName)";

        try (Connection connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(query)) {

            for (String campaign : campaignNames) {
                try {
                    System.out.println("Saving campaign: " + campaign);
                    preparedStatement.setString(1, campaign);
                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    System.err.println("Error saving campaign: " + campaign);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error while saving campaigns: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Utility to load influencers from the database
    private static void loadInfluencers(List<Influencer> influencers) {
        String query = "SELECT * FROM Influencers";

        try (Connection connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Influencer influencer = new Influencer(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("platform"),
                        resultSet.getInt("followers")
                );
                influencers.add(influencer);
            }
        } catch (Exception e) {
            System.err.println("Error while loading influencers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Utility to load an influencer by their ID from the database
    private static Influencer loadInfluencerById(String influencerId) {
        String query = "SELECT * FROM Influencers WHERE id = ?";
        Influencer influencer = null;

        try (Connection connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, influencerId);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Create and populate the influencer object
                    influencer = new Influencer(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("platform"),
                            resultSet.getInt("followers")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error while loading influencer with ID " + influencerId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return influencer;
    }

    // Utility to load posts from the database into a list
    private static void loadPosts(List<Post> posts) {
        String query = "SELECT * FROM Posts";

        try (Connection connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                try {
                    // Create a Post object using the parameterized constructor
                    Post post = new Post(
                            resultSet.getString("content"),
                            resultSet.getTimestamp("date"), // SQL DATETIME converted to Java Date
                            resultSet.getInt("expectedLikes"),
                            resultSet.getInt("currentLikes"),
                            resultSet.getInt("previousLikes"),
                            resultSet.getInt("comments"),
                            resultSet.getInt("currentFollowers"),
                            resultSet.getInt("previousFollowers"),
                            resultSet.getString("campaignName")
                    );

                    // Set additional properties for the Post
                    post.setId(resultSet.getString("id"));
                    post.setInfluencerId(resultSet.getString("influencerId"));
                    post.setExpectedComments(resultSet.getInt("expectedComments"));

                    // Set the Influencer object if available
                    String influencerId = resultSet.getString("influencerId");
                    if (influencerId != null) {
                        Influencer influencer = loadInfluencerById(influencerId);
                        if (influencer != null) {
                            post.setInfluencer(influencer);
                        } else {
                            System.err.println("No influencer found with ID: " + influencerId);
                        }
                    }

                    // Add the fully constructed Post to the list
                    posts.add(post);
                } catch (CustomException.PostCreationException | CustomException.InvalidNumberException e) {
                    System.err.println("Error while creating Post object: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error while loading posts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Utility to load campaigns from the database into a Set of Strings
    private static Set<String> loadCampaign() {
        String query = "SELECT campaignName FROM Campaigns";
        Set<String> campaigns = new HashSet<>();

        try (Connection connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                campaigns.add(resultSet.getString("campaignName"));
            }
        } catch (Exception e) {
            System.err.println("Error while loading campaigns: " + e.getMessage());
            e.printStackTrace();
        }

        return campaigns;
    }
}