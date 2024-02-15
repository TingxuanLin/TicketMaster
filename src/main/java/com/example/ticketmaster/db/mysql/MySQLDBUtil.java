package com.example.ticketmaster.db.mysql;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:22 AM
 */
public class MySQLDBUtil {
    private static final String HOSTNAME = "localhost";
    private static final String PORT_NUM = "8889";
    private static final String DB_NAME = "laiproject";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    public static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT_NUM
            + "/" + DB_NAME + "?user=" + USERNAME + "&password=" + PASSWORD
            + "&autoReconnect=true&serverTimezone=UTC";
}
