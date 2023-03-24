package com.sud.custom_jwt_token.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sud.custom_jwt_token.constant.AppConstant;
import com.sud.custom_jwt_token.constant.AppErrorConstants;
import com.sud.custom_jwt_token.exception.BadRequestException;
import com.sud.custom_jwt_token.model.GenerateKeysRequest;
import com.sud.custom_jwt_token.service.TokenService;

@RestController
@CrossOrigin
public class PublicKeysController {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	TokenService tokenService;
	
	/**
	 * @return all public keys
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/v1/keys", method = RequestMethod.GET)
	public ResponseEntity<?> getAllExistingKeys() throws ParseException{
		try {
			return ResponseEntity.ok(tokenService.getPublicKey());
		} catch (NumberFormatException ne) {
			throw new BadRequestException(AppErrorConstants.SOMETHING_WENT_WRONG);
		}
	}
	
	/**
	 * @return delete the all public keys
	 * @throws Exception
	 */
	@RequestMapping(value = "/v1/keys", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteExistingKeys(@RequestParam String authValue)
			throws Exception {
		try {
			if(null != authValue && authValue.equals(AppConstant.AUTH_VALUE)) {
				tokenService.deletePublicKeys();	
				return ResponseEntity.status(HttpStatus.OK).body(AppErrorConstants.KEYS_DELETED_SUCCESS);
			}else {
				throw new BadRequestException(AppErrorConstants.AUTH_VALUE);
			}
		}catch(Exception e) {
			throw new BadRequestException(AppErrorConstants.SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * @return generate all the public keys
	 * @throws Exception
	 */
	@RequestMapping(value = "/v1/keys", method = RequestMethod.POST)
	public ResponseEntity<?> generatNewKeys(@RequestBody GenerateKeysRequest request)
			throws Exception {
		try {
			if(null != request && request.getAuthValue().equals(AppConstant.AUTH_VALUE))
				return ResponseEntity.status(HttpStatus.OK).body(tokenService.generateKeys());
			else
				throw new BadRequestException(AppErrorConstants.AUTH_VALUE);
		}catch(Exception e) {
			throw new BadRequestException(e.getMessage());
		}
	}

}
