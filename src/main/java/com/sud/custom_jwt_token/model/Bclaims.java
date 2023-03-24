package com.sud.custom_jwt_token.model;


import java.util.Arrays;

public class Bclaims {
	
	private String aud[];
	private String iss;
	private String rol;
	private long exp;
	private String iat;
	private String jti;
	public String[] getAud() {
		return aud;
	}
	public void setAud(String[] aud) {
		this.aud = aud;
	}
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public String getIat() {
		return iat;
	}
	public void setIat(String iat) {
		this.iat = iat;
	}
	public String getJti() {
		return jti;
	}
	public void setJti(String jti) {
		this.jti = jti;
	}
	public Bclaims() {
		super();
	}
	@Override
	public String toString() {
		return "bclaims [aud=" + Arrays.toString(aud) + ", iss=" + iss + ", rol=" + rol + ", exp=" + exp + ", iat="
				+ iat + ", jti=" + jti + "]";
	}
	
}
