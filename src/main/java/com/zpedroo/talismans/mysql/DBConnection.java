package com.zpedroo.talismans.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    public static DBConnection getInstance() { return instance; }

    protected static final String TABLE = "talismans";

    private final HikariDataSource hikari = new HikariDataSource();
    private final DBManager dbManager = new DBManager();

    public DBConnection(FileConfiguration file) {
        instance = this;

        enable(file);
        getDBManager().createTable();
    }

    private void enable(FileConfiguration file) {
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", file.getString("MySQL.host"));
        hikari.addDataSourceProperty("port", file.getInt("MySQL.port"));
        hikari.addDataSourceProperty("databaseName", file.getString("MySQL.database"));
        hikari.addDataSourceProperty("user", file.getString("MySQL.username"));
        hikari.addDataSourceProperty("password", file.getString("MySQL.password"));
        hikari.setMaximumPoolSize(10);
    }

    public void closeConnection() {
        hikari.close();
    }

    public Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }

    public DBManager getDBManager() {
        return dbManager;
    }
}