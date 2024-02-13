package com.example.ticketmaster.extermal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.ticketmaster.entity.Item;
import org.json.*;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:29 AM
 */
public class TicketMasterAPI {
    //{"address":"","distance":0,"item_id":"Z7r9jZ1AdJ9AK",
    //"image_url":"https://s1.ticketm.net/dam/a/c70/98044e71-9085-471b-9f21-78525c745c70_1340061_RECOMENDATION_16_9.jpg",
    //"name":"Minnesota Timberwolves vs. Phoenix Suns","rating":0,
    //"categories":["Sports"],"url":"https://www.ticketmaster.com/event/Z7r9jZ1AdJ9AK"}


    private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    //wy3f383GNqgVB2tV
    private static final String  DEFAULT_KEYWORD = "";

    private static final String API_KEY = "s6wDg9Ia0eWFrgzvSO5Assvyto9ReH2W";

    private String getAddress(JSONObject event) throws JSONException {
        if (!event.isNull("_embedded")) {
            JSONObject embedded = event.getJSONObject("_embedded");
            if (!event.isNull("venues")) {
                JSONArray venues = embedded.getJSONArray("venues");
                for (int i = 0; i < venues.length(); i++) {
                    JSONObject venue = venues.getJSONObject(i);
                    StringBuilder sb = new StringBuilder();
                    if (!venue.isNull("address")) {
                        JSONObject address = venue.getJSONObject("address");

                        if (!address.isNull("line1")) {
                            sb.append(address.getString("line1"));
                        }
                        if (!address.isNull("line2")) {
                            sb.append(" ");
                            sb.append(address.getString("line2"));
                        }
                        if (!address.isNull("line3")) {
                            sb.append(" ");
                            sb.append(address.getString("line3"));
                        }
                    }
                    if (!venue.isNull("city")) {
                        JSONObject city = venue.getJSONObject("city");

                        if (!city.isNull("name")) {
                            sb.append(" ");
                            sb.append(city.getString("name"));
                        }
                    }
                    if (sb.length() > 0) {
                        return sb.toString();
                    }
                }
            }
        }
        return "";
    }

    private String getImageUrl(JSONObject event) throws JSONException {
        if (!event.isNull("images")) {
            JSONArray images = event.getJSONArray("images");
            for (int i = 0; i < images.length(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (!image.isNull("url")) {
                    return image.getString("url");
                }
            }
        }
        return "";
    }

    private Set<String> getCategories(JSONObject event) throws JSONException {
        Set<String> categories  = new HashSet<>();
        if (!event.isNull("classifications")) {
            JSONArray classifications = event.getJSONArray("classifications");
            for (int i = 0; i < classifications.length(); i++) {
                JSONObject classification = classifications.getJSONObject(i);
                if (!classification.isNull("segment")) {
                    JSONObject segment = classification.getJSONObject("segment");
                    if (!segment.isNull("name")) {
                        categories.add(segment.getString("name"));
                    }
                }
            }
        }
        return categories;
    }

    private List<Item> getItemList(JSONArray array) throws JSONException {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);

            Item.ItemBuilder builder = new Item.ItemBuilder();

            if (!item.isNull("name")) {
                builder.setName(item.getString("name"));
            }
            if (!item.isNull("id")) {
                builder.setItemId(item.getString("id"));
            }
            if (!item.isNull("url")) {
                builder.setUrl(item.getString("url"));
            }
            if (!item.isNull("rating")) {
                builder.setRating(item.getDouble("rating"));
            }
            if (!item.isNull("distance")) {
                builder.setDistance(item.getDouble("distance"));
            }
            builder.setAddress(getAddress(item));
            builder.setCategories(getCategories(item));
            builder.setImageUrl(getImageUrl(item));
            itemList.add(builder.build());
        }
//		System.out.println("getItemList size: " + itemList.size());
        return itemList;
    }

    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }

        // 1. encoding keyword
        try {
            keyword = java.net.URLEncoder.encode(keyword, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 2. combine url with INFOs
        String geoHash = GeoHash.encode(lat, lon, 8);
        String query = String.format("apikey=%s&geoPoint%s&keyword=%s&radius=%s",
                API_KEY, geoHash, keyword, 50);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "?" + query)
                    .openConnection();
            int responseCode = connection.getResponseCode();

            // debug
            System.out.println("\nSending 'GET' request to URL: " + URL + "?" + query);
            System.out.println("Response code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputline;
            StringBuilder response = new StringBuilder();
            while ((inputline = in.readLine()) != null) {
                response.append(inputline);
            }
            in.close();

            JSONObject object = new JSONObject(response.toString());
            if (object.isNull("_embedded")) {
                return new ArrayList<>();
            }

            JSONObject embedded = object.getJSONObject("_embedded");
            JSONArray events = embedded.getJSONArray("events");
//			System.out.println(events.length());
            return getItemList(events);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * test search api
     * @param lat
     * @param lon
     */
    private void queryAPI(double lat, double lon) {
        List<Item> events = search(lat, lon, null);
        try {
            for (Item event: events) {
                System.out.println(event.toJSONObject());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TicketMasterAPI tmApi = new TicketMasterAPI();
        tmApi.queryAPI(29.682684, -95.295410);
    }
}
