package com.sud.custom_jwt_token.model;


public class Hclaims {
	
	private String typ;
	private String alg;
	private String jku;
	private String kid;
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public String getAlg() {
		return alg;
	}
	public void setAlg(String alg) {
		this.alg = alg;
	}
	public String getJku() {
		return jku;
	}
	public void setJku(String jku) {
		this.jku = jku;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public Hclaims() {
		super();
	}
	
}
