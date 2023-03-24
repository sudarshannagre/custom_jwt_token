package com.sud.custom_jwt_token.util;


import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sud.custom_jwt_token.config.LoadConfigJson;
import com.sud.custom_jwt_token.constant.AppConstant;
import com.sud.custom_jwt_token.model.Bclaims;
import com.sud.custom_jwt_token.model.JWTValidateResponse;
import com.sud.custom_jwt_token.model.JwtResponse;
import com.sud.custom_jwt_token.model.Keys;
import com.sud.custom_jwt_token.model.Token;

@Component
public class JWTKeyGenerator {
	
	private static int Number_of_keys=5;
	
	public ECKey generateKeys() throws JOSEException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException, ParseException {
		return generateKey((new Random().nextInt(9)));//;
	}
	
	private ECKey generateKey(int keyId) throws ParseException, JOSEException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException {
		ECKey ecPublicJWK = ECKey.parse(JSONObjectUtils.parse(new String(Base64.getDecoder().decode(LoadConfigJson.keysList.get(keyId).getPublicKey()))));
		byte[] privateKeyS1 = Base64.getDecoder().decode(LoadConfigJson.keysList.get(keyId).getPrivateKey());
	    
	    AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
	    ECParameterSpec ecSpec = Curve.P_256.toECParameterSpec();
	    parameters.init(ecSpec);
	    
	    ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);
	    
	    ECPrivateKeySpec privateSpec = new ECPrivateKeySpec(new BigInteger(1, privateKeyS1), ecParameters);
	    
	    KeyFactory kf = KeyFactory.getInstance("EC");
	    ECPrivateKey privateKey = (ECPrivateKey) kf.generatePrivate(privateSpec);
	    
	    ECKey ecKey = new ECKey.Builder(Curve.P_256, ecPublicJWK.toECPublicKey())
	  		  .privateKey(privateKey)
	  		  .keyID(LoadConfigJson.keysList.get(keyId).getKid()) 
	  		  .algorithm(JWSAlgorithm.ES256) 
	  		  .build();
	    System.out.println(ecKey.toPrivateKey());
	    System.out.println(ecKey.toECPrivateKey());
	    System.out.println(ecKey);
	    return ecKey;
	}
	
	
	/**
	 * @param bClaims
	 * @return the Body claim sets
	 */
	private JWTClaimsSet getClaimSet(Bclaims bClaims) {
		JWTClaimsSet claimsSet = null;
		Date expiry = new Date((System.currentTimeMillis()) + bClaims.getExp()*1000);
		
		if(Arrays.stream(bClaims.getAud()).anyMatch(AppConstant.OPEN::equalsIgnoreCase)){
			// Prepare JWT with claims set
			claimsSet = new JWTClaimsSet.Builder()
				.audience(Arrays.asList(bClaims.getAud()))
			    .issuer(bClaims.getIss())
			    .expirationTime(expiry)
			    .issueTime(new Date())
			    .jwtID(getUUID())
			    .build();
			
		}else if(Arrays.stream(bClaims.getAud()).anyMatch(AppConstant.PROTECTED::equalsIgnoreCase)) {
			// Prepare JWT with claims set
			claimsSet = new JWTClaimsSet.Builder()
			    .audience(Arrays.asList(bClaims.getAud()))
			    .expirationTime(expiry)
			    .build();
		}
		return claimsSet;
	}
	
	public JwtResponse getJWTToken(ECKey key,Token token) throws JOSEException, URISyntaxException {
		// Create RSA-signer with the private key
		JWSSigner signer = new ECDSASigner(key);
		Bclaims bClaims = token.getBclaims();
		//Create claimsSet based on audience
		JWTClaimsSet claimsSet = getClaimSet(token.getBclaims()) ;

		if(claimsSet != null) {
			SignedJWT signedJWT = new SignedJWT(
				    new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(key.getKeyID()).
				    type(new JOSEObjectType(token.getHclaims().getTyp())).jwkURL(new URI(token.getHclaims().getJku())).build(),
				    claimsSet);
			
			// Compute the ESA signature
			signedJWT.sign(signer);
			
			//set expiry
			Date expiry = new Date((System.currentTimeMillis()/1000) + bClaims.getExp());
			
			JwtResponse response = new JwtResponse();
			response.setAud(bClaims.getAud());
			System.out.println(claimsSet.getExpirationTime());
			response.setExp(expiry.getTime());
			response.setToken(signedJWT.serialize());
			return response;
		}else {
			return null;
		}
	}
	
	public JWTValidateResponse validate(String jwtString) throws ParseException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException, JOSEException{

		if (StringUtils.isNotEmpty(jwtString)) {
			//ECKey ecPublicJWK = ECKey.parse(JSONObjectUtils.parse(new String(Base64.getDecoder().decode(jwtString))));
			JWTValidateResponse response = new JWTValidateResponse();
			SignedJWT jwtSign = SignedJWT.parse(jwtString);
			response.setHeader(""+jwtSign.getHeader());
			response.setPayLoad(""+jwtSign.getPayload());
			
			String keyId = jwtSign.getHeader().getKeyID();
			int indexPosition=0;
			for(Keys key : LoadConfigJson.keysList) {
				if(key.getKid().equals(keyId)) {
					indexPosition = LoadConfigJson.keysList.indexOf(key);
				}
			}

			if (jwtSign.verify(new ECDSAVerifier(generateKey(indexPosition)))) {
				response.setTokenVerified(true);
			}
			if (jwtSign.getJWTClaimsSet().getExpirationTime() != null
					&& new Date().after(jwtSign.getJWTClaimsSet().getExpirationTime())) {
				response.setTokenExpired(true);
			}
			return response;
		}
		return null;
	}
	
	public List<Keys> generatePrivatePublicKeys() throws JOSEException {
		List<Keys> keysList = new LinkedList<Keys>();
		for (int i = 1; i <= Number_of_keys; i++) {
			Keys key = new Keys();
			String keyId = getUUID();
			ECKey ecJWK = new ECKeyGenerator(Curve.P_256).algorithm(JWSAlgorithm.ES256).keyID(keyId)
					.generate();
			ECKey ecPublicJWK = ecJWK.toPublicJWK();
			ECPrivateKey priv = (ECPrivateKey) ecJWK.toPrivateKey();
			key.setKid(keyId);
			key.setPrivateKey(Base64.getEncoder().encodeToString(priv.getS().toByteArray()));
			key.setPublicKey(Base64.getEncoder().encodeToString(ecPublicJWK.toString().getBytes()));
			keysList.add(key);
		}
	
		return keysList;
	}

	private String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
