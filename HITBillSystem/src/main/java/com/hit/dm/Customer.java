package com.hit.dm;

import com.hit.controller.BillController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    long id;
    String fullName;
    double balance = 0;
    List<Long> billIds = new ArrayList();
    int unpayedBills = 0;

    public Customer(long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Long> getBillIds() {
        return billIds;
    }

    public boolean addBill(Bill bill){
        if(bill != null){
            billIds.add(bill.getId());
            this.balance += bill.getSum();
            this.unpayedBills++;
            return true;
        }

        return false;
    }

    public int getUnpayedBills(){
        return this.unpayedBills;
    }

    public String toString(){
        String customer = "" + this.id +","+ this.fullName +","+ this.balance +","+ this.billIds.size() + "," +this.unpayedBills;
        return customer;
    }


    public void reduceUnpayedBills() {
        if(this.unpayedBills == 0){
            return;
        }
        else{
            this.unpayedBills--;
        }
    }
}
