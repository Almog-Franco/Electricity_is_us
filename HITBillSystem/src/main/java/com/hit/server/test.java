package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hit.controller.CustomerController;
import com.hit.dm.Bill;
import com.hit.dm.Customer;

import java.lang.reflect.Type;
import java.util.HashMap;

public class test {
    public static void main(String[] args) {

        HashMap<Long, String> bills = new HashMap<>();
        bills.put(1234L,(new Bill(1234L,"2023",24.5,"315313288")).toString());
        bills.put(2345L,(new Bill(2345L,"2023",24.5,"315313288")).toString());
        bills.put(3456L,(new Bill(3456L,"2023",24.5,"315313288")).toString());
        bills.put(4567L,(new Bill(4567L,"2023",24.5,"315313288")).toString());
        String test = bills.toString();
        System.out.println(test);
        Gson gson = (new GsonBuilder()).create();
        Type type = new TypeToken<HashMap<Long,Bill>>(){}.getType();
        HashMap bills2 = gson.fromJson(test,HashMap.class);

        System.out.println(bills2.get(1234L));

    }
}
