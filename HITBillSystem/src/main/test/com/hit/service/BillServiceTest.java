package com.hit.service;

import com.hit.dao.BillDaoImpl;
import com.hit.dao.CustomerDaoImpl;
import com.hit.dm.Bill;
import com.hit.dm.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillServiceTest {

        public BillServiceTest() {
        }

        CustomerDaoImpl customerDaoImpl;
        BillService billService;

        @BeforeEach
        public void setUp(){
            customerDaoImpl = new CustomerDaoImpl();
            billService = new BillService(new BillDaoImpl());
        }

        @Test
        public void addNewBillTest(){
            Bill bill = new Bill(1234l,"14-05-2023",46.9,"Almog");
            billService.addNewBill(bill);
            Bill sameBill = billService.getBill(1234l);
            Assertions.assertNotNull(sameBill);
        }

        @Test
        public void payBillTest(){
            Bill bill = new Bill(2345l,"14-05-2023",46.9,"Eden");
            Assertions.assertFalse(bill.isPayed());
            billService.addNewBill(bill);
            billService.payBill(bill.getId());
            Bill sameBill = billService.getBill(2345l);
            Assertions.assertTrue(sameBill.isPayed());
        }

        @Test
        public void deleteBillTest(){
            Bill bill = new Bill(2345l,"14-05-2023",46.9,"Eden");
            billService.addNewBill(bill);
            billService.removeBill(bill);
            Bill sameBill = billService.getBill(2345l);
            Assertions.assertNull(sameBill);
        }

        @Test
        public void addBillToCustomer(){
            Bill bill = new Bill(1234l,"14-05-2023",46.9,"Almog");
            Customer customer = new Customer(123456789l,"Almog Franco");
            customer.addBill(bill);
            customerDaoImpl.save(customer);
            Customer newCustomer = customerDaoImpl.find(123456789l);
            Assertions.assertNotNull(newCustomer);
            Assertions.assertTrue(customer.getBillIds().contains(1234l));

        }
    }

