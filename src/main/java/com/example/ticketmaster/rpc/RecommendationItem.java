package com.example.ticketmaster.rpc;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:30 AM
 */

import com.example.ticketmaster.algorithm.GeoRecommendation;
import com.example.ticketmaster.db.DBConnection;
import com.example.ticketmaster.db.DBConnectionFactory;
import com.example.ticketmaster.entity.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.json.*;

/**
 * Servlet implementation class RecommandationItem
 */
@WebServlet("/recommendation")
public class RecommendationItem extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecommendationItem() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        JSONArray array = new JSONArray();
        try {
            String userId = request.getParameter("user_id");
            double lat = Double.parseDouble(request.getParameter("lat"));
            double lon = Double.parseDouble(request.getParameter("lon"));
            String keyWord = request.getParameter("term");

            DBConnection conn = DBConnectionFactory.getConnection();
            List<Item> items = conn.searchItems(lat, lon, keyWord);
            conn.close();

            Set<String> favorite = conn.getFavoriteItemIds(userId);

            for (Item item : items) {
                JSONObject obj = item.toJSONObject();

                obj.put("favorite", favorite.contains(item.getItemId()));
                array.put(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RpcHelper.writeJsonArray(response, array);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
