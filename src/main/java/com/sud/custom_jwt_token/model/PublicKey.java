package com.sud.custom_jwt_token.model;


import java.util.List;

public class PublicKey {

	private List<JwkKey> keys;

	public List<JwkKey> getKeys() {
		return keys;
	}

	public void setKeys(List<JwkKey> keys) {
		this.keys = keys;
	}
	
}
