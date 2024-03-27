package com.example.roommateapp.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperMethods {

    public static ArrayList<String> parseStringToList(String input) {
        ArrayList<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String number = matcher.group();
            list.add(number);
        }

        return list;
    }
}
