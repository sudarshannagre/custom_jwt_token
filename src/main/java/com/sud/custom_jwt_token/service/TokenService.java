package com.sud.custom_jwt_token.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.sud.custom_jwt_token.config.LoadConfigJson;
import com.sud.custom_jwt_token.constant.AppErrorConstants;
import com.sud.custom_jwt_token.exception.TemplateNotFoundException;
import com.sud.custom_jwt_token.model.JWTValidateResponse;
import com.sud.custom_jwt_token.model.JwkKey;
import com.sud.custom_jwt_token.model.JwtResponse;
import com.sud.custom_jwt_token.model.Keys;
import com.sud.custom_jwt_token.model.PublicKey;
import com.sud.custom_jwt_token.model.Token;
import com.sud.custom_jwt_token.util.JWTKeyGenerator;

@Service
public class TokenService{
	
	@Autowired
	JWTKeyGenerator jwtKeyGenerator;
	
	public List<JwtResponse> generateJWTToken(String audEncoded) throws JOSEException, URISyntaxException, ParseException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException, UnsupportedEncodingException {
		List<JwtResponse> response = new ArrayList<>();
		String aud = URLDecoder.decode(audEncoded, "UTF-8");
		
		if(aud.contains(",")) {
			String[] audStrings = aud.split(",");
			for(String audience : audStrings) {
				Token token = validateAudience(audience);
				if(null != token)
					response.add(jwtKeyGenerator.getJWTToken(jwtKeyGenerator.generateKeys(), token));
				else {
					JwtResponse jwtResponse = new JwtResponse();
					jwtResponse.setAud(new String[] {audience});
					response.add(jwtResponse);
				}
			}
		}else {
			Token token = validateAudience(aud);
			if(null != token)
				response.add(jwtKeyGenerator.getJWTToken(jwtKeyGenerator.generateKeys(), token));
			else
			{
				throw new TemplateNotFoundException(AppErrorConstants.CONFIG);
			}
		}
		return response;
	}  
	
	private Token validateAudience(String audience) {
		
		for(Token token : LoadConfigJson.configTokenList) {
			 
			if(Arrays.stream(token.getBclaims().getAud()).anyMatch(audience:: equalsIgnoreCase)) {
				return token;
			}
		}
		return null;
	}

  
	public JWTValidateResponse validate(String token) throws NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException, ParseException, JOSEException{
		return jwtKeyGenerator.validate(token);
	}


	public PublicKey getPublicKey() throws ParseException {
		PublicKey response = new PublicKey();
		List<JwkKey> list = new ArrayList<>();
		
		Iterator<Keys> it = LoadConfigJson.keysList.iterator();
		while(it.hasNext()) {
			JwkKey key = new Gson().fromJson(new String(Base64.getDecoder().decode(it.next().getPublicKey())), JwkKey.class);
			list.add(key);
		}
		//response.setPublicKey(LoadConfigJson.keysList.stream().map(pubKey -> new String(Base64.getDecoder().decode(pubKey.getPublicKey()))).collect(Collectors.toList()));
		response.setKeys(list);
		return response;
	}

	public void deletePublicKeys() {
		LoadConfigJson.keysList.clear();
	}
	

	public String generateKeys() throws JOSEException {
		if(LoadConfigJson.keysList.isEmpty()) {
			LoadConfigJson.keysList.addAll(jwtKeyGenerator.generatePrivatePublicKeys());
			return AppErrorConstants.KEYS_GENERATED_SUCCESS;
		}else {
			return AppErrorConstants.DELETE_KEYS;
		}
	}
}
