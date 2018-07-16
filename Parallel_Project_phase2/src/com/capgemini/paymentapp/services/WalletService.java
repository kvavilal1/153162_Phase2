package com.capgemini.paymentapp.services;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.capgemini.paymentapp.beans.Customer;

public interface WalletService {
	public Customer createAccount(String name, String mobileNo,
			BigDecimal amount);

	public Customer showBalance(String mobileNo) throws SQLException;

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo,
			BigDecimal amount) throws SQLException;

	public Customer depositAmount(String mobileNo, BigDecimal amount)
			throws SQLException;

	public Customer withdrawAmount(String mobileNo, BigDecimal amount)
			throws SQLException;
}
