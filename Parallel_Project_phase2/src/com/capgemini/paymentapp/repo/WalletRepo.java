package com.capgemini.paymentapp.repo;

import java.sql.SQLException;

import com.capgemini.paymentapp.beans.Customer;

public interface WalletRepo {
public boolean save(Customer customer);
	
	public Customer findOne(String mobileNo) throws SQLException;
	public void update(Customer customer) throws SQLException;
}
