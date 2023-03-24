package com.sud.custom_jwt_token.config;


import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sud.custom_jwt_token.constant.AppConstant;
import com.sud.custom_jwt_token.model.Bclaims;
import com.sud.custom_jwt_token.model.Hclaims;
import com.sud.custom_jwt_token.model.Keys;
import com.sud.custom_jwt_token.model.Token;


@Configuration
public class LoadConfigJson implements InitializingBean {
	
	public static List<Token> configTokenList = new LinkedList<Token>();
	public static List<Keys> keysList = new LinkedList<Keys>();

	private void loadConfigKeysJson() throws IOException, InvalidAttributesException {
		JsonNode node = new ObjectMapper().readTree(new ClassPathResource("config.keys.json").getInputStream())
				.get("keys");
		Iterator<JsonNode> it = node.iterator();
		
		while(it.hasNext()) {
			Gson gson = new Gson();
			Keys key = gson.fromJson(it.next().toString(), Keys.class);
			
			if(null != key.getKid() && null != key.getPrivateKey() && null != key.getPublicKey()) {
				keysList.add(key);
			}else {
				throw new InvalidAttributesException("Please check the config.keys.json file and its data");
			}
		}
	}
	
	private void loadConfigJson() throws IOException, InvalidAttributesException {
		JsonNode node = new ObjectMapper().readTree(new ClassPathResource("config.json").getInputStream())
				.get("tokens");
		Iterator<JsonNode> it = node.iterator();

		while (it.hasNext()) {
			Gson gson = new Gson();
			Token token = gson.fromJson(it.next().toString(), Token.class);
			
			Hclaims hClaims = token.getHclaims();
			Bclaims bClaims = token.getBclaims();
			
			if(null != hClaims.getTyp() && hClaims.getTyp().equalsIgnoreCase(AppConstant.TOKEN_TYPE_JWT) &&
					null != hClaims.getAlg() && hClaims.getAlg().equalsIgnoreCase(AppConstant.JTW_ALGORITHM) &&
					null != bClaims.getAud() && bClaims.getAud().length > 0 && bClaims.getExp() != 0) {
				configTokenList.add(token);
			}else {
				throw new InvalidAttributesException("Please check the config.json file and its data");
			}
		}
	
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		loadConfigJson();
		loadConfigKeysJson();
	}

}
