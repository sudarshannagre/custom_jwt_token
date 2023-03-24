package com.sud.custom_jwt_token.model;


import java.io.Serializable;

public class JwtResponse implements Serializable{
	
	private static final long serialVersionUID = -6245189744785030611L;
	
	private String aud[];
	private long exp;
	private String token;
	
	public String[] getAud() {
		return aud;
	}
	public void setAud(String[] aud) {
		this.aud = aud;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public JwtResponse() {
		super();
	}
}
