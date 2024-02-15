package com.example.ticketmaster.rpc;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:31 AM
 */

import com.example.ticketmaster.db.DBConnection;
import com.example.ticketmaster.db.DBConnectionFactory;
import com.example.ticketmaster.entity.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray array = new JSONArray();
        try {
            double lat = Double.parseDouble(request.getParameter("lat"));
            double lon = Double.parseDouble(request.getParameter("lon"));
            String keyword = request.getParameter("term");

            DBConnection connection = DBConnectionFactory.getConnection();
            List<Item> items = connection.searchItems(lat, lon, keyword);
            connection.close();

            for (Item item : items) {
                JSONObject obj = item.toJSONObject();
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
        doGet(request, response);
    }

}

