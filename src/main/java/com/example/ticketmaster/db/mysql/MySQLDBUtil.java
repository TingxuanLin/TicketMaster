package com.example.ticketmaster.db.mysql;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:22 AM
 */
public class MySQLDBUtil {
    private static final String INSTANCE = "laiproject-instance.ckcrklezfvdz.us-east-2.rds.amazonaws.com";
    private static final String PORT_NUM = "3306";
    private static final String DB_NAME = "laiproject";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "Ltx727498";
    public static final String URL = "jdbc:mysql://" + INSTANCE + ":" + PORT_NUM
            + "/" + DB_NAME + "?user=" + USERNAME + "&password=" + PASSWORD
            + "&autoReconnect=true&serverTimezone=UTC";
}
