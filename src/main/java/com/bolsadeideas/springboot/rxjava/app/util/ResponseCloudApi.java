package com.bolsadeideas.springboot.rxjava.app.util;

import lombok.Data;

@Data
public class ResponseCloudApi<T> {
	
	private String code;
	private String message;
	private T body;
	
	public ResponseCloudApi() {
		this.code = Constants.GENERAL_SUCCESS_CODE ;
		this.message = Constants.GENERAL_SUCCESS_MESSAGE ;
	}
	
}