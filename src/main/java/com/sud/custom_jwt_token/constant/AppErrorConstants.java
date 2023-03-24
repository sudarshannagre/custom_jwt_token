package com.sud.custom_jwt_token.constant;


public interface AppErrorConstants {

	String NO_BODY_FOUND = "No Body Found in Request";
	String NO_REQUEST_PARAM_FOUND = "No Parameter Found in Request";
	String NO_AUDIENCE_FOUND ="No Audience found in request body";
	String VALIDATION_ERROR = "Unable to validate JWT Token, provide the proper Authorization";
	String SOMETHING_WENT_WRONG = "Something went wrong";
	String KEYS_DELETED_SUCCESS = "All Keys are deleted !!";
	
	String KEYS_GENERATED_SUCCESS = "Keys are Generated !!"; 
	String DELETE_KEYS = "Please delete previous keys first";
	
	String CONFIG = "No Template Found in Config";
	String AUTH_VALUE = "Provide the proper authValue";
}

