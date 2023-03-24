package com.sud.custom_jwt_token.model;


public class JWTValidateResponse {
	
	private String payLoad;
	private String header;
	private boolean isTokenExpired;
	private boolean isTokenVerified;
	
	public JWTValidateResponse() {
		super();
	}
	
	public boolean isTokenVerified() {
		return isTokenVerified;
	}
	public void setTokenVerified(boolean isTokenVerified) {
		this.isTokenVerified = isTokenVerified;
	}
	public String getPayLoad() {
		return payLoad;
	}
	public void setPayLoad(String payLoad) {
		this.payLoad = payLoad;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public boolean isTokenExpired() {
		return isTokenExpired;
	}
	public void setTokenExpired(boolean isTokenExpired) {
		this.isTokenExpired = isTokenExpired;
	}
}
