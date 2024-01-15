package com.hit.service;

import com.hit.dao.BillDaoImpl;
import com.hit.dm.Bill;

import java.util.ArrayList;

public class BillService {


    BillDaoImpl billDao;

    public BillService(BillDaoImpl billDao) {
        this.billDao = billDao;
    }

    public boolean payBill(Long billId){
        Bill bill = billDao.find(billId);
        if(bill == null)
            return false;
        bill.setPayed(true);
        billDao.save(bill);
        return true;
    }


    public Bill getBill(long billId) {
        Bill bill = billDao.find(billId);
        if(bill == null)
            return null;
        return bill;
    }

    public boolean addNewBill(Bill bill) {
        if(bill == null)
            return false;
        return billDao.save(bill);
    }

    public boolean removeBill(Bill bill) {
        if(bill == null)
            return false;
        return billDao.delete(bill);
    }

    public ArrayList<String> getAll(){
        return billDao.getAll();
    }

    public ArrayList<String> getIdFilteredBills(String id) {
        return billDao.getIdFilteredBills(id);
    }

    public ArrayList<String> getSumFilteredBills(double sum, String threshold) {
        return billDao.getSumFilteredBills(sum,threshold);
    }
}
