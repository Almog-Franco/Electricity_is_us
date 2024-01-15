package com.hit.view;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class test {

    public static void main(String[] args) {
        Object arr = "315313288,2023-06-08,315313288,false";
        String test = arr.toString();
        System.out.println(arr instanceof String);
        System.out.println(test);
        System.out.println(test.split(",")[0]);
    }
}
