package com.hit.controller;

import com.hit.dao.CustomerDaoImpl;
import com.hit.dm.Bill;
import com.hit.dm.Customer;
import com.hit.service.CustomerService;

import java.util.ArrayList;

public class CustomerController {

    CustomerService customerService = new CustomerService(new CustomerDaoImpl());

    public CustomerController() {}

    public boolean addNewCustomer(long id,String fullName) {
        return customerService.addNewCustomer(new Customer(id,fullName));
    }

    public Customer getCustomer(long id){
        return customerService.getCustomer(id);
    }

    public boolean addBillToCustomer(Bill bill, long customerId){
        return customerService.addBillToCustomer(bill,customerId);
    }

    public ArrayList<String> getAll() {
        return customerService.getAll();
    }

    public ArrayList<String> getIdFilteredBills(String id) {
        return customerService.getIdFilteredBills(id);
    }

    public ArrayList<String> getSumFilteredBills(double sum, String threshold) {
        return customerService.getSumFilteredBills(sum,threshold);
    }

    public Boolean payBill(long parseLong) {
        return customerService.payBill(parseLong);
    }
}
