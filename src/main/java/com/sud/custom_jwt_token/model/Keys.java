package com.sud.custom_jwt_token.model;


public class Keys {

	private String kid;
	private String privateKey;
	private String publicKey;
	
	public Keys() {
		super();
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String toString() {
		return "Keys [kid=" + kid + ", privateKey=" + privateKey + ", publicKey=" + publicKey + "]";
	}
	
}
