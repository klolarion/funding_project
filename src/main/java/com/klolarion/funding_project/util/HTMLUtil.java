package com.klolarion.funding_project.util;


public class HTMLUtil {
    /*상품검색결과에서 태그삭제*/
    public static String removeHtmlTags(String input) {
        return input.replaceAll("<[^>]*>", " ");
    }
}
