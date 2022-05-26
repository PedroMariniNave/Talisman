package com.zpedroo.talismans.mysql;

import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.PlayerData;
import com.zpedroo.talismans.objects.Talisman;
import com.zpedroo.talismans.utils.serialization.TalismanSerialization;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager extends TalismanSerialization {

    public void savePlayerData(PlayerData data) {
        executeUpdate("REPLACE INTO `" + DBConnection.TABLE + "` (`uuid`, `fragments_amount`, `equipped_talisman`, `unlocked_talismans`) VALUES " +
                "('" + data.getUniqueId() + "', " +
                "'" + data.getFragmentsAmount() + "', " +
                "'" + data.getEquippedTalisman().getName() + "', " +
                "'" + serializeTalismans(data.getUnlockedTalismans()) + "');");
    }

    public PlayerData getPlayerData(Player player) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `" + DBConnection.TABLE + "` WHERE `uuid`='" + player.getUniqueId() + "';";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                int fragmentsAmount = result.getInt(2);
                Talisman equippedTalisman = DataManager.getInstance().getTalismanByName(result.getString(3));
                List<Talisman> unlockedTalismans = deserializeTalismans(result.getString(4));

                return new PlayerData(player.getUniqueId(), fragmentsAmount, equippedTalisman, unlockedTalismans);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return new PlayerData(player.getUniqueId(), 0, null, new ArrayList<>(2));
    }

    private void executeUpdate(String query) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, null, null, statement);
        }
    }

    private void closeConnection(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void createTable() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `" + DBConnection.TABLE + "` (" +
                "`uuid` VARCHAR(255)," +
                "`fragments_amount` INTEGER," +
                "`equipped_talisman` VARCHAR(32)," +
                "`unlocked_talismans` VARCHAR(100)," +
                " PRIMARY KEY(`uuid`));");
    }

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
}