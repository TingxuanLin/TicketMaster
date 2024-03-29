package com.example.ticketmaster.db.mysql;

import com.example.ticketmaster.db.DBConnection;
import com.example.ticketmaster.entity.Item.ItemBuilder;
import com.example.ticketmaster.entity.Item;
import com.example.ticketmaster.extermal.TicketMasterAPI;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:22 AM
 */
 public class MySQLConnection implements DBConnection {

    private Connection conn;

    public MySQLConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance(); // 解决某些Java版本无法reflection
            conn = DriverManager.getConnection(MySQLDBUtil.URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setFavoriteItem(String userId, List<String> itemIds) {
        if (conn == null) {
            return;
        }
        try {
            String sql = "INSERT IGNORE INTO history (user_id, item_id) VALUES(?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (String itemId : itemIds) {
                stmt.setString(1, userId);
                stmt.setString(2, itemId);
                stmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsetFavoriteItem(String userId, List<String> itemIds) {
        if (conn == null) {
            return;
        }
        try {
            String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (String itemId : itemIds) {
                stmt.setString(1, userId);
                stmt.setString(2, itemId);
                stmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Item> getFavoriteItems(String userId) {
        if (conn == null) {
            return new HashSet<>();
        }
        Set<Item> favoriteItems = new HashSet<>();
        Set<String> itemIds = getFavoriteItemIds(userId);
        try {
            String sql = "SELECT * FROM items WHERE item_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (String itemId: itemIds) {
                stmt.setString(1, itemId);
                ResultSet rs = stmt.executeQuery();
                ItemBuilder builder = new ItemBuilder();
                while (rs.next()) {
                    builder.setItemId(rs.getString("item_id"));
                    builder.setName((rs.getString("name")));
                    builder.setAddress((rs.getString("address")));
                    builder.setImageUrl(rs.getString("image_url"));
                    builder.setUrl(rs.getString("url"));
                    builder.setCategories(getCategories(itemId));
                    builder.setDistance(rs.getDouble("distance"));
                    builder.setRating(rs.getDouble("rating"));

                    favoriteItems.add(builder.build());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteItems;
    }

    @Override
    public Set<String> getFavoriteItemIds(String userId) {
        if (conn == null) {
            return new HashSet<>();
        }

        Set<String> favoriteItemIds = new HashSet<>();

        try {
            String sql = "SELECT item_id from history where user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String itemId = rs.getString("item_id");
                favoriteItemIds.add(itemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteItemIds;
    }

    public Set<String> getCategories(String itemId) {
        if (conn == null) {
            return new HashSet<>();
        }

        Set<String> categories = new HashSet<>();

        try {
            String sql = "SELECT category FROM categories WHERE item_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, itemId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("categories"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public List<Item> searchItems(double lat, double lon, String term) {
        TicketMasterAPI tmAPI = new TicketMasterAPI();
        List<Item> items = tmAPI.search(lat, lon, term);
        for (Item item : items) {
            saveItem(item);
        }
        return items;
    }


    @Override
    public void saveItem(Item item) {
        if (conn == null) {
            return;
        }
        try {
            String sql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?, ?, ?)";
            // 1. avoid SQL injection
            // 2. more general
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, item.getItemId());
            stat.setString(2, item.getName());
            stat.setDouble(3, item.getRating());
            stat.setString(4, item.getAddress());
            stat.setString(5, item.getImageUrl());
            stat.setString(6, item.getUrl());
            stat.setDouble(7, item.getDistance());
            System.out.println(stat);
            stat.execute();

            sql = "INSERT IGNORE INTO categories VALUES(?, ?)";
            stat = conn.prepareStatement(sql);
            for (String category : item.getCategories()) {
                stat.setString(1, item.getItemId());
                stat.setString(2, category);
                stat.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String getFullName(String userId) {
        if (conn == null) {
            return null;
        }
        String name = "";
        try {
            String sql = "SELECT first_name, last_name FROM users WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                name = String.join("", rs.getString("first_name"),
                        rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public boolean verifyLogin(String userId, String password) {
        if (conn == null) {
            return false;
        }
        try {
            String sql = "SELECT user_id FROM users WHERE user_id = ? and password = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        MySQLConnection connection = new MySQLConnection();
    }
}

