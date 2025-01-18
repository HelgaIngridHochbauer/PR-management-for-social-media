package org.example.demo;

import org.example.demo.model.CustomException;
import org.example.demo.model.Influencer;
import org.example.demo.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InfluencerDAO {

    private final Connection connection;

    // Constructor that accepts a database connection
    public InfluencerDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to fetch the posts for a specific influencer
    public Post[] getPostsByInfluencerId(String influencerId) {
        String query = "SELECT * FROM posts WHERE influencerId = ?";

        // Estimate maximum posts (adjust this as per your application)
        Post[] posts = new Post[100];
        int count = 0;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, influencerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Create a Post object and populate its details from the ResultSet
                Post post = new Post();
                post.setId(rs.getString("id"));
                post.setContent(rs.getString("content"));
                post.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("date")));
                post.setCurrentLikes(rs.getInt("currentLikes"));
                post.setComments(rs.getInt("comments"));
                post.setCampaignName(rs.getString("campaignName"));


                // Add the post to the array
                posts[count] = post;
                count++;

                // Prevent exceeding array bounds
                if (count >= posts.length) {
                    break;
                }
            }

            // Trim the posts array to fit the actual used size
            Post[] trimmedPosts = new Post[count];
            for (int i = 0; i < count; i++) {
                trimmedPosts[i] = posts[i];
            }

            return trimmedPosts;

        } catch (SQLException | ParseException | CustomException.InvalidNumberException e) {
            e.printStackTrace();
        }

        return new Post[0]; // Return empty array if no posts are found or an exception occurs
    }

    // Method to fetch influencer details by name
    public Influencer getInfluencerByName(String name) {
        String query = "SELECT id, name, platform, followers FROM influencers WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name); // Use parameterized query to avoid SQL injection
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // If the influencer is found, populate an Influencer object
                return new Influencer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("platform"),
                        rs.getInt("followers")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle database exceptions
        }

        return null; // Return null if influencer not found
    }
    public ArrayList<Influencer> getAllInfluencers() {
        ArrayList<Influencer> influencers = new ArrayList<>();
        String query = "SELECT id, name, platform, followers FROM influencers";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Populate an Influencer object with database data and add to the list
                Influencer influencer = new Influencer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("platform"),
                        rs.getInt("followers")
                );
                influencers.add(influencer);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log SQL errors
        }

        return influencers;
    }
}