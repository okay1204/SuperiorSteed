package me.screescree.SuperiorSteed;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Database {
    private static final String DB_LOCATION = "plugins/SuperiorSteed";
    private static final String CONNECT_URL = "jdbc:sqlite:" + DB_LOCATION + "/storage.db";
    
    private Logger logger;
    private Connection conn;

    public Database() {
        logger = SuperiorSteed.getInstance().getLogger();

        writeDBPath();

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(CONNECT_URL);
            
            // create the table if they don't exist
            // String createTableString = "CREATE TABLE IF NOT EXISTS \"players\" ("
            // + "\"uuid\"	TEXT, "
            // + "\"deathCoordsVisible\"	INTEGER NOT NULL DEFAULT 1, "
            // + "\"deathCoordX\"	INTEGER, "
            // + "\"deathCoordY\"	INTEGER, "
            // + "\"deathCoordZ\"	INTEGER, "
            // + "\"deathCoordWorldUuid\"	TEXT, "
            // + "PRIMARY KEY(\"uuid\") "
            // + ");";
            // Statement statement = conn.createStatement();
            
            // statement.execute(createTableString);

            logger.info("Connected to database.");

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

    }
    
    public void writeDBPath() {
        File directory = new File(DB_LOCATION);
        if (!directory.exists()){
            directory.mkdirs();
        }
    }

    public void safeDisconnect() {
        if (conn != null) {
            try {
                conn.close();
                logger.info("Disconnected from database");
            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        return conn;
    }
}