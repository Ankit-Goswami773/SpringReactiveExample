package com.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bank {
private String bankname;
private String bankcode;
public String getBankname() {
	return bankname;
}
public void setBankname(String bankname) {
	this.bankname = bankname;
}
public String getBankcode() {
	return bankcode;
}
public void setBankcode(String bankcode) {
	this.bankcode = bankcode;
}
public Bank(String bankname, String bankcode) {
	super();
	this.bankname = bankname;
	this.bankcode = bankcode;
}
public Bank() {
	super();
	// TODO Auto-generated constructor stub
}
@Override
public String toString() {
	return "Bank [bankname=" + bankname + ", bankcode=" + bankcode + "]";
}

}
