package com.capgemini.paymentapp.exception;

public class InsufficientBalanceException extends RuntimeException
{
	public InsufficientBalanceException(String msg) {
		super(msg);
	}
}
