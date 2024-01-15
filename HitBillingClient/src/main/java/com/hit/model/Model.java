package com.hit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Model implements Serializable {
    Client client = new Client();


    public Model() throws IOException, RuntimeException {

    }

    public void disconnect(){
        client.sendRequest("stop",new HashMap<>());
        client.disconnect();
    }

    public void sendRequest(String header, Map<String,String> body){
        if(!header.isEmpty() && !body.isEmpty()){
            client.sendRequest(header,body);

        }

    }

    public String getResponse() throws IOException, ClassNotFoundException {
       return client.readResponse();
    }

    public Request getResponseToRequest() throws IOException, ClassNotFoundException {
        String json = client.readResponse();
        Gson gson = (new GsonBuilder()).create();
        Request response = gson.fromJson(json, Request.class);
        return response;
    }

}
