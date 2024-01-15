package com.hit.service;

import com.hit.dao.CustomerDaoImpl;
import com.hit.dm.Bill;
import com.hit.dm.Customer;

import java.util.ArrayList;

public class CustomerService {


    CustomerDaoImpl customerDao;

    public CustomerService(CustomerDaoImpl customerDao) {
        this.customerDao = customerDao;
    }

    public Customer getCustomer(Long customerId) {
        Customer customer = customerDao.find(customerId);
        if(customer == null)
            return null;
        return customer;
    }

    public boolean addNewCustomer(Customer customer) {
        if(customer == null)
            return false;
        return customerDao.save(customer);
    }

    public ArrayList<Customer> getCustomers(String customerName){
        return customerDao.getCustomers(customerName);
    }

    public boolean addBillToCustomer(Bill bill, long customerId) {
        Customer customer = customerDao.find(customerId);
        if(customer.addBill(bill)){
            if(customerDao.save(customer)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getAll() {
        return customerDao.getAll();
    }

    public ArrayList<String> getIdFilteredBills(String id) {
        return customerDao.getIdFilteredBills(id);
    }

    public ArrayList<String> getSumFilteredBills(double sum, String threshold) {
        return customerDao.getSumFilteredBills(sum,threshold);
    }

    public Boolean payBill(long id) {
        Customer customer = getCustomer(id);
        customer.reduceUnpayedBills();
        return customerDao.save(customer);
    }
}