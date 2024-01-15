package com.hit.model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Client {
    private Socket clientSocket= new Socket("localhost",12345);
    private ObjectOutputStream objectOutputStream;
    private InputStreamReader inputSteamReader;

    public Client() throws IOException {

    }

    public void sendRequest(String action,Map<String, String> body){
        try{
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            Gson gson = (new GsonBuilder()).create();
            Request request = new Request(action,body);
            String json = gson.toJson(request,Request.class);
            PrintWriter out = new PrintWriter(objectOutputStream,true);
            out.println(json);
        }catch(Exception re){
            re.printStackTrace();
        }

    }

    public String readResponse() throws IOException {
        inputSteamReader = new InputStreamReader(clientSocket.getInputStream());
        BufferedReader in = new BufferedReader(inputSteamReader);
        Scanner scanner = new Scanner(in);
        String json = scanner.nextLine();
        return json.substring(json.indexOf("{"));
    }

    public void disconnect(){
        try{
            if(clientSocket.isConnected()){
                clientSocket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
