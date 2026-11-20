/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage database connection to SQL Server.
 * 
 * @author Group 3
 */
public class DatabaseConnection
{
    private static final String CONNECTION_URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=ADSE2509_Hospital;"
            + "encrypt=false;"
            + "trustServerCertificate=true;"
            + "loginTimeout=2";

    private static final String USERNAME = "sa";
    private static final String PASSWORD = "YourPassword123!";

    private static Boolean serverAvailable = null;

    static
    {
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch (ClassNotFoundException cnfe)
        {
            System.err.println("Microsoft JDBC Driver not found on classpath.");
        }
    }

    private DatabaseConnection()
    {
    }

    public static Connection getConnection() throws SQLException
    {
        if (Boolean.FALSE.equals(serverAvailable))
        {
            throw new SQLException("SQL Server is offline. Using local repository.");
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
            throw e;
        }
    }

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
