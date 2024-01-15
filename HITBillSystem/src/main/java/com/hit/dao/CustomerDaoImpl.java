package com.hit.dao;

import com.hit.dm.Bill;
import com.hit.dm.Customer;
import main.java.com.hit.algorithim.KMP;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerDaoImpl implements com.hit.dao.IDao<Long, Customer> {

    public static final String FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\customer.txt\\";

    public CustomerDaoImpl() {
        try {
            boolean emptyData = readData();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            if(emptyData) {
                objectOutputStream.writeObject(new HashMap<Long, Customer>());
                objectOutputStream.flush();
            }
            objectOutputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getAll() {
        ArrayList<String> customersArr = new ArrayList();
        HashMap<Long, Customer> allCustomers;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            allCustomers = (HashMap<Long, Customer>) objectInputStream.readObject();
            for(Map.Entry<Long, Customer> set: allCustomers.entrySet()){
                customersArr.add(set.getValue().toString());
            }
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return customersArr;
    }

    @Override
    public Customer find(Long id) throws IllegalArgumentException {
        HashMap<Long, Customer> customers = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            customers = (HashMap<Long, Customer>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return customers.get(id);
    }

    @Override
    public boolean save(Customer customer) throws IllegalArgumentException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            Object foundObj = objectInputStream.readObject();
            HashMap<Long, Customer> customers = (HashMap<Long, Customer>) foundObj;
            customers.put(customer.getId(), customer);
            objectInputStream.close();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            objectOutputStream.writeObject(customers);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<Customer> getCustomers(String name){
        ArrayList<Customer> customersList = new ArrayList<>();
        KMP kmp = new KMP();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            HashMap<Long, Customer> customers = (HashMap<Long, Customer>) objectInputStream.readObject();
            objectInputStream.close();
            for(Customer customer: customers.values()){
                if(kmp.isPatternInText(name,customer.getFullName())){
                    customersList.add(customer);
                }
            }

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            objectOutputStream.writeObject(customers);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }


        return customersList;
    }

    private boolean readData() throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        return file.exists() || file.length() == 0;
    }

    public ArrayList<String> getIdFilteredBills(String id) {
        ArrayList<String> customersArr = new ArrayList();
        KMP kmp = new KMP();
        HashMap<Long, Customer> allCustomers;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            allCustomers = (HashMap<Long, Customer>) objectInputStream.readObject();
            for(Map.Entry<Long, Customer> set: allCustomers.entrySet()){
                if(kmp.isPatternInText(id,String.valueOf(set.getValue().getId()))) {
                    customersArr.add(set.getValue().toString());
                }
            }
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return customersArr;
    }

    public ArrayList<String> getSumFilteredBills(double sum, String threshold) {
        ArrayList<String> customersArr = new ArrayList();
        HashMap<Long, Customer> allCustomers;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            allCustomers = (HashMap<Long, Customer>) objectInputStream.readObject();
            for(Map.Entry<Long, Customer> set: allCustomers.entrySet()){
                if(threshold.equals("greater than")){
                    if(set.getValue().getBalance() > sum){
                        customersArr.add(set.getValue().toString());
                    }
                }
                if(threshold.equals("lower than")){
                    if(set.getValue().getBalance() < sum){
                        customersArr.add(set.getValue().toString());
                    }
                }
                if(threshold.equals("equals")){
                    if(set.getValue().getBalance() == sum){
                        customersArr.add(set.getValue().toString());
                    }
                }

            }
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return customersArr;
    }
}
