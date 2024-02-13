package com.example.ticketmaster.db;

import com.example.ticketmaster.entity.Item;

import java.util.List;
import java.util.Set;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:21 AM
 */
public interface DBConnection {
    public void close();

    public void setFavoriteItem(String userId, List<String> itemId);

    public void unsetFavoriteItem(String userId, List<String> itemId);

    public Set<Item> getFavoriteItems(String userId);

    public Set<String> getFavoriteItemIds(String userId);

    public Set<String> getCategories(String itemId);

    public List<Item> searchItems(double lat, double lon, String term);

    public void saveItem(Item item);

    public String getFullName(String userId);

    public boolean verifyLogin(String username, String password);



}
