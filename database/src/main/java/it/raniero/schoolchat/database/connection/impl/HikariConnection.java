package it.raniero.schoolchat.database.connection.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.raniero.schoolchat.database.connection.IConnection;
import it.raniero.schoolchat.database.connection.utils.ConnectionDetails;


import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnection implements IConnection {


    private HikariDataSource dataSource;

    @Override
    public void initialize(ConnectionDetails details) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + details.getHostname() + ":" + details.getPort() + "/" + details.getDatabase());
        config.setUsername(details.getUsername());
        config.setPassword(details.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(6);
        dataSource = new HikariDataSource(config);

    }


    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}