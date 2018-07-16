package com.capgemini.paymentapp.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.capgemini.paymentapp.beans.Customer;
import com.capgemini.paymentapp.beans.Wallet;
import com.capgemini.paymentapp.exception.InsufficientBalanceException;
import com.capgemini.paymentapp.exception.InvalidInputException;
import com.capgemini.paymentapp.repo.WalletRepo;
import com.capgemini.paymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {
	public static Scanner sc = new Scanner(System.in);
	private WalletRepo repo;

	public WalletServiceImpl(Map<String, Customer> data) {
		repo = new WalletRepoImpl(data);
	}

	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}

	public WalletServiceImpl() {
		repo = new WalletRepoImpl(new HashMap<String, Customer>());
	}

	@Override
	public Customer createAccount(String name, String mobileNo,
			BigDecimal amount) {

		while (true) {
			try {
				if (validateMobile(mobileNo)) {
					break;
				} else {
					throw new InvalidInputException(
							"Wrong mobile Number entered. Enter 10 digit Mobile Number");
				}
			} catch (InvalidInputException e) {
				System.err.println(e.getMessage());
				mobileNo = sc.next();
			}
		}
		Customer customer = new Customer(name, mobileNo, new Wallet(amount));
		boolean result = repo.save(customer);
		if (result == true) {
			return customer;
		} else {
			return null;
		}

	}

	public boolean validateMobile(String mobileNo) {
		String pattern = "[1-9][0-9]{9}";
		if (mobileNo.matches(pattern)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Customer showBalance(String mobileNo) throws SQLException {
		Customer customer = repo.findOne(mobileNo);
		if (customer != null)
			return customer;
		else
			throw new InvalidInputException("No matching mobile number found");
	}

	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo,
			BigDecimal amount) throws SQLException {

		Customer sourceCustomer = repo.findOne(sourceMobileNo);
		Customer targetCustomer = repo.findOne(targetMobileNo);
		Wallet sourceWallet = sourceCustomer.getWallet();
		Wallet targetWallet = targetCustomer.getWallet();
		BigDecimal balance1 = sourceWallet.getBalance().subtract(amount);
		sourceWallet.setBalance(balance1);
		sourceCustomer.setWallet(sourceWallet);
		BigDecimal balance2 = targetWallet.getBalance().add(amount);
		targetWallet.setBalance(balance2);
		targetCustomer.setWallet(targetWallet);
		if (sourceWallet.getBalance().compareTo(amount) == -1) {
			throw new InsufficientBalanceException("Insufficient balance.");
		} else {
			repo.update(sourceCustomer);
			repo.update(targetCustomer);
		}
		return sourceCustomer;
	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount)
			throws SQLException {

		Customer depCustomer = repo.findOne(mobileNo);
		Wallet depWallet = depCustomer.getWallet();

		BigDecimal balance1 = depWallet.getBalance().add(amount);
		depWallet.setBalance(balance1);
		depCustomer.setWallet(depWallet);
		repo.update(depCustomer);
		return depCustomer;

	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount)
			throws SQLException {
		Customer withCustomer = repo.findOne(mobileNo);

		Wallet withWallet = withCustomer.getWallet();

		BigDecimal balance1 = withWallet.getBalance().subtract(amount);
		withWallet.setBalance(balance1);
		withCustomer.setWallet(withWallet);
		if (withWallet.getBalance().compareTo(amount) == -1) {
			throw new InsufficientBalanceException("Insufficient balance.");
		} else
			repo.update(withCustomer);
		return withCustomer;
	}
}
