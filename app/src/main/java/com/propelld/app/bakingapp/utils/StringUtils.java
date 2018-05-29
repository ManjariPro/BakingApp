package com.propelld.app.bakingapp.utils;

public class StringUtils
{
    public static boolean isNullOrWhiteSpace(String value)
    {
        if (value == null)
        {
            return true;
        }

        for (int i = 0; i < value.length(); i++)
        {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
