package com.example.ticketmaster.db;

import com.example.ticketmaster.db.mysql.MySQLConnection;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:21 AM
 */
public class DBConnectionFactory {
    private static final String DEFAULT_DB = "mysql";

    public static DBConnection getConnection(String db) {
        switch(db) {
            case "mysql":
                return new MySQLConnection();
            case "mongodb":
//			return new MongoDBConnection();
                return null;
            default:
                throw new IllegalArgumentException("Invalid db: " + db);

        }
    }

    public static DBConnection getConnection() {
        return getConnection(DEFAULT_DB);
    }
}

