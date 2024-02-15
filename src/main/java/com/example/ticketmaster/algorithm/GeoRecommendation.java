package com.example.ticketmaster.algorithm;

import com.example.ticketmaster.db.DBConnection;
import com.example.ticketmaster.db.DBConnectionFactory;
import com.example.ticketmaster.entity.Item;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author lintingxuan
 * @create 2024-02-14 9:39 AM
 */
public class GeoRecommendation {

    public List<Item> recommendItems(String userId, double lat, double lon) {
        List<Item> recommendedItems = new ArrayList<>();
        DBConnection conn = DBConnectionFactory.getConnection();

        // Step 1 Get all favorite items
        Set<String> favoriteItemIds = conn.getFavoriteItemIds(userId);

        // Step 2 Get all categories of favorite items, sort by count
        Map<String, Integer> allCategories = new HashMap<>();
        for (String itemId : favoriteItemIds) {
            Set<String> categories = conn.getCategories(itemId);
            for (String category : categories) {
                allCategories.put(category, allCategories.getOrDefault(category, 0) + 1);
            }
        }

        List<Entry<String, Integer>> categoryList =
                new ArrayList<Entry<String, Integer>>(allCategories.entrySet());
        Collections.sort(categoryList, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        });


        // Step 3 do search based on category, filter out favorite events sorted by
        // distance
        Set<Item> visitedItems = new HashSet<>();

        for (Entry<String, Integer> category : categoryList) {
            List<Item> items = conn.searchItems(lat, lon, category.getKey());
            List<Item> filteredItems = new ArrayList<>();
            for (Item item : items) {
                if (!favoriteItemIds.contains(item.getItemId())
                    && !visitedItems.contains(item)) {
                    filteredItems.add(item);
                }
            }
            Collections.sort(filteredItems, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return Double.compare(o1.getDistance(), o2.getDistance());
                }
            });
        }

        return recommendedItems;
    }
}
