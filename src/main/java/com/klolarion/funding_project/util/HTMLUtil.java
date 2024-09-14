package com.klolarion.funding_project.util;


public class HTMLUtil {
    public static String removeHtmlTags(String input) {
        return input.replaceAll("<[^>]*>", " ");
    }
}
