package me.screescree.SuperiorSteed;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.screescree.SuperiorSteed.commands.HorsePerms;

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
            String createTableString = """
                CREATE TABLE IF NOT EXISTS "HorsePermissions" (
                    "ownerUuid"	TEXT NOT NULL,
                    "recieverUuid"	TEXT NOT NULL,
                    UNIQUE ("ownerUuid", "recieverUuid")
                );
            """;
            Statement statement = conn.createStatement();
            
            statement.execute(createTableString);

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

    private UUID stringToUUID(String uuid) {
        if (uuid == null) {
            return null;
        }
        else {
            return UUID.fromString(uuid);
        }
    }

    public int getNumRecievers(UUID ownerUuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM HorsePermissions WHERE ownerUuid = ?");
            statement.setString(1, ownerUuid.toString());

            ResultSet result = statement.executeQuery();
            
            result.next();

            return result.getInt(1);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return 0;
        }
    }

    public ArrayList<OfflinePlayer> getRecievers(UUID ownerUuid, int page) {
        try {
            PreparedStatement statement = conn.prepareStatement("""
                SELECT recieverUuid
                FROM HorsePermissions
                WHERE ownerUuid = ?
                LIMIT ?
                OFFSET ?;
            """);
            statement.setString(1, ownerUuid.toString());
            statement.setInt(2, HorsePerms.PEOPLE_PER_PAGE);
            statement.setInt(3, (page - 1) * HorsePerms.PEOPLE_PER_PAGE);

            ResultSet resultSet = statement.executeQuery();

            ArrayList<OfflinePlayer> recievers = new ArrayList<>();

            UUID recieverUuid;
            while (resultSet.next()) {
                recieverUuid = stringToUUID(resultSet.getString("recieverUuid"));
                recievers.add(Bukkit.getOfflinePlayer(recieverUuid));
            }

            return recievers;

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return null;
        }
    }

    public boolean addReciever(UUID ownerUuid, UUID recieverUuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO HorsePermissions (ownerUuid, recieverUuid)
                VALUES (?, ?);
            """);
            statement.setString(1, ownerUuid.toString());
            statement.setString(2, recieverUuid.toString());

            statement.execute();
            return true;
        }
        catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
            return false;
        }
    }

    public boolean removeReciever(UUID ownerUuid, UUID recieverUuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("""
                DELETE FROM HorsePermissions
                WHERE ownerUuid = ? AND recieverUuid = ?;
            """);
            statement.setString(1, ownerUuid.toString());
            statement.setString(2, recieverUuid.toString());

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
            return false;
        }
    }

    public boolean hasPermission(UUID ownerUuid, UUID recieverUuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("""
                SELECT *
                FROM HorsePermissions
                WHERE ownerUuid = ? AND recieverUuid = ?;
            """);
            statement.setString(1, ownerUuid.toString());
            statement.setString(2, recieverUuid.toString());

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return false;
        }
    }
}