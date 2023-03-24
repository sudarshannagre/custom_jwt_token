package com.sud.custom_jwt_token.model;


public class Token {

	private int id;
	private Hclaims hclaims;
	private Bclaims bclaims;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Hclaims getHclaims() {
		return hclaims;
	}
	public void setHclaims(Hclaims hclaims) {
		this.hclaims = hclaims;
	}
	public Bclaims getBclaims() {
		return bclaims;
	}
	public void setBclaims(Bclaims bclaims) {
		this.bclaims = bclaims;
	}
}
