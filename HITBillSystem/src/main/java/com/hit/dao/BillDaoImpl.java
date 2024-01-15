package com.hit.dao;

import com.hit.dm.Bill;
import main.java.com.hit.algorithim.KMP;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillDaoImpl implements IDao<Long, Bill> {

    public static final String FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\bill.txt";
    CustomerDaoImpl customerDaoImpl = new CustomerDaoImpl();

    public BillDaoImpl() {
        try {
            boolean emptyData = readData();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            if(emptyData) {
                objectOutputStream.writeObject(new HashMap<Long, Bill>());
                objectOutputStream.flush();
            }
            objectOutputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean delete(Bill bill) {
        HashMap bills;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            bills = (HashMap)objectInputStream.readObject();
            bills.remove(bill.getId());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            objectOutputStream.writeObject(bills);
            objectOutputStream.flush();
            objectOutputStream.close();
            return true;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Bill find(Long id) throws IllegalArgumentException {
        HashMap<Long, Bill> bills = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            bills = (HashMap<Long, Bill>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return bills.get(id);
    }

    @Override
    public boolean save(Bill bill) throws IllegalArgumentException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            Object foundObj = objectInputStream.readObject();
            HashMap<Long, Bill> bills = (HashMap<Long, Bill>) foundObj;
            bills.put(bill.getId(), bill);
            objectInputStream.close();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(FILE_PATH)));
            objectOutputStream.writeObject(bills);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }
        return true;
    }

    public ArrayList<String> getAll(){
        ArrayList<String> billsArr = new ArrayList();
        HashMap<Long, Bill> allBills;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            allBills = (HashMap<Long, Bill>) objectInputStream.readObject();
            for(Map.Entry<Long, Bill> set: allBills.entrySet()){
                billsArr.add(set.getValue().toString());
            }
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return billsArr;
    }

    private boolean readData() throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        return file.exists() || file.length() == 0;
    }

    public ArrayList<String> getIdFilteredBills(String id) {
        ArrayList<String> billsArr = new ArrayList();
        KMP kmp = new KMP();
        HashMap<Long, Bill> allBills;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            allBills = (HashMap<Long, Bill>) objectInputStream.readObject();
            for(Map.Entry<Long, Bill> set: allBills.entrySet()){
                if(kmp.isPatternInText(id,set.getValue().getOwnerId())) {
                    billsArr.add(set.getValue().toString());
                }
            }
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return billsArr;
    }

    public ArrayList<String> getSumFilteredBills(double sum, String threshold) {
        ArrayList<String> billsArr = new ArrayList();
        HashMap<Long, Bill> allBills;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
            allBills = (HashMap<Long, Bill>) objectInputStream.readObject();
            for(Map.Entry<Long, Bill> set: allBills.entrySet()){
                if(threshold.equals("greater than")){
                    if(set.getValue().getSum() > sum){
                        billsArr.add(set.getValue().toString());
                    }
                }
                if(threshold.equals("lower than")){
                    if(set.getValue().getSum() < sum){
                        billsArr.add(set.getValue().toString());
                    }
                }
                if(threshold.equals("equals")){
                    if(set.getValue().getSum() == sum){
                        billsArr.add(set.getValue().toString());
                    }
                }

            }
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return billsArr;
    }
}
