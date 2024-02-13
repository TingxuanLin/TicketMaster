package com.example.ticketmaster.rpc;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:30 AM
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        JSONArray array = new JSONArray();
        JSONObject o1 = new JSONObject();
        JSONObject o2 = new JSONObject();

        try {
            array.put(o1.put("name", "abcd").put("address", "San Francisco").put("time", "01/01/2017"));
            array.put(o2.put("name", "1234").put("address", "San Jose").put("time", "01/02/2017"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
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
