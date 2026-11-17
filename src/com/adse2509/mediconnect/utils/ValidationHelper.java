package com.adse2509.mediconnect.utils;

/**
 * Utility helper validating inputs for Patient, Doctor, and Billing entries.
 *
 * @author Group 3
 */
public class ValidationHelper
{
    private ValidationHelper()
    {
    }

    public static boolean isNotEmpty(String text)
    {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidAge(int age)
    {
        return age >= 0 && age <= 125;
    }

    public static boolean isValidMetric(double val)
    {
        return val > 0.0 && val <= 500.0;
    }

    public static boolean isValidPhone(String phone)
    {
        return isNotEmpty(phone) && phone.replaceAll("[^0-9+]", "").length() >= 7;
    }

    public static boolean isValidDiscount(double discount)
    {
        return discount >= 0.0 && discount <= 100.0;
    }
}
