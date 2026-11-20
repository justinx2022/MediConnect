/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.utils;

/**
 * Utility helper for field validation and data conversion.
 * 
 * @author Group 3
 */
public class ValidationHelper
{
    private ValidationHelper()
    {
    }

    public static boolean isNotEmpty(String value)
    {
        return value != null && !value.trim().isEmpty();
    }

    public static int parseIntegerOrDefault(String str, int defaultVal)
    {
        if (str == null || str.trim().isEmpty())
        {
            return defaultVal;
        }
        try
        {
            return Integer.parseInt(str.trim());
        }
        catch (NumberFormatException e)
        {
            return defaultVal;
        }
    }

    public static double parseDoubleOrDefault(String str, double defaultVal)
    {
        if (str == null || str.trim().isEmpty())
        {
            return defaultVal;
        }
        try
        {
            return Double.parseDouble(str.trim());
        }
        catch (NumberFormatException e)
        {
            return defaultVal;
        }
    }
}
