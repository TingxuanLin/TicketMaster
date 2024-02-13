package com.example.ticketmaster.rpc;

import com.example.ticketmaster.db.DBConnection;
import com.example.ticketmaster.db.DBConnectionFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:30 AM
 */
public class itemHistory {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public itemHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject input = RpcHelper.readJsonObject(request);
            String user_id = input.getString("user_id");

            JSONArray array = input.getJSONArray("favorite");
            List<String> itemIds = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                itemIds.add(array.get(i).toString());
            }

            DBConnection conn = DBConnectionFactory.getConnection();
            conn.setFavoriteItem(user_id, itemIds);
            conn.close();

            RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet doDelete(HttpServletRequest, HttpServletResponse)
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject input = RpcHelper.readJsonObject(request);
            String user_id = input.getString("user_id");

            JSONArray array = input.getJSONArray("favorite");
            List<String> itemIds = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                itemIds.add(array.get(i).toString());
            }

            DBConnection conn = DBConnectionFactory.getConnection();
            conn.unsetFavoriteItem(user_id, itemIds);
            conn.close();

            RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
