package com.sud.custom_jwt_token.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sud.custom_jwt_token.constant.AppErrorConstants;
import com.sud.custom_jwt_token.exception.BadRequestException;
import com.sud.custom_jwt_token.model.JWTValidateResponse;
import com.sud.custom_jwt_token.service.TokenService;

@RestController
@CrossOrigin
public class JWTTokenController {
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	TokenService tokenService;
	
	@RequestMapping(value = "/v1/jwttoken", method = RequestMethod.GET)
	public ResponseEntity<?> createJWTToken(@RequestParam String aud)throws Exception
	{
		if (!aud.isEmpty()) {
			return ResponseEntity.ok(tokenService.generateJWTToken(aud));
		} else {
			throw new BadRequestException(AppErrorConstants.NO_AUDIENCE_FOUND);
		}
	}
	
	/**
	 * 
	 * @return Token Validation
	 */
	@RequestMapping(value="/v1/validatejwt",method = RequestMethod.GET)
	public ResponseEntity<?> tokenValidation(){
		try {
			final String requestTokenHeader = request.getHeader("Authorization");
			String jwtToken = null;
			JWTValidateResponse response = null;
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				try {
					response = tokenService.validate(jwtToken);
				} catch (IllegalArgumentException e) {
					throw new BadRequestException(AppErrorConstants.VALIDATION_ERROR);
				}
			}else {
				throw new BadRequestException(AppErrorConstants.VALIDATION_ERROR);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new BadRequestException(AppErrorConstants.VALIDATION_ERROR);
		}
	}
	
}
