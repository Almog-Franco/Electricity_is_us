package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hit.controller.BillController;
import com.hit.controller.CustomerController;
import com.hit.dm.Bill;
import com.hit.dm.Customer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HandleRequest implements Runnable {
    Gson gson = (new GsonBuilder()).create();
    Socket socket;
    BillController billController = new BillController();
    CustomerController customerController = new CustomerController();

    public HandleRequest(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            while (true){
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter out = new PrintWriter(new ObjectOutputStream(this.socket.getOutputStream()),true);
            Scanner scanner = new Scanner(in);
            String requestStr = scanner.nextLine().substring(6);
            Request request = gson.fromJson(requestStr,Request.class);
            String header = request.getHeader();

            if(header.contains("bill")){
                if(header.contains("pay")){
                    Bill bill = billController.getBill(Long.parseLong(request.getBody().get("id")));
                    if(billController.payBill(Long.parseLong(request.getBody().get("id"))) &&  customerController.payBill(Long.parseLong(bill.getOwnerId()))){
                        customerController.payBill(Long.parseLong(bill.getOwnerId()));
                        HashMap<String, String> payedBillBody = new HashMap<>();
                        payedBillBody.put("confirm","bill " + request.getBody().get("id")+ " was payed successfully");
                        Request response = new Request("bill/payed", payedBillBody);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                        }
                    else{
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "Bill not payed");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);

                    }
                }
                if(header.contains("get")){
                    Bill bill = billController.getBill(Long.parseLong(request.getBody().get("id")));
                    if(bill != null){
                        HashMap<String, String> foundBillDetails = new HashMap<>();
                        foundBillDetails.put("billDate", bill.getDate());
                        foundBillDetails.put("ownerId", bill.getOwnerId());
                        foundBillDetails.put("billSum", String.valueOf(bill.getSum()));
                        Request response = new Request("bill/found", foundBillDetails);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }else{
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "Bill not found");
                        Request response = new Request("null", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }

                }
                if(header.contains("Single")){
                    Bill bill = billController.getBill(Long.parseLong(request.getBody().get("id")));
                    if(bill != null && !bill.isPayed()){
                        HashMap<String, String> foundBillDetails = new HashMap<>();
                        foundBillDetails.put("result",bill.toString());
                        Request response = new Request("bill/found", foundBillDetails);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }else{
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "Bill not found");
                        Request response = new Request("null", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }

                }
                if(header.contains("add")) {
                    if (customerController.getCustomer(Long.parseLong(request.getBody().get("ownerId"))) != null
                            && billController.addBill(Long.parseLong(request.getBody().get("id")),
                                                        request.getBody().get("billDate"),
                                                        Double.parseDouble(request.getBody().get("billSum")),
                                                        request.getBody().get("ownerId"))) {
                        Bill bill = billController.getBill(Long.parseLong(request.getBody().get("id")));
                        if(customerController.addBillToCustomer(bill,Long.parseLong(request.getBody().get("ownerId")))){
                            HashMap<String, String> messages = new HashMap<>();
                            System.out.println("Bill added");
                            messages.put("message", "Bill added successfully");
                            Request response = new Request("success", messages);
                            String json = gson.toJson(response, Request.class);
                            out.println(json);
                        }
                    } else {
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "Bill addition failed");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                  }

                if(header.contains("delete")){
                    Bill bill = billController.getBill(Long.parseLong(request.getBody().get("id")));
                    if(bill != null) {
                        if(billController.removeBill(bill)){
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "Bill removed successfully");
                        Request response = new Request("success", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                        }
                    } else {
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "Bill deletion failed");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                }

                if(header.contains("all")){
                    ArrayList<String> allBills = billController.getAll();
                    if(allBills.size() > 0){
                        HashMap<String, String> body = new HashMap<>();
                        int i = 0;
                        for(String entry : allBills){
                            body.put(String.valueOf(i),entry);
                            i++;
                        }
                        Request response = new Request("success", body);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                    else {
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "No bills were found");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                }

                if(header.contains("billsById")){
                    ArrayList<String> allBills = billController.getIdFilteredBills(request.getBody().get("id"));
                    if(allBills.size() > 0){
                        HashMap<String, String> body = new HashMap<>();
                        int i = 0;
                        for(String entry : allBills){
                            body.put(String.valueOf(i),entry);
                            i++;
                        }
                        Request response = new Request("success", body);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                    else {
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "No bills were found");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                }

                if(header.contains("billsBySum")){
                    ArrayList<String> allBills = billController.getSumFilteredBills(Double.parseDouble(request.getBody().get("sum")),request.getBody().get("threshold"));
                    if(allBills.size() > 0){
                        HashMap<String, String> body = new HashMap<>();
                        int i = 0;
                        for(String entry : allBills){
                            body.put(String.valueOf(i),entry);
                            i++;
                        }
                        Request response = new Request("success", body);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                    else {
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "No bills were found");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                }


            } else if (header.contains("customer")) {
                if(header.contains("add")){
                    if(customerController.addNewCustomer(Long.parseLong(request.getBody().get("id")),
                            request.getBody().get("fullName"))){
                        HashMap<String,String> messages = new HashMap<>();
                        System.out.println("Customer added");
                        messages.put("message","Customer added successfully");
                        Request response = new Request("success",messages);
                        String json = gson.toJson(response,Request.class);
                        out.println(json);
                    }
                    else{
                        HashMap<String,String> messages = new HashMap<>();
                        messages.put("message","Customer addition failed");
                        Request response = new Request("failure",messages);
                        String json = gson.toJson(response,Request.class);
                        out.println(json);
                    }
                }
                if(header.contains("customersById")){
                    ArrayList<String> allCustomers = customerController.getIdFilteredBills(request.getBody().get("id"));
                    if(allCustomers.size() > 0){
                        HashMap<String, String> body = new HashMap<>();
                        int i = 0;
                        for(String entry : allCustomers){
                            body.put(String.valueOf(i),entry);
                            i++;
                        }
                        Request response = new Request("success", body);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                    else {
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "No customers were found");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                }

                if(header.contains("customersBySum")){
                    ArrayList<String> allCustomers = customerController.getSumFilteredBills(Double.parseDouble(request.getBody().get("sum")),request.getBody().get("threshold"));
                    if(allCustomers.size() > 0){
                        HashMap<String, String> body = new HashMap<>();
                        int i = 0;
                        for(String entry : allCustomers){
                            body.put(String.valueOf(i),entry);
                            i++;
                        }
                        Request response = new Request("success", body);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                    else {
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "No customers were found");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                }

                if(header.contains("all")){
                    ArrayList<String> allBills = customerController.getAll();
                    if(allBills.size() > 0){
                        HashMap<String, String> body = new HashMap<>();
                        int i = 0;
                        for(String entry : allBills){
                            body.put(String.valueOf(i),entry);
                            i++;
                        }
                        Request response = new Request("success", body);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                    else {
                        HashMap<String, String> messages = new HashMap<>();
                        messages.put("message", "No customers were found");
                        Request response = new Request("failure", messages);
                        String json = gson.toJson(response, Request.class);
                        out.println(json);
                    }
                }

            }
            else if(header.contains("stop")){
                out.close();
                in.close();
                this.socket.close();
                break;
            }

            }

        } catch (IOException var4) {
            System.out.println("Server error");
        }

    }
}
