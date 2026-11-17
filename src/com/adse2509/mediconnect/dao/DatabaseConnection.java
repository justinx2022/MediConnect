package com.adse2509.mediconnect.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class responsible for creating and supplying JDBC connections to the
 * ADSE2509_Hospital database hosted on Microsoft SQL Server.
 *
 * <p>
 * This follows the exact structure and comment style from the course reference
 * {@code com.adse2509.sess06_jdbc_intro.DatabaseConnection}.
 * </p>
 *
 * @author Group 3
 */
public class DatabaseConnection
{
    // Standard connection URL for local Microsoft SQL Server
    private static final String CONNECTION_URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=ADSE2509_Hospital;"
            + "encrypt=false;"
            + "trustServerCertificate=true;"
            + "loginTimeout=2";

    private static final String USERNAME = "sa";
    private static final String PASSWORD = "YourPassword123!";

    // Cached status of SQL Server availability to prevent repetitive connection delays
    private static Boolean serverAvailable = null;

    static
    {
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("[DatabaseConnection] Microsoft SQL Server JDBC Driver loaded successfully.");
        }
        catch (ClassNotFoundException cnfe)
        {
            System.err.println("""
                [DatabaseConnection WARNING] Microsoft JDBC Driver not found on classpath.
                Ensure mssql-jdbc-<version>.jar is added to the project dependencies.
                """);
        }
    }

    private DatabaseConnection()
    {
    }

    /**
     * Opens and returns a new connection to the ADSE2509_Hospital database.
     *
     * @return an open {@link Connection} to SQL Server
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException
    {
        if (Boolean.FALSE.equals(serverAvailable))
        {
            throw new SQLException("SQL Server marked offline. Operating in in-memory fallback mode.");
        }

        try
        {
            Connection connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
            serverAvailable = true;
            return connection;
        }
        catch (SQLException e)
        {
            serverAvailable = false;
            System.out.println("[DatabaseConnection] SQL Server offline: " + e.getMessage());
            System.out.println("[DatabaseConnection] Using synchronized memory repository for demonstration.");
            throw e;
        }
    }

    /**
     * Checks whether Microsoft SQL Server is currently accessible.
     *
     * @return true if database responds, false otherwise
     */
    public static boolean isServerAvailable()
    {
        if (serverAvailable != null)
        {
            return serverAvailable;
        }

        try (Connection test = getConnection())
        {
            return true;
        }
        catch (SQLException ex)
        {
            return false;
        }
    }
}
